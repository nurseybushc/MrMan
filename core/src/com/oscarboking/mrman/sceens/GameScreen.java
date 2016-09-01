package com.oscarboking.mrman.sceens;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.oscarboking.mrman.LevelGenerator;
import com.oscarboking.mrman.Player;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.oscarboking.mrman.Spawnable;
import com.oscarboking.mrman.listeners.CollisionDetector;
import java.util.List;

import javafx.scene.control.Tab;

/**
 * Created by Boking on 2016-07-17.
 */
public class GameScreen implements Screen{

    private Game game;
    private Box2DDebugRenderer debugRenderer;
    private World world;
    private OrthographicCamera camera;
    private Vector3 bottomLeft, bottomRight, upperLeft,upperRight;

    private float currentScore;
    private Vector2 movement = new Vector2();

    public boolean isFirst;

    //Bodies
    private Array<Body> tmpBodies = new Array<Body>();
    private Player player;

    //Sprites
    private SpriteBatch batch;

    private final float TIMESTEP = 1 / 60f;
    //The higher the numbers, the higher quality of simulation
    private final int VELOCITYITERATIONS = 8;
    private final int POSITIONITERATIONS = 3;

    private com.oscarboking.mrman.LevelGenerator levelGenerator;

    private TextureAtlas atlas;

    private boolean isPaused;

    //GUI VARIABLES
    private Skin skin;
    private BitmapFont font;
    private Label scoreLabel;
    private LabelStyle textStyle;
    private Stage stage;
    private Stage backgroundStage;
    private Table table;
    private Table backgroundTable;
    private Label deathLabel;
    private Label deathScore;
    private Label deathHighscore;
    private Label deathRestartLabel;
    private TextButton pauseButton;
    private Label pauseLabel;

    private TextureAtlas buttonAtlas;
    private TextButton.TextButtonStyle buttonStyle;

    public static final short DEFAULT = 1;
    public static final short PLAYER_BIT = 2;
    public static final short WIZARD_BIT = 4;
    public static final short WALL_BIT = 8;
    public static final short DESTROYED_BIT = 16;

    InputMultiplexer multiplexer;

    Preferences prefs;

    TextureRegionDrawable backgroundImage;
    Texture backgroundTexture;

    public GameScreen(Game game, boolean isFirst){
        this.game = game;
        this.isFirst = isFirst;
        prefs = Gdx.app.getPreferences("My Preferences");

    }

    public Game getGame(){
        return game;
    }

    public List<com.oscarboking.mrman.Spawnable> getObjectInWorld(){
        return levelGenerator.getObjectInWorld();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0 / 255f, 0 / 255f, 0 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        if(!isPaused) {

            levelGenerator.setPlayerX(player.getX());

            if (!player.isAlive()) {
                gameOver();
                player.setAlive(true);
                player.setPlaying(false);
            }

            world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);


            if (player.canDash()) {
                scoreLabel.setColor(Color.FOREST);
            } else {
                scoreLabel.setColor(Color.RED);
            }
            if (player.getBody().getPosition().y > 35) {
                player.getBody().setLinearVelocity(new Vector2(player.getBody().getLinearVelocity().x, -5));
            }
            camera.position.set(player.getBody().getPosition().x + 25, +15, 0);
            camera.update();

            player.update(delta);
            levelGenerator.update(camera.position.x + camera.viewportWidth / 2);

            //commented out since the player doesn't have any sprite yet
            // player.draw(batch);

            currentScore = player.getScore();
            stage.act();
            stage.draw();
        }

        levelGenerator.draw(batch);

        //we dont need to do this v because of ^, i think
        /*world.getBodies(tmpBodies);
        for (Body body : tmpBodies) {
            if (body.getUserData() != null && body.getUserData() instanceof Sprite) {
                Sprite sprite = (Sprite) body.getUserData();
                sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);
                sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
                sprite.draw(batch);
            }
        }*/

        batch.end();

        //update GUI
        scoreLabel.setText(String.format("%.0f", currentScore));

        //ENABLED DEBUG RENDERING
        debugRenderer.render(world, camera.combined);

        if(isFirst){
            System.out.println("isfirst we should pause");
            setPauseModeTrue();
            pauseLabel.setText("<- Tap left side to Jump \nTap right side to dash/use item ->");
            deathScore.setVisible(false);
        }

    }

    public void setIsFirst(boolean first){
        isFirst = first;
    }

    public SpriteBatch getBatch(){
        return batch;
    }

    public TextureAtlas getTextureAtlas(){
        return atlas;
    }

    public void gameOver(){
        //Player has died.


        if(prefs.getInteger("highscore")<currentScore){
            System.out.println("new highscore!");
            prefs.putInteger("highscore", Math.round(currentScore));
            prefs.flush();
            deathHighscore.setText("Highscore: "+Integer.toString(Math.round(currentScore)));
        }

        player.getBody().setLinearVelocity(0, 0);
        scoreLabel.setVisible(false);
        deathLabel.setVisible(true);
        deathScore.setText(Integer.toString(Math.round(currentScore)));
        deathScore.setVisible(true);
        deathHighscore.setVisible(true);
        deathRestartLabel.setVisible(true);
    }
    public void setPauseModeTrue() {
        isPaused = true;
        scoreLabel.setVisible(false);
        pauseLabel.setText("Paused");
        pauseLabel.setVisible(true);
        deathScore.setText("Score: " + String.format("%.0f", currentScore));
        deathScore.setVisible(true);
    }

    public void setPauseModeFalse(){
        isPaused=false;
        isFirst = false;
        scoreLabel.setVisible(true);
        pauseLabel.setVisible(false);
        deathScore.setVisible(false);
    }

    public Player getPlayer(){
        return player;
    }

    public boolean isPaused(){
        return isPaused;
    }

    @Override
    public void show() {

        currentScore = 0;

        backgroundTexture = new Texture("background.jpg");
        backgroundImage = new TextureRegionDrawable(new TextureRegion(backgroundTexture));
        atlas = new TextureAtlas("spritesheet.pack");

        //Initiate GUI components
        stage = new Stage();
        backgroundStage = new Stage();
        font = new BitmapFont(Gdx.files.internal("fonts/gamefont.fnt"));

        skin = new Skin();
        buttonAtlas = new TextureAtlas("buttons/pause.pack");
        skin.addRegions(buttonAtlas);
        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = skin.getDrawable("button");
        buttonStyle.over = skin.getDrawable("button");
        buttonStyle.down = skin.getDrawable("button");
        buttonStyle.font = font;

        pauseButton = new TextButton("||", buttonStyle);

        pauseButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("paused");
                if(isPaused()){
                    setPauseModeFalse();
                }else{
                    setPauseModeTrue();
                }
                //pause game

                return true;
            }
        });

        textStyle = new LabelStyle(font, Color.WHITE);
        scoreLabel = new Label(Float.toString((currentScore / 1000) * 1000), textStyle);
        scoreLabel.setPosition(Gdx.graphics.getWidth() / 2 - scoreLabel.getWidth(), Gdx.graphics.getHeight() / 2);
        scoreLabel.setFontScale(2);

        pauseLabel = new Label("Paused", textStyle);
        pauseLabel.setPosition(Gdx.graphics.getWidth() / 2 - pauseLabel.getWidth(), Gdx.graphics.getHeight() / 2);
        pauseLabel.setFontScale(2);

        if(!isFirst){
            pauseLabel.setVisible(false);
        }

        deathLabel = new Label("Game Over!", textStyle);
        deathLabel.setPosition(Gdx.graphics.getWidth() - deathLabel.getWidth(), Gdx.graphics.getHeight());
        deathLabel.setFontScale(3);
        deathLabel.setVisible(false);

        deathScore = new Label("Score: " + String.format("%.0f", currentScore), textStyle);
        deathScore.setPosition(Gdx.graphics.getWidth() - deathScore.getWidth(), Gdx.graphics.getHeight() + deathScore.getHeight() * 2);
        deathScore.setFontScale(3);
        deathScore.setVisible(false);

        deathHighscore = new Label("Highscore: " + Integer.toString(prefs.getInteger("highscore")), textStyle);
        deathHighscore.setPosition(Gdx.graphics.getWidth() - deathHighscore.getWidth(), Gdx.graphics.getHeight() + deathHighscore.getHeight() * 2);
        deathHighscore.setFontScale(1.5f);
        deathHighscore.setVisible(false);

        deathRestartLabel = new Label("Tap to restart!", textStyle);
        deathRestartLabel.setPosition(Gdx.graphics.getWidth() - deathRestartLabel.getWidth(), Gdx.graphics.getHeight() + deathRestartLabel.getHeight() * 2);
        deathRestartLabel.setFontScale(3);
        deathRestartLabel.setVisible(false);


        world = new World(new Vector2(0,-100.0f),true);

        debugRenderer = new Box2DDebugRenderer();

        batch = new SpriteBatch();

        camera = new OrthographicCamera();

        bottomLeft = new Vector3(0,Gdx.graphics.getHeight(),0);
        bottomRight = new Vector3(Gdx.graphics.getWidth(),bottomLeft.y,0);
        //upperLeft = new Vector3(camera.position.x-camera.viewportWidth/2,camera.position.y-camera.viewportHeight/2,0);
        camera.unproject(bottomLeft);
        camera.unproject(bottomRight);

        //Body definitions
        //ground
        BodyDef groundDef = new BodyDef();
        groundDef.type = BodyDef.BodyType.StaticBody;
        groundDef.position.set(0, 0);


        //Body Shapes
        //Ground shape
        ChainShape groundShape = new ChainShape();
        groundShape.createChain(new Vector2[]{new Vector2(-500, 0), new Vector2(10000, 0)});

        //Fixture definitions
        //Player
        FixtureDef playerFixture = new FixtureDef();

        //ground
        FixtureDef groundFixture = new FixtureDef();
        groundFixture.shape = groundShape;
        groundFixture.friction = 0f;
        groundFixture.restitution = 0;

        //player
        player = new Player(world,this,4,8,1,1);

        world.setContactListener(new CollisionDetector(world,this));

        multiplexer = new InputMultiplexer(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {

                return false;
            }
        }, player);

        table = new Table();
        table.setBackground(backgroundImage);
        table.setFillParent(true);
        table.add(pauseButton).width(pauseButton.getWidth() * 3).height(pauseButton.getHeight() * 3).top().left().expand().row();
        table.add(scoreLabel).top().left().expand();
        table.row();
        table.add(pauseLabel).top().center().expand();
        table.row();
        table.add(deathLabel).padBottom(deathLabel.getHeight() * 2).top().center().expand();;
        table.row();
        table.add(deathScore).padBottom(deathScore.getHeight()).top().center().expand();;
        table.row();
        table.add(deathHighscore).padBottom(deathHighscore.getHeight() * 2).top().center().expand();;
        table.row();
        table.add(deathRestartLabel).top().center().expand();;
        table.row();
        //table.debug(); //show debug lines
        stage.addActor(table);

        Body ground = world.createBody(groundDef);
        ground.createFixture(groundFixture);

        groundShape.dispose();

        levelGenerator = new LevelGenerator(world,ground,player.getBody(),this,
                bottomLeft.y,camera.viewportHeight/2,
                player.height*70,player.height*150,
                1, 4, player.height/3);

        InputProcessor inputProcessorOne = stage;
        InputProcessor inputProcessorTwo = multiplexer;
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(inputProcessorOne);
        inputMultiplexer.addProcessor(inputProcessorTwo);
        Gdx.input.setInputProcessor(inputMultiplexer);

        System.out.println("checking if we should pause");
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width/25;
        camera.viewportHeight = height/25;
        camera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        world.dispose();
        debugRenderer.dispose();
        //player.getSprite().getTexture().dispose();
        player.dispose();
    }

}
