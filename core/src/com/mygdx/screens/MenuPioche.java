/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.minexploration.MEGame;
import com.mygdx.minexploration.ShutdownHandler;

/**
 *
 * @author Hugo
 */
public class MenuPioche extends Window {
    
    public MenuPioche(Skin skin, final MEGame game, final GameScreen screen) {
        super("Pause", skin);
        TextButton fermer = new TextButton("Fermer", skin);

        fermer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setVisible(false);
            }
        });
        add(fermer);
        row();
        
        // basic layout
        setBounds((Gdx.graphics.getWidth()/2)-(getWidth()/2), (Gdx.graphics.getHeight()/2)-(getHeight()/2), 200, 200);
        defaults().space(8);
        row().fill().expandX();

        pack();

        // it is hidden by default
        setVisible(false);
    }    
}
