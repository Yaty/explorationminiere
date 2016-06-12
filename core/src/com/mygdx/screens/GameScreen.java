package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.mygdx.gameworld.GameRenderer;
import com.mygdx.gameworld.GameWorld;
import com.mygdx.mehelpers.KeyBoard;
import com.mygdx.minexploration.MEGame;

/**
 *
 * @author Hugo
 */
public class GameScreen implements Screen {
    private final GameWorld gameWorld;
    private final GameRenderer gameRenderer;
    private MEGame game;
    
    /**
     *
     */
    public GameScreen(MEGame game) {
        Gdx.app.log("GameScreen", "GameScreen créé");
        this.game = game;
        gameWorld = new GameWorld();
        gameRenderer = new GameRenderer(gameWorld);
        Gdx.input.setInputProcessor(new KeyBoard());
    }
    
    /**
     *
     */    
    @Override
    public void show() {
        Gdx.app.log("GameScreen", "show appelé");
    }
    
    /**
     *
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
     *
     * @param width
     * @param height
     */   
    @Override
    public void resize(int width, int height) {
        Gdx.app.log("GameScreen", "resize appelé");
    }
    
    /**
     *
     */   
    @Override
    public void pause() {
        Gdx.app.log("GameScreen", "pause appelé");
    }
    
    /**
     *
     */   
    @Override
    public void resume() {
        Gdx.app.log("GameScreen", "resume appelé");
    }
    
    /**
     *
     */   
    @Override
    public void hide() {
        Gdx.app.log("GameScreen", "hide appelé");
    }
    
    /**
     *
     */   
    @Override
    public void dispose() {}
}
