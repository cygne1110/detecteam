package VueControleur;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;


import modele.plateau.*;


/** Cette classe a deux fonctions :
 *  (1) Vue : proposer une représentation graphique de l'application (cases graphiques, etc.)
 *  (2) Controleur : écouter les évènements clavier et déclencher le traitement adapté sur le modèle (flèches direction, etc.))
 *
 */
public class VueControleur extends JFrame implements Observer {
    private Jeu jeu; // référence sur une classe de modèle : permet d'accéder aux données du modèle pour le rafraichissement, permet de communiquer les actions clavier (ou souris)

    private final int sizeX; // taille de la grille affichée
    private final int sizeY; // taille de la grille affichée

    // icones affichées dans la grille
    private ImageIcon icoHero;
    private ImageIcon icoCaseNormale;
    private ImageIcon icoMur;
    private ImageIcon icoCaseUniqueUtilise;
    private ImageIcon icoCaseUnique;
    private ImageIcon icoPorteBleueFermee;
    private ImageIcon icoPorteBleueOuverte;
    private ImageIcon icoPorteVerteOuverte;
    private ImageIcon icoPorteVerteFermee;
    private ImageIcon icoPorteRougeOuverte;
    private ImageIcon icoPorteRougeFermee;
    private ImageIcon icoCapsuleEau;
    private ImageIcon icoCle;
    private ImageIcon icoCoffre;
    private ImageIcon icoCoffreOuvert;
    private ImageIcon icoVie;
    private ImageIcon icoPique;
    private ImageIcon icoPiquePoison;
    private ImageIcon icoCaseVide;
    private ImageIcon icoAntiPoison;

    //ImageIcon des leviers
    private ImageIcon icoLevierBleuActive;
    private ImageIcon icoLevierBleuDesactive;
    private ImageIcon icoLevierRougeActive;
    private ImageIcon icoLevierRougeDesactive;
    private ImageIcon icoLevierVertActive;
    private ImageIcon icoLevierVertDesactive;

    private JLabel[][] tabJLabel; // cases graphique (au moment du rafraichissement, chaque case va être associée à une icône, suivant ce qui est présent dans le modèle)
    private JLabel nbCle; // Une case graphique qui va contenir le nombre de clé possédé par le joueur
    private JLabel nbCapsule; // Une case graphique qui va contenir le nombre capsule possédé par le joueur
    private JLabel nbVie; // Une case graphique qui va contenir le nombre de vie du joueur
    private JLabel etat; // Une case graphique qui va contenir l'état du joueur (son statut : neutre,empoisonné,...)

    ///////////////////////////////////////////////////////////////////////////
    //Constructeurs
    ///////////////////////////////////////////////////////////////////////////

    public VueControleur(Jeu _jeu) {
        sizeX = Jeu.SIZE_X;
        sizeY = Jeu.SIZE_Y;
        jeu = _jeu;

        chargerLesIcones();
        placerLesComposantsGraphiques();
        ajouterEcouteurClavier();
    }



    ///////////////////////////////////////////////////////////////////////////
    //Les fonctions qui gère les évenements (Partie controleur)
    ///////////////////////////////////////////////////////////////////////////

    // Permet d'écouter certaine entrée clavier
    // Appelé dans le constructeur
    private void ajouterEcouteurClavier() {
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {  // on regarde quelle touche a été pressée

                    case KeyEvent.VK_LEFT -> {
                        jeu.getTour().incr();
                        jeu.actPiegeMobile();
                        //jeu.getHeros().getDeplacerHero().gauche();
                    }
                    case KeyEvent.VK_RIGHT -> {
                        jeu.getTour().incr();
                        jeu.actPiegeMobile();
                        //jeu.getHeros().getDeplacerHero().droite();
                    }
                    case KeyEvent.VK_DOWN -> {
                        jeu.getTour().incr();
                        jeu.actPiegeMobile();
                        //jeu.getHeros().getDeplacerHero().bas();
                    }
                    case KeyEvent.VK_UP -> {
                        jeu.getTour().incr();
                        jeu.actPiegeMobile();
                        //jeu.getHeros().getDeplacerHero().haut();
                    }
                    case KeyEvent.VK_C -> {
                        jeu.getTour().incr();
                        jeu.actPiegeMobile();
                        //jeu.getHeros().getAction().lancerCapsule();
                    }
                    case KeyEvent.VK_O -> {
                        jeu.getTour().incr();
                        jeu.actPiegeMobile();
                        //jeu.getHeros().getAction().ouvrirPorte();
                    }
                    case KeyEvent.VK_SPACE ->{
                        //jeu.getHeros().getAction().sauter();
                    }
                    case KeyEvent.VK_R ->{
                        jeu.restart();
                    }
                }
            }
        });
    }



    ///////////////////////////////////////////////////////////////////////////
    //Les fonctions (Partie Vue)
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Charge toutes les icônes
     */
    private void chargerLesIcones() {
        for(int i = 0; i < 4; i++) {
            if (jeu.getHeros(i).getCouleur() == 'V') {
                icoHero = chargerIcone("Images/agent_vert_est.png");
            } else {
                icoHero = chargerIcone("Images/agent_rouge_est.png");
            }
        }
        icoCaseNormale = chargerIcone("Images/Sol.png");
        icoMur = chargerIcone("Images/Mur.png");
        icoCaseUnique = chargerIcone("Images/Piege_de_feu.png");
        icoCaseUniqueUtilise = chargerIcone("Images/Feu.png");

        //Icones des portes
        icoPorteBleueOuverte = chargerIcone("Images/porte_bleue_ouverte.png");
        icoPorteBleueFermee = chargerIcone("Images/porte_bleue_fermee.png");
        icoPorteVerteOuverte = chargerIcone("Images/porte_verte_ouverte.png");
        icoPorteVerteFermee = chargerIcone("Images/porte_verte_fermee.png");
        icoPorteRougeOuverte = chargerIcone("Images/porte_rouge_ouverte.png");
        icoPorteRougeFermee = chargerIcone("Images/porte_rouge_fermee.png");

        icoCapsuleEau = chargerIcone("Images/Capsule_eau.png");
        icoCle = chargerIcone("Images/Cle.png");
        icoCoffre = chargerIcone("Images/Coffre.png");
        icoCoffreOuvert = chargerIcone("Images/CoffreOuvert.png");
        icoVie = chargerIcone("Images/coeur.png");
        icoPique = chargerIcone("Images/pique.png");
        icoPiquePoison = chargerIcone("Images/piquePoison.png");
        icoCaseVide = chargerIcone("Images/Vide.png");
        icoAntiPoison = chargerIcone("Images/Potion_Anti_Poison.png");

        //Icones des leviers
        icoLevierBleuActive = chargerIcone("Images/levier_bleu_active.png");
        icoLevierBleuDesactive = chargerIcone("Images/levier_bleu_desactive.png");
        icoLevierRougeActive = chargerIcone("Images/levier_rouge_active.png");
        icoLevierRougeDesactive = chargerIcone("Images/levier_rouge_desactive.png");
        icoLevierVertActive = chargerIcone("Images/levier_vert_active.png");
        icoLevierVertDesactive = chargerIcone("Images/levier_vert_desactive.png");
    }


    /**
     * Charge l'image du héro en fonction de son orientation
     */
    private void chargerHero(){
        for(int i = 0; i < 4; i++) {
            char orientation = jeu.getHeros(i).getOrientation();
            switch (orientation) {
                case 'E':
                    if (jeu.getHeros(i).getCouleur() == 'V') {
                        icoHero = chargerIcone("Images/agent_vert_est.png");
                    } else {
                        icoHero = chargerIcone("Images/agent_rouge_est.png");
                    }
                    break;
                case 'O':
                    if (jeu.getHeros(i).getCouleur() == 'V') {
                        icoHero = chargerIcone("Images/agent_vert_ouest.png");
                    } else {
                        icoHero = chargerIcone("Images/agent_rouge_ouest.png");
                    }
                    break;
                case 'N':
                    if (jeu.getHeros(i).getCouleur() == 'V') {
                        icoHero = chargerIcone("Images/agent_vert_nord.png");
                    } else {
                        icoHero = chargerIcone("Images/agent_rouge_nord.png");
                    }
                    break;
                case 'S':
                    if (jeu.getHeros(i).getCouleur() == 'V') {
                        icoHero = chargerIcone("Images/agent_vert_sud.png");
                    } else {
                        icoHero = chargerIcone("Images/agent_rouge_sud.png");
                    }
                    break;
                default:
                    break;
            }
        }
    }


    private ImageIcon chargerIcone(String urlIcone) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(new File(urlIcone));
        } catch (IOException ex) {
            Logger.getLogger(VueControleur.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return new ImageIcon(image);
    }


    /**
     * Créer la fenêtre de jeu
     */
    private void placerLesComposantsGraphiques() {
        setTitle("DetecTeam");
        //setSize(390, 275);
        setSize(1025, 605);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre

       // On créer le conteneur grille
        JComponent grilleJLabels = new JPanel(new GridLayout(sizeY, sizeX)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille
        tabJLabel = new JLabel[sizeX][sizeY];

        // On remplit la grille
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                JLabel jlab = new JLabel();
                tabJLabel[x][y] = jlab; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )
                grilleJLabels.add(jlab);
            }
        }

        // On créer un conteneur qui contiendra les infos sur héros
        JPanel partieInfo = new JPanel(new BorderLayout());

        // On créer un conteneur qui contiendra les infos sur les clé et les vies
        JPanel conteneurCleEtVie = new JPanel(new BorderLayout());
        conteneurCleEtVie.setBackground(Color.decode("#68afad"));

        // On créer un conteneur qui contiendra les infos sur les capsules et l'état du héros
        JPanel conteneurCapsuleEtEtat = new JPanel(new BorderLayout());
        conteneurCapsuleEtEtat.setBackground(Color.decode("#68afad"));

        // On créer les conteneurs vie,cle,capsule,etat
        JPanel conteneurCle = new JPanel(new FlowLayout(FlowLayout.LEFT));
        conteneurCle.setBackground(Color.decode("#68afad"));
        JPanel conteneurCapsule = new JPanel(new FlowLayout(FlowLayout.LEFT));
        conteneurCapsule.setBackground(Color.decode("#68afad"));
        JPanel conteneurVie = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        conteneurVie.setBackground(Color.decode("#68afad"));
        JPanel conteneurEtat = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        conteneurVie.setBackground(Color.decode("#68afad"));

        // On créer les conteneurs pour les icônes
        JLabel cle = new JLabel();
        cle.setIcon(icoCle);
        JLabel capsule = new JLabel();
        capsule.setIcon(icoCapsuleEau);
        JLabel vie = new JLabel();
        vie.setIcon(icoVie);

        // On créer les conteneurs pour les textes
        //nbCapsule = new JLabel("Nombre de capsule eau : "+jeu.getHeros().getSac_a_dos().getNbCapsuleEau());
        //nbCle = new JLabel("Nombre de clé : "+jeu.getHeros().getSac_a_dos().getNbCle());
        //nbVie = new JLabel("Vie : "+jeu.getHeros().getNbVie());
        //etat = new JLabel("Etat du joueur : "+jeu.getHeros().getTypeStatut());


        //On assemble tous les conteneurs
        conteneurCle.add(cle);
        //conteneurCle.add(nbCle);

        conteneurCapsule.add(capsule);
        //conteneurCapsule.add(nbCapsule);

        conteneurVie.add(vie);
        //conteneurVie.add(nbVie);

        // conteneurEtat.add(etat);

        conteneurCleEtVie.add(BorderLayout.WEST,conteneurCle);
        conteneurCleEtVie.add(BorderLayout.EAST,conteneurVie);

        conteneurCapsuleEtEtat.add(BorderLayout.WEST,conteneurCapsule);
        conteneurCapsuleEtEtat.add(BorderLayout.EAST,conteneurEtat);


        partieInfo.add(BorderLayout.NORTH,conteneurCleEtVie);
        partieInfo.add(BorderLayout.SOUTH, conteneurCapsuleEtEtat);

        getContentPane().add(BorderLayout.NORTH, partieInfo);
        getContentPane().add(BorderLayout.CENTER, grilleJLabels);
    }

    /**
     * Met à jour les informations du héros
     */
    void mettreAJourHero(){
        chargerHero(); // Met à jour l'image du héro en fonction de son orientation
        for(int i = 0; i < 4; i++) {
            tabJLabel[jeu.getHeros(i).getX()][jeu.getHeros(i).getY()].setIcon(icoHero);
        }
        //nbCapsule.setText("Nombre de capsule eau : "+jeu.getHeros().getSac_a_dos().getNbCapsuleEau()); // Met à jour son nombre de capsule d'eau
        //nbCle.setText("Nombre de clé : "+jeu.getHeros().getSac_a_dos().getNbCle()); // Met à jour son nombre de clé
        //nbVie.setText("Vie : "+jeu.getHeros().getNbVie()); // Met à jour son nombre de vie
        //etat.setText("Etat du héro  : "+jeu.getHeros().getTypeStatut());
    }


    /**
     * Il y a une grille du côté du modèle ( jeu.getGrille() ) et une grille du côté de la vue (tabJLabel)
     */
    private void mettreAJourAffichage() {

        for (int x = 0; x < sizeX; x++)
        {
            for (int y = 0; y < sizeY; y++)
            {
				EntiteStatique e = jeu.getEntite(x, y);
                if (e instanceof Mur) {
                    tabJLabel[x][y].setIcon(icoMur);
                }
                else if (e instanceof CaseNormale) {
                    if(((CaseNormale)e).getObjet() == null ) {
                        tabJLabel[x][y].setIcon(icoCaseNormale);
                    }
                    else if(((CaseNormale)e).getObjet() instanceof Cle) {
                        tabJLabel[x][y].setIcon(icoCle);
                    }
                    else if(((CaseNormale)e).getObjet() instanceof CapsuleEau) {
                        tabJLabel[x][y].setIcon(icoCapsuleEau);
                    }
                    else if(((CaseNormale)e).getObjet() instanceof Coeur) {
                        tabJLabel[x][y].setIcon(icoVie);
                    }
                    else if(((CaseNormale)e).getObjet() instanceof AntiPoison) {
                        tabJLabel[x][y].setIcon(icoAntiPoison);
                    }
                    else if(((CaseNormale)e).getObjet() instanceof Coffre){
                        if(((Coffre) ((CaseNormale)e).getObjet()).getCoffreObjet().getNbObjetActuel() == 0){
                            tabJLabel[x][y].setIcon(icoCoffreOuvert);
                        }
                        else{
                            tabJLabel[x][y].setIcon(icoCoffre);
                        }

                    }

                }

                else if (e instanceof CasePiege) { // Case piege
                    if (!((CasePiege) e).getActiver()) {  // Case piege pas encore utilisé
                        if(e instanceof CaseUnique){
                            tabJLabel[x][y].setIcon(icoCaseUnique);
                        }
                        else if(e instanceof CasePique){
                            tabJLabel[x][y].setIcon(icoCaseNormale);
                        }
                        else if(e instanceof CasePiqueMobile){
                            tabJLabel[x][y].setIcon(icoCaseNormale);
                        }
                        else if(e instanceof CaseVide){
                            tabJLabel[x][y].setIcon(icoCaseVide);
                        }

                    }
                    else { // Case piege utilisé
                        if(e instanceof CaseUnique){
                            tabJLabel[x][y].setIcon(icoCaseUniqueUtilise);
                        }
                        else if(e instanceof CasePique){
                            tabJLabel[x][y].setIcon(icoPique);
                        }
                        else if(e instanceof CasePiqueMobile){
                            tabJLabel[x][y].setIcon(icoPique);
                        }
                    }
                }
                else if(e instanceof Porte) { // Porte
                    if(((Porte) e).getOuverte()){
                        if((((Porte) e).getCouleur()) == 'R') {
                            tabJLabel[x][y].setIcon(icoPorteRougeOuverte);
                        }
                        if((((Porte) e).getCouleur()) == 'V') {
                            tabJLabel[x][y].setIcon(icoPorteVerteOuverte);
                        }
                        if((((Porte) e).getCouleur()) == 'B') {
                            tabJLabel[x][y].setIcon(icoPorteBleueOuverte);
                        }
                    }
                    else{
                        if((((Porte) e).getCouleur()) == 'R') {
                            tabJLabel[x][y].setIcon(icoPorteRougeFermee);
                        }
                        if((((Porte) e).getCouleur()) == 'V') {
                            tabJLabel[x][y].setIcon(icoPorteVerteFermee);
                        }
                        if((((Porte) e).getCouleur()) == 'B') {
                            tabJLabel[x][y].setIcon(icoPorteBleueFermee);
                        }
                    }
                }
                else if(e instanceof Levier) { //Levier
                    if(((Levier) e).getActive()){
                        if((((Levier) e).getCouleur()) == 'R'){
                            tabJLabel[x][y].setIcon(icoLevierRougeActive);
                        }
                        if((((Levier) e).getCouleur()) == 'V'){
                            tabJLabel[x][y].setIcon(icoLevierVertActive);
                        }
                        if((((Levier) e).getCouleur()) == 'B'){
                            tabJLabel[x][y].setIcon(icoLevierBleuActive);
                        }
                    }
                    else{
                        if((((Levier) e).getCouleur()) == 'R'){
                            tabJLabel[x][y].setIcon(icoLevierRougeDesactive);
                        }
                        if((((Levier) e).getCouleur()) == 'V'){
                            tabJLabel[x][y].setIcon(icoLevierVertDesactive);
                        }
                        if((((Levier) e).getCouleur()) == 'B'){
                            tabJLabel[x][y].setIcon(icoLevierBleuDesactive);
                        }
                    }
                }
            }
        }
        mettreAJourHero();
    }



    @Override
    public void update(Observable o, Object arg) {
        mettreAJourAffichage();
        /*
        SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mettreAJourAffichage();
                    }
                }); 
        */

    }
}
