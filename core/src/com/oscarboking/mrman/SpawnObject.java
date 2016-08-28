package com.oscarboking.mrman;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 * Created by Boking on 2016-07-25.
 */
public abstract class SpawnObject extends Sprite implements Spawnable {

    protected Body body;
    protected BodyDef bodyDef;

    protected PolygonShape shape;
    protected Fixture fixture;
    protected FixtureDef fixtureDef;

    protected float x,y,width,height;

    @Override
    public float getX() {
        return x;
    }

    @Override
    public BodyDef getBodyDef() {
        return bodyDef;
    }


}
