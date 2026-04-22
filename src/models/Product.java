package models;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;


public class Product { //Affichage des produit et affichage des détailles
    
    
    public void afficherProduits() {

        String cheminFichier = "jsonFiles/products.json";
        JSONParser parser = new JSONParser();

        try (FileReader fileReader = new FileReader(cheminFichier)) {
            
            //On récupère le contenu du fichier JSON (ici en Object):
            JSONObject productsObjectJSON =(JSONObject) new JSONParser().parse(fileReader);

            //On récupère la liste des dishes, desserts et drinks
            JSONArray dishesListJSON = (JSONArray) productsObjectJSON.get("dishes");
            JSONArray dessertsListJSON = (JSONArray) productsObjectJSON.get("desserts");
            JSONArray drinksListJSON = (JSONArray) productsObjectJSON.get("drinks");

            //On initialise un compteur pour numéroter les Produits
            int num = 1;

            //On affiche la liste de produit numéroté à partir de 1
            System.out.println("Voicie la liste des produits numéroter de 1 à " + (dishesListJSON.size()+ dessertsListJSON.size()+ drinksListJSON.size()));

            //On récupère chaque Plats de la liste dishes et on les affiches
            for(int i = 0; i < dishesListJSON.size(); i++) {
                JSONObject dishesJSON = (JSONObject) dishesListJSON.get(i);
                String nomPlat = (String) dishesJSON.get("name");
                System.out.println(num + " - " + nomPlat);
                num += 1;
            }
            //On récupère chaque Desserts de la liste desserts et on les affiches
            for(int i = 0; i < dessertsListJSON.size(); i++) {
                JSONObject dessertsJSON = (JSONObject) dessertsListJSON.get(i);
                String nomDessert = (String) dessertsJSON.get("name");
                System.out.println(num + " - " + nomDessert);
                num += 1;
            }
            //On récupère chaque Boissons de la liste drinks et on les affiches
            for(int i = 0; i < drinksListJSON.size(); i++) {
                JSONObject drinksJSON = (JSONObject) drinksListJSON.get(i);
                String nomBoisson = (String) drinksJSON.get("name");
                System.out.println(num + " - " + nomBoisson);
                num += 1;
            }
        }catch (IOException | ParseException e) {
            System.err.println("ERROR : ");
            System.err.println(e);
        }
    }
}
