package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.minexploration.MEGame;

/**
 * Cette classe sert a l'affichage du menu de depart quand l'utilisateur lance le programme
 * 
 */



public class MainMenuScreen implements Screen {

    private final MEGame game;  
    private Stage stage;
    private Skin skin;
    
    /**
     * Ce constructeur initialise les boutons avec leur images
     * @param game represente le jeu pour agir dessus (le lancer)
     */
    
    public MainMenuScreen (MEGame game){
        this.game = game;        
        stage = new Stage();
        Gdx.input.setInputProcessor(stage); // Le stage va s'occuper des E/S
        create(); // Création du skin de base, puis ajout ci-dessous
        createButtons();
    }
    
    private void createButtons() {
        TextButton btnNvlPartie = new TextButton("Nouvelle partie", skin); // On utilise le skin
        btnNvlPartie.setPosition(Gdx.graphics.getWidth()/2 - Gdx.graphics.getWidth()/8 , Gdx.graphics.getHeight()/2+150);
        btnNvlPartie.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new GameScreen(game));
                };
        });
        
        TextButton btnChargerPartie = new TextButton("Charger une partie", skin);
        btnChargerPartie.setPosition(Gdx.graphics.getWidth()/2 - Gdx.graphics.getWidth()/8 , Gdx.graphics.getHeight()/2+50);
        btnChargerPartie.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new ChargementPartie(game));
                };
        });        
        
        TextButton btnInfos = new TextButton("Informations", skin);
        btnInfos.setPosition(Gdx.graphics.getWidth()/2 - Gdx.graphics.getWidth()/8 , Gdx.graphics.getHeight()/2-50);
        btnInfos.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new InfosScreen(game));
                };
        });    
        
        TextButton btnQuitter = new TextButton("Quitter", skin);
        btnQuitter.setPosition(Gdx.graphics.getWidth()/2 - Gdx.graphics.getWidth()/8 , Gdx.graphics.getHeight()/2-150);
        btnQuitter.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.exit();
                };
        });  
        
        stage.addActor(btnNvlPartie);
        stage.addActor(btnChargerPartie);
        stage.addActor(btnInfos);
        stage.addActor(btnQuitter);
    }
    
    private void create() {     
        //Create a font
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
    }

    @Override
    public void show() {
    }
/**
 * Cette methode sert pour l'affichage dans l'ecrans du menu
 * 
 */
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
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
        stage.dispose();
        skin.dispose();
    }
    
    
}
