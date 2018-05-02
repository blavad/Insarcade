/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insarcade.javafx.games.demineur;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author David
 */
public class Grille {
    
    private static byte Nb_Colonnes = 20;
    private static byte Nb_Lignes = 15;
    private static byte Taille_Cote = 40; // taille d'une case en pixels
    
    private int nb_bombe;
    private int case_restante;
    private ArrayList<ArrayList<Case>> cases;
    
    Grille(byte difficulte){
        cases = new ArrayList<>();
        for (byte ligne =0; ligne <Nb_Lignes;ligne++){
            ArrayList<Case> ma_ligne = new ArrayList<>();
            for (byte colonne = 0; colonne <Nb_Colonnes;colonne++){
                ma_ligne.add(new Case(0));
            }
            cases.add(ma_ligne);
        }
        
        switch (difficulte){
            case 1 : nb_bombe = (int) (Nb_Lignes*Nb_Colonnes*0.12); break;
            case 2 : nb_bombe = (int) (Nb_Lignes*Nb_Colonnes*0.18); break;
            case 3 : nb_bombe = (int) (Nb_Lignes*Nb_Colonnes*0.24); break;
            case 4 : nb_bombe = (int) (Nb_Lignes*Nb_Colonnes*0.30); break;
        }
        System.out.println("Nombre bombe : " + nb_bombe);
        Random rand = new Random();
        int bomb = nb_bombe;
        do {
            int lign = rand.nextInt(Nb_Lignes), col = rand.nextInt(Nb_Colonnes);
            if (cases.get(lign).get(col).obtenirSymbole() == 0){
                bomb--;
                cases.get(lign).get(col).setSymbole(10);
            }
            
        } while (bomb >0);
        for (int lign = 0; lign<Nb_Lignes;lign++){
            for (int col = 0; col<Nb_Colonnes;col++){
                if (cases.get(lign).get(col).obtenirSymbole() != 10){ // si il n'y a pas de bombe
                    int debutLign=-1,finLign=1, debutCol =-1, finCol =1;
                    int bombes_alentours =0;
                    if (lign == 0) {
                        debutLign = 0;
                        finLign = 1;
                    }
                    else if (lign == Nb_Lignes-1) {
                        debutLign = -1;
                        finLign = 0;
                    }
                    if (col==0){
                        debutCol =0;
                        finCol = 1;
                    }
                    else if (col==Nb_Colonnes-1){
                        debutCol =-1;
                        finCol = 0;
                    }
                    for (int av_ap = debutCol; av_ap <=finCol;av_ap++){
                        for (int sus_sous = debutLign;sus_sous<=finLign;sus_sous++){
                            if (cases.get(lign+sus_sous).get(col+av_ap).obtenirSymbole() == 10) {
                                bombes_alentours ++;
                            }
                        }
                    }
                cases.get(lign).get(col).setSymbole(bombes_alentours);
                }
            }
        }
        case_restante = (Nb_Lignes*Nb_Colonnes) - nb_bombe;
    }
    
    public static byte getNbLignes(){
        return Nb_Lignes;
    }
    
    public static byte getNbColonnes(){
        return Nb_Colonnes;
    }
    
        
    public static byte getTailleCote(){
        return Taille_Cote;
    }
    
    public int obtenirSymboleCase(int ligne, int colonne){
        return cases.get(ligne).get(colonne).obtenirSymbole();
    }
    
    public boolean estDecouvert(int ligne, int colonne){
        return  cases.get(ligne).get(colonne).estDecouvert();
    }
    
    public void decouvrirCase(int ligne, int colonne){
        cases.get(ligne).get(colonne).decouvrir();
        case_restante--;
    }
    
    public int obtenirNombreCaseADecouvrir(){
        return case_restante;
    }
    
}
