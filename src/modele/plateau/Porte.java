package modele.plateau;

/**
 * Permet d'accéder à la salle suivante
 * @classe Porte
 * @superClasse EntiteStatique
 */
public class Porte extends EntiteStatique {

    private boolean ouverte;


    /**
     * Constructeur de Porte
     * @param _jeu
     */
    public Porte(Jeu _jeu) {
        super(_jeu);
        ouverte = false;
    }


    public boolean getOuverte() {
        return ouverte;
    }


    @Override
    public boolean traversable() {
        return ouverte;
    }


    public void porteOuverte(){
        ouverte = true;
    }
}
