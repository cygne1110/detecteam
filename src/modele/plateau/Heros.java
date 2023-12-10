/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

import java.awt.*;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

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

    private char couleur;

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
    public Heros(Jeu _jeu, int _x, int _y, char _couleur) {
        jeu = _jeu;
        x = _x;
        y = _y;
        sac_a_dos = new Inventaire(30);
        nbVie = 10;
        statut = null;
        typeStatut = 'N';
        action = new ActionHero(this);
        deplacerHero = new Deplacement(this);
        couleur = _couleur;
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


    public char getCouleur() { return couleur; }


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
            if(getCouleur() == ((Levier) e).getCouleur() || ((Levier) e).getCouleur() == 'B' || getCouleur() == 'B') {
                if (((Levier) e).getActive() == false) {
                    ((Levier) e).activerLevier();
                    EntiteStatique[][] GrilleEntitesStatiques = getJeu().getGrilleEntitesStatiques();
                    for (int i = 0; i < getJeu().SIZE_X; i++) {
                        for (int j = 0; j < getJeu().SIZE_Y; j++) {
                            if (GrilleEntitesStatiques[i][j] instanceof Porte) {
                                if (((Porte) GrilleEntitesStatiques[i][j]).getIndice() == ((Levier) e).getIndice()) {
                                    ((Porte) GrilleEntitesStatiques[i][j]).porteOuverte();
                                }
                            }
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
            EntiteStatique e = jeu.getEntite(x, y);
            if(((Porte) e).getFinale()) {
                jeu.getSalle().salleAleatoire(jeu.getHeros()); //On appelle la méthode qui créer les salles aléatoirement
                resetHeroSalle();
            }
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

    /*
    public Coord2D checkPath(int dest_x, int dest_y) {
        int delta;
        Coord2D lastStep = new Coord2D(0, 0);
        EntiteStatique grid[][] = jeu.getGrilleEntitesStatiques();
        if(dest_x == x) {
            lastStep.x = x;
            lastStep.y = y;
            delta = dest_y - y;
            if (delta < 0) {
                for (int i = 0; i > delta; i--) {
                    if (!(grid[x][y - i - 1].traversable()) || grid[x][y - i] instanceof Levier) return lastStep;
                    lastStep.y = y - i;
                }
                return lastStep;
            } else {
                for (int i = 0; i <= delta; i++) {
                    if (!(grid[x][y + i + 1].traversable()) || grid[x][y + i] instanceof Levier) return lastStep;
                    lastStep.y = y + i;
                }
                return lastStep;
            }
        } else if(dest_y == y) {
            lastStep.x = x;
            lastStep.y = y;
            delta = dest_x - x;
            if(delta < 0) {
                for (int i = 0; i > delta; i--) {
                    if (!(grid[x - i - 1][y].traversable()) || grid[x - i][y] instanceof Levier) return lastStep;
                    lastStep.x = x - i;
                }
                return lastStep;
            } else {
                for(int i = 0; i < delta; i++) {
                    if (!(grid[x + i + 1][y].traversable()) || grid[x + i][y] instanceof Levier) return lastStep;
                    lastStep.x = x + i;
                }
                return lastStep;
            }
        } else {
            return null;
        }
    }

    public char pathFind(int dest_x, int dest_y) {
        Coord2D tmpPos;
        tmpPos = checkPath(x, y);
        if(tmpPos == null) {
            System.out.println("checkPath returned null");
            return (char)0;
        }
        System.out.println(tmpPos.x + " " + tmpPos.y);
        Coord2D delta = new Coord2D(tmpPos.x - x, tmpPos.y - y);
        System.out.println(delta.x + " " + delta.y);
        if(delta.x == 0 && delta.y != 0) {
            if(delta.y < 0) return 'B';
            else return 'H';
        } else if(delta.y == 0) {
            if(delta.x < 0) return 'G';
            else return 'D';
        } else return pathFind(x, dest_y);
    }

    public void moveTo(int dest_x, int dest_y) {
        EntiteStatique grid[][] = jeu.getGrilleEntitesStatiques();
        if(!(grid[dest_x][dest_y].traversable())) {
            System.out.println("moveTo: you're trying to go to an inaccessible case");
            return;
        }
        char dest = pathFind(dest_x, dest_y);
        switch(dest) {
            case 'H':
                deplacerHero.haut();
                break;
            case 'B':
                deplacerHero.bas();
                break;
            case 'D':
                deplacerHero.droite();
                break;
            case 'G':
                deplacerHero.gauche();
                break;
            default:
                System.out.println("pathFind returned 0");
                return;
        }
    }
    */
    private Queue<Coord2D> fetchVoisins(Coord2D node) {
        Queue<Coord2D> res = new LinkedList<Coord2D>();
        if(node.x - 1 > 0) res.add(new Coord2D(node.x - 1, node.y));
        if(node.x + 1 < jeu.SIZE_X) res.add(new Coord2D(node.x + 1, node.y));
        if(node.y - 1 > 0) res.add(new Coord2D(node.x, node.y - 1));
        if(node.y + 1 < jeu.SIZE_Y) res.add(new Coord2D(node.x, node.y + 1));
        return res;
    }

    private boolean containsLess(Queue<Coord2D> queue, Coord2D elt) {
        if(!queue.contains(elt)) return false;
        Queue<Coord2D> copy = new LinkedList<Coord2D>(queue);
        while(!copy.isEmpty()) {
            Coord2D e = copy.remove();
            if(e.x == elt.x && e.y == elt.y && e.cout <= elt.cout) return true;
        }
        return false;
    }

    public Queue<Coord2D> pathFind(Coord2D dest) {
        EntiteStatique grid[][] = jeu.getGrilleEntitesStatiques();
        Comparator<Coord2D> comparator = new Coord2DComparator();
        PriorityQueue<Coord2D> openList = new PriorityQueue<Coord2D>(comparator);
        Queue<Coord2D> closedList = new LinkedList<Coord2D>();
        Coord2D start = new Coord2D(x, y, 0, 0);
        assert(grid[dest.x][dest.y].traversable());

        openList.add(start);
        while(!openList.isEmpty()) {
            Coord2D tmp = openList.remove();
            System.out.println(tmp.x + " " + tmp.y);
            if(tmp.x == dest.x && tmp.y == dest.y) {
                return closedList;
            }
            Queue<Coord2D> voisins = fetchVoisins(tmp);
            for(Coord2D v: voisins) {
                if(grid[v.x][v.y].traversable() && !(grid[v.x][v.y] instanceof Levier) && !(closedList.contains(v) || containsLess(openList, v))) {
                    v.cout = tmp.cout + 1;
                    v.manhattanDistance(v, dest);
                    v.heuristique += v.cout;
                    openList.add(v);
                }
            }
            closedList.add(tmp);
        }
        System.out.println("pathFind did not find a path");
        return null;

    }

    public void moveTo(Coord2D dest) {
        EntiteStatique grid[][] = jeu.getGrilleEntitesStatiques();
        assert(grid[dest.x][dest.y].traversable());
        Coord2D delta = new Coord2D(dest.x - x, dest.y - y);
        if(delta.x == 0 && delta.y != 0) {
            if(y < 0) deplacerHero.haut();
            else deplacerHero.bas();
        } else if(delta.y == 0) {
            if(x < 0) deplacerHero.gauche();
            else deplacerHero.droite();
        } else {
            System.out.println("Can't move here");
        }
    }

}


