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
public class BoutonNiveau {
    
    
    private static double Hauteur_Bouton = GameDemineur.obtenirDimensionEcran(true)/8;
    private static double Largeur_Bouton = (GameDemineur.obtenirDimensionEcran(false)/8)*6;
    
    private double[] position;
    private String nom;
    
    public BoutonNiveau(double positionY, String nom){
        position = new double[2];
        position[0] = GameDemineur.obtenirPositionEcran(true) + GameDemineur.obtenirDimensionEcran(false)/8;
        position[1] = GameDemineur.obtenirPositionEcran(false) + positionY;
        this.nom = nom;
    }
    
    public double obtenirHauteur(){
        return Hauteur_Bouton;
    }
    
    
    public double obtenirLargeur(){
        return Largeur_Bouton;
    }
       
    public double obtenirPositionX(){
        return position[0];
    }
    
    public double obtenirPositionY(){
        return position[1];
    }
    
    public String obtenirNom(){
        return nom;
    }
}
