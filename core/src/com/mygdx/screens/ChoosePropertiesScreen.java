/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.minexploration.MEGame;
import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class ChoosePropertiesScreen implements Screen {
    private final MEGame game;
    private TextField nomPartie;
    private final int niveau;
    private final Stage stage;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final Skin skin;
    private String[] directories;
    
    /**
     * Ce constructeur est appelé pour créer une partie au niveau 1
     * @param game
     */
    public ChoosePropertiesScreen(MEGame game) {
        this.game = game;
        this.niveau = 1;
        this.batch = new SpriteBatch();
        this.stage = new Stage();
        this.font = new BitmapFont();
        this.font.getData().setScale(1);
        this.font.setColor(Color.BROWN);
        Gdx.input.setInputProcessor(stage); // Le stage va s'occuper des E/S
        this.skin = new Skin(Gdx.files.internal("./skin/uiskin.json"));
        createBtn("Valider");
        createTextField();
    }
    
    private void createTextField() {
        nomPartie = new TextField("", skin);
        nomPartie.setWidth(200);
        nomPartie.setPosition((Gdx.graphics.getHeight()/2)-70,  (Gdx.graphics.getWidth()/2)+40);
        stage.addActor(nomPartie);
    }
    
    private int getNumPartie() {
        File file = new File("./map/");
        directories = file.list(new FilenameFilter() {
          @Override
          public boolean accept(File current, String name) {
            return new File(current, name).isDirectory();
          }
        });
        int maxi = 1;
        for(int i = 0 ; i < directories.length ; i++) {
            if(Integer.parseInt(directories[i]) > maxi)
                maxi = Integer.parseInt(directories[i]);
        }
        return maxi+1;
    }
    
    public final void createBtn(String text) {
        TextButton valider = new TextButton(text, skin); // On utilise le skin
        valider.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        valider.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    dispose();
                    if(!nomPartie.getText().isEmpty())
                        game.newGame(getNumPartie(), nomPartie.getText());
                };
        });
        stage.addActor(valider);
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
        font.draw(batch, "Veuillez nommer votre partie :", (Gdx.graphics.getHeight()/2)-65,  (Gdx.graphics.getWidth()/2)+100);
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
    }
    
}
