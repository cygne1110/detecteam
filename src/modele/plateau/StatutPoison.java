package modele.plateau;

/**
 * C'est le statut poison lorsqu'un joueur à ce statut il subit des dégat tous les 7 tours
 * @classe StatutPoison
 * @superClasse Statut
 */
public class StatutPoison extends Statut {
    
    /**
     * Constructeur de la classe StatutPoison
     */
    public StatutPoison() {
        super(7);
    }


    /**
     * Empoisonne le héros
     * @param h le héros
     */
    @Override
    public void effet(Heros h) {
        h.setNbVie(h.getNbVie()-1);     //Son nombre de vies diminue de 1
    }
}
