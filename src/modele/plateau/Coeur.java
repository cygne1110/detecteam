package modele.plateau;

/**
 * C'est un objet qui permet de récupérer un point de vie
 * @classe Coeur
 * @superClasse ObjetUtilisable
 */

public class Coeur extends ObjetUtilisable{
    /**
     * Constructeur de Coeur
     */
    public Coeur() {

    }


    public void effet(Heros h){
        if(h.getNbVie() < 10) {
            h.setNbVie(h.getNbVie() + 1);
        }
    }
}
