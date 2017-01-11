/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.gameobjects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.mehelpers.CellsHandler;

public class BaseIntermediaire extends Rectangle {    
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
    
    public BaseIntermediaire(int x0, int y0) {
        super(x0, y0, CELLS[0].length, CELLS.length);
    } 

    public Vector2 getPos() {
        return new Vector2(x, y);
    }   
    
}
