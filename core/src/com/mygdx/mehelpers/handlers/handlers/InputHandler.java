/* 
 * Copyright 2017 
 * - Hugo Da Roit - Benjamin Lévêque
 * - Alexis Montagne - Alexis Clément
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mygdx.mehelpers.handlers.handlers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

/**
 * Input handler class
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class InputHandler implements InputProcessor {
    public static boolean
            GO_RIGHT = false, GO_LEFT = false,
            GO_UPWARDS = false, GO_DOWN = false;
    public static boolean
            PUT_LADDER = false, PUT_TNT = false,
            EXPLODE_TNT = false, PUT_PILLAR = false,
            PUT_BASE  = false;
    
    /**
     * Called once when a touch is pressed
     * @param keycode the key's code :3
     * @return true because we handle it
     */
    @Override
    public boolean keyDown(int keycode) {
        switch(keycode) {
            case Keys.Q:
            case Keys.LEFT:
                GO_LEFT = true;
                break;
            case Keys.D:
            case Keys.RIGHT:
                GO_RIGHT = true;
                break;
            case Keys.S:
            case Keys.DOWN:            
                GO_DOWN = true;
                break;
            case Keys.Z:
            case Keys.UP:
                GO_UPWARDS = true;
                break;
            case Keys.E:
                PUT_LADDER = true;
                break;
            case Keys.R:
                PUT_PILLAR = true;
                break;
            case Keys.T:
                PUT_TNT = true;
                break;
            case Keys.Y:
                EXPLODE_TNT = true;
                break;
            case Keys.B:
                PUT_BASE = true;
                break;
        }
        return true;
    }

    /**
     * Called once when a touch is released
     * @param keycode the key's code :3
     * @return true because we handle it
     */
    @Override
    public boolean keyUp(int keycode) {
        // On reset
        switch(keycode) {
            case Keys.Q:
            case Keys.LEFT:
                GO_LEFT = false;
                break;
            case Keys.D:
            case Keys.RIGHT:
                GO_RIGHT = false;
                break;
            case Keys.S:
            case Keys.DOWN:            
                GO_DOWN = false;
                break;
            case Keys.Z:
            case Keys.UP:
                GO_UPWARDS = false;
                break;
            case Keys.E:
                PUT_LADDER = false;
                break;
            case Keys.R:
                PUT_PILLAR = false;
                break;
            case Keys.T:
                PUT_PILLAR = false;
                break;
            case Keys.Y:
                EXPLODE_TNT = false;
                break;
            case Keys.B:
                PUT_BASE = false;
                break;
        }
        return true;
    }

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
