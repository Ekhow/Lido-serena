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
    public void affichageTicket() {

        /** on doit afficher le ticket de la table i
         *
         */
        Scanner sc = new Scanner(System.in);

        // on choisi le ticket que l'on veut
        System.out.println("Quel ticket voulez vous ?");
        int numeroticket = sc.nextInt();
        String cheminFichier = "jsonFiles/"+numeroticket+".json";
        String cheminFichier2 = "jsonFiles/products.json";
        JSONParser parser = new JSONParser();


        try (FileReader fileReader = new FileReader(cheminFichier);
             FileReader carteReader = new FileReader(cheminFichier2)) {
            // On récupère le contenu du fichier JSON (ici en Array)
            JSONObject carteJSON = (JSONObject) new JSONParser().parse(carteReader);

            JSONObject table1JSON = (JSONObject) new JSONParser().parse(fileReader);
            long table = (long) table1JSON.get("tabNum");
            String date = (String) table1JSON.get("date");
            long nbPersonnes = (long) table1JSON.get("nbOfPeople");
            JSONArray products = (JSONArray) table1JSON.get("products");
            //On récupère le contenu du fichier JSON (ici en Object):
            JSONObject productsObjectJSON =(JSONObject) new JSONParser().parse(fileReader);

            //On récupère la liste des dishes, desserts et drinks
            JSONArray dishesListJSON = (JSONArray) productsObjectJSON.get("dishes");
            JSONArray dessertsListJSON = (JSONArray) productsObjectJSON.get("desserts");
            JSONArray drinksListJSON = (JSONArray) productsObjectJSON.get("drinks");

            double total = 0.0;
            for (Object produit : products) {
                // On cherche le prix dans chaque catégorie de la carte
                double prix1 = 0.0;
                for (String categorie : new String[]{"dishes", "desserts", "drinks"}) {
                    for (Object item : (JSONArray) carteJSON.get(categorie)) {
                        JSONObject itemJSON = (JSONObject) item;
                        if (itemJSON.get("name").equals(produit)) {
                            double prix1 = ((Number) itemJSON.get("price")).doubleValue();
                        }
                    }
                }

                }

            System.out.println("-----Ticket-----");
            // on affiche la table
            System.out.println("Table : " + table);
            //on affiche la date
            System.out.println("Le : " + date);
            //on affiche le nombre de personnes
            System.out.println("Pour : " + nbPersonnes);
            //on affiche les produits
            for (Object produit : products) {
                System.out.println("- " + produit+"___"+prixproduit);
            }
            System.out.println("----------------");
            System.out.printf("TOTAL : %.2f€%n", total);


        } catch (IOException e) {
            System.err.println("ERROR : ");
            System.err.println(e);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    public void CB() {
        String cheminFichier3 = "res/2.json";
        /**
         * on veut faire payer en cb , que le serveur accepte le payement pour qu'il se sauvegarde dans un fichier
         */
        System.out.println("Paiement par CB en cours...");
        System.out.println("Paiement CB accepté !");
        //archiverCommande("CB");
    }


    public void especes() {

        System.out.println("Montant remis par le client : ");
        System.out.println(" Paiement espèces accepté !");
        //archiverCommande("especes");

    }

    private void archiverCommande(String modePaiement) {
        String cheminArchive = "jsonFiles/archive.json";

        try {
            // Charger l'archive existante ou créer un tableau vide
            JSONArray archive = new JSONArray();
            try (FileReader fileReader = new FileReader(cheminArchive)) {
                archive = (JSONArray) new JSONParser().parse(fileReader);
            } catch (IOException e) {
                // Le fichier n'existe pas encore, on part d'un tableau vide
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }


    }
}
