/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

import java.awt.*;
import java.util.*;

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
    private int state = 0; // Etat de l'agent
    /*
    *  0 -> état d'exploration sans but
    *  1 -> état d'exploration avec but
    *  2 -> état de déplacement où le robot connait le chemin (pas trouvé de meilleur nom)
    */

    private int objective = -1; // Objectif (-1 = Rien, 0 = Porte, 1 = Levier)
    private int objectiveID = -1; // Indice de l'objectif
    public boolean explored[][] = new boolean[20][10]; // Cases connues du héro
    // -> Communication entre les agents (OU des maps)
    private EntiteStatique grid[][];

    private char couleur;
    private int indice;
    private Coord2D destination;

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
    public Heros(Jeu _jeu, int _x, int _y, int _indice, char _couleur) {
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
        grid = jeu.getGrilleEntitesStatiques();
        for(int i = 0; i < jeu.SIZE_X; i++) {
            for(int j = 0; j < jeu.SIZE_Y; j++) {
                explored[i][j] = false;
            }
        }
        explored[x][y] = true;
        indice = _indice;
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

    public int getIndice() {return indice;}



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
                //jeu.getSalle().salleAleatoire(jeu.getHeros()); //On appelle la méthode qui créer les salles aléatoirement
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

    private Queue<Coord2D> fetchVoisins(Coord2D node) {
        Queue<Coord2D> res = new LinkedList<Coord2D>();
        if(node.x - 1 > 0) {
            if(grid[node.x-1][node.y].traversable() && !(grid[node.x-1][node.y] instanceof Levier)) {
                res.add(new Coord2D(node.x - 1, node.y));
            }
        }
        if(node.x + 1 < jeu.SIZE_X) {
            if(grid[node.x+1][node.y].traversable() && !(grid[node.x+1][node.y] instanceof Levier)) {
                res.add(new Coord2D(node.x + 1, node.y));
            }
        }
        if(node.y - 1 > 0) {
            if(grid[node.x][node.y-1].traversable() && !(grid[node.x][node.y-1] instanceof Levier)) {
                res.add(new Coord2D(node.x, node.y - 1));
            }
        }
        if(node.y + 1 < jeu.SIZE_Y) {
            if(grid[node.x][node.y+1].traversable() && !(grid[node.x][node.y+1] instanceof Levier)) {
                res.add(new Coord2D(node.x, node.y + 1));
            }
        }
        return res;
    }

    private boolean containsLess(Queue<Coord2D> queue, Coord2D elt) {
        if(contains(queue, elt)) return false;
        Queue<Coord2D> copy = new LinkedList<Coord2D>(queue);
        while(!copy.isEmpty()) {
            Coord2D e = copy.remove();
            if(elt.equals(e) && e.cout < elt.cout) return true;
        }
        return false;
    }

    private boolean contains(Queue<Coord2D> queue, Coord2D elt) {
        Queue<Coord2D> copy = new LinkedList<Coord2D>(queue);
        while(!copy.isEmpty()) {
            Coord2D e = copy.remove();
            if(elt.equals(e)) return true;
        }
        return false;
    }

    private Queue<Coord2D> reconstructPath(Map<Coord2D, Coord2D> cameFrom, Coord2D dest) {
        Stack<Coord2D> tmp = new Stack<Coord2D>();
        Coord2D curr = dest;
        tmp.push(curr);
        while(cameFrom.containsKey(curr)) {
            curr = cameFrom.get(curr);
            tmp.push(curr);
        }
        Queue<Coord2D> path = new LinkedList<Coord2D>();
        while(!tmp.isEmpty()) {
            path.add(tmp.pop());
        }
        path.remove();
        return path;
    }

    private void printQueue(Queue<Coord2D> queue) {
        Queue<Coord2D> copy = new LinkedList<Coord2D>(queue);
        while(!copy.isEmpty()) {
            Coord2D print = copy.remove();
            System.out.println(print.x + " " + print.y);
        }
    }

    public Queue<Coord2D> pathFind(Coord2D dest) {
        Comparator<Coord2D> comparator = new Coord2DComparator();
        PriorityQueue<Coord2D> openList = new PriorityQueue<Coord2D>(comparator);
        Queue<Coord2D> closedList = new LinkedList<Coord2D>();
        Map<Coord2D, Coord2D> cameFrom = new HashMap<Coord2D, Coord2D>();
        Coord2D start = new Coord2D(x, y, 0, 0);
        assert(grid[dest.x][dest.y].traversable());

        openList.add(start);
        while(!openList.isEmpty()) {
            Coord2D tmp = openList.remove();
            closedList.add(tmp);
            if(tmp.equals(dest)) {
                return reconstructPath(cameFrom, tmp);
            }
            Queue<Coord2D> voisins = fetchVoisins(tmp);
            for(Coord2D v: voisins) {
                if (contains(closedList, v) || containsLess(openList, v)) {
                    continue;
                }
                v.cout = tmp.cout + 1;
                v.manhattanDistance(v, dest);
                v.heuristique += v.cout;
                cameFrom.put(v, tmp);
                openList.add(v);
            }
        }
        System.out.println("pathFind did not find a path");
        return null;

    }

    public boolean moveTo(Coord2D dest) {
        // assert(grid[dest.x][dest.y].traversable());
        Coord2D delta = new Coord2D(dest.x - x, dest.y - y);
        System.out.println(delta.x + " " + delta.y);
        if(delta.x == 0 && delta.y != 0) {
            if(delta.y < 0) {
                deplacerHero.haut();
                return true;
            } else {
                deplacerHero.bas();
                return true;
            }
        } else if(delta.y == 0) {
            if(delta.x < 0) {
                deplacerHero.gauche();
                return true;
            } else {
                deplacerHero.droite();
                return true;
            }
        } else {
            return false;
        }
    }

    public int checkClear(Coord2D target) {
        return jeu.checkClear(target);
    }

    public void communicate(int index) {
        for(int i = 0; i < 20; i++) {
            for(int j = 0; j < 10; j++) {
                explored[i][j] = explored[i][j] || jeu.getHeros(index).explored[i][j];
                jeu.getHeros(index).explored[i][j] = explored[i][j] || jeu.getHeros(index).explored[i][j];
            }
        }
    }

    private boolean info(Coord2D target) {
        if(grid[target.x][target.y] instanceof Levier || grid[target.x][target.y] instanceof Porte)
            return true;
        int i = checkClear(target);
        if(i != -1) {
            communicate(i);
        }
        return false;
    }

    private void action(Coord2D target) {
        if(state == 0) {
            if (grid[target.x][target.y] instanceof Porte) {
                if (((Porte) grid[target.x][target.y]).getCouleur() == couleur && !((Porte) grid[target.x][target.y]).getOuverte()) {
                    state = 1;
                    objective = 1;
                    objectiveID = ((Porte) grid[target.x][target.y]).getIndice();
                }
            } else if (grid[target.x][target.y] instanceof Levier) {
                if (((Levier) grid[target.x][target.y]).getCouleur() == couleur && !((Levier) grid[target.x][target.y]).getActive()) {
                    state = 2;
                    destination = target;
                }
            }
        } else if(state == 1) {
            if(grid[target.x][target.y] instanceof Levier && objective == 1) {
                if (((Levier) grid[target.x][target.y]).getIndice() == objectiveID && ((Levier) grid[target.x][target.y]).getCouleur() == couleur && !((Levier) grid[target.x][target.y]).getActive()) {
                    state = 2;
                    destination = target;
                }
            } else if(grid[target.x][target.y] instanceof Porte && objective == 0) {
                if (((Porte) grid[target.x][target.y]).getIndice() == objectiveID && ((Porte) grid[target.x][target.y]).getCouleur() == couleur && ((Porte) grid[target.x][target.y]).getOuverte()) {
                    state = 2;
                    destination = target;
                }
            }

        }
    }

    private void discover() {
        if(x - 1 > 0) {
            explored[x-1][y] = true;
            Coord2D target = new Coord2D(x-1, y);
            if(info(target)) {
                action(target);
            }
        }
        if(x + 1 < jeu.SIZE_X) {
            explored[x+1][y] = true;
            Coord2D target = new Coord2D(x+1, y);
            if(info(target)) {
                action(target);
            }
        }
        if(y - 1 > 0) {
            explored[x][y-1] = true;
            Coord2D target = new Coord2D(x, y-1);
            if(info(target)) {
                action(target);
            }
        }
        if(y + 1 < jeu.SIZE_Y) {
            explored[x][y+1] = true;
            Coord2D target = new Coord2D(x, y+1);
            if(info(target)) {
                action(target);
            }
        }
    }

    public void explore() {
        Random rand = new Random();
        boolean moved = false;
        // exploredThreshold =
        while(!moved) {
            int choice = rand.nextInt(0, 4);
            System.out.println(choice);
            switch(choice) {
                case 0:
                    if(x - 1 > 0 && checkClear(new Coord2D(x - 1, y)) == -1) {
                        moved = moveTo(new Coord2D(x - 1, y));
                    }
                    break;
                case 1:
                    if(x + 1 < jeu.SIZE_X && checkClear(new Coord2D(x + 1, y)) == -1) {
                        moved = moveTo(new Coord2D(x + 1, y));
                    }
                    break;
                case 2:
                    if(y - 1 > 0 && checkClear(new Coord2D(x, y-1)) == -1) {
                        moved = moveTo(new Coord2D(x, y - 1));
                    }
                    break;
                case 3:
                    if(y + 1 < jeu.SIZE_Y && checkClear(new Coord2D(x, y+1)) == -1) {
                        moved = moveTo(new Coord2D(x, y + 1));
                    }
                    break;
            }
        }
    }

    public void run() {
        discover();
        System.out.println("run:" + indice + " " + state);
        System.out.println("A");
        if(state == 0) {
            System.out.println("C");
            explore();
        } else if(state == 1) {
            explore();
        } else if(state == 2) {
            Queue<Coord2D> path = pathFind(destination);
            moveTo(path.remove());
        }
        System.out.println("B");
    }

}


