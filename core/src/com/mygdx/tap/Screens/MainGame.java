package com.mygdx.tap.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.tap.Tap;

public class MainGame implements Screen {
    private final Stage stage;
    private Tap parent;
    OrthographicCamera camera;
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }
    public MainGame(Tap tap) {
        parent = tap;
        stage = new Stage(new FitViewport(600, 700));

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 1f, 300);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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

    }


}
