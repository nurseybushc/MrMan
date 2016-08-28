package com.oscarboking.mrman.sceens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

/**
 * Created by Boking on 2016-07-13.
 */
public class PlayScreen implements Screen {

    private Stage stage;
    private Table table;

    private SpriteBatch batch;
    private TextureAtlas buttonAtlas;
    private TextButtonStyle buttonStyle;
    private Skin skin;
    private BitmapFont font;
    private BitmapFont white,black;
    private Label heading;
    private LabelStyle labelStyle;

    //UI components
    private TextButton fightButton;
    private TextButton inventoryButton;
    private Label levelLabel;

    private Game game;
    public PlayScreen(Game game){
            this.game = game;
    }

    //Data
    private int currentLevel;

    @Override
    public void show() {
        currentLevel = 1;
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));

        labelStyle = new LabelStyle(font, Color.WHITE);


        stage = new Stage();
        table = new Table();
        table.setBounds(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        skin = new Skin();
        buttonAtlas = new TextureAtlas("buttons/button.pack");
        skin.addRegions(buttonAtlas);
        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = skin.getDrawable("button");
        buttonStyle.over = skin.getDrawable("buttonpressed");
        buttonStyle.down = skin.getDrawable("buttonpressed");
        buttonStyle.font = font;


        //UI-components
        fightButton = new TextButton("Fight!!", buttonStyle);
        inventoryButton = new TextButton("Inventory", buttonStyle);
        levelLabel = new Label("Level :" + currentLevel + "/100",labelStyle);
        levelLabel.setFontScale(2);
        fightButton.setPosition((Gdx.graphics.getWidth() / 2) - fightButton.getWidth(), Gdx.graphics.getWidth() / 3);
        inventoryButton.setPosition((Gdx.graphics.getWidth() / 2) - inventoryButton.getWidth(), Gdx.graphics.getWidth() / 3 - fightButton.getHeight());
        levelLabel.setPosition(0, Gdx.graphics.getHeight());

        fightButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }
        });

        inventoryButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                showInventory();
                return true;
            }
        });

        Gdx.input.setInputProcessor(stage);

        //add to table
        table.add(levelLabel);
        table.add(fightButton);
        table.add(inventoryButton);
        table.debug();
        //add actor to stage
        stage.addActor(levelLabel);
        stage.addActor(fightButton);
        stage.addActor(inventoryButton);
    }

    public void startFight(){
        //Start the fight
    }

    public void showInventory(){
        //Show inventory
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
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

    }
}
