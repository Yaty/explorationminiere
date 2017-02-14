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
package com.mygdx.mehelpers.inventory;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * http://pixelscientists.com/wordpress/?p=17
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class TooltipListener extends InputListener {
   private boolean inside;

    private final Actor tooltip;
    private final boolean followCursor;

    private final Vector2 position = new Vector2();
    private final Vector2 tmp = new Vector2();
    private final Vector2 offset = new Vector2(10, 10);

    /**
     * Constructor
     * @param tooltip
     * @param followCursor
     */
    public TooltipListener(Actor tooltip, boolean followCursor) {
        this.tooltip = tooltip;
        this.followCursor = followCursor;
    }

    @Override
    public boolean mouseMoved(InputEvent event, float x, float y) {
        if (inside && followCursor) {
            event.getListenerActor().localToStageCoordinates(tmp.set(x, y));
            tooltip.setPosition(tmp.x + position.x + offset.x, tmp.y + position.y + offset.y);
        }
        return false;
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        inside = true;
        tooltip.setVisible(true);
        tmp.set(x, y);
        event.getListenerActor().localToStageCoordinates(tmp);
        tooltip.setPosition(tmp.x + position.x + offset.x, tmp.y + position.y + offset.y);
        tooltip.toFront();
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        inside = false;
        tooltip.setVisible(false);
    }

    /**
     * The offset of the tooltip from the touch position. It should not be
     * positive as the tooltip will flicker otherwise.
     */
    public void setOffset(float offsetX, float offsetY) {
        offset.set(offsetX, offsetY);
    }    
}
