package com.mygdx.tap.Screens;

import com.badlogic.gdx.Application;
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
    private Texture heart;
    private Texture gameover;

    private Rectangle skeletonRect;
    private Rectangle hitRectangle;
    private Array<Rectangle> meteors;
    private Array<Float> rotations;
    private Array<Float> speeds;

    private String scoreText;
    private long score = 0;
    private int hitpoints = 8;
    private float renderX;
    private float renderY;
    private float lastMeteor;
    private float lastHit = 0.0f;
    private int direction = 1;

    private BitmapFont bf = new BitmapFont();
    ShapeRenderer sr = new ShapeRenderer();

    public MainGame(Tap tap) {
        parent = tap;
        this.tap = tap;

        stage = new Stage(new FitViewport(600, 700));
        skeleton = new Texture("skeletodd.png");
        meteorDrop = new Texture("meteor.png");
        hitStar = new Texture("hit.png");
        heart = new Texture("heart_pixel_art_32x32.png");
        gameover = new Texture("deadskeleton.jpg");

        camera = new OrthographicCamera();
        camera.setToOrtho(false,600,700);
        meteors = new Array<Rectangle>();
        rotations = new Array<Float>();
        speeds = new Array<Float>();
        dropMeteor();

        skeletonRect = new Rectangle();
        hitRectangle = new Rectangle();

        skeletonRect.x = 300 - 64;
        skeletonRect.y = 10;
        skeletonRect.height=64;
        skeletonRect.width=64;
        renderX=100;
        renderY=100;

        score();
    }

    @Override
    public void render(float delta) {
        if(hitpoints <= 0) {
            endgame(delta);
            return;
        }

        play(delta);
    }

    public void endgame(float delta) {
        parent.batch.begin();
            parent.batch.draw(gameover,0,0);
        parent.batch.end();
    }

    public void play(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0.5f, 300);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.getViewport().apply();
        camera.update();
        updateThings(delta);


        parent.batch.setProjectionMatrix(camera.combined);

        sr.setProjectionMatrix(camera.combined);
        sr.setColor(Color.BROWN);
        sr.begin(ShapeRenderer.ShapeType.Filled);

        /* debug rects */
//            for (Rectangle meteor : meteors) {
//                sr.rect(meteor.x, meteor.y,meteor.width, meteor.height);
//            }
//
//            sr.setColor(Color.WHITE);
//            sr.rect(skeletonRect.x, skeletonRect.y,skeletonRect.width, skeletonRect.height);

        sr.rect(0,0,600,25);
        sr.end();

        parent.batch.begin();
            parent.batch.draw(skeleton,
                (direction==-1
                    ? skeletonRect.x + skeletonRect.width
                    : skeletonRect.x
                ),
                skeletonRect.y,
                skeletonRect.width*direction,
                skeletonRect.height
            );

            if(lastHit > 0.0f){
                parent.batch.draw(hitStar, hitRectangle.x, hitRectangle.y, hitRectangle.width, hitRectangle.height);
            }
            for(int i = 0; i != meteors.size; i++) {
                Rectangle meteor = meteors.get(i);
//            for(Rectangle meteor: meteors) {
//                meteorSprite.draw(parent.batch);
//                parent.batch.draw(meteorDrop, meteor.x, meteor.y, meteor.width, meteor.height);
                parent.batch.draw(
                    meteorDrop,
                    meteor.x,
                    meteor.y,
                    32,32,
                    meteor.width,
                    meteor.height,
                    1,1,
                    rotations.get(i),
                    0,0,meteorDrop.getWidth(),meteorDrop.getHeight(),false,false
                );
            }

            bf.draw(parent.batch, scoreText, 10,690);
            for(int i=0; i!= hitpoints; i++) parent.batch.draw(heart, 10 + (10*i) + (32*i), 640);
        parent.batch.end();

    }


    private void updateThings(float delta){
        if(lastHit > 0.0f) {
            lastHit -= delta;
        }

        if(Gdx.app.getType() == Application.ApplicationType.Android) {
            renderX -= Gdx.input.getAccelerometerX();
        } else {
            renderX = Gdx.input.getX();
        }

        if(renderX < 0) renderX = 0;
        if(renderX > stage.getViewport().getWorldWidth()-64) renderX = stage.getViewport().getWorldWidth()- 64;
        if(Math.abs(skeletonRect.x - renderX) > 1.5f){ //threshold to change direction
            direction = (skeletonRect.x > renderX) ? -1: 1;
        }
        skeletonRect.x=renderX;// + (skeletonRect.width);

        lastMeteor+=delta;
        if(lastMeteor >= (1.0f - score/1000.0f) ) dropMeteor();

//        for (Iterator<Rectangle> iter = meteors.iterator(); iter.hasNext(); ) {
        for(int i = meteors.size-1; i >= 0; --i){
            Rectangle meteor = meteors.get(i);
            meteor.y -= 200 * delta;
            rotations.set(i, rotations.get(i)+speeds.get(i) );

            if(meteor.y < 0) {
                hitRectangle.set(meteor);
                lastHit = 0.20f; // one second
                score+=5;
                parent.playSfx(Tap.crashSound);
                meteors.removeIndex(i);
                rotations.removeIndex(i);
                speeds.removeIndex(i);
            }
            if(meteor.overlaps(skeletonRect)){
                System.out.println("collision");
                hitRectangle.set(skeletonRect);
                lastHit = 0.4f; // one second
                score -= 10;
                hitpoints--;
                parent.playSfx(Tap.oofSound);
                meteors.removeIndex(i);
                rotations.removeIndex(i);
                speeds.removeIndex(i);
            }
        }

        score();
    }

    int t = 0;
    private void dropMeteor() {
        Rectangle meteor = new Rectangle();
        meteor.x = MathUtils.random(0, 600-64) ;
//        meteor.x = t++;
        meteor.y = 700;
        meteor.width = 64;
        meteor.height = 64;
        rotations.add( 0.0f );
        meteors.add(meteor);
        speeds.add(MathUtils.randomBoolean() ? 2.5f : -2.5f);
        lastMeteor = 0;
    }

    private void score(){
        scoreText =  String.format("Score: %4s %.4f", score, score/1000.0f);
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

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }


}
