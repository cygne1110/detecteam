package modele.plateau;

/**
 * AntiPoison retire le poison du joueur
 * @classe AntiPoison
 * @superClasse ObjetUtilisable
 */
public class AntiPoison extends ObjetUtilisable{
    public AntiPoison() {
        super();
    }


    /**
     * Retire les effets du poison au héros
     * @param h
     */
    @Override
    public void effet(Heros h) {
        if(h.getStatut() instanceof StatutPoison){
            h.setStatut(null);
            h.setTypeStatue('N');
        }
    }
}
