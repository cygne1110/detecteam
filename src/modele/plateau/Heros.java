/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

/**
 * Héros du jeu
 * Il posséde un inventaire, un statut (empoisonné,...),un nombre de vies,...
 */
public class Heros {
    private int x; // Coordonnée du héros
    private int y; // Coordonnée du héros
    private char orientation; // Un héros a une orientation
    private Inventaire sac_a_dos; // L'inventaire du héros
    private int nbVie; // La vie du héros
    private Statut statut; // Malus ou Bonus reçu
    private char typeStatut; // Le type de malus ou bonus reçu ('P' empoisonnement,...)
    private ActionHero action; // Les actions que le héro peut faire
    private Deplacement deplacerHero; // Les déplacement que le héro peut faire

    private Jeu jeu;



    ///////////////////////////////////////////////////////////////////////////
    //Constructeurs
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Constructeur d' Heros
     * @param _jeu
     * @param _x coordonnées du heros
     * @param _y coordonnées du heros
     */
    public Heros(Jeu _jeu, int _x, int _y) {
        jeu = _jeu;
        x = _x;
        y = _y;
        sac_a_dos = new Inventaire(30);
        nbVie = 10;
        statut = null;
        typeStatut = 'N';
        action = new ActionHero(this);
        deplacerHero = new Deplacement(this);
    }



    ///////////////////////////////////////////////////////////////////////////
    //Getters
    ///////////////////////////////////////////////////////////////////////////

    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }


    public char getOrientation() {
        return orientation;
    }


    public int getNbVie() { return nbVie; }


    public Inventaire getSac_a_dos() { return sac_a_dos; }


    public Statut getStatut() { return statut; }


    public char getTypeStatut() { return typeStatut; }


    public ActionHero getAction() { return action; }


    public Deplacement getDeplacerHero() { return deplacerHero; }


    public Jeu getJeu() {
        return jeu;
    }



    ///////////////////////////////////////////////////////////////////////////
    //Setters
    ///////////////////////////////////////////////////////////////////////////

    public void setX(int x1) {
        x = x1;
    }


    public void setY(int x2) {
        y = x2;
    }


    public void setNbVie(int nbVie) { this.nbVie = nbVie;}


    public void setStatut(Statut statut) { this.statut = statut; }


    public void setTypeStatue(char typeStatut) { this.typeStatut = typeStatut; }


    public void setOrientation(char orientation) { this.orientation = orientation; }



    ///////////////////////////////////////////////////////////////////////////
    //Fonctions Case
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Si le héro est sur une case piege cela lui applique les effets liés à la case (dégat,statut,...)
     * @param x
     * @param y
     */
    public void EstSurCasePiege(int x, int y) {
        if (x > 0 && x < Jeu.SIZE_X && y > 0 && y < Jeu.SIZE_Y) {
            EntiteStatique e = jeu.getEntite(x, y);
            if (e instanceof CasePiegeStatique) {  // Un piége statique est activer donc visible si le héros marche dessus
                ((CasePiege)e).effet(this);
                ((CasePiege)e).activer();
            }
            else if(e instanceof CasePiegeMobile){ // Un piége mobile se désactive et s'active tout seule
                if(((CasePiegeMobile) e).getActiver()){
                    ((CasePiege)e).effet(this);
                }
            }
        }
    }

    /**
     * Si le héros est sur un levier alors cela l'active le levier qui ouvre la porte
     * @param x
     * @param y
     */
    public void EstSurLevier(int x,int y){
        EntiteStatique e = jeu.getEntite(x, y);
        if(e instanceof Levier){
            if(((Levier) e).getActive() == false) {
                ((Levier) e).activerLevier();
                EntiteStatique[][] GrilleEntitesStatiques = getJeu().getGrilleEntitesStatiques();
                for(int i = 0; i < getJeu().SIZE_X; i++){
                    for(int j = 0; j < getJeu().SIZE_Y; j++){
                        if(GrilleEntitesStatiques[i][j] instanceof Porte){
                            ((Porte) GrilleEntitesStatiques[i][j]).porteOuverte();
                        }
                    }
                }
            }
        }
    }

    /**
     * Si le héros est sur une porte alors cela créer aléatoirement la prochaine salle
     * @param x
     * @param y
     */
    public void EstSurPorte(int x,int y){
        if(jeu.getEntite(x, y) instanceof Porte){
            jeu.getSalle().salleAleatoire(jeu.getHeros()); //On appelle la méthode qui créer les salles aléatoirement
            resetHeroSalle();
        }
    }


    /**
     * La case possède un objet on l'ajoute à l'inventaire du héros et le supprime de la case
     * Il existe différents objets, des coffres, des objet utilisables et des objets autres tels que des clés et des capsules d'eau
     * @param x
     * @param y
     */
    public void LaCaseAUnObjet(int x, int y) {
        if (x > 0 && x < Jeu.SIZE_X && y > 0 && y < Jeu.SIZE_Y) {
            EntiteStatique e = jeu.getEntite(x, y);
            if (e instanceof CaseNormale) { //La case est une case normale
                Ramassable objet = ((CaseNormale) e).getObjet();
                if (objet != null) { // Est-ce que la case à un objet
                    if(objet instanceof ObjetUtilisable){ // Est-ce que l'objet est utilisable (Attribut un effet au héros)
                        ((ObjetUtilisable)objet).effet(this);
                        ((CaseNormale) e).setObjet(null);
                    }
                    else if (objet instanceof Coffre) { // L'objet de la case est un coffre
                        this.action.ouvertureCoffre((Coffre) objet);
                    }
                    else{ // Objet autre
                        if (this.sac_a_dos.ajoutObjet(objet)) { // Si j'ai de la place dans mon inventaire, Ajoute l'objet
                            ((CaseNormale) e).setObjet(null);
                        }
                    }
                }
            }
        }
    }



    ///////////////////////////////////////////////////////////////////////////
    //Fonctions du reset
    ///////////////////////////////////////////////////////////////////////////
    
    public void resetHero(){
        sac_a_dos.resetInventaire();
        nbVie = 10;
        statut = null;
        typeStatut = 'N';
    }


    public void resetHeroSalle(){
        sac_a_dos.resetInventaire();
        for(int i = 0 ; i < 5 ; i++){
            sac_a_dos.ajoutObjet(new CapsuleEau());
        }
    }
}


