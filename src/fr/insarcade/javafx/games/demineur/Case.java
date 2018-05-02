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
public class Case {
    
    
    private boolean estDecouvert;
    private int symbole;
    
    public Case(int symbole){
        estDecouvert = false;
        this.symbole = symbole;
    }
    
    public int obtenirSymbole(){
        return symbole;
    }
    
    public boolean estDecouvert(){
        return estDecouvert;
    }
    
    public void setSymbole(int symb){
        this.symbole = symb;
    }
    
    public void decouvrir(){
        estDecouvert = true;
    }
}
