package modele.plateau;

/**
 * Représente un piège qui peut apporter des malus comme des bonus selon son type et son effet
 * @classe CasePiege (ABSTRAITE)
 * @superClasse EntiteStatique
 */
public abstract class CasePiege extends EntiteStatique {
    private char type;
    private boolean activer;


    /**
     * Constructeur avec paramètre
     */
    public CasePiege(Jeu _jeu,char type) {
        super(_jeu);
        this.type = type;
    }



    ///////////////////////////////////////////////////////////////////////////
    //Getters
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Renvoie le type de la case ('P' : empoisonné, 'N' : neutre,....)
     * @return char
     */
    public char getType() { return type; }


    /**
     * Renvoie un boolean qui indique si la case est activée ou non (true or false)
     * @return boolean
     */
    public boolean getActiver() {
        return activer;
    }



    ///////////////////////////////////////////////////////////////////////////
    //Fonctions activer et désactiver
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Active la case
     */
    public void activer(){
        activer = true;
    }


    /**
     * Désactive la case
     */
    public void desactiver(){
        activer = false;
    }



    ///////////////////////////////////////////////////////////////////////////
    //Fonctions Autres
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Indique si la case est traversable ou non
     */
    @Override
    public boolean traversable() {
        return true;
    }


    /**
     * Indique le statut à renvoyer en fonction du type de la classe
     * @return new StatutPoison
     */
    public Statut quelStatut(char type){
        if(type == 'P'){ // Si le piege est EmPoisoné
            return new StatutPoison();
        }
        else{ // Si le piège est Neutre
            return null;
        }

    }


    /**
     * Méthode abstraite à définir dans les sous-classes
     * Cette fonction permet d'attribuer des effets au héros (dégats physiques,malus,bonus,...)
     */
    public abstract void effet(Heros h);
}
