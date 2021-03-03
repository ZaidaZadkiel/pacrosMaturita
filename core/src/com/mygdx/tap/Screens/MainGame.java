package com.mygdx.tap.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.tap.Tap;

public class MainGame implements Screen {
    private final Stage stage;
    private final Tap tap;
    private Tap parent;
    OrthographicCamera camera;
    private Texture skeleton;
    private Rectangle skeletonRect;
    private float renderX;
    private float renderY;







    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    public MainGame(Tap tap) {
        parent = tap;
        this.tap = tap;
        stage = new Stage(new FitViewport(600, 700));
        skeleton = new Texture("skeletodd.png");
        camera = new OrthographicCamera();
        camera.setToOrtho(false,600,700);
        skeletonRect = new Rectangle();
        skeletonRect.x = 300 - 64;
        skeletonRect.y = 10;
        skeletonRect.height=64;
        skeletonRect.width=64;
        renderX=100;
        renderY=100;

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 300);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.getViewport().apply();
        camera.update();
        parent.batch.setProjectionMatrix(camera.combined);

        renderX -= Gdx.input.getAccelerometerX();
        if(renderX < 0) renderX = 0;
        if(renderX > Gdx.graphics.getWidth()-128) renderX = Gdx.graphics.getWidth() - 128;

        parent.batch.begin();
        parent.batch.draw(skeleton,renderX,10,128,128);
        parent.batch.end();

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
