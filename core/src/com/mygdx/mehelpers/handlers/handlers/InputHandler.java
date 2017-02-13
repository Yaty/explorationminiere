/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.mehelpers.handlers.handlers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

/**
 *
 * @author Hugo Da Roit - contact@hdaroit.fr
 */
public class InputHandler implements InputProcessor {
    public static boolean
            ALLER_DROITE = false, ALLER_GAUCHE = false,
            ALLER_HAUT = false, ALLER_BAS = false;
    public static boolean
            POSER_ECHELLE = false, POSER_TNT = false,
            EXPLOSER_TNT = false, POSER_PILIER = false,
            POSER_BASE  = false;
    
    /**
     * S'active lorsque la touche est enfoncée, une seule fois
     * @param keycode
     * @return 
     */
    @Override
    public boolean keyDown(int keycode) {
        switch(keycode) {
            case Keys.Q:
            case Keys.LEFT:
                ALLER_GAUCHE = true;
                break;
            case Keys.D:
            case Keys.RIGHT:
                ALLER_DROITE = true;
                break;
            case Keys.S:
            case Keys.DOWN:            
                ALLER_BAS = true;
                break;
            case Keys.Z:
            case Keys.UP:
                ALLER_HAUT = true;
                break;
            case Keys.E:
                POSER_ECHELLE = true;
                break;
            case Keys.R:
                POSER_PILIER = true;
                break;
            case Keys.T:
                POSER_PILIER = true;
                break;
            case Keys.Y:
                EXPLOSER_TNT = true;
                break;
            case Keys.B:
                POSER_BASE = true;
                break;
        }
        return true;
    }

    /**
     * S'active lorsque la touche est relachée
     * @param keycode
     * @return 
     */
    @Override
    public boolean keyUp(int keycode) {
        // On reset
        switch(keycode) {
            case Keys.Q:
            case Keys.LEFT:
                ALLER_GAUCHE = false;
                break;
            case Keys.D:
            case Keys.RIGHT:
                ALLER_DROITE = false;
                break;
            case Keys.S:
            case Keys.DOWN:            
                ALLER_BAS = false;
                break;
            case Keys.Z:
            case Keys.UP:
                ALLER_HAUT = false;
                break;
            case Keys.E:
                POSER_ECHELLE = false;
                break;
            case Keys.R:
                POSER_PILIER = false;
                break;
            case Keys.T:
                POSER_PILIER = false;
                break;
            case Keys.Y:
                EXPLOSER_TNT = false;
                break;
            case Keys.B:
                POSER_BASE = false;
                break;
        }
        return true;
    }

    /**
     * S'active quand une touche est enfoncée, en continu
     * @param character
     * @return 
     */
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
    
}
