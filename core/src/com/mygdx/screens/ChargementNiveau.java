/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.minexploration.MEGame;

/**
 *
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class ChargementNiveau extends Chargement implements Screen {
    private String partie;
    
    public ChargementNiveau(MEGame game, String partie) {
        super(game);
        this.partie = partie;
        createListe("./map/" + partie +"/");
        createBtn("Valider");
    }
    
    
    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
        batch.begin();
        font.draw(batch, "Sélectionnez le niveau :", (Gdx.graphics.getHeight()/2)-65,  (Gdx.graphics.getWidth()/2)+50);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void createBtn(String text) {
        TextButton valider = new TextButton(text, skin); // On utilise le skin
        valider.setPosition(Gdx.graphics.getWidth()/2 - Gdx.graphics.getWidth()/8 , Gdx.graphics.getHeight()/2-150);
        valider.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    dispose();
                    game.setScreen(new GameScreen(game, partie, Integer.parseInt(nomDossier[sb.getSelectedIndex()])));
                };
        });
        stage.addActor(valider);
    }
    
}
