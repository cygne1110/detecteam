package modele.plateau;

import Util.Outils;

/**
 * C'est un objet qui peut contenir d'autre objet tel que des Capsules d'eau, Cles, Coeurs,....
 * @classe Coffre
 * @superClasse Ramassable
 */
public class Coffre extends Ramassable {
    Inventaire coffreObjet;

    /**
     * Constructeur de Coffre
     */
    public Coffre() {
        coffreObjet = new Inventaire(4);
        coffreObjet.ajoutObjet(new AntiPoison());
        for(int i = 0 ; i < 3 ; i++){
            if(Outils.nombreAleatoire(1,5)== 1){coffreObjet.ajoutObjet(new CapsuleEau());}
            else if(Outils.nombreAleatoire(1,10)==2){coffreObjet.ajoutObjet(new Cle());}
            else if(Outils.nombreAleatoire(1,5)==3){coffreObjet.ajoutObjet(new Coeur());}
        }
    }

    public Inventaire getCoffreObjet() {
        return coffreObjet;
    }
}
