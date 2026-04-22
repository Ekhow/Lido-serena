package models;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.io.IOException;


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
        String cheminFichier = "jsonFiles/2.json";
        try (FileReader fileReader = new FileReader(cheminFichier)) {
            // On récupère le contenu du fichier JSON (ici en Array)

            JSONObject table1JSON = (JSONObject) new JSONParser().parse(fileReader);
            long table = (long) table1JSON.get("tabNum");
            String date = (String) table1JSON.get("date");
            long nbPersonnes = (long) table1JSON.get("nbOfPeople");
            JSONArray products = (JSONArray) table1JSON.get("products");

            System.out.println("-----Ticket-----");
            // on affiche la table
            System.out.println("Table : "+table);
            //on affiche la date
            System.out.println("Le : "+date);
            //on affiche le nombre de personnes
            System.out.println("Pour : "+nbPersonnes);
            //on affiche les produits
            for(Object produit : products) {
                System.out.println("- " + produit);
            }

        } catch (IOException e) {
            System.err.println("ERROR : ");
            System.err.println(e);

        } catch (org.json.simple.parser.ParseException e) {
            throw new RuntimeException(e);
        }

    }
    public void CB(){
        String cheminFichier = "res/2.json";
        /**
         * on veut faire payer en cb , que le serveur accepte le payement pour qu'il se sauvegarde dans un fichier
         */

    }
}

