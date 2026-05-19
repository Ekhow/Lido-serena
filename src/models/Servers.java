package models;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Servers {
    private static JSONArray parseJson() throws RuntimeException {
        try (FileReader fileReader = new FileReader("jsonFiles/servers.json")) {
            return (JSONArray) new JSONParser().parse(fileReader);
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int proposerChoix() {
        IO.println("---------- Gérer les serveurs -----------");
        IO.println("1 - Afficher les serveurs");
        IO.println("2 - Ajouter un serveur");
        IO.println("3 - Retirer un serveur");
        IO.println("4 - Retour au menu");
        IO.println("---------------------------------------");

        try {
            return Integer.parseInt(IO.readln("Faites votre choix : "));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static void afficher() {
        JSONArray serveurs = parseJson();

        if (serveurs.isEmpty()) {
            IO.println("Aucun serveur trouvé.");
            return;
        }

        IO.println("---------- Liste des serveurs ----------");
        for (Object obj : serveurs) {
            JSONObject serveur = (JSONObject) obj;
            String nom = (String) serveur.get("nom");
            double totalEncaisse = (double) serveur.get("totalEncaisse");
            double totalPourboire = (double) serveur.get("totalPourboire");
            IO.println("- " + nom + " | Total encaissé : " + totalEncaisse + " | Total pourboire : " + totalPourboire);
        }
        IO.println("---------------------------------------");
    }

    public static void ajouterServeur(String nom) throws RuntimeException {
        JSONArray serveurs = parseJson();

        if (nom.trim().isEmpty()) {
            IO.println("Le nom du serveur ne peut pas être vide !");
            return;
        }

        if (serveurExiste(nom)) {
            IO.println("Le serveur " + nom + " existe déjà !");
            return;
        }

        JSONObject nouveauServeur = new JSONObject();
        nouveauServeur.put("nom", nom);
        nouveauServeur.put("totalEncaisse", 0.0f);
        nouveauServeur.put("totalPourboire", 0.0f);

        serveurs.add(nouveauServeur);

        try (FileWriter fileWriter = new FileWriter("jsonFiles/servers.json")) {
            fileWriter.write(serveurs.toJSONString());
            fileWriter.flush();

            IO.println("Le serveur " + nom + " a été ajouté avec succès !");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void retirerServeur(String nom) throws RuntimeException {
        JSONArray serveurs = parseJson();

        if (!serveurExiste(nom)) {
            IO.println("Le serveur " + nom + " n'existe pas !");
            return;
        }

        serveurs.removeIf(obj -> {
            JSONObject serveur = (JSONObject) obj;
            return serveur.get("nom").equals(nom);
        });

        try (FileWriter fileWriter = new FileWriter("jsonFiles/servers.json")) {
            fileWriter.write(serveurs.toJSONString());
            fileWriter.flush();

            IO.println("Le serveur " + nom + " a été retiré avec succès !");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean serveurExiste(String nom) {
        JSONArray serveurs = parseJson();

        for (Object obj : serveurs) {
            JSONObject serveur = (JSONObject) obj;

            if (serveur.get("nom").equals(nom)) {
                return true;
            }
        }

        return false;
    }

    public static void encaisser(String nomServeur, float montant) throws RuntimeException {
        JSONArray serveurs = parseJson();

        if (serveurs.isEmpty()) {
            throw new RuntimeException("Il n'existe pas de serveur !");
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

    public static void pourboire(String nomServeur, float montant) throws RuntimeException {
        JSONArray serveurs = parseJson();

        if (serveurs.isEmpty()) {
            throw new RuntimeException("Il n'existe pas de serveur !");
        }

        for (Object obj : serveurs) {
            JSONObject serveur = (JSONObject) obj;
            String nom = (String) serveur.get("nom");

            if (nom.equals(nomServeur)) {
                try (FileWriter fileWriter = new FileWriter("jsonFiles/servers.json")) {
                    float totalPourboire = ((Number) serveur.get("totalPourboire")).floatValue();
                    serveur.put("totalPourboire", totalPourboire + montant);
                    fileWriter.write(serveurs.toJSONString());
                    fileWriter.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
}
