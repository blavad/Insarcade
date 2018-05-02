/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insarcade.javafx.games.pong;

import fr.insarcade.javafx.core.Arcade;

/**
 *
 * @author David
 */
public class Ball {
    
    private static final double RADIUS =20;
    
    private double x, y;
    private double speed;
    
    
    public Ball(){
        this.x= Arcade.getWindowWidth()/2;
        this.y= Arcade.getWindowHeight()/2;
        this.speed =400;
    }
    
    public Ball(double x, double y){
        this.x= x;
        this.y= y;
    }
    
    public double getX(){
        return x;
    }
    
    public double getY(){
        return y;
    }
    
    public void increseSpeed(){
        this.speed +=0.5;
    }
    
    public double getSpeed(){
        return speed;
    }
    
    public double getRadius(){
        return RADIUS;
    }
    
    
    
    
   
    
    
    public void setPosition(double x, double y){
        this.x = x;
        this.y = y;
    }
    
    
}
