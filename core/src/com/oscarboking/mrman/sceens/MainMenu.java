package com.oscarboking.mrman.sceens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Align;
import com.oscarboking.mrman.MyGdxGame;


/**
 * Created by Boking on 2016-07-13.
 */
public class MainMenu implements Screen {

    private Game game;

    public MainMenu(Game game){
        this.game = game;
    }

    private SpriteBatch batch;
    private TextureAtlas buttonAtlas;
    private TextButtonStyle buttonStyle;
    private Skin skin;
    private BitmapFont font;
    private Label heading;
    private LabelStyle labelStyle;

    private Sound menuSelectSound;

    Texture backgroundTexture;

    private Stage stage;
    private TextButton buttonPlay, buttonExit, buttonStats, buttonLeaderboard, buttonOptions;
    private Table table;

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
    public void show() {

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        batch = new SpriteBatch();

        menuSelectSound = Gdx.audio.newSound(Gdx.files.internal("music/doublejump.ogg"));

        font = new BitmapFont(Gdx.files.internal("fonts/gamefont.fnt"));
        font.getData().setScale(2);

        labelStyle = new LabelStyle(font, Color.WHITE);

        heading = new Label(MyGdxGame.TITLE,labelStyle);

        heading.setPosition(Gdx.graphics.getWidth()/2 - heading.getWidth()/2, Gdx.graphics.getHeight() / 2+200);
        heading.setAlignment(Align.center);
        heading.setFontScale(3);
        heading.setWrap(true);

        table = new Table();
        table.setFillParent(true);

        skin = new Skin();
        buttonAtlas = new TextureAtlas("buttons/button.pack");
        skin.addRegions(buttonAtlas);
        buttonStyle = new TextButtonStyle();
        buttonStyle.up = skin.getDrawable("button");
        buttonStyle.over = skin.getDrawable("button_down");
        buttonStyle.down = skin.getDrawable("button_down");
        buttonStyle.font = font;

        buttonPlay = new TextButton("Play", buttonStyle);
        buttonStats = new TextButton("Stats", buttonStyle);
        buttonExit = new TextButton("Quit", buttonStyle);
        buttonLeaderboard = new TextButton("Leaderboards", buttonStyle);
        buttonOptions = new TextButton("Options", buttonStyle);

        //Gdx.input.setInputProcessor(stage);

        buttonPlay.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                menuSelectSound.play(1.0f);
                System.out.println("clicked play!");
                game.setScreen(new GameScreen(game,true));
                //((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen());
                return true;
            }
        });

        buttonStats.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                menuSelectSound.play(1.0f);
                System.out.println("clicked stats!");
                //game.setScreen(new StatScreen(game,true));
                return true;
            }
        });

        buttonOptions.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                menuSelectSound.play(1.0f);
                System.out.println("clicked options!");
                //game.setScreen(new OptionScreen(game,true));
                return true;
            }
        });

        buttonLeaderboard.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                menuSelectSound.play(1.0f);
                System.out.println("clicked leaderboard!");
                return true;
            }
        });

        buttonExit.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("clicked quit!");
                Gdx.app.exit();
                return true;
            }
        });
        stage.addActor(heading);
        table.add(buttonPlay).width(Gdx.graphics.getWidth()/5).height(Gdx.graphics.getHeight()/10).padRight(30).padBottom(50).padTop(300);
        table.add(buttonStats).width(Gdx.graphics.getWidth()/5).height(Gdx.graphics.getHeight()/10).padBottom(50).padTop(300);
        table.row();
        table.add(buttonLeaderboard).width(Gdx.graphics.getWidth()/5).height(Gdx.graphics.getHeight()/10).padRight(30);
        table.add(buttonOptions).width(Gdx.graphics.getWidth()/5).height(Gdx.graphics.getHeight()/10);
        //table.debug();
        stage.addActor(table);
    }


    @Override
    public void resize(int width, int height){
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
