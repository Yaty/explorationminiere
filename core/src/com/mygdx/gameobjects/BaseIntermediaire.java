/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.gameobjects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.mehelpers.CellsHandler;

public class BaseIntermediaire {    
    // XXXXX
    // X   X AVEC X = mur, M magasin et vide = air
    //    MX
    // XXXXX
    public static final int[][] CELLS =
    {
        //SOL
        {CellsHandler.idSolBase, CellsHandler.idSolBase, CellsHandler.idSolBase, CellsHandler.idSolBase, CellsHandler.idSolBase},
        // Air
        {0, 0, 0, CellsHandler.idMagasin, CellsHandler.idSolBase},
        // Air
        {CellsHandler.idSolBase, 0, 0, 0, CellsHandler.idSolBase},
        // Plafond
        {CellsHandler.idSolBase, CellsHandler.idSolBase, CellsHandler.idSolBase, CellsHandler.idSolBase, CellsHandler.idSolBase}
    };
    
    private final int LARGEUR, HAUTEUR;
    
    private final int x, y;
    
    private final Rectangle base;
    
    public BaseIntermediaire(int x0, int y0) {
        x = x0;
        y = y0;
        HAUTEUR = CELLS.length;
        LARGEUR = CELLS[0].length;
        base = new Rectangle(this.x, this.y, LARGEUR, HAUTEUR);
    } 
    
    public boolean contains(float x, float y) {
        return base.contains(x, y);
    }

    public Vector2 getPos() {
        return new Vector2(x, y);
    }

    public int getHauteur() {
        return HAUTEUR;
    }

    public int getLargeur() {
        return LARGEUR;
    }
    
    
}
