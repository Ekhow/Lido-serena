package models;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

public class CaisseSecours {

    public void lancer() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Numero de table : ");
        int numeroTable = sc.nextInt();
        System.out.print("Nombre de personnes : ");
        int nbPersonnes = sc.nextInt();

        String[] nomsChoisis = new String[50];
        double[] prixChoisis = new double[50];
        int nbProduits = 0;

        try {
            FileReader fileReader = new FileReader("jsonFiles/products.json");
            JSONParser parser = new JSONParser();
            JSONObject products = (JSONObject) parser.parse(fileReader);

            JSONArray dishes   = (JSONArray) products.get("dishes");
            JSONArray drinks   = (JSONArray) products.get("drinks");
            JSONArray desserts = (JSONArray) products.get("desserts");

            System.out.println("\n-- Plats --");
            for (int i = 0; i < dishes.size(); i++) {
                JSONObject plat = (JSONObject) dishes.get(i);
                System.out.println((i + 1) + " - " + plat.get("name") + " (" + plat.get("price") + "e)");
            }
            System.out.println("0 - Passer");

            boolean continuer = true;
            while (continuer) {
                System.out.print("Votre choix : ");
                int choix = sc.nextInt();
                if (choix == 0) {
                    continuer = false;
                } else if (choix >= 1 && choix <= dishes.size()) {
                    JSONObject plat = (JSONObject) dishes.get(choix - 1);
                    nomsChoisis[nbProduits] = (String) plat.get("name");
                    prixChoisis[nbProduits] = ((Number) plat.get("price")).doubleValue();
                    nbProduits++;
                    System.out.println(plat.get("name") + " ajoute.");
                    System.out.print("Ajouter un autre plat ? (o/n) : ");
                    String rep = sc.next();
                    if (rep.equals("n")) continuer = false;
                } else {
                    System.out.println("Choix invalide.");
                }
            }

            System.out.println("\n-- Boissons --");
            for (int i = 0; i < drinks.size(); i++) {
                JSONObject boisson = (JSONObject) drinks.get(i);
                System.out.println((i + 1) + " - " + boisson.get("name") + " (" + boisson.get("price") + "e)");
            }
            System.out.println("0 - Passer");

            continuer = true;
            while (continuer) {
                System.out.print("Votre choix : ");
                int choix = sc.nextInt();
                if (choix == 0) {
                    continuer = false;
                } else if (choix >= 1 && choix <= drinks.size()) {
                    JSONObject boisson = (JSONObject) drinks.get(choix - 1);
                    nomsChoisis[nbProduits] = (String) boisson.get("name");
                    prixChoisis[nbProduits] = ((Number) boisson.get("price")).doubleValue();
                    nbProduits++;
                    System.out.println(boisson.get("name") + " ajoute.");
                    System.out.print("Ajouter une autre boisson ? (o/n) : ");
                    String rep = sc.next();
                    if (rep.equals("n")) continuer = false;
                } else {
                    System.out.println("Choix invalide.");
                }
            }

            System.out.println("\n-- Desserts --");
            for (int i = 0; i < desserts.size(); i++) {
                JSONObject dessert = (JSONObject) desserts.get(i);
                System.out.println((i + 1) + " - " + dessert.get("name") + " (" + dessert.get("price") + "e)");
            }
            System.out.println("0 - Passer");

            continuer = true;
            while (continuer) {
                System.out.print("Votre choix : ");
                int choix = sc.nextInt();
                if (choix == 0) {
                    continuer = false;
                } else if (choix >= 1 && choix <= desserts.size()) {
                    JSONObject dessert = (JSONObject) desserts.get(choix - 1);
                    nomsChoisis[nbProduits] = (String) dessert.get("name");
                    prixChoisis[nbProduits] = ((Number) dessert.get("price")).doubleValue();
                    nbProduits++;
                    System.out.println(dessert.get("name") + " ajoute.");
                    System.out.print("Ajouter un autre dessert ? (o/n) : ");
                    String rep = sc.next();
                    if (rep.equals("n")) continuer = false;
                } else {
                    System.out.println("Choix invalide.");
                }
            }

            // --- Récapitulatif ---
            double total = 0;
            System.out.println("\n--- Recapitulatif ---");
            for (int i = 0; i < nbProduits; i++) {
                System.out.println("- " + nomsChoisis[i] + " : " + prixChoisis[i] + "e");
                total += prixChoisis[i];
            }
            System.out.printf("TOTAL : %.2f e%n", total);

            JSONArray archive = new JSONArray();
            try {
                FileReader fr = new FileReader("jsonFiles/archive.json");
                JSONParser p2 = new JSONParser();
                archive = (JSONArray) p2.parse(fr);
            } catch (Exception e) {

            }

            JSONObject commande = new JSONObject();
            commande.put("table", (long) numeroTable);
            commande.put("nbPersonnes", (long) nbPersonnes);
            commande.put("total", total);

            JSONArray produitsArray = new JSONArray();
            for (int i = 0; i < nbProduits; i++) {
                JSONObject item = new JSONObject();
                item.put("name", nomsChoisis[i]);
                item.put("price", prixChoisis[i]);
                produitsArray.add(item);
            }
            commande.put("produits", produitsArray);
            archive.add(commande);

            FileWriter fw = new FileWriter("jsonFiles/archive.json");
            fw.write(archive.toJSONString());
            fw.flush();
            fw.close();

            System.out.println("Commande archivee.");

        } catch (Exception e) {
            System.err.println("Erreur : ");
            System.err.println(e);
        }
    }
}
