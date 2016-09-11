package com.oscarboking.mrman;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.oscarboking.mrman.sceens.GameScreen;


/**
 * Created by Boking on 2016-07-17.
 */
public class Player extends Sprite implements InputProcessor{

    public enum State {FALLING,JUMPING,DASHING,RUNNING}
    public State currentState;
    public State previousState;

    private Animation playerRun;
    private Animation playerJump;
    private Animation playerDash;
    private float stateTimer; //tells us how long we are in any state etc.

    private World world;
    private Body body;
    private BodyDef bodyDef;

    PolygonShape playerShape;
    private Fixture fixture;
    private FixtureDef fixtureDef;

    Sprite playerSprite;

    private Vector2 velocity = new Vector2();
    private Vector2 jumpVector = new Vector2(0,1000f);
    private Vector2 doubleJumpVector = new Vector2(0,800f);
    private Vector2 dashVector = new Vector2(1000f,0);
    private float movementForce = 25000;

    private boolean canDoubleJump;
    private boolean offCooldown;
    private boolean isAirBound;
    private boolean isUsing;
    private boolean isAlive;

    private float targetSpeed;
    public  float width, height;
    private GameScreen mainGameScreen;

    private TextureRegion playerStatic;

    public Usable item;
    public float cooldown;
    public boolean isPlaying;
    public boolean itemOffCooldown;

    //Sounds and music
    Sound jumpSound;
    Sound doubleJumpSound;
    Sound dashSound;

    public float killScore;
    public int killsThisRound;
    public int jumpsThisRound;

    public TextureRegion textureRegion;

    public Player(World world, com.oscarboking.mrman.sceens.GameScreen screen, float x, float y, float width, float height){

        super(screen.getCharacterAtlas().findRegion("Samurai"));

        currentState = State.RUNNING;
        previousState = State.RUNNING;
        stateTimer = 0;
        killScore = 0;
        killsThisRound = 0;
        jumpsThisRound = 0;

        initializeSounds();

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i <= 5; i++){    //if the running animation has 6 frames
            frames.add(new TextureRegion(getTexture(),45,5+(30*i),30,22));
        }
        playerRun = new Animation(0.1f,frames);
        playerRun = new Animation(0.1f,frames);
        frames.clear();


        for(int i = 0; i <= 3; i++){    //if the jumping animation has 4 frames
            frames.add(new TextureRegion(getTexture(),90,5+(30*i),30,22));
        }
        playerJump = new Animation(0.1f,frames);
        frames.clear();

        /*
        for(int i = 1; i <= 3; i++){    //if the dash animation has 3 frames
            frames.add(new TextureRegion(getTexture(),i*336,0,76,120));
        }
        playerDash = new Animation(0.1f,frames);
*/
        this.world = world;

        item = null;
        itemOffCooldown = true;

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = true;

        playerShape = new PolygonShape();
        playerShape.setAsBox(width * 2, height * 2);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = playerShape;
        fixtureDef.density = 1.3f;

        fixtureDef.filter.categoryBits = GameScreen.PLAYER_BIT;
        fixtureDef.filter.maskBits = GameScreen.DEFAULT | GameScreen.WIZARD_BIT | GameScreen.WALL_BIT;

        this.body = world.createBody(bodyDef);
        this.body.createFixture(fixtureDef).setUserData(this);

        this.mainGameScreen = screen;

        this.width = width;
        this.height = height;

        targetSpeed = 30;
        offCooldown = true;
        isAirBound = false;
        isUsing = false;
        isAlive = true;

        isPlaying = true;

        textureRegion = new TextureRegion(getTexture(), 0, 0, 32, 32);
        setBounds(0/3f,0/3f, 32/5.5f, 32/ 5.5f);
        setRegion(textureRegion);

        body.setLinearVelocity(targetSpeed, 0);

    }

    public void initializeSounds(){
        jumpSound = Gdx.audio.newSound(Gdx.files.internal("music/jump.ogg"));
        doubleJumpSound = Gdx.audio.newSound(Gdx.files.internal("music/doublejump.ogg"));
        //dashSound = Gdx.audio.newSound(Gdx.files.internal("music/dash.flac"));

    }
    public void dispose(){
        jumpSound.dispose();
        doubleJumpSound.dispose();
        //dashSound.dispose();
    }

    public void setVelocity(float xVelocity, float yVelocity){
        body.setLinearVelocity(xVelocity,yVelocity);
    }

    public boolean hasItem(){
        if(item == null){
            return false;
        }else{
            return true;
        }
    }

    public void equipItem(Usable item){
        //diplay something?
        this.item = item;
    }

    public int getKillsThisRound(){
        return killsThisRound;
    }
    public int getJumpsThisRound(){return jumpsThisRound;}

    public void increaseKillScore(float value){
        killsThisRound++;
        killScore +=value;
    }
    public float getKillScore(){
        return killScore;
    }

    public void restoreVelocity(){
        body.setLinearVelocity(targetSpeed,body.getLinearVelocity().y);
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();

        TextureRegion region;
        switch (currentState){
            case JUMPING:
                region = playerJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = playerRun.getKeyFrame(stateTimer,true);
                break;
            case DASHING:
                region = playerDash.getKeyFrame(stateTimer);
                break;
            case FALLING:
            default:
                region = playerStatic;
                break;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState(){

        if(body.getLinearVelocity().y>0){
            return State.JUMPING;
        }else{
            return State.RUNNING;
        }
        /*
        if(body.getLinearVelocity().y>0){
            return State.JUMPING;
        }else if (body.getLinearVelocity().y<0){
            return State.FALLING;
        }else{
            if(isUsing())
                return State.DASHING;
            else
                return State.RUNNING;
        }
    */
    }
    public void setAlive(boolean alive){
        isAlive = alive;
    }

    public float getTargetSpeed(){
        return targetSpeed;
    }
    public void increaseTargetSpeed(float amount){
        targetSpeed+=amount;
        if(!isUsing){
            body.setLinearVelocity(targetSpeed,body.getLinearVelocity().y);
        }
    }
    public BodyDef getBodyDef(){
        return this.bodyDef;
    }
    public void update(float dt){

        setRegion(getFrame(dt));


        //update where the sprite is
        setPosition(body.getPosition().x-width*2-1,body.getPosition().y-height*2);

        if(targetSpeed > body.getLinearVelocity().x && isAlive){
            body.setLinearVelocity(targetSpeed,0);
        }


        //body.setLinearVelocity(targetSpeed,body.getLinearVelocity().y);

        if(!isUsing && isAlive){
            body.setLinearVelocity(targetSpeed,body.getLinearVelocity().y);
            body.setGravityScale(1);
        }else if(isUsing){

            body.setLinearVelocity(targetSpeed*2.5f,0);
            //body.setLinearVelocity(body.getLinearVelocity().x, 0);
            body.setGravityScale(0);
        }

        //When touching ground
        if(body.getLinearVelocity().y == 0.0f && offCooldown){
            canDoubleJump = true;
            isAirBound = false;
        }

        //Move the player
        //body.applyForceToCenter(velocity, true);

        //Figure out why the bolt isn't moving
        //update somehow?
        //How is the player position updated?

        if(!isPlaying){
            body.setLinearVelocity(0,0);
        }
    }

    public void setPlaying(boolean isPlaying){
        this.isPlaying = isPlaying;
    }

    public void hitRegistered(){
        Gdx.app.log("HIT","HIT REGISTERED");
        body.setLinearVelocity(0, 0);
        isPlaying = false;
        isAlive = false;
    }

    public float getScore(){
        return body.getPosition().x/5;
    }

    public String getScoreAsString(){
        return String.format ("%.0f", (body.getPosition().x/5));
    }
    public boolean isAlive(){
        return isAlive;
    }

    public boolean canDash(){
        return this.offCooldown;
    }

    public float getVelocityX(){
        return body.getLinearVelocity().x;
    }
    public float getVelocityY(){
        return body.getLinearVelocity().y;
    }

    public Sprite getSprite(){
        return this.playerSprite;
    }
    public Body getBody(){
        return this.body;
    }

    public boolean isUsing(){
        return isUsing;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {


        if(mainGameScreen.isPaused()){
            mainGameScreen.setPauseModeFalse();
        }else {
            if (!isPlaying) {
                if(!mainGameScreen.getDeathScreenTimer()) {
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(mainGameScreen.getGame(), false));
                    //mainGameScreen.getGame().setScreen(new GameScreen(mainGameScreen.getGame()));
                }
            }
            if (screenX < Gdx.graphics.getWidth() / 2) {

                if (body.getLinearVelocity().y == 0.0f && !isAirBound) {
                    jumpsThisRound++;
                    //normal jump
                    if(Settings.isSoundEnabled()) {
                        jumpSound.play(1.0f);
                    }
                    body.applyLinearImpulse(jumpVector, body.getWorldCenter(), true);
                    isAirBound = true;

                } else if (canDoubleJump) {
                    jumpsThisRound++;
                    //double jump
                    if(Settings.isSoundEnabled()) {
                        doubleJumpSound.play(1.0f);
                    }
                    if (body.getLinearVelocity().y > 0) {
                        doubleJumpVector.set(0, 900f);
                    } else {
                        doubleJumpVector.set(0, 1100f);
                    }
                    body.applyLinearImpulse(doubleJumpVector, body.getWorldCenter(), true);
                    canDoubleJump = false;
                    isAirBound = true;
                }
            }else {
                if (offCooldown && item == null) {

                    //dashSound.play(1.0f);
                    isUsing = true;
                    body.applyLinearImpulse(dashVector, body.getWorldCenter(), true);
                    offCooldown = false;

                    //Duration
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            isUsing = false;
                        }
                    }, 0.4f);

                    //Cooldown
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            offCooldown = true;
                        }
                    }, 0.8f);
                } else if (item != null && itemOffCooldown) {
                    cooldown = item.getCooldown();
                    itemOffCooldown = false;

                    item.use(screenX, screenY);

                    //Cooldown
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            itemOffCooldown = true;
                        }
                    }, cooldown);
                    }
                }
            }
        return true;
    }

    public void addPoints(float points){

    }


    /*@Override
    public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
        if(fixtureA == fixture || fixtureB==fixture){
            return body.getLinearVelocity().y < 0;
        }
        return false;
    }*/

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        item = null;
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
