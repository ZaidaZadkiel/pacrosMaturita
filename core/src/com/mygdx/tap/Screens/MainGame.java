package com.mygdx.tap.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.tap.Tap;

import java.util.Iterator;

public class MainGame implements Screen {
    private final Stage stage;
    private final Tap tap;
    private Tap parent;
    OrthographicCamera camera;
    private Texture skeleton;
    private Texture meteorDrop;
    private Texture hitStar;
    private Rectangle skeletonRect;
    private Rectangle hitRectangle;
    private float renderX;
    private float renderY;
    private Array<Rectangle> meteors;
    private long lastMeteor;
    private float lastHit = 0.0f;

    //private Sprite spriteSkeleton = new Sprite(skeleton);
    //private Sprite spriteMeteor = new Sprite(meteorDrop);
    ShapeRenderer sr = new ShapeRenderer();



    private void dropMeteor() {
        Rectangle meteor = new Rectangle();
        meteor.x = MathUtils.random(0, 600-64);
        meteor.y = 480;
        meteor.width = 64;
        meteor.height = 64;
        meteors.add(meteor);
        lastMeteor = TimeUtils.nanoTime();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    public MainGame(Tap tap) {
        parent = tap;
        this.tap = tap;

        stage = new Stage(new FitViewport(600, 700));
        skeleton = new Texture("skeletodd.png");
        meteorDrop = new Texture("meteor.png");
        hitStar = new Texture("hit.png");

        camera = new OrthographicCamera();
        camera.setToOrtho(false,600,700);
        meteors = new Array<Rectangle>();
        dropMeteor();

        skeletonRect = new Rectangle();
        hitRectangle = new Rectangle();

        skeletonRect.x = 300 - 64;
        skeletonRect.y = 10;
        skeletonRect.height=64;
        skeletonRect.width=64;
        renderX=100;
        renderY=100;

    }

    private void updateThings(float delta){
        if(lastHit > 0.0f) {
            lastHit -= delta;
            Gdx.app.log("hit", String.valueOf(lastHit) );
        }

        if(TimeUtils.nanoTime() - lastMeteor > 1000000000) dropMeteor();

        for (Iterator<Rectangle> iter = meteors.iterator(); iter.hasNext(); ) {
            Rectangle meteor = iter.next();
            meteor.y -= 200 * delta;

            if(meteor.y < 0) iter.remove();
            if(meteor.overlaps(skeletonRect)){
                System.out.println("collision");
                hitRectangle.set(skeletonRect);
                lastHit = 1.0f; // one second
                iter.remove();
            }
        }

        renderX -= Gdx.input.getAccelerometerX();
        if(renderX < 0) renderX = 0;
        if(renderX > stage.getViewport().getWorldWidth()-64) renderX = stage.getViewport().getWorldWidth()- 64;

        skeletonRect.x=renderX-64;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0.5f, 300);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.getViewport().apply();
        camera.update();
        updateThings(delta);


        parent.batch.setProjectionMatrix(camera.combined);

//        if(TimeUtils.nanoTime() - lastMeteor > 1000000000) dropMeteor();

        sr.setProjectionMatrix(camera.combined);
        sr.setColor(Color.RED);
        sr.begin(ShapeRenderer.ShapeType.Line);

            for (Rectangle meteor : meteors) {
                sr.rect(meteor.x, meteor.y,meteor.width, meteor.height);
            }

            sr.setColor(Color.WHITE);
            sr.rect(skeletonRect.x, skeletonRect.y,skeletonRect.width, skeletonRect.height);

        sr.end();

        parent.batch.begin();
            parent.batch.draw(skeleton,skeletonRect.x,skeletonRect.y,skeletonRect.width,skeletonRect.height);
            if(lastHit > 0.0f){
                parent.batch.draw(hitStar, hitRectangle.x, hitRectangle.y, hitRectangle.width, hitRectangle.height);
            }
            for(Rectangle meteor: meteors) {
                parent.batch.draw(meteorDrop, meteor.x, meteor.y, meteor.width, meteor.height);
            }

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
        skeleton.dispose();

    }


}
