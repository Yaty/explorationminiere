/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
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
    private Skin skin;
    private FileHandle[] directories;
    
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
        //createSkin();
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        Gdx.app.log("Choose", Gdx.files.getLocalStoragePath());
        createBtn("Valider");
        createTextField();
    }
    
    private void createSkin() {
        BitmapFont font = new BitmapFont();
        skin = new Skin();
        skin.add("default", font);

        //Create a texture
        Pixmap pixmap = new Pixmap((int)Gdx.graphics.getWidth()/4,(int)Gdx.graphics.getHeight()/10, Pixmap.Format.RGB888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("background",new Texture(pixmap));

        //Create a button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("background", Color.GRAY);
        textButtonStyle.down = skin.newDrawable("background", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("background", Color.DARK_GRAY);
        textButtonStyle.over = skin.newDrawable("background", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);
        
        // Create de TextFieldStyle
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = skin.getFont("default");
        textFieldStyle.fontColor = Color.BLACK;
        textFieldStyle.background = skin.getDrawable("background");
        textFieldStyle.selection = skin.newDrawable("background", 0.5f, 0.5f, 0.5f,
                0.5f);
        skin.add("default", textFieldStyle);
    }
    
    private void createTextField() {
        nomPartie = new TextField("", skin);
        nomPartie.setWidth(200);
        nomPartie.setPosition((Gdx.graphics.getHeight()/2)-70,  (Gdx.graphics.getWidth()/2)+40);
        stage.addActor(nomPartie);
    }
    
    private int getNumPartie() {
        FileHandle file = Gdx.files.local("map");
        directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        int maxi = 1;
        for(int i = 0 ; i < directories.length ; i++) {
            if(Integer.parseInt(directories[i].name()) > maxi)
                maxi = Integer.parseInt(directories[i].name());
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
                        game.nouvellePartie(getNumPartie(), nomPartie.getText());
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
