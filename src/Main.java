import models.*;
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
    double table = 0;
    double total = 0;


    while((choix = proposerChoix()) != 5) {
        Payement payement= new Payement();
        Product product= new Product();
        CaisseSecours cassedes= new CaisseSecours();
        
        switch(choix) {
            case 1:
                product.afficherProduits();
                product.afficher_details();

                break;
            case 2:
                int choix1 = 0;
                while (choix1 != 4) {
                    System.out.println("---------------");
                    System.out.println("1 - Voulez-vous afficher le Ticket ?");
                    System.out.println("2 - Payer en CB ");
                    System.out.println("3 - Payer en espece");
                    System.out.println("4 - Quitter");
                    System.out.println("---------------");
                    choix1 = sc.nextInt();

                    while (choix1 < 1 || choix1 > 4) { // Verification de l entree utilisateur
                        System.out.println("Merci de choisir un chiffre entre 1 et 4");
                        choix1 = sc.nextInt();
                    }
                    switch(choix1) {
                        case 1 :
                            double[] infos = payement.affichageTicket();
                            table = infos[0];
                            total = infos[1];
                            payement.affichageTicket();
                            break;
                        case 2:
                            payement.CB(table,total);
                            break;
                        case 3:
                            payement.especes(table,total);
                            break;
                        case 4:
                            IO.println("Retour au menu principal");
                    }
                }
                break;
            case 3:
                cassedes.lancer();
                break;
            case 4:
                Servers.afficher();
                break;
            default:
                IO.println("Choix invalide, veuillez réessayer.");
        }
    }
}
