/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.mehelpers;

import com.badlogic.gdx.InputAdapter;

/**
 *
 * @author Hugo
 */
public class KeyBoard extends InputAdapter {
    
   @Override
   public boolean keyDown(int k) {
        //System.out.println("Touche " + k + " enfoncée !");
        InputHandler.setKey(k, true);
        return true;
    }

   @Override
    public boolean keyUp(int k) {
        //System.out.println("Touche " + k + " levée !");
        InputHandler.setKey(k, false);
        return true;
    }

    
}
