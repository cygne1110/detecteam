package modele.plateau;

/**
 * C'est un piège statique qui est invisble mais qui devient visible lorsqu'on marche dessus
 * Inflige 1 point de dégat
 * @classe CasePique
 * @superClasse CasePiegeStatique
 */
public class CasePique extends CasePiegeStatique {

    /**
     * Constructeur avec paramètre
     */
    public CasePique(Jeu _jeu,char type) {
        super(_jeu,type);
    }


    /**
     * Indique si la case est traversable ou non
     */
    @Override
    public boolean traversable() {
        return true;
    }


    /**
     * Détermine les effets que la case aura sur le héros
     * Son effet est d'enlever de la vie + d'ajouter un statut au héros si la case est d'un type autre que neutre 'N'
     */
    @Override
    public void effet(Heros h) {
        h.setNbVie(h.getNbVie()-1);
        super.activer();
        if(super.getType() != 'N' && h.getTypeStatut() != super.getType()) { //Si le type de la case est autre que neutre et si le héros n'a pas déjà été touché par le type de la case
            h.setStatut(super.quelStatut(super.getType()));
            h.setTypeStatue(super.getType());
        }
    }
}
