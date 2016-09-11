package com.oscarboking.mrman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.oscarboking.mrman.sceens.GameScreen;

import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Boking on 2016-07-18.
 */
public class LevelGenerator {
    private Body environment;
    private World world;
    private float bottomBound,upperBound, minGap, maxGap, minWidth, maxWidth, height, x;
    private com.oscarboking.mrman.Spikes spikes;
    private FixtureDef fixtureDef;
    private List<com.oscarboking.mrman.Spawnable> objectInWorld;
    private com.oscarboking.mrman.Wizard wizard;
    private Wall wall;
    private float spawnNumber;

    private Body playerBody;
    private float playerX;
    private GameScreen screen;

    public static Texture backgroundTexture;
    public static Sprite backgroundSprite;

    private float lastEnd;

    private Ground firstGround;
    private Ground secondGround;

    //item drops
    PistolDrop pistolDrop;

    public LevelGenerator(World world, Body environment, Body playerBody, GameScreen screen,
                          float bottomBound, float upperBound, float minGap, float maxGap,
                          float minWidth, float maxWidth, float height){

        this.screen = screen;
        this.playerBody = playerBody;
        objectInWorld = new LinkedList<com.oscarboking.mrman.Spawnable>();
        this.environment = environment;
        this.bottomBound = bottomBound;
        //this.upperBound = upperBound; // use 35 instead for now
        this.minGap = minGap;
        this.maxGap = maxGap;
        this.minWidth = minWidth;
        this.maxWidth = maxWidth;
        this.height = height;


        //(10,-12,20,4)
        firstGround = new Ground(world,screen,20,-12,20,4);
        secondGround = new Ground(world,screen,100,-12,20,4);
        System.out.println("getbody position: " + firstGround.getBody().getPosition().x);
        System.out.println("getbodydef position: "+ firstGround.getBodyDef().position.x);
        System.out.println("getspriteX : "+ firstGround.getSpriteX());
        objectInWorld.add(firstGround);
        objectInWorld.add(secondGround);

        this.world = world;
    }

    public void setPlayerX(float x){
        playerX = x;
    }

    //re-draw all the sprites (except player)
    public void draw(SpriteBatch batch){
        for(Spawnable sprite : objectInWorld){
            if(!sprite.isFlaggedForKill()) {
                sprite.redraw(batch);
            }
        }

    }

    public void update(float bottomRight) {


        if(screen.getCamera().position.x-(screen.getCamera().viewportWidth/2) > firstGround.getBody().getPosition().x+firstGround.getWidth()){

            firstGround.reposition(secondGround.getX());
            //firstGround.getBody().setTransform(new Vector2(secondGround.getX()+secondGround.getWidth(),-8), firstGround.getBody().getAngle());
            //firstGround.setPosition(secondGround.getBody().getPosition().x-20*2,secondGround.getBody().getPosition().y-6*2);

         }else if(screen.getCamera().position.x-(screen.getCamera().viewportWidth/2) > secondGround.getBody().getPosition().x+secondGround.getWidth()){

            secondGround.reposition(firstGround.getX());
            //secondGround.getBody().setTransform(new Vector2(firstGround.getX()+firstGround.getWidth(),-8), secondGround.getBody().getAngle());
            //secondGround.setPosition(firstGround.getBody().getPosition().x-20*2,firstGround.getBody().getPosition().y-6*2);
        }

        if (bottomRight > 100) {
            //update current objects as well
            for (Spawnable o : objectInWorld) {
                o.update();
            }

            //create new objects

            if (x + MathUtils.random(minGap, maxGap) > bottomRight) {
                return;
            }

            x = bottomRight;
            float width = MathUtils.random(minWidth, maxWidth);
            float y = MathUtils.random(bottomBound + 10, 30);


            //param 1: minimum value (first preset), param 2 maximum (doesn't include max so add 1)
            spawnNumber = MathUtils.random(0,5);

            if(MathUtils.random(0,10) < 2){
                System.out.println("spawning item");
                //choose which item to spawn and position
                int itemDropNumber = MathUtils.random(0,1);
                int itemY = MathUtils.random(0,20);
                int itemX = Math.round(x) + MathUtils.random(-10,10);

                if(itemDropNumber == 0){
                    pistolDrop = new PistolDrop(world,screen,x+itemX,y+itemY);
                    objectInWorld.add(pistolDrop);
                }
            }
            //LIST OF PRESETS
            if (spawnNumber == 0) {
                //spawn spike
                Gdx.app.log("SPAWN", "SPIKE");
                spikes = new Spikes(world, screen, x, 1, width, 1);
                objectInWorld.add(spikes);
            } else if (spawnNumber == 1) {
                //spawn wizard
                Gdx.app.log("SPAWN", "wizard");
                wizard = new Wizard(world, screen, playerBody, x, 2, 1, 2);
                objectInWorld.add(wizard);
            } else if (spawnNumber == 2) {
                Gdx.app.log("SPAWN", "wizard on wall");
                //spawn wizard on top of wall
                wall = new Wall(world, screen, x, 3, 4, 3);
                wizard = new Wizard(world, screen, playerBody, x, 14, 1, 2);
                objectInWorld.add(wall);
                objectInWorld.add(wizard);
            } else if (spawnNumber == 3) {
                wall = new Wall(world, screen, x, 3, 4, 3);
                objectInWorld.add(wall);
            } else if (spawnNumber == 4) {
                wall = new Wall(world, screen, x, 30, 4, 3);
                wizard = new Wizard(world, screen, playerBody, x, 2, 1, 2);
                objectInWorld.add(wall);
                objectInWorld.add(wizard);
            } else if (spawnNumber == 5) {
                wall = new Wall(world, screen, x - 5, 30, 4, 3);
                wizard = new Wizard(world, screen, playerBody, x - 15, 2, 1, 2);
                spikes = new Spikes(world, screen, x + 10, 1, width, 1);
                objectInWorld.add(spikes);
                objectInWorld.add(wall);
                objectInWorld.add(wizard);
            }

            killObjects();
        }
    }

    public List<com.oscarboking.mrman.Spawnable> getObjectInWorld(){
        return objectInWorld;
    }

    public void killObjects(){
        Iterator<Spawnable> iterator = objectInWorld.iterator();
        Spawnable object;
        while (iterator.hasNext()){
            object = iterator.next();
            if(object.isFlaggedForKill()){
                world.destroyBody(object.getBody());
                iterator.remove();
                Gdx.app.log("KILL", "REMOVE BECAUSE FLAG");
            }else if (playerX > object.getX() + 30){
                world.destroyBody(object.getBody());
                iterator.remove();
                Gdx.app.log("KILL", "Killing object");
            }
        }
    }

    public Body getEnvironment() {
        return environment;
    }

    public void setEnvironment(Body environment) {
        this.environment = environment;
    }

    public float getBottomBound() {
        return bottomBound;
    }

    public void setBottomBound(float bottomBound) {
        this.bottomBound = bottomBound;
    }

    public float getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(float upperBound) {
        this.upperBound = upperBound;
    }

    public float getMinGap() {
        return minGap;
    }

    public void setMinGap(float minGap) {
        this.minGap = minGap;
    }

    public float getMaxGap() {
        return maxGap;
    }

    public void setMaxGap(float maxGap) {
        this.maxGap = maxGap;
    }

    public float getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(float minWidth) {
        this.minWidth = minWidth;
    }

    public float getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(float maxWidth) {
        this.maxWidth = maxWidth;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
