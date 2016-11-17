/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.mehelpers.inventaire;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.mygdx.gameobjects.Item;

/**
 *
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class SlotSource extends Source {
  private Slot sourceSlot;

    public SlotSource(SlotActor actor) {
        super(actor);
        this.sourceSlot = actor.getSlot();
    }

    @Override
    public Payload dragStart(InputEvent event, float x, float y, int pointer) {
        if (sourceSlot.getAmount() == 0) {
            return null;
        }

        Payload payload = new Payload();
        Slot payloadSlot = new Slot(sourceSlot.getItem(), sourceSlot.getAmount());
        sourceSlot.take(sourceSlot.getAmount());
        payload.setObject(payloadSlot);

        TextureAtlas icons = new TextureAtlas(Gdx.files.internal("icons/inventaire/icons.atlas"));
        TextureRegion icon = icons.findRegion(payloadSlot.getItem().getTextureRegion());

        Actor dragActor = new Image(icon);
        payload.setDragActor(dragActor);

        Actor validDragActor = new Image(icon);
        payload.setValidDragActor(validDragActor);

        Actor invalidDragActor = new Image(icon);
        payload.setInvalidDragActor(invalidDragActor);

        return payload;
    }

    @Override
    public void dragStop(InputEvent event, float x, float y, int pointer, Payload payload, Target target) {
        Slot payloadSlot = (Slot) payload.getObject();
        // the payload was dropped over a valid target
        if (target != null) {
            Slot targetSlot = ((SlotActor) target.getActor()).getSlot();
            // if the item is the same, stack it
            if (targetSlot.getItem() == payloadSlot.getItem() || targetSlot.getItem() == null) {
                targetSlot.add(payloadSlot.getItem(), payloadSlot.getAmount());
            } else {
                // the item is not the same, thus switch the items
                Item targetType = targetSlot.getItem();
                int targetAmount = targetSlot.getAmount();
                targetSlot.take(targetAmount);
                targetSlot.add(payloadSlot.getItem(), payloadSlot.getAmount());
                sourceSlot.add(targetType, targetAmount);
            }
        } else {
            // the payload was not dropped over a target, thus put it back to where it came from
            sourceSlot.add(payloadSlot.getItem(), payloadSlot.getAmount());
        }
    }    
}
