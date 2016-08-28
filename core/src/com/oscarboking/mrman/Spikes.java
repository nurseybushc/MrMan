package com.oscarboking.mrman;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Boking on 2016-07-18.
 */
public class Spikes extends Sprite implements Spawnable{

    private World world;

    protected Body body;
    protected BodyDef bodyDef;

    protected PolygonShape shape;
    protected Fixture fixture;
    protected FixtureDef fixtureDef;
    protected TextureRegion textureRegion;

    protected boolean destroyFlag;

    public Spikes(World world, com.oscarboking.mrman.sceens.GameScreen screen, float x, float y, float width, float height) {

        super(screen.getTextureAtlas().findRegion("wall"));

        destroyFlag = false;
        this.world = world;

        shape = new PolygonShape();
        shape.setAsBox(width * 2, height * 2);

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y + height);
        bodyDef.fixedRotation = true;

        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 5;

        this.body = world.createBody(bodyDef);
        this.body.createFixture(fixtureDef).setUserData(this);


        textureRegion = new TextureRegion(getTexture(), 176, 0, 76, 120);
        setBounds(176 / 16f, 0 / 15f, 76 / 17f, 120 / 16f);
        setRegion(textureRegion);

        shape.dispose();
    }

    @Override
    public void update() {
        setPosition(body.getPosition().x-1*2,body.getPosition().y-2*2);
    }

    @Override
    public float getX() {
        return body.getPosition().x;
    }

    @Override
    public void redraw(SpriteBatch batch) {
        draw(batch);
    }

    @Override
    public void destroy() {
        destroyFlag = true;
    }

    public BodyDef getBodyDef() {
        return bodyDef;
    }

    @Override
    public boolean isFlaggedForKill() {
        return destroyFlag;
    }
}
