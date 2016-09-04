package com.oscarboking.mrman.sceens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.oscarboking.mrman.Settings;

/**
 * Created by boking on 2016-09-04.
 */
public class OptionsMenu implements Screen {

    private Game game;

    private SpriteBatch batch;
    private TextureAtlas buttonAtlas;
    private TextButton.TextButtonStyle buttonStyle;
    private Skin skin;
    private BitmapFont font;
    private Label.LabelStyle labelStyle;
    private Stage stage;
    private Table table;

    private Label soundLabel;
    private TextButton soundButton;
    private Label musicLabel;
    private TextButton musicButton;
    private Label.LabelStyle textStyle;
    private TextButton backButton;

    private Sound menuSelectSound;

    public OptionsMenu(Game game){
        this.game = game;
    }

    @Override
    public void show() {

        stage = new Stage();

        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();

        menuSelectSound = Gdx.audio.newSound(Gdx.files.internal("music/doublejump.ogg"));
        font = new BitmapFont(Gdx.files.internal("fonts/gamefont.fnt"));
        font.getData().setScale(2);

        skin = new Skin();
        buttonAtlas = new TextureAtlas("buttons/button.pack");
        skin.addRegions(buttonAtlas);
        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = skin.getDrawable("button");
        buttonStyle.over = skin.getDrawable("button_down");
        buttonStyle.down = skin.getDrawable("button_down");
        buttonStyle.font = font;
        textStyle = new Label.LabelStyle(font, Color.WHITE);

        soundLabel = new Label("Sound:",textStyle);
        musicLabel = new Label("Music:",textStyle);
        soundLabel.setFontScale(3);
        musicLabel.setFontScale(3);

        if(Settings.isSoundEnabled()){
            soundButton = new TextButton("Enabled",buttonStyle);
        }else{
            soundButton = new TextButton("Disabled",buttonStyle);
        }
        if(Settings.isMusicEnabled()){
            musicButton = new TextButton("Enabled",buttonStyle);
        }else{
            musicButton = new TextButton("Disabled",buttonStyle);
        }

        backButton = new TextButton("Back",buttonStyle);


        soundButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(Settings.isSoundEnabled()){
                    Settings.setSoundEnabled(false);
                    soundButton.setText("Disabled");
                }else{
                    menuSelectSound.play(1.0f);
                    Settings.setSoundEnabled(true);
                    soundButton.setText("Enabled");
                }
                return true;
            }
        });

        musicButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(Settings.isSoundEnabled()){
                    menuSelectSound.play(1.0f);
                }
                if(Settings.isMusicEnabled()){
                    Settings.setMusicEnabled(false);
                    musicButton.setText("Disabled");
                }else{
                    Settings.setMusicEnabled(true);
                    musicButton.setText("Enabled");
                }
                return true;
            }
        });

        backButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(Settings.isSoundEnabled()){
                    menuSelectSound.play(1.0f);
                }
                game.setScreen(new MainMenu(game));
                return true;
            }
        });
        backButton.align(Align.center);

        table = new Table();
        table.setFillParent(true);
        table.add(soundLabel);
        table.add(soundButton).padLeft(100).row();
        table.add(musicLabel).padTop(100);
        table.add(musicButton).padLeft(100).padTop(100).row();
        table.row();
        table.add(backButton).padTop(150).colspan(3).center();

        stage.addActor(table);

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(40 / 255f, 37 / 255f, 44 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        batch.begin();
        stage.draw();
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
        menuSelectSound.dispose();
    }
}
