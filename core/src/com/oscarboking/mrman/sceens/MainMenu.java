package com.oscarboking.mrman.sceens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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

    Texture backgroundTexture;

    private Stage stage;
    private TextButton buttonPlay, buttonExit;
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

        font = new BitmapFont(Gdx.files.internal("fonts/gamefont.fnt"));
        font.getData().setScale(2);

        labelStyle = new LabelStyle(font, Color.WHITE);

        heading = new Label(com.oscarboking.mrman.MyGdxGame.TITLE,labelStyle);


        heading.setPosition(Gdx.graphics.getWidth() / 2 - heading.getWidth(), Gdx.graphics.getHeight() / 2);
        heading.setFontScale(3);

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
        buttonPlay.setPosition(0,0);

        buttonExit = new TextButton("Quit", buttonStyle);
        buttonExit.setPosition((Gdx.graphics.getWidth() - buttonExit.getWidth()), 0);

        //Gdx.input.setInputProcessor(stage);

        buttonPlay.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("clicked play!");
                game.setScreen(new GameScreen(game,true));
                //((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen());
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
        table.add(heading).padBottom(100);
        table.row();
        table.add(buttonPlay).width(Gdx.graphics.getWidth()/5).height(Gdx.graphics.getHeight()/10).padBottom(30);
        table.row();
        table.add(buttonExit).width(Gdx.graphics.getWidth()/5).height(Gdx.graphics.getHeight()/10);
        //table.debug();    show debug lines
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

    }
}
