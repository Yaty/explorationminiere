package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.mygdx.gameworld.GameRenderer;
import com.mygdx.gameworld.GameWorld;
import com.mygdx.mehelpers.KeyBoard;
import com.mygdx.minexploration.MEGame;

/**
 * Classe qui est l'écran du jeu, c'est elle qui contient les éléments du jeu
 * et se fait afficher par un TiledMapRenderer.
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class GameScreen implements Screen {
    private final GameWorld gameWorld;
    private final GameRenderer gameRenderer;
    private final MEGame game;
    
    /**
     * @param game le jeu
     */
    public GameScreen(MEGame game) {
        Gdx.app.log("GameScreen", "GameScreen créé");
        this.game = game;
        gameWorld = new GameWorld();
        gameRenderer = new GameRenderer(gameWorld);
        Gdx.input.setInputProcessor(new KeyBoard());
    }
    
    /**
     * Méthode appelée à la création du jeu
     */    
    @Override
    public void show() {
        Gdx.app.log("GameScreen", "show appelé");
    }
    
    /**
     * Méthode appelée à chaque frame
     */   
    @Override
    public void render(float delta) {
        gameWorld.update(delta);
        gameRenderer.render(gameWorld.getMineur().getRunTime());
        if(gameWorld.getMineur().getCellsHandler().isVictory()) {
            game.createMenuFin();
        }
    }
    
    /**
     * Redimensionne la fenetre
     * @param width la largeur
     * @param height la hauteur
     */   
    @Override
    public void resize(int width, int height) {
        Gdx.app.log("GameScreen", "resize appelé");
    }
    
    /**
     * Méthode appelée quand le jeu passe en pause
     */   
    @Override
    public void pause() {
        Gdx.app.log("GameScreen", "pause appelé");
    }
    
    /**
     * Méthode appelée quand le jeu passe sort de la pause
     */   
    @Override
    public void resume() {
        Gdx.app.log("GameScreen", "resume appelé");
    }
    
    /**
     * Méthode appelée quand la fenetre est minimiser
     */   
    @Override
    public void hide() {
        Gdx.app.log("GameScreen", "hide appelé");
    }
    
    /**
     * Méthode appelée quand on demande de supprimer le contenu de l'écran
     */   
    @Override
    public void dispose() {}
}
