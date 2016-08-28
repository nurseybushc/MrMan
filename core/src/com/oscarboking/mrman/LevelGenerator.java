package com.oscarboking.mrman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
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

    //item drops
    PistolDrop pistolDrop;

    public LevelGenerator(World world, Body environment, Body playerBody, GameScreen screen,
                          float bottomBound, float upperBound, float minGap, float maxGap,
                          float minWidth, float maxWidth, float height){


       // backgroundTexture = new Texture("background.jpg");
       // backgroundSprite = new Sprite(backgroundTexture);

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

        this.world = world;
    }

    public void drawBackground(SpriteBatch batch){
        //backgroundSprite.draw(batch);
    }

    public void setPlayerX(float x){
        playerX = x;
    }

    //re-draw all the sprites (except player)
    public void draw(SpriteBatch batch){
        drawBackground(batch);
        for(com.oscarboking.mrman.Spawnable sprite : objectInWorld){
            sprite.redraw(batch);
        }

    }

    public void update(float bottomRight) {


        if (bottomRight > 100) {
            //update current objects as well
            for (com.oscarboking.mrman.Spawnable o : objectInWorld) {
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
            spawnNumber = ThreadLocalRandom.current().nextInt(0, 5 + 1);

            if(MathUtils.random(0,10) < 2){
                //choose which item to spawn and position
                int itemDropNumber = ThreadLocalRandom.current().nextInt(0, 0 + 1);
                int itemY = ThreadLocalRandom.current().nextInt(0,20+1);
                int itemX = Math.round(x) + ThreadLocalRandom.current().nextInt(-10,10);

                if(itemDropNumber == 0){
                    pistolDrop = new PistolDrop(world,screen,x,y);
                    objectInWorld.add(pistolDrop);
                }
            }
            //LIST OF PRESETS
            if (spawnNumber == 0) {
                //spawn spike
                Gdx.app.log("SPAWN", "SPIKE");
                spikes = new com.oscarboking.mrman.Spikes(world, screen, x, 1, width, 1);
                objectInWorld.add(spikes);
            } else if (spawnNumber == 1) {
                //spawn wizard
                Gdx.app.log("SPAWN", "wizard");
                wizard = new com.oscarboking.mrman.Wizard(world, screen, playerBody, x, 2, 1, 2);
                objectInWorld.add(wizard);
            } else if (spawnNumber == 2) {
                Gdx.app.log("SPAWN", "wizard on wall");
                //spawn wizard on top of wall
                wall = new Wall(world, screen, x, 3, 4, 3);
                wizard = new com.oscarboking.mrman.Wizard(world, screen, playerBody, x, 14, 1, 2);
                objectInWorld.add(wall);
                objectInWorld.add(wizard);
            } else if (spawnNumber == 3) {
                wall = new Wall(world, screen, x, 3, 4, 3);
                objectInWorld.add(wall);
            } else if (spawnNumber == 4) {
                wall = new Wall(world, screen, x, 30, 4, 3);
                wizard = new com.oscarboking.mrman.Wizard(world, screen, playerBody, x, 2, 1, 2);
                objectInWorld.add(wall);
                objectInWorld.add(wizard);
            } else if (spawnNumber == 5) {
                wall = new Wall(world, screen, x - 5, 30, 4, 3);
                wizard = new com.oscarboking.mrman.Wizard(world, screen, playerBody, x - 15, 2, 1, 2);
                spikes = new com.oscarboking.mrman.Spikes(world, screen, x + 10, 1, width, 1);
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
        Iterator<com.oscarboking.mrman.Spawnable> iterator = objectInWorld.iterator();
        com.oscarboking.mrman.Spawnable object;
        while (iterator.hasNext()){
            object = iterator.next();

            if(playerX > object.getX() || object.isFlaggedForKill()){
                //destroy the object
                object.destroy();
                iterator.remove();
                Gdx.app.log("KILL", "Killing object");
            }
            //object.destroy();
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
