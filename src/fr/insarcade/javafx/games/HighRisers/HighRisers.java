/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insarcade.javafx.games.HighRisers;

import fr.insarcade.controller.ControllerData;
import fr.insarcade.javafx.core.Arcade;
import fr.insarcade.javafx.core.ArcadeGame;
import static java.lang.Math.round;
import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 *
 * @author David
 */
public class HighRisers extends ArcadeGame {

    //CONSTANTES
    private static final int HR_X = 0;
    private static final int HR_Y = 0;
    private static double HR_WIDTH=Arcade.getWindowWidth();
    private static double HR_HEIGHT = Arcade.getWindowHeight();
    private static int STOREYS_SHOWED = 13;
    private static final double WALL_WIDTH =7;
    private static final double SPEED_X=200;
    private static final double SPEED_Y=5;
    private static final double TIME_TO_UP = 0.18;
    private static final int MAX_PLAYERS=4;
    private static final double DEADLINE =90.4;
    
    //VARIABLES
    int i=0;
    private double timer;
    private Building building;
    private ArrayList<Player> players;
    private int nb_players;
    private Image[] images;
    
    //CONSTRUCTEUR JEU
    public HighRisers() {
        super("High Risers", "David Albert");
        this.timer =0;
        this.building = new Building();
        this.nb_players =2;
        this.images = new Image[11];
        images[0]= new Image("res/HighRisers/mario_R1.png");
        images[1]= new Image("res/HighRisers/mario_R2.png");
        images[2]= new Image("res/HighRisers/mario_R3.png");
        images[3]= new Image("res/HighRisers/mario_RS.png");
        images[4]= new Image("res/HighRisers/mario_chute.png");
        images[5]= new Image("res/HighRisers/mario_L1.png");
        images[6]= new Image("res/HighRisers/mario_L2.png");
        images[7]= new Image("res/HighRisers/mario_L3.png");
        images[8]= new Image("res/HighRisers/mario_LS.png");
        
        images[10]= new Image("res/HighRisers/trophée.png");
    }

    // ACCESSEURS CONSTANTES
    public static final double getWindowX(){
        return HR_X;
    }
    
     public static double getWindowY(){
        return HR_Y;
    }
     
    public static final double getWindowWidth(){
        return HR_WIDTH;
    }
    
     public static double getWindowHeight(){
        return HR_HEIGHT;
    }
     
     public static int getNbStoreysShowed(){
        return STOREYS_SHOWED;
    }
    
     public static double getWallWidth(){
         return WALL_WIDTH;
     }
     

    //Agit en fonction du controller
    private void updateController(ControllerData controller){
            if (controller.isJoystick1_ACTION() && (! players.get(0).isDead()))
                {
                    if (!(players.get(0).isButtonPressed())){
                        players.get(0).setTimerPress(timer);
                        players.get(0).setButtonPressed();           
                    }      
                }
            else 
                {
                    if (players.get(0).isButtonPressed()) 
                        players.get(0).setButtonPressed();
                }
                
            if (controller.isJoystick2_ACTION() && (! players.get(1).isDead()))
                {
                    if (!(players.get(1).isButtonPressed())){
                        players.get(1).setTimerPress(timer);
                        players.get(1).setButtonPressed();           
                    }      
                }
            else 
                {
                    if (players.get(1).isButtonPressed()) 
                        players.get(1).setButtonPressed();
                }   
    }
    
    @Override
    public void updateGame(double interpolation, ControllerData controller) {
        switch (getState()){
               
            case 1 :
                
                updateController(controller);
                
                for (int i =0; i < nb_players;i++){
                    if (! players.get(i).isDead()){
                        
                        int crtStorey = (int) players.get(i).getCurrentStorey();
                        
                        if ((timer-players.get(i).getTimerPress() < TIME_TO_UP) && ((int) (players.get(i).getCurrentStorey()+SPEED_Y*interpolation) == crtStorey))
                            players.get(i).setCurrentStorey(players.get(i).getCurrentStorey()+SPEED_Y*interpolation);
                        else if ((timer-players.get(i).getTimerPress() >= TIME_TO_UP) && (players.get(i).getTimerSize()>0)) {
                            players.get(i).removeCurrentTimer();
                            players.get(i).setCurrentStorey(crtStorey +1);
                        }
               
                        double tmp =HR_X+i*HR_WIDTH/nb_players+(HR_WIDTH/nb_players -building.getStorey(players.get(i).getCurrentStorey()).getWidth())/2;
                        double speed =0;
                        if ((players.get(i).getCurrentStorey() - round(players.get(i).getCurrentStorey())) == 0){
                            speed = SPEED_X*interpolation;
                        }
                        else {
                            speed = SPEED_X*interpolation;
                        }
                    
                        if (players.get(i).isDirectionRight()) {
                            if (players.get(i).getPosition(0)+Storey.getStoreyHeight()<tmp+building.getStorey(players.get(i).getCurrentStorey()).getWidth())
                                players.get(i).setPosX(speed);
                            else {
                                if (building.getWall(3, players.get(i).getCurrentStorey()) && (players.get(i).getPosition(0)+Storey.getStoreyHeight()-players.get(i).getSize()/5<tmp+building.getStorey(players.get(i).getCurrentStorey()).getWidth()))
                                    players.get(i).changeDirection();
                                else {
                                    players.get(i).dead(i,nb_players, timer);
                                }
                            }
                        }
                
                        else {
                            if (players.get(i).getPosition(0)>tmp)
                                players.get(i).setPosX(-speed);
                            else {
                                if (building.getWall(2, players.get(i).getCurrentStorey()) && players.get(i).getPosition(0)+players.get(i).getSize()/5>tmp)
                                    players.get(i).changeDirection();
                                else {
                                    players.get(i).dead(i,nb_players,timer);
                                }
                            }
                        }
                        players.get(i).newBestScore();
                    }
                    else {
                        if (timer-players.get(i).getTimeToDeath() >3){
                            players.get(i).noDead();
                        }
                    }
                }
                    
                timer += interpolation;
                if ((DEADLINE - timer) <=0)
                    setState(100);
                break;
                
            case 100: 
                for (int i =0; i < nb_players;i++){ 
                    players.get(i).dead(i, nb_players, timer);
                }
                this.setScore_PLAYER1(players.get(0).getBestScore());
                switch (nb_players){
                    case 1 :
                        this.setScore_PLAYER2(0);
                        this.gameIsFinish(winner.Player1, true);
                    break;
                    case 2 : 
                        this.setScore_PLAYER2(players.get(1).getBestScore());
                        if (this.getScore_PLAYER1()>this.getScore_PLAYER2())
                            this.gameIsFinish(winner.Player1, true);
                        else
                            this.gameIsFinish(winner.Player2, true);
                    break;    
                }
                break;
        }
    }

    
    
    @Override
    public void renderGame(GraphicsContext drawContext) {
        switch (getState()){
            default :
                drawContext.setStroke(Color.BLACK);
                drawContext.drawImage(this.getGameBackground(), 0, 0, Arcade.getWindowWidth(), Arcade.getWindowHeight());
                
            for(int pl =0 ; pl <nb_players;pl++){
                
                if (players.get(pl).isDead()){
                    
                    
                    drawContext.setFill(Color.gray(0.1));
                    drawContext.fillRect(HighRisers.getWindowX()+pl*HighRisers.getWindowWidth()/nb_players, HighRisers.getWindowHeight() - 3*Storey.getStoreyHeight(),HighRisers.getWindowWidth()/nb_players, 3*Storey.getStoreyHeight());
                    
                    drawContext.drawImage(images[4], players.get(pl).getPosition(0), (timer-players.get(pl).getTimeToDeath())*150);
                }
                else {
                
               
                    building.draw(drawContext, players.get(pl).getCurrentStorey(),nb_players, pl, players.get(pl).getPosition(1) + (players.get(pl).getCurrentStorey() - round(players.get(pl).getCurrentStorey())));
                
                
                   
                    int currentStorey = ((int) players.get(pl).getCurrentStorey());
                    drawContext.setFill(Color.BLACK);
                    drawContext.fillRect(HR_X+(pl+1)*(HighRisers.getWindowWidth()/nb_players)-5, HR_Y, 10, HighRisers.getWindowHeight());
                    drawContext.setFill(Color.CORNFLOWERBLUE);
                    switch (pl){
                        case 0 : drawContext.setFill(Color.RED);break;
                        case 1 : drawContext.setFill(Color.DARKTURQUOISE);break;
                        case 2 : drawContext.setFill(Color.LIME);break;
                        case 3 : drawContext.setFill(Color.YELLOW);break;
                         
                    }
                    drawContext.setFont(Arcade.FONT_60());
                    drawContext.fillText(""+currentStorey, pl*(HighRisers.getWindowWidth()/nb_players)+HR_X+(HighRisers.getWindowWidth()/(2*nb_players))+5, HR_Y+55);
                    drawContext.strokeText(""+ currentStorey, pl*(HighRisers.getWindowWidth()/nb_players)+HR_X+(HighRisers.getWindowWidth()/(2*nb_players))+5, HR_Y+55);
                    
                    drawContext.drawImage(images[10],pl*(HighRisers.getWindowWidth()/nb_players)+HR_X+(HighRisers.getWindowWidth()/(10*nb_players)), HR_HEIGHT +HR_Y-(HR_HEIGHT/10),HR_HEIGHT/20,HR_HEIGHT/20);
                    drawContext.setFont(Arcade.FONT_40());
                    drawContext.fillText(""+players.get(pl).getBestScore(),pl*(HighRisers.getWindowWidth()/nb_players)+HR_X+(HighRisers.getWindowWidth()/(10*nb_players))+HR_HEIGHT/13, HR_HEIGHT +HR_Y-(HR_HEIGHT/10)+HR_HEIGHT/22);
                    drawContext.strokeText(""+players.get(pl).getBestScore(),pl*(HighRisers.getWindowWidth()/nb_players)+HR_X+(HighRisers.getWindowWidth()/(10*nb_players))+HR_HEIGHT/13, HR_HEIGHT +HR_Y-(HR_HEIGHT/10)+HR_HEIGHT/22);
                    
                    int numImage = (int) (10*timer % 3);
                    if (players.get(pl).isDirectionRight()){
                        if (players.get(pl).getCurrentStorey() - currentStorey >0.1)
                            drawContext.drawImage(images[3], players.get(pl).getPosition(0), players.get(pl).getPosition(1), Storey.getStoreyHeight(), 0.9*Storey.getStoreyHeight());
                        else
                            drawContext.drawImage(images[numImage], players.get(pl).getPosition(0) , players.get(pl).getPosition(1)+ 0.1*Storey.getStoreyHeight(), Storey.getStoreyHeight(), 0.9*Storey.getStoreyHeight());
                    }
                        
                    else {
                        if ((players.get(pl).getCurrentStorey() - currentStorey) > 0.1) 
                            drawContext.drawImage(images[8], players.get(pl).getPosition(0), players.get(pl).getPosition(1), Storey.getStoreyHeight(), 0.9*Storey.getStoreyHeight());
                        else
                            drawContext.drawImage(images[5 + numImage], players.get(pl).getPosition(0), players.get(pl).getPosition(1)+ 0.1*Storey.getStoreyHeight(), Storey.getStoreyHeight(), 0.9*Storey.getStoreyHeight());
                    }
                }
            }
                
                
                
                drawContext.setFill(Color.gray(0.9,0.95));
                drawContext.fillRoundRect(HR_X+0.42*HR_WIDTH, HR_Y+HR_HEIGHT/30, 0.16*HR_WIDTH,HR_HEIGHT/15, 20, 20);
                drawContext.strokeRoundRect(HR_X+0.42*HR_WIDTH, HR_Y+HR_HEIGHT/30, 0.16*HR_WIDTH,HR_HEIGHT/15, 20, 20);
                if ((DEADLINE - timer) <=10){
                    drawContext.setStroke(Color.DARKRED);
                    drawContext.setFill(Color.ORANGERED);
                }
                else
                    drawContext.setFill(Color.LIMEGREEN);
                drawContext.setFont(Arcade.FONT_40());
                
                float chrono = ((float) ((int) (10*(DEADLINE - timer)))/10);
                drawContext.fillText(""+chrono,HR_X+0.45*HR_WIDTH, HR_Y+HR_HEIGHT/12);
                drawContext.strokeText(""+chrono,HR_X+0.45*HR_WIDTH, HR_Y+HR_HEIGHT/12);
                
                break;
        }
    }
    
    
    @Override
    public void resetGame() {
        
        //Crée tableau de "players"
        
        this.players = new ArrayList<>();
        for (int pl =0; pl< nb_players;pl++){
            players.add(new Player(HR_X+pl*HR_WIDTH/nb_players+HR_WIDTH/(2*nb_players)));
        }
        
        //Création aléatoire immeuble
        building = new Building(200,nb_players);
        timer =0.4;
    }
}
