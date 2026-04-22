import models.Servers;

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

    while((choix = proposerChoix()) != 5) {
        switch(choix) {
            case 1:
                IO.println("Détails des produits...");
                break;
            case 2:
                IO.println("Faire payer le client...");
                break;
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