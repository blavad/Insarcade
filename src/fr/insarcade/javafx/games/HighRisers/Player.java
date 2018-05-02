/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insarcade.javafx.games.HighRisers;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author David
 */
public class Player {
    
    private static final double SIZE =Storey.getStoreyHeight();
    
    private float current_storey;
    private int best_storey;
    private ArrayList <Double> timer_press;
    private final double[] position = new double[2];
    private boolean isDirectionRight;
    private boolean buttonPressed;
    private boolean isDead;
    private double deadTime;
    
    Player(double x){
        this.timer_press= new ArrayList<>();
        this.current_storey =0;
        this.best_storey =0;
        this.position[0]= x;
        this.position[1]=HighRisers.getWindowY() + HighRisers.getWindowHeight()-4*Storey.getStoreyHeight();
        this.isDirectionRight = true;
        this.buttonPressed = false;
        this.isDead = false;
        this.deadTime =0;
    }
    
    public double getSize(){
        return SIZE;
    }
    
    public double removeCurrentTimer(){
        return timer_press.remove(0);
    }
    
     public int getTimerSize(){
        return timer_press.size();
    }
    
    public double getTimerPress(){
        if (timer_press.size() > 0)
            return timer_press.get(0);
        else return 0.0;
    }
    
    public void setTimerPress(double timer){
        timer_press.add(timer);
    }

    
    public float getCurrentStorey() {
        return current_storey;
    }

    public void setCurrentStorey(double interpolation) {
        current_storey= (float) interpolation;
    }
    
   
    public double getPosition(int i){
        return position[i];
    }
    
    public void setPosX(double interpolation) {
        position[0]+=interpolation;
    }
    
    public boolean isDirectionRight(){
        return this.isDirectionRight;
    }
    
    public void changeDirection(){
        isDirectionRight = !(isDirectionRight);
    }
    

    
    public boolean isButtonPressed() {
        return buttonPressed;
    }

    public void setButtonPressed(){
        buttonPressed = !(buttonPressed);
    }

     public boolean isDead() {
        return isDead;
    }
    public void dead(int pl, int nb_pl, double timeToDeath) {
        deadTime = timeToDeath;
        isDead = !(isDead);
        position[0]=HighRisers.getWindowX()+pl*HighRisers.getWindowWidth()/nb_pl+HighRisers.getWindowWidth()/(2*nb_pl);
    }
    
    public void noDead(){
        isDead = false;
        if (current_storey > best_storey){
            best_storey = (int) current_storey;}
        this.current_storey =0;
        timer_press.clear();
    }
    public double getTimeToDeath(){
        return deadTime;
    }

    public void newBestScore(){
        if ((int) current_storey >= best_storey)
            best_storey = (int) current_storey;
    }
    
    public int getBestScore() {
        return best_storey;
    }
    
}
