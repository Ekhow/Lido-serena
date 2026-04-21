import java.util.Scanner;
public class Restau{

    public static void proposerChoix(){

        System.out.println("------------------");
        System.out.println("1. Afficher les details d'un produit");
        System.out.println("2. Réaliser le payement d'une table");
        System.out.println("3. Utiliser la casse de secours");
        System.out.println("4. Quitter");
        System.out.println("------------------");
        System.out.println("Choisir entre 1 et 4 : ");

    }
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        while (true) {

            proposerChoix(); //Affiche les différents choix possible et recupere l entree utilisateur
            int choix = sc.nextInt();

            while(choix < 1 || choix > 4){ // Verification de l entree utilisateur
                System.out.println("Merci de choisir un chiffre entre 1 et 4");
                proposerChoix();
                choix= sc.nextInt();
            }

            if(choix == 1) { //Afficher les détails d’un produit

            } else if (choix == 2) { //Réaliser le paiement d’une table

            } else if (choix == 3) { //Utiliser la caisse de secours

            } else { //Quitter le programme
                System.out.println("Au revoir !");
                break;
            }
        }
    }
}
