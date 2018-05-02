/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insarcade.javafx.games.pong;

/**
 *
 * @author David
 */
public class Stick {
    
    
    
    private double x, y, h, w;
    
    public Stick(double x, double y, double h, double w){
        this.h = h;
        this.w = w;
        this.y = y;
        this.x = x;
    }
    
    public double getY(){
        return y;
    }
    
    public double getX(){
        return x;
    }
    
    public double getHeight(){
        return h;
    }
    
    public double getWidth(){
        return w;
    }
    
    public void setY(double delta){
        
        this.y += delta;
    }
    
    public void setX(double delta){
        this.x += delta;
    }
    
    public boolean collisionBall(Ball b){
        return ((b.getX()+b.getRadius() > this.x) && (b.getX() < this.x +this.w)&& (b.getY()<this.y+this.h) && (b.getY()+b.getRadius()> this.y));
    }
    
    public boolean collision(double x, double y){
        return (x>this.x) && (x<this.x+this.w) && (y>this.y) && (y<this.y+this.h);
    }
}
