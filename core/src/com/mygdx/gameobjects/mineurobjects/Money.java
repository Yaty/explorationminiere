/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.gameobjects.mineurobjects;

import com.mygdx.mehelpers.handlers.handlers.MapHandler;

/**
 *
 * @author Hugo Da Roit - contact@hdaroit.fr
 */
public class Money {
    private int argent;

    public Money(int argent) {
        this.argent = argent;
    }
    
    public int getArgent() {
        return argent;
    }
    
    public void recolterArgent(int id) {
        if (id == MapHandler.idDiamant) argent += 500;
        else if (id == MapHandler.idCharbon) argent += 10;
        else if (id == MapHandler.idTerre) argent++;
        else if (id == MapHandler.idEmeraude) argent += 100;
        else if (id == MapHandler.idGlowstone) argent += 10;
        else if (id == MapHandler.idOr) argent += 50;
        else if (id == MapHandler.idHerbe) argent++;
        else if (id == MapHandler.idFer) argent += 30;
        else if (id == MapHandler.idLapis) argent += 80;
    }

    public void retirer(int somme) {
        argent -= somme;
    }
}
