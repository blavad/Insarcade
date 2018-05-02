/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insarcade.javafx.games.HighRisers;

import fr.insarcade.javafx.core.Arcade;
import java.util.ArrayList;
import java.util.Random;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 *
 * @author David
 */
public class Building {
    
    private final ArrayList<Storey> storey;
    private boolean[][] wall;
    
    Building(){
        storey = new ArrayList();
    }


    Building(int nb_story, int nb_players) {
        this.storey = new ArrayList();
        this.wall = new boolean[4][nb_story];
        for (int ten_stor = 0; ten_stor<nb_story/10; ten_stor++){
            Random random = new Random();
            int rand = random.nextInt(9)-4;
            for (int stor=0; stor<10;stor++){
                storey.add(new Storey(ten_stor*10 + stor, rand, nb_players));
            } 
        }
        for (int i =0; i< nb_story;i++){
            wall[0][i] = true;
            wall[1][i] = true;
            wall[2][i] = isPresent(2,i);
            wall[3][i] = isPresent(3,i);
        }
    }
    
    private int iswall(boolean wallB){
        if (wallB)
            return 1;
        else return 0;
    }
    private boolean isPresent(int side , int num){
        if (num %10 == 0){
            return true;
        }
        else
        {
            Random rand = new Random();
            if (wall[side][num-1] && rand.nextInt(100) <60) {
                return false;
            }
            else 
            {
                return rand.nextBoolean();
            }
            
        }
            
    }

    public Storey getStorey(float i) {
       return storey.get(Math.round(i));
    }

    public boolean getWall(int wall, float storey) {
        return this.wall[wall][Math.round(storey)];
    }

    void draw(GraphicsContext drawContext, float currentStorey, int nb_players, int player, double posY) {
        
                int currentStor = (int) currentStorey;
                double x=HighRisers.getWindowX()+player*HighRisers.getWindowWidth()/nb_players-(storey.get(currentStor).getX_RELATED()*(currentStorey - currentStor))+(HighRisers.getWindowWidth()/nb_players -storey.get(Math.round(currentStorey)).getWidth())/2;
                
                drawContext.setStroke(Color.BLACK);
                for(int etage=(int) currentStorey; etage < Math.round(currentStorey)+HighRisers.getNbStoreysShowed()-2;etage++){
                    drawStorey(drawContext, currentStorey, nb_players, player, x, posY, etage);
                    x = x+storey.get(Math.round(etage)).getX_RELATED();
                }
                x =HighRisers.getWindowX()+player*HighRisers.getWindowWidth()/nb_players-(storey.get(currentStor).getX_RELATED()*(currentStorey - currentStor))+(HighRisers.getWindowWidth()/nb_players -storey.get(Math.round(currentStorey)).getWidth())/2;
                for(int etage=(currentStor)-1; etage>Math.round(currentStorey)-5;etage--){
                    if (etage>1)
                        x= x-storey.get(Math.round(etage)).getX_RELATED();
                    drawStorey(drawContext, currentStorey, nb_players, player, x, posY, etage);
                    
                }
                
    }
                public void drawStorey(GraphicsContext drawContext, float currentStorey, int nb_players, int player, double x, double posY, int etage){
                     
                    if (etage<0){
                        drawContext.setFill(Color.gray(0.1));
                        drawContext.fillRect(HighRisers.getWindowX()+player*HighRisers.getWindowWidth()/nb_players, posY - (etage-currentStorey)*Storey.getStoreyHeight(),HighRisers.getWindowWidth()/nb_players, Storey.getStoreyHeight());
                    }
                    else {
                        boolean isBefore=false, isAfter =false;
                        double posX=0, stW =0;
                        if (x<HighRisers.getWindowX()+player*HighRisers.getWindowWidth()/nb_players){
                            isBefore = true;
                            posX =HighRisers.getWindowX()+player*HighRisers.getWindowWidth()/nb_players;
                            stW=storey.get(etage).getWidth()-((HighRisers.getWindowX()+player*HighRisers.getWindowWidth()/nb_players)-x);
                                
                        }
                        else if (x + storey.get(etage).getWidth()>HighRisers.getWindowX()+(player+1)*HighRisers.getWindowWidth()/nb_players){
                            isAfter=true;
                            posX =x;
                            stW=HighRisers.getWindowX()+storey.get(etage).getWidth()-((x+storey.get(etage).getWidth())-(player+1)*HighRisers.getWindowWidth()/nb_players);
                        }
                        else {
                            posX =x;
                            stW=storey.get(etage).getWidth();
                        }
                        if (etage % 10 ==0)
                        {
                            
                            drawContext.setFill(Color.ORANGE);
                            if (isBefore) {
                                drawContext.fillRect(posX, posY - (etage-currentStorey)*Storey.getStoreyHeight(), stW, Storey.getStoreyHeight());
                                //drawContext.strokeRect(posX, posY - (etage-currentStorey)*Storey.getStoreyHeight(), stW, Storey.getStoreyHeight());
                            }
                            else if (isAfter){
                                drawContext.fillRect(posX, posY - (etage-currentStorey)*Storey.getStoreyHeight(), stW, Storey.getStoreyHeight());
                                //drawContext.strokeRect(posX, posY - (etage-currentStorey)*Storey.getStoreyHeight(), stW, Storey.getStoreyHeight());
                                drawContext.setFill(Color.DARKRED);
                                drawContext.setFont(new Font(Storey.getStoreyHeight()));
                                drawContext.fillText(""+etage, x + 3*HighRisers.getWallWidth(), posY - (etage-1-currentStorey)*Storey.getStoreyHeight()-4);
                            }
                            else 
                            {
                                drawContext.fillRect(posX, posY - (etage-currentStorey)*Storey.getStoreyHeight(), stW, Storey.getStoreyHeight());
                                //drawContext.strokeRect(posX, posY - (etage-currentStorey)*Storey.getStoreyHeight(), stW, Storey.getStoreyHeight());
                                drawContext.setFill(Color.DARKRED);
                                drawContext.setFont(new Font(Storey.getStoreyHeight()));
                                drawContext.fillText(""+etage, x + 3*HighRisers.getWallWidth(), posY - (etage-1-currentStorey)*Storey.getStoreyHeight()-4);
                            }
                            
                            
                        }
                        else  
                        {
                            drawContext.setFill(Color.ANTIQUEWHITE);
                            if (isBefore){
                                drawContext.fillRect(posX, posY - (etage-currentStorey)*Storey.getStoreyHeight(), stW, Storey.getStoreyHeight());
                                //drawContext.strokeRect(posX, posY - (etage-currentStorey)*Storey.getStoreyHeight(), stW, Storey.getStoreyHeight());
                            }
                            else if (isAfter){
                                drawContext.fillRect(posX, posY - (etage-currentStorey)*Storey.getStoreyHeight(), stW, Storey.getStoreyHeight());
                                //drawContext.strokeRect(posX, posY - (etage-currentStorey)*Storey.getStoreyHeight(), stW, Storey.getStoreyHeight());
                            }
                            else 
                            {
                                drawContext.fillRect(posX, posY - (etage-currentStorey)*Storey.getStoreyHeight(), stW, Storey.getStoreyHeight());
                                //drawContext.strokeRect(posX, posY - (etage-currentStorey)*Storey.getStoreyHeight(), stW, Storey.getStoreyHeight());
                            }
                           
                        }
                        
                        drawContext.setFill(Color.SADDLEBROWN);
                        for (int mur =0;mur<4;mur++){
                            if (wall[mur][etage])
                            {
                                switch (mur){
                                case 0:
                                    drawContext.fillRect(posX, posY-(etage-1-currentStorey)*Storey.getStoreyHeight(),stW,HighRisers.getWallWidth());
                                    //drawContext.strokeRect(posX, posY-(etage-1-currentStorey)*Storey.getStoreyHeight(),stW,HighRisers.getWallWidth());
                                    break;
                                case 1:
                                    drawContext.fillRect(posX, posY-(etage-currentStorey)*Storey.getStoreyHeight(),stW,HighRisers.getWallWidth());
                                    //drawContext.strokeRect(posX, posY-(etage-currentStorey)*Storey.getStoreyHeight(),stW,HighRisers.getWallWidth());
                                    break;
                                case 2:
                                    if (!(isBefore)){ 
                                        drawContext.fillRect(x-HighRisers.getWallWidth(), posY-(etage-currentStorey)*Storey.getStoreyHeight(),HighRisers.getWallWidth(),Storey.getStoreyHeight()+HighRisers.getWallWidth());
                                        //drawContext.strokeRect(x-HighRisers.getWallWidth(), posY-(etage-currentStorey)*Storey.getStoreyHeight(),HighRisers.getWallWidth(),Storey.getStoreyHeight()+HighRisers.getWallWidth());
                                    }
                                      
                                    break;
                                case 3:
                                    if (!(isAfter)){
                                        drawContext.fillRect(x+storey.get(etage).getWidth(), posY-(etage-currentStorey)*Storey.getStoreyHeight(),HighRisers.getWallWidth(),Storey.getStoreyHeight()+HighRisers.getWallWidth());
                                        //drawContext.strokeRect(x+storey.get(etage).getWidth(), posY-(etage-currentStorey)*Storey.getStoreyHeight(),HighRisers.getWallWidth(),Storey.getStoreyHeight()+HighRisers.getWallWidth());
                                    }
                                    break;
                                default:
                                    break;
                                }
                            }
                            else {
                            
                            }
                        }   
                        
                    }
                }
}





