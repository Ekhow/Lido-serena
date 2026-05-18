package models;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


public class Payement {
    /**
     * affichage du ticket
     * calcul du ticket
     * payer
     * Payer/espèce
     */
    public double[] affichageTicket() {

        /** on doit afficher le ticket de la table i
         *
         */
        Scanner sc = new Scanner(System.in);

        // on choisi le ticket que l'on veut
        System.out.println("Quel ticket voulez vous ?");
        int numeroticket = sc.nextInt();
        String cheminFichier = "jsonFiles/" + numeroticket + ".json";
        String cheminFichier2 = "jsonFiles/products.json";
        JSONParser parser = new JSONParser();


        long table = 0;
        float total = 0;
        try (FileReader fileReader = new FileReader(cheminFichier);
             FileReader carteReader = new FileReader(cheminFichier2)) {
            // On récupère le contenu du fichier JSON (ici en Array)
            JSONObject carteJSON = (JSONObject) new JSONParser().parse(carteReader);

            JSONObject table1JSON = (JSONObject) new JSONParser().parse(fileReader);
            table = (long) table1JSON.get("tabNum");
            String date = (String) table1JSON.get("date");
            long nbPersonnes = (long) table1JSON.get("nbOfPeople");
            JSONArray products = (JSONArray) table1JSON.get("products");
            //On récupère le contenu du fichier JSON (ici en Object):

            //On récupère la liste des dishes, desserts et drinks
            JSONArray dishesListJSON = (JSONArray) table1JSON.get("dishes");
            JSONArray dessertsListJSON = (JSONArray) table1JSON.get("desserts");
            JSONArray drinksListJSON = (JSONArray) table1JSON.get("drinks");


            total = 0;
            System.out.println("-----Ticket-----");
            // on affiche la table
            System.out.println("Table : " + table);
            //on affiche la date
            System.out.println("Le : " + date);
            //on affiche le nombre de personnes
            System.out.println("Pour : " + nbPersonnes);
            //on affiche les produits
            for (Object produit : products) {
                String nomProduit = (String) produit;
                double prixProduit = 0.0;

                for (String categorie : new String[]{"dishes", "desserts", "drinks"}) {
                    JSONArray listeCategorie = (JSONArray) carteJSON.get(categorie);
                    for (Object item : listeCategorie) {
                        JSONObject itemJSON = (JSONObject) item;
                        if (itemJSON.get("name").equals(nomProduit)) {
                            prixProduit = ((Number) itemJSON.get("price")).doubleValue();
                            break;


                        }
                    }

                }
                total += prixProduit;
                System.out.println("- " + nomProduit + " : " + prixProduit + " €");
            }
            System.out.println("----------------");
            System.out.println("TOTAL :" + total);
            System.out.println("----------------");

            System.out.println("Choisir le serveur :");
            String serveur = sc.next();
            if (Servers.serveurExiste(serveur)) {
                System.out.println("Choisissez un pourboire :");
                float pourboire =  sc.nextFloat();

                if (pourboire < 0) {
                    System.out.println("Le pourboire ne peut pas être négatif. Aucun pourboire ajouté.");
                    pourboire = 0;
                }
                Servers.pourboire(serveur, pourboire);
                Servers.encaisser(serveur, (float) total);
            } else {
                System.out.println("Serveur non reconnu, paiement sans serveur.");
            }

        } catch (IOException e) {
            System.err.println("ERROR : ");
            System.err.println(e);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return new double[]{table, total};
    }




    public void CB(double table, double total) {
        String cheminFichier3 = "res/2.json";
        /**
         * on veut faire payer en cb , que le serveur accepte le payement pour qu'il se sauvegarde dans un fichier
         */
        affichageTicket();
        System.out.println("Paiement par CB en cours...");
        System.out.println("Paiement CB accepté !");
        archiverCommande(table, total, "CB");
    }


    public void especes(double table, double total) {

        Scanner sc = new Scanner(System.in);
        affichageTicket();
        System.out.println("Montant remis par le client : ");
        int remis = sc.nextInt();
        double remise = total - remis;
        System.out.println("Remise pour le client : " + remise);
        System.out.println(" Paiement espèces accepté !");
        archiverCommande(table, total, "ESPECE");

    }

    private void archiverCommande(double table1, double total1, String modePaiement) {
        String cheminArchive = "jsonFiles/archive.json";
        JSONArray archive = new JSONArray();
        JSONParser parser = new JSONParser();

        try {
            archive = (JSONArray) new JSONParser().parse(new FileReader("jsonFiles/archive.json"));
        } catch (Exception e) {
            // fichier vide ou inexistant, on repart d'un tableau vide
        }
        JSONObject sauvegarde = new JSONObject();
        sauvegarde.put("table", table1);
        sauvegarde.put("total", total1);
        sauvegarde.put("modePaiement", modePaiement);

        archive.add(sauvegarde);

        try (java.io.FileWriter writer = new java.io.FileWriter(cheminArchive)) {
            writer.write(archive.toJSONString());
            writer.flush();
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture dans archive.json");
        }
    }
}







