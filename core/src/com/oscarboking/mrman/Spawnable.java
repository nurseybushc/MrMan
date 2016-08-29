package com.oscarboking.mrman;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

/**
 * Created by Boking on 2016-07-25.
 */
public interface Spawnable {

    public void update();
    public void destroy();
    public float getX();
    public void redraw(SpriteBatch batch);
    public BodyDef getBodyDef();
    public boolean isFlaggedForKill();
    public Body getBody();
}
