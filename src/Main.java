import models.Payement;
import models.Product;
import models.Servers;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

int proposerChoix() {
    IO.println("---------------------------------------");
    IO.println("1 - Afficher les détails des produits");
    IO.println("2 - Faire payer le client");
    IO.println("3 - Caisse de secours");
    IO.println("4 - Afficher les serveurs");
    IO.println("5 - Quitter");
    IO.println("---------------------------------------");

    try {
        return Integer.parseInt(IO.readln("Faites votre choix : "));
    } catch (NumberFormatException e) {
        return 0;
    }
}

void main() {
    int choix;
    Scanner sc = new Scanner(System.in);
    String cheminFichier = "jsonFiles/products.json";
    JSONParser parser = new JSONParser();
    //On récupère le contenu du fichier JSON (ici en Object):


    while((choix = proposerChoix()) != 5) {
        Payement payement= new Payement();
        Product product= new Product();
        
        switch(choix) {
            case 1:
                product.afficherProduits();

                break;
            case 2:
                System.out.println("---------------");
                System.out.println("1 - Voulez-vous afficher le Ticket ?");
                System.out.println("2 - Payer en CB ");
                System.out.println("3 - Payer en espece");
                System.out.println("---------------");
                int choix1=sc.nextInt();

                while(choix < 1 || choix > 3){ // Verification de l entree utilisateur
                    System.out.println("Merci de choisir un chiffre entre 1 et 3");
                    choix= sc.nextInt();
                }
                if(choix1 == 1){
                    payement.affichageTicket();
                }else if(choix1 == 2){
                    payement.CB();
                    break;
                } else if (choix1 == 3) {
                    payement.especes();
                    break;
                }
            case 3:
                IO.println("Caisse de secours...");
                break;
            case 4:
                Servers.afficher();
                break;
            default:
                IO.println("Choix invalide, veuillez réessayer.");
        }
    }
}
