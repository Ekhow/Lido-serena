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

    public void lancer() {
        try {
            Scanner sc = new Scanner(System.in);

            System.out.print("Numero de table : ");
            int numeroTable = sc.nextInt();
            System.out.print("Nombre de personnes : ");
            int nbPersonnes = sc.nextInt();

            // On charge le fichier products.json
            FileReader fileReader = new FileReader("jsonFiles/products.json");
            JSONParser parser = new JSONParser();
            JSONObject products = (JSONObject) parser.parse(fileReader);

            // On récupère les 3 listes
            JSONArray dishes   = (JSONArray) products.get("dishes");
            JSONArray drinks   = (JSONArray) products.get("drinks");
            JSONArray desserts = (JSONArray) products.get("desserts");

            // Liste des produits choisis par le caissier
            List commande = new ArrayList();

            // On fait choisir les produits catégorie par catégorie
            choisirProduits(sc, "Plats",    dishes,   commande);
            choisirProduits(sc, "Boissons", drinks,   commande);
            choisirProduits(sc, "Desserts", desserts, commande);

            // Calcul du total et affichage
            double total = 0;
            System.out.println("\n--- Récapitulatif ---");
            for (int i = 0; i < commande.size(); i++) {
                JSONObject p = (JSONObject) commande.get(i);
                System.out.println("- " + p.get("name") + " : " + p.get("price") + "e");
                total += ((Number) p.get("price")).doubleValue();
            }
            System.out.printf("TOTAL : %.2f e%n", total);

            // Archivage
            archiverCommande(numeroTable, nbPersonnes, commande, total);

        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
        }
    }

    private void choisirProduits(Scanner sc, String categorie, JSONArray liste, List commande) {
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

    private void archiverCommande(int table, int nbPersonnes, List commande, double total) throws Exception {
        // On charge l'archive existante ou on crée un tableau vide
        JSONArray archive = new JSONArray();
        try {
            FileReader fr = new FileReader("jsonFiles/archive.json");
            JSONParser parser = new JSONParser();
            archive = (JSONArray) parser.parse(fr);
        } catch (Exception e) {
            // Le fichier n'existe pas encore, on continue avec un tableau vide
        }

        // On crée l'objet commande à archiver
        JSONObject commandeJSON = new JSONObject();
        commandeJSON.put("table", (long) table);
        commandeJSON.put("nbPersonnes", (long) nbPersonnes);
        commandeJSON.put("total", total);

        // On ajoute les produits
        JSONArray produitsArray = new JSONArray();
        for (int i = 0; i < commande.size(); i++) {
            JSONObject p = (JSONObject) commande.get(i);
            JSONObject item = new JSONObject();
            item.put("name", p.get("name"));
            item.put("price", p.get("price"));
            produitsArray.add(item);
        }
        commandeJSON.put("produits", produitsArray);

        // On ajoute la commande à l'archive et on écrit le fichier
        archive.add(commandeJSON);
        FileWriter fw = new FileWriter("jsonFiles/archive.json");
        fw.write(archive.toJSONString());
        fw.flush();
        fw.close();

        System.out.println("Commande archivee.");
    }
}
