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
    private int idPartie;
    
    public ChargementNiveau(MEGame game, int idPartie, String nomPartie) {
        super(game);
        this.idPartie = idPartie;
        createListe("./map/" + idPartie +"/");
        createBtn("Valider");
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
    public void createBtn(String text) {
        TextButton valider = new TextButton(text, skin); // On utilise le skin
        valider.setPosition(Gdx.graphics.getWidth()/2 - Gdx.graphics.getWidth()/8 , Gdx.graphics.getHeight()/2-150);
        valider.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    dispose();
                    game.loadingGame(idPartie, nom[sb.getSelectedIndex()], Integer.parseInt(nomDossier[sb.getSelectedIndex()]));
                };
        });
        stage.addActor(valider);
    }
    
}
