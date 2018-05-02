/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insarcade.javafx.games.HighRisers;

/**
 *
 * @author David
 */
public class Storey {
    
    private static final double STOREY_HEIGHT = HighRisers.getWindowHeight()/ HighRisers.getNbStoreysShowed();
    
    private double x_related;
    private double width;
    
    
    
    Storey(int num, int rand, int nb_players) {
        if (nb_players <=2)
            this.width= HighRisers.getWindowWidth()/2 - ((num/10)+5) * HighRisers.getWindowWidth()/(30*nb_players);
        else
            this.width= HighRisers.getWindowWidth()/nb_players - ((num/10)+5) * HighRisers.getWindowWidth()/(30*nb_players);
        this.x_related = calc(num, rand);
    }
    
    public static double getStoreyHeight(){
        return STOREY_HEIGHT;
    }
    private double calc(int num, int rand){
        if (num<=10)
            return 0;
        else
        {
            if (num>150){
                return (width/2)-Math.random()*width;
            }
            else 
            {
                return rand*(width/100);
             }
        }
    }
    
    public double getWidth() {
        return width;
    }
    
    public double getX_RELATED(){
        return x_related;
    }
}
