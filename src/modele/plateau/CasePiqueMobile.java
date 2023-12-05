package modele.plateau;

/**
 * C'est un piège qui est tantôt visible tantôt invsible
 * Lorsqu'il est visible il inflige des dégats
 * @classe CasePiqueMobile (ABSTRAITE)
 * @superClasse CasePiegeMobile
 */
public class CasePiqueMobile extends CasePiegeMobile{

    /**
     * Constructeur avec paramètre
     */
    public CasePiqueMobile(Jeu _jeu,int modulo) {
        super(_jeu,modulo);
    }


    /**
     * Détermine si la case est visible ou non en fonction du tour auquel on est dans le jeu
     */
    @Override
    public void visible(int tour) {
        if(tour%modulo==0){
            super.activer();
        }
        else{
            super.desactiver();
        }
    }


    /**
     * Détermine les effets que le piège aura sur le joueur
     * Son effet est d'enlever de la vie au héros
     * @retun void
     */
    @Override
    public void effet(Heros h) {
        h.setNbVie(h.getNbVie()-1);
    }
}
