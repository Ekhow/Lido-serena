import java.util.Scanner;
public class Resto{


    public static void proposerChoix(){

        System.out.println("------------------");
        System.out.println("1. Afficher les details d'un produit");
        System.out.println("2. Réaliser le payement d'une table");
        System.out.println("3. Utiliser la casse de secours");
        System.out.println("4. Quitter");
        System.out.println("------------------");
        System.out.println("Choisi entre 1 et 4 : ");

    }
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        while (true) {

            proposerChoix();
            int choix = sc.nextInt();


            while(choix < 1 || choix > 4){

                System.out.println("Merci de choisir un chiffre entre 1 et 4");
                proposerChoix();
                choix= sc.nextInt();

            }


        }
    }

}