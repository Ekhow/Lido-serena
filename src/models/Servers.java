package models;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Servers {
    private static JSONArray parseJson() {
        try (FileReader fileReader = new FileReader("jsonFiles/servers.json")) {
            return (JSONArray) new JSONParser().parse(fileReader);
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void afficher() {
        JSONArray serveurs = (JSONArray) parseJson();

        if (serveurs.isEmpty()) {
            IO.println("Aucun serveur trouvé.");
            return;
        }

        IO.println("---------- Liste des serveurs ----------");
        for (Object obj : serveurs) {
            JSONObject serveur = (JSONObject) obj;
            String nom = (String) serveur.get("nom");
            IO.println("- " + nom);
        }
        IO.println("---------------------------------------");
    }

    public void encaisser(String nomServeur, float montant) {
        JSONArray serveurs = (JSONArray) parseJson();

        if (serveurs.isEmpty()) {
            throw new RuntimeException("Le serveur n'existe pas.");
        }

        for (Object obj : serveurs) {
            JSONObject serveur = (JSONObject) obj;
            String nom = (String) serveur.get("nom");

            if (nom.equals(nomServeur)) {
                try (FileWriter fileWriter = new FileWriter("jsonFiles/servers.json")) {
                    float totalEncaisse = ((Number) serveur.get("totalEncaisse")).floatValue();
                    serveur.put("totalEncaisse", totalEncaisse + montant);
                    fileWriter.write(serveurs.toJSONString());
                    fileWriter.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
