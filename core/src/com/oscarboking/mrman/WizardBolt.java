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
import com.oscarboking.mrman.sceens.GameScreen;

/**
 * Created by Boking on 2016-07-25.
 */
public class WizardBolt extends Sprite implements Spawnable {

    private float randomY;
    private Body playerBody;
    protected Body body;
    protected BodyDef bodyDef;

    protected PolygonShape shape;
    protected Fixture fixture;
    protected FixtureDef fixtureDef;
    private TextureRegion textureRegion;
    private float width;
    protected boolean destroyFlag;

    public WizardBolt(World world, Body playerBody, GameScreen screen, float x, float y, float width, float height) {

        super(screen.getTextureAtlas().findRegion("wizardbolt"));

        destroyFlag = false;
        this.width = width;

        textureRegion = new TextureRegion(getTexture(), 66, 15, 8, 8);
        setBounds(66/3f,15/3f, 8/3f, 8 / 3f);
        setRegion(textureRegion);


        this.playerBody = playerBody;

        shape = new PolygonShape();
        shape.setAsBox(width * 2, height * 2);

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y + height);
        bodyDef.fixedRotation = true;

        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 5;

        this.body = world.createBody(bodyDef);
        this.body.createFixture(fixtureDef).setUserData(this);

        body.setGravityScale(0);
        body.setLinearVelocity(-25, playerBody.getPosition().y);

        shape.dispose();

    }
    public void update(){
        //body.setAngularVelocity(20f);
        rotate(5);
        setOriginCenter();
        setPosition(body.getPosition().x-width*2,body.getPosition().y-width*2);
    }

    @Override
    public void destroy() {
        destroyFlag = true;

    }

    @Override
    public void redraw(SpriteBatch batch) {
        draw(batch);
    }

    @Override
    public BodyDef getBodyDef() {
        return null;
    }

    @Override
    public boolean isFlaggedForKill() {
        return destroyFlag;
    }

    @Override
    public float getX() {
        return body.getPosition().x;
    }
}