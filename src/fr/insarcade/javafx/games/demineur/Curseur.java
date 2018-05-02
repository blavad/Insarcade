/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insarcade.javafx.games.demineur;


/**
 *
 * @author David
 */
public class Curseur {
   
    private static double Taille_Curseur = 50;
    
    private double[] positionCurseur; // 1 : coordonnée x, 2 : coordonnee y
    
    public Curseur(){
        positionCurseur = new double[2];
    }

    public static final double obtenirTaille(){
        return Taille_Curseur;
    }
    
    public void initCurseur(){
        positionCurseur[0] = (GameDemineur.obtenirPositionEcran(true) + GameDemineur.obtenirDimensionEcran(false))/2;
        positionCurseur[1] = (GameDemineur.obtenirPositionEcran(false) + GameDemineur.obtenirDimensionEcran(true))/8;
    }
    
    public double obtenirPositionX(){
        return positionCurseur[0];
    }
    
    public double obtenirPositionY(){
        return positionCurseur[1];
    }
    
    public void changerPositionX(double deltaX){
        positionCurseur[0] += deltaX;
    }
    
    public void changerPositionY(double deltaY){
        positionCurseur[1] += deltaY;
    }
    
}
