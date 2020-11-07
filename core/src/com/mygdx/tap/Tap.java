package com.mygdx.tap;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.tap.Screens.MainGame;
import com.mygdx.tap.Screens.Menu;
import com.mygdx.tap.Screens.Options;
import com.mygdx.tap.Utility.MusicPlayer;
import com.mygdx.tap.Utility.OptionsConfig;


public class Tap extends Game {
    public final static int MENUSCREEN = 0;
    public final static int GAMESCREEN = 1;
    public static final int OPTIONSCREEN = 2;
	OrthographicCamera camera;
    private Menu menu;
    private MainGame game;
    private Options options;
    private OptionsConfig optionsCfg;

    SpriteBatch batch;
    Texture img;
   MusicPlayer arcade = MusicPlayer.vratInstanci();

    @Override
    public void create() {
        menu = new Menu(this);
        game = new MainGame(this);
        options = new Options(this);
        optionsCfg = new OptionsConfig();
        setScreen(menu);
        arcade.muzika = Gdx.audio.newMusic(Gdx.files.internal("arcade.ogg"));
       // arcade.setLooping(true);
        //arcade.play();
        if(optionsCfg.musicOn()){

            arcade.muzika.play();
            arcade.muzika.setVolume(optionsCfg.getMusicVolume());
        }
        else{
            arcade.muzika.stop();
        }
    }
    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
    public void screenChanger(int screen){
        switch(screen){
            case MENUSCREEN:
                if(menu == null) menu = new Menu(this);
                this.setScreen(menu);
                break;
            case GAMESCREEN:
                if(game == null) game = new MainGame(this);
                this.setScreen(game);
                break;

            case OPTIONSCREEN:
                if(options == null) options = new Options(this);
                this.setScreen(options);
                break;
        }
    }

    public OptionsConfig getPreferences(){
        return this.optionsCfg;
    }


}
