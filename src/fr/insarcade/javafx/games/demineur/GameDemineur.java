/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insarcade.javafx.games.demineur;

import fr.insarcade.controller.ControllerData;
import fr.insarcade.javafx.core.Arcade;
import fr.insarcade.javafx.core.ArcadeGame;
import java.util.ArrayList;
import java.util.Random;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 *
 * @author David
 */
public class GameDemineur extends ArcadeGame
{
    private static double Ecran_X = Arcade.getWindowWidth()/2 - (Grille.getNbColonnes()*Grille.getTailleCote()/2);
    private static double Ecran_Y = Arcade.getWindowHeight() - ((Grille.getNbLignes()+1)*Grille.getTailleCote());
    private static double Largeur_Ecran = Grille.getNbColonnes()*Grille.getTailleCote();
    private static double Hauteur_Ecran = Grille.getNbLignes()*Grille.getTailleCote();
    private static double Vitesse_Deplacement_Souris = 250;
    private static int Nb_Niveaux = 4;
    
    private Grille grille;
    private byte difficulte; // 0 : facile ; 1 : moyen ; 2 : difficile; 3 : kamikaze
    private boolean joueur1joue; // vraie si tour du joueur 1
    private Curseur curseur;
    private ArrayList<BoutonNiveau> niveau;
    private Image[] imageGrille = new Image[4];
    private Image[] imageCurseur = new Image[3];
    private double timer;

    public GameDemineur() {
        super("Demineur", "David Albert");
        curseur = new Curseur();
        niveau = new ArrayList<>();
        for (int nb_niveau=0;nb_niveau<Nb_Niveaux;nb_niveau++){
            BoutonNiveau btn;
            if (nb_niveau == 0) 
                btn = new BoutonNiveau((2+nb_niveau)*(Hauteur_Ecran/(Nb_Niveaux+4))+nb_niveau*20, "Très facile");
            else if (nb_niveau == 1) 
                btn = new BoutonNiveau((2+nb_niveau)*(Hauteur_Ecran/(Nb_Niveaux+4))+nb_niveau*20, "Facile");
            else if (nb_niveau == 2) 
                btn = new BoutonNiveau((2+nb_niveau)*(Hauteur_Ecran/(Nb_Niveaux+4))+nb_niveau*20, "Moyen");
            else if (nb_niveau == 3) 
                btn = new BoutonNiveau((2+nb_niveau)*(Hauteur_Ecran/(Nb_Niveaux+4))+nb_niveau*20, "Difficile");
            else if (nb_niveau == 4) 
                btn = new BoutonNiveau((2+nb_niveau)*(Hauteur_Ecran/(Nb_Niveaux+4))+nb_niveau*20, "Très difficile");
            else
                btn = new BoutonNiveau((2+nb_niveau)*(Hauteur_Ecran/(Nb_Niveaux+4))+nb_niveau*20, "Kamikaze");
            niveau.add(btn);
            imageCurseur[0] = new Image("res/GameDemineur/main.png");
            imageCurseur[1] = new Image("res/GameDemineur/mainJ1.png");
            imageCurseur[2] = new Image("res/GameDemineur/mainJ2.png");
            imageGrille[0] = new Image("res/GameDemineur/mine.png");
            imageGrille[1] = new Image("res/GameDemineur/cacheTotal.png");
            imageGrille[2] = new Image("res/GameDemineur/cache2.png");
            imageGrille[3] = new Image("res/GameDemineur/decouvert.png");
            
        }
        
    }
    
    public static final double obtenirPositionEcran(boolean x){
        if (x) {
            return Ecran_X;
        }
        else {
            return Ecran_Y;
        }
    }

    public static final double obtenirDimensionEcran(boolean hauteur){
        if (hauteur) {
            return Hauteur_Ecran;
        }
        else {
            return Largeur_Ecran;
        }
    }
    
    @Override
    public void updateGame(double interpolation, ControllerData controller) {
        switch (getState()) {
            case 1 :
                choisirNiveau(interpolation,controller);
                if (difficulte != 0 ){
                    grille = new Grille(difficulte); 
                    setState(2);
                }
            break;
            case 2 :
                if (timer < 1){
                    timer += interpolation;
                }
                else {
                    choisirCaseADecouvrir(interpolation, controller);
                }
            break;
            case 3 :
                timer += interpolation;
                if (timer > 4){
                    if (grille.obtenirNombreCaseADecouvrir() == 0)
                        this.gameIsFinish(winner.None, false);
                    else if (joueur1joue)
                        this.gameIsFinish(winner.Player2, false);
                    else
                        this.gameIsFinish(winner.Player1, false);
                }
            break;
        }
    }

    private void choisirNiveau(double interpolation, ControllerData controller){
        if ((controller.isJoystick1_DOWN() || controller.isJoystick2_DOWN()) && (curseur.obtenirPositionY() + curseur.obtenirTaille()<Arcade.getWindowHeight())) 
                    curseur.changerPositionY(interpolation*Vitesse_Deplacement_Souris);
        if ((controller.isJoystick1_UP() || controller.isJoystick2_UP()) && (curseur.obtenirPositionY()>0)) 
                    curseur.changerPositionY(-interpolation*Vitesse_Deplacement_Souris);
        if ((controller.isJoystick1_RIGHT() || controller.isJoystick2_RIGHT()) && (curseur.obtenirPositionX() + curseur.obtenirTaille()< Arcade.getWindowWidth())) 
                    curseur.changerPositionX(interpolation*Vitesse_Deplacement_Souris);
        if ((controller.isJoystick1_LEFT() || controller.isJoystick2_LEFT())  && (curseur.obtenirPositionX()> 0)) 
                    curseur.changerPositionX(-interpolation*Vitesse_Deplacement_Souris);
        // cas de la sélection d'une difficulté
        if (controller.isJoystick1_ACTION() || controller.isJoystick2_ACTION()){
            if ((curseur.obtenirPositionX() > niveau.get(0).obtenirPositionX()) && (curseur.obtenirPositionX() < (niveau.get(0).obtenirPositionX()+ niveau.get(0).obtenirLargeur()))){
                if ((curseur.obtenirPositionY() > niveau.get(0).obtenirPositionY() ) && (curseur.obtenirPositionY() < niveau.get(0).obtenirPositionY()+niveau.get(0).obtenirHauteur() )){
                    difficulte = 1;
                }
                if ((curseur.obtenirPositionY() > niveau.get(1).obtenirPositionY() ) && (curseur.obtenirPositionY() < niveau.get(1).obtenirPositionY()+niveau.get(1).obtenirHauteur() )){
                    difficulte = 2;
                }
                if ((curseur.obtenirPositionY() > niveau.get(2).obtenirPositionY() ) && (curseur.obtenirPositionY() < niveau.get(2).obtenirPositionY()+niveau.get(2).obtenirHauteur() )){
                    difficulte = 3;
                }
                if ((curseur.obtenirPositionY() > niveau.get(3).obtenirPositionY() ) && (curseur.obtenirPositionY() < niveau.get(3).obtenirPositionY()+niveau.get(3).obtenirHauteur() )){
                    difficulte = 4;
                }
            }
        }
    }
    
    private void choisirCaseADecouvrir(double interpolation, ControllerData controller){
        if (joueur1joue){
            if (controller.isJoystick1_DOWN() && (curseur.obtenirPositionY() + curseur.obtenirTaille()<Arcade.getWindowHeight())) 
                    curseur.changerPositionY(interpolation*Vitesse_Deplacement_Souris);
            if (controller.isJoystick1_UP() && (curseur.obtenirPositionY()>0)) 
                    curseur.changerPositionY(-interpolation*Vitesse_Deplacement_Souris);
            if (controller.isJoystick1_RIGHT() && (curseur.obtenirPositionX() + curseur.obtenirTaille()< Arcade.getWindowWidth())) 
                    curseur.changerPositionX(interpolation*Vitesse_Deplacement_Souris);
            if (controller.isJoystick1_LEFT() && (curseur.obtenirPositionX()> 0)) 
                    curseur.changerPositionX(-interpolation*Vitesse_Deplacement_Souris);
            // cas de la sélection d'une case à découvrir
            if (controller.isJoystick1_ACTION()) {
                if ((curseur.obtenirPositionX()+Curseur.obtenirTaille()/3 <Ecran_X+ Largeur_Ecran) && (curseur.obtenirPositionX()+Curseur.obtenirTaille()/3 >Ecran_X) && (curseur.obtenirPositionY() < Ecran_Y+Hauteur_Ecran) && (curseur.obtenirPositionY() > Ecran_Y)){
                    int ligneCurseur, colonneCurseur;
                    ligneCurseur = (int) (curseur.obtenirPositionY()+Curseur.obtenirTaille()/10 - Ecran_Y)/Grille.getTailleCote();
                    colonneCurseur = (int) (curseur.obtenirPositionX()+Curseur.obtenirTaille()/3 - Ecran_X)/Grille.getTailleCote();
                    if ((grille.obtenirSymboleCase(ligneCurseur, colonneCurseur) != 10 ) && (!(grille.estDecouvert(ligneCurseur, colonneCurseur)))){
                        decouvrir(ligneCurseur,colonneCurseur);
                        if (grille.obtenirNombreCaseADecouvrir() == 0){
                            setState(3);
                        }
                        joueur1joue = false;
                    }
                    else if (grille.obtenirSymboleCase(ligneCurseur, colonneCurseur) == 10 ) {
                        setState(3);
                    }
                    
                }
                
            }
        }
        else {
            if (controller.isJoystick2_DOWN() && (curseur.obtenirPositionY() + curseur.obtenirTaille()<Arcade.getWindowHeight())) 
                    curseur.changerPositionY(interpolation*Vitesse_Deplacement_Souris);
            if (controller.isJoystick2_UP() && (curseur.obtenirPositionY()>0)) 
                    curseur.changerPositionY(-interpolation*Vitesse_Deplacement_Souris);
            if (controller.isJoystick2_RIGHT() && (curseur.obtenirPositionX() + curseur.obtenirTaille()< Arcade.getWindowWidth())) 
                    curseur.changerPositionX(interpolation*Vitesse_Deplacement_Souris);
            if (controller.isJoystick2_LEFT() && (curseur.obtenirPositionX()> 0)) 
                    curseur.changerPositionX(-interpolation*Vitesse_Deplacement_Souris);
            // cas de la sélection d'une case à découvrir
            if (controller.isJoystick2_ACTION()) {
                if ((curseur.obtenirPositionX()+Curseur.obtenirTaille()/3 <Ecran_X+ Largeur_Ecran) && (curseur.obtenirPositionX()+Curseur.obtenirTaille()/3 >Ecran_X) && (curseur.obtenirPositionY() < Ecran_Y+Hauteur_Ecran) && (curseur.obtenirPositionY() > Ecran_Y)){
                    int ligneCurseur, colonneCurseur;
                    ligneCurseur = (int) (curseur.obtenirPositionY()+Curseur.obtenirTaille()/10 - Ecran_Y)/Grille.getTailleCote();
                    colonneCurseur = (int) (curseur.obtenirPositionX()+Curseur.obtenirTaille()/3 - Ecran_X)/Grille.getTailleCote();
                    if ((grille.obtenirSymboleCase(ligneCurseur, colonneCurseur) != 10 ) && (!(grille.estDecouvert(ligneCurseur, colonneCurseur)))){
                        decouvrir(ligneCurseur, colonneCurseur);
                         if (grille.obtenirNombreCaseADecouvrir() == 0){
                            setState(3);
                        }
                        joueur1joue = true;
                    }
                    else if (grille.obtenirSymboleCase(ligneCurseur, colonneCurseur) == 10 )
                        setState(3);
                }
            }
        }
    }
    
    private void decouvrir(int ligneCurseur, int colonneCurseur){
        grille.decouvrirCase(ligneCurseur, colonneCurseur);
        if (grille.obtenirSymboleCase(ligneCurseur, colonneCurseur)==0) {
            
            if ((ligneCurseur<grille.getNbLignes()-1) && (! (grille.estDecouvert(ligneCurseur+1, colonneCurseur)))){
                decouvrir(ligneCurseur+1,colonneCurseur);
            }
            
            if ((colonneCurseur<grille.getNbColonnes()-1) && (! (grille.estDecouvert(ligneCurseur, colonneCurseur+1)))) {
                 decouvrir(ligneCurseur,colonneCurseur+1);
            }
            
            if ((ligneCurseur>0) && (! (grille.estDecouvert(ligneCurseur-1, colonneCurseur)))){
                decouvrir(ligneCurseur-1,colonneCurseur);
            }
            
            if ((colonneCurseur>0) && (! (grille.estDecouvert(ligneCurseur, colonneCurseur-1)))){
                decouvrir(ligneCurseur,colonneCurseur-1);
            }
            if ((ligneCurseur+1<grille.getNbLignes()) && (colonneCurseur+1<grille.getNbColonnes()) && (! (grille.estDecouvert(ligneCurseur+1, colonneCurseur+1)))){
                decouvrir(ligneCurseur+1,colonneCurseur+1);
            }
            if ((ligneCurseur+1<grille.getNbLignes()) && (colonneCurseur>0) && (! (grille.estDecouvert(ligneCurseur+1, colonneCurseur-1)))){
                decouvrir(ligneCurseur+1,colonneCurseur-1);
            }
            if ((ligneCurseur>0) && (colonneCurseur+1<grille.getNbColonnes()) && (! (grille.estDecouvert(ligneCurseur-1, colonneCurseur+1)))){
                decouvrir(ligneCurseur-1,colonneCurseur+1);
            }
            if ((ligneCurseur>0) && (colonneCurseur>0) && (! (grille.estDecouvert(ligneCurseur-1, colonneCurseur-1)))){
                decouvrir(ligneCurseur-1,colonneCurseur-1);
            }
             
            
        } 
    }
    
    @Override
    public void renderGame(GraphicsContext drawContext) {
         switch (getState()) {
            case 0:
            case 1 :
                
                drawContext.drawImage(this.getGameBackground(), 0, 0, Arcade.getWindowWidth(), Arcade.getWindowHeight());
                for (int nb_niveau = 0 ; nb_niveau<Nb_Niveaux;nb_niveau++){   
                    
                    drawContext.setFill(Color.SADDLEBROWN);
                    drawContext.fillRect(niveau.get(nb_niveau).obtenirPositionX(), niveau.get(nb_niveau).obtenirPositionY(), niveau.get(nb_niveau).obtenirLargeur(), niveau.get(nb_niveau).obtenirHauteur() );
                    
                    drawContext.setFill(Color.PERU);
                    drawContext.fillRect(niveau.get(nb_niveau).obtenirPositionX()+4, niveau.get(nb_niveau).obtenirPositionY()+4, niveau.get(nb_niveau).obtenirLargeur()-8, niveau.get(nb_niveau).obtenirHauteur()-8 );
                }
                drawContext.setFont(Arcade.FONT_40());
                drawContext.setFill(Color.BLACK);
                for (int nb_niveau = 0 ; nb_niveau<Nb_Niveaux;nb_niveau++){   
                    drawContext.fillText(niveau.get(nb_niveau).obtenirNom(), niveau.get(nb_niveau).obtenirPositionX()+niveau.get(nb_niveau).obtenirLargeur()*0.4, niveau.get(nb_niveau).obtenirPositionY()+niveau.get(nb_niveau).obtenirHauteur()*0.7);
                }
                drawContext.setFont(Arcade.FONT_50());
                drawContext.fillText("Choix Difficulté",Arcade.getWindowWidth()*0.35,Arcade.getWindowHeight()*0.25);
             
                drawContext.drawImage(imageCurseur[0],curseur.obtenirPositionX(), curseur.obtenirPositionY(), curseur.obtenirTaille(), curseur.obtenirTaille());
            break;
            
            case 2 :
                
                drawContext.drawImage(this.getGameBackground(), 0, 0, Arcade.getWindowWidth(), Arcade.getWindowHeight());
                
                afficherGrille(drawContext, true);
                drawContext.setFont(Arcade.FONT_50());
                if (joueur1joue) {
                    drawContext.setFill(Color.RED);
                    drawContext.fillText("JOUEUR 1", Arcade.getWindowWidth()*0.4, Arcade.getWindowHeight()*0.1);
                    drawContext.drawImage(imageCurseur[1], curseur.obtenirPositionX(), curseur.obtenirPositionY(), curseur.obtenirTaille(), curseur.obtenirTaille());
                }
                    
                else  {
                    drawContext.setFill(Color.BLUE);
                    drawContext.fillText("JOUEUR 2", Arcade.getWindowWidth()*0.4, Arcade.getWindowHeight()*0.1);
                    drawContext.drawImage(imageCurseur[2], curseur.obtenirPositionX(), curseur.obtenirPositionY(), curseur.obtenirTaille(), curseur.obtenirTaille());
                }
            break;
            case 3 :
                drawContext.drawImage(this.getGameBackground(), 0, 0, Arcade.getWindowWidth(), Arcade.getWindowHeight());
                afficherGrille(drawContext, false);
                drawContext.setFont(Arcade.FONT_50());
                drawContext.setFill(Color.ORANGERED);
                drawContext.fillText("FIN PARTIE", Arcade.getWindowWidth()*0.4, Arcade.getWindowHeight()*0.1);
            break;
            
    }
    
}
    private void afficherGrille(GraphicsContext drawContext, boolean normal){
                
                
                drawContext.setFont(Arcade.FONT_40());
                int ligneCurseur, colonneCurseur;
                ligneCurseur = (int) (curseur.obtenirPositionY()+Curseur.obtenirTaille()/10 - Ecran_Y)/Grille.getTailleCote();
                colonneCurseur = (int) (curseur.obtenirPositionX()+Curseur.obtenirTaille()/3 - Ecran_X)/Grille.getTailleCote();
                drawContext.drawImage(imageGrille[1],Ecran_X , Ecran_Y );
                for (int ligne = 0; ligne < Grille.getNbLignes(); ligne++){
                    for (int colonne =0 ; colonne < Grille.getNbColonnes(); colonne++){
                        
                        if ((! grille.estDecouvert(ligne, colonne)) && (normal) ){
                            
                            if (ligne == ligneCurseur && colonne == colonneCurseur){
                                drawContext.drawImage(imageGrille[2],Ecran_X + colonne*Grille.getTailleCote(), Ecran_Y + ligne*Grille.getTailleCote());
                            }
                            else {
                                
                            }
                        }
                        
                        else {
                            
                            if (grille.obtenirSymboleCase(ligne, colonne) != 10){
                                drawContext.drawImage(imageGrille[3],Ecran_X + colonne*Grille.getTailleCote(), Ecran_Y + ligne*Grille.getTailleCote());
                                if (grille.obtenirSymboleCase(ligne, colonne) != 0){
                                    changerCouleur(drawContext,grille.obtenirSymboleCase(ligne, colonne));
                                    drawContext.fillText(""+grille.obtenirSymboleCase(ligne, colonne), Ecran_X + colonne*Grille.getTailleCote()+Grille.getTailleCote()*0.25, Ecran_Y + ligne*Grille.getTailleCote()+Grille.getTailleCote()*0.85);
                                }
                            }
                            else if ((grille.obtenirSymboleCase(ligne, colonne) == 10)){
                                drawContext.drawImage(imageGrille[0], Ecran_X + colonne*Grille.getTailleCote(), Ecran_Y + ligne*Grille.getTailleCote(),Grille.getTailleCote(),Grille.getTailleCote());
                            }
                            
                            
                        }
                        
                        
                    }
                }
    }
    
    private void changerCouleur(GraphicsContext drawContext, int numero){
        switch (numero){
            case 0:
            case 1: drawContext.setFill(Color.BLUE); break;
            case 2: drawContext.setFill(Color.GREEN); break;
            case 3: drawContext.setFill(Color.RED); break;
            case 4: drawContext.setFill(Color.DARKBLUE); break;
            case 5: drawContext.setFill(Color.DARKGREEN); break;
            case 6: 
            case 7:
            case 8: drawContext.setFill(Color.DARKRED); break;
            
        }
    }

    @Override
    public void resetGame() {
         curseur.initCurseur();
         Random rand = new Random();
         joueur1joue = rand.nextBoolean();
         difficulte = 0;
         timer =0;
         
    }
}
