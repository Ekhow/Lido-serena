package models; // Ce fichier appartient au dossier models


import org.json.simple.JSONArray; // Outil pour lire les tableaux JSON [ ]
import org.json.simple.JSONObject; // Outil pour lire les objets JSON { }
import org.json.simple.parser.JSONParser; // Outil qui transforme le fichier JSON en objet Java
import org.json.simple.parser.ParseException; // Gère les erreurs si le JSON est mal formé

import java.io.FileReader; // Pour ouvrir et lire un fichier
import java.io.FileWriter; // Pour écrire dans un fichier
import java.io.IOException; // Gère les erreurs liées aux fichiers
import java.util.ArrayList; // Liste flexible dont la taille s'adapte
import java.util.List; // Le type "liste" en Java
import java.util.Scanner; // Pour lire ce que l'utilisateur tape au clavier

public class CaisseSecours { // Déclaration de la classe CaisseSecours

    public void lancer() { // Méthode appelée depuis Main.java avec cassedes.lancer()
        Scanner sc = new Scanner(System.in); // Crée le scanner pour lire le clavier

        System.out.print("Numero de table : "); // Affiche la question sans saut de ligne
        int numeroTable = sc.nextInt(); // Stocke le numéro tapé par le caissier, ex: 1
        System.out.print("Nombre de personnes : "); // Affiche la question
        int nbPersonnes = sc.nextInt(); // Stocke le nombre de personnes, ex: 3

        // Chargement du catalogue
        JSONObject products = null; // Variable vide qui va recevoir le contenu de products.json
        try {
            products = (JSONObject) new JSONParser().parse(new FileReader("jsonFiles/products.json")); // Ouvre et transforme products.json en objet Java utilisable
            JSONArray dishes = (JSONArray) products.get("dishes"); // Récupère la liste des plats
            JSONArray drinks = (JSONArray) products.get("drinks"); // Récupère la liste des boissons
            JSONArray desserts = (JSONArray) products.get("desserts"); // Récupère la liste des desserts

            List<JSONObject> commande = new ArrayList<>(); // Crée une liste vide pour stocker les produits choisis

            // Sélection des produits par catégorie
            choisirProduits(sc, "Plats", dishes, commande); // Fait choisir les plats au caissier
            choisirProduits(sc, "Boissons", drinks, commande); // Fait choisir les boissons au caissier
            choisirProduits(sc, "Desserts", desserts, commande); // Fait choisir les desserts au caissier

            // Calcul et affichage du total
            double total = 0; // Variable qui va stocker le total de la commande, commence à 0, double car nombre à virgule
            System.out.println("\n--- Récapitulatif ---"); // Affiche le titre du récapitulatif
            for (JSONObject p : commande) { // Parcourt chaque produit dans la commande
                System.out.println("- " + p.get("name") + " : " + p.get("price") + "e"); // Affiche le nom et prix du produit, ex: - Margherita : 5e
                total += ((Number) p.get("price")).doubleValue(); // Ajoute le prix au total, converti en double pour le calcul
            }
            System.out.printf("TOTAL : %.2f e%n", total); // Affiche le total avec 2 décimales, ex: TOTAL : 43.00 e

            // Archivage
            try { // On essaye d'archiver la commande
                archiverCommande(numeroTable, nbPersonnes, commande, total); // Appelle la méthode qui écrit dans archive.json
            } catch (Exception e) { // Si ça plante
                throw new RuntimeException(e); // On remonte l'erreur
            }
        } catch (Exception e) { // Si le fichier products.json est introuvable ou mal formé
            System.out.println(e.getMessage()); // Affiche un message d'erreur
        }
    }

    private void choisirProduits(Scanner sc, String categorie, JSONArray liste, List<JSONObject> commande) { // Méthode qui gère la sélection d'une catégorie, reçoit le scanner, le nom de la catégorie, la liste des produits et la commande
        System.out.println("\n-- " + categorie + " --"); // Affiche le nom de la catégorie, ex: -- Plats --

        for (int i = 0; i < liste.size(); i++) { // Parcourt chaque produit de la liste avec un compteur i qui commence à 0
            JSONObject item = (JSONObject) liste.get(i); // Récupère le produit à la position i, ex: {"name": "Margherita", "price": 5}
            System.out.println((i + 1) + " - " + item.get("name") + " (" + item.get("price") + "e)"); // Affiche le produit numéroté, ex: 1 - Margherita (5e). i+1 car les listes commencent à 0 mais on affiche à partir de 1
        }
        System.out.println("0 - Passer");  // Affiche l'option pour passer la catégorie sans rien choisir

        while (true) { // Boucle infinie, on reste dans la sélection jusqu'à un break
            System.out.print("Votre choix : "); // Affiche la question
            int choix = sc.nextInt(); // Stocke le numéro tapé par le caissier

            if (choix == 0) break; // Si le caissier tape 0, on sort de la boucle et on passe à la catégorie suivante

            if (choix >= 1 && choix <= liste.size()) { // Vérifie que le choix est valide, entre 1 et le nombre de produits
                JSONObject choisi = (JSONObject) liste.get(choix - 1);  // Récupère le produit choisi. choix-1 car la liste commence à 0 mais l'utilisateur voit à partir de 1
                commande.add(choisi); // Ajoute le produit choisi dans la commande
                System.out.println(choisi.get("name") + " ajoute."); // Confirme l'ajout, ex: Margherita ajoute.
                System.out.print("Ajouter un(e) autre " + categorie + " ? (o/n) : ");  // Demande si on veut ajouter un autre produit de la même catégorie
                String rep = sc.next(); // Stocke la réponse du caissier, "o" ou "n"
                if (rep.equalsIgnoreCase("n")) break; // Si le caissier tape "n" ou "N", on sort de la boucle
            } else { // Si le choix est invalide
                System.out.println("Choix invalide."); // Affiche un message d'erreur et on reboucle
            }
        }
    }

    private void archiverCommande(int table, int nbPersonnes, List<JSONObject> produits, double total) throws Exception { // Méthode qui sauvegarde la commande dans archive.json, reçoit les infos de la table et la liste des produits
        JSONArray archive = new JSONArray(); // Crée un tableau JSON vide qui va recevoir l'archive existante
        try {
            archive = (JSONArray) new JSONParser().parse(new FileReader("jsonFiles/archive.json")); // Essaie de charger l'archive existante pour ne pas écraser les commandes déjà enregistrées
        } catch (Exception e) {
            // Le fichier n'existe pas encore, on continue avec le tableau vide
        }

        JSONObject commande = new JSONObject(); // Crée un objet JSON vide pour la nouvelle commande
        commande.put("table", (long) table); // Ajoute le numéro de table, ex: "table": 1. (long) car JSON simple stocke les entiers en long
        commande.put("nbPersonnes", (long) nbPersonnes); // Ajoute le nombre de personnes, ex: "nbPersonnes": 3
        commande.put("total", total); // Ajoute le total, ex: "total": 43.0

        JSONArray produitsArray = new JSONArray(); // Crée un tableau JSON vide pour stocker les produits
        for (JSONObject p : produits) { // Parcourt chaque produit de la commande
            JSONObject item = new JSONObject(); // Crée un objet JSON vide pour chaque produit
            item.put("name", p.get("name")); // Ajoute le nom du produit, ex: "name": "Margherita"
            item.put("price", p.get("price")); // Ajoute le prix du produit, ex: "price": 5
            produitsArray.add(item); // Ajoute le produit dans le tableau des produits
        }
        commande.put("produits", produitsArray); // Ajoute le tableau des produits dans la commande
        archive.add(commande); // Ajoute la nouvelle commande dans l'archive

        FileWriter fw = new FileWriter("jsonFiles/archive.json"); // Ouvre le fichier archive.json pour écrire dedans
        fw.write(archive.toJSONString()); // Convertit l'archive en texte JSON et l'écrit dans le fichier
        fw.flush(); // Force l'écriture sur le disque, comme appuyer sur "Enregistrer"
        fw.close(); // Ferme le fichier proprement pour libérer la mémoire

        System.out.println("Commande archivee."); // Confirme que la commande a bien été sauvegardée
    }
}