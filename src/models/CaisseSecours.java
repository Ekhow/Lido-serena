package models;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CaisseSecours {

    public void lancer() catch Exception {
        Scanner sc = new Scanner(System.in);

        System.out.print("Numero de table : ");
        int numeroTable = sc.nextInt();
        System.out.print("Nombre de personnes : ");
        int nbPersonnes = sc.nextInt();

        // Chargement du catalogue
        JSONObject products = (JSONObject) new JSONParser().parse(new FileReader("jsonFiles/products.json"));
        JSONArray dishes   = (JSONArray) products.get("dishes");
        JSONArray drinks   = (JSONArray) products.get("drinks");
        JSONArray desserts = (JSONArray) products.get("desserts");

        List<JSONObject> commande = new ArrayList<>();

        // Sélection des produits par catégorie
        choisirProduits(sc, "Plats",    dishes,   commande);
        choisirProduits(sc, "Boissons", drinks,   commande);
        choisirProduits(sc, "Desserts", desserts, commande);

        // Calcul et affichage du total
        double total = 0;
        System.out.println("\n--- Récapitulatif ---");
        for (JSONObject p : commande) {
            System.out.println("- " + p.get("name") + " : " + p.get("price") + "e");
            total += ((Number) p.get("price")).doubleValue();
        }
        System.out.printf("TOTAL : %.2f e%n", total);

        // Archivage
        archiverCommande(numeroTable, nbPersonnes, commande, total);
    }

    private void choisirProduits(Scanner sc, String categorie, JSONArray liste, List<JSONObject> commande) {
        System.out.println("\n-- " + categorie + " --");
        for (int i = 0; i < liste.size(); i++) {
            JSONObject item = (JSONObject) liste.get(i);
            System.out.println((i + 1) + " - " + item.get("name") + " (" + item.get("price") + "e)");
        }
        System.out.println("0 - Passer");

        while (true) {
            System.out.print("Votre choix : ");
            int choix = sc.nextInt();

            if (choix == 0) break;

            if (choix >= 1 && choix <= liste.size()) {
                JSONObject choisi = (JSONObject) liste.get(choix - 1);
                commande.add(choisi);
                System.out.println(choisi.get("name") + " ajoute.");
                System.out.print("Ajouter un(e) autre " + categorie + " ? (o/n) : ");
                String rep = sc.next();
                if (rep.equalsIgnoreCase("n")) break;
            } else {
                System.out.println("Choix invalide.");
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void archiverCommande(int table, int nbPersonnes, List<JSONObject> produits, double total) throws Exception {
        JSONArray archive = new JSONArray();
        try {
            archive = (JSONArray) new JSONParser().parse(new FileReader("jsonFiles/archive.json"));
        } catch (Exception e) {
            // fichier vide ou inexistant, on repart d'un tableau vide
        }

        JSONObject commande = new JSONObject();
        commande.put("table", (long) table);
        commande.put("nbPersonnes", (long) nbPersonnes);
        commande.put("total", total);

        JSONArray produitsArray = new JSONArray();
        for (JSONObject p : produits) {
            JSONObject item = new JSONObject();
            item.put("name", p.get("name"));
            item.put("price", p.get("price"));
            produitsArray.add(item);
        }
        commande.put("produits", produitsArray);
        archive.add(commande);

        FileWriter fw = new FileWriter("jsonFiles/archive.json");
        fw.write(archive.toJSONString());
        fw.flush();
        fw.close();

        System.out.println("Commande archivee.");
    }
}
