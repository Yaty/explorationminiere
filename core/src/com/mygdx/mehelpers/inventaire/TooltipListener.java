/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.mehelpers.inventaire;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 *
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class TooltipListener extends InputListener {
   private boolean inside;

    private Actor tooltip;
    private boolean followCursor;

    private Vector2 position = new Vector2();
    private Vector2 tmp = new Vector2();
    private Vector2 offset = new Vector2(10, 10);

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
