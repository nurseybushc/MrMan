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
 * Created by Boking on 2016-07-27.
 */
public class Wall extends Sprite implements Spawnable{

    protected Body body;
    protected BodyDef bodyDef;

    protected PolygonShape shape;
    protected Fixture fixture;
    protected FixtureDef fixtureDef;
    private TextureRegion textureRegion;
    private float width;
    private float y;
    protected boolean destroyFlag;

    public Wall(World world, com.oscarboking.mrman.sceens.GameScreen screen, float x, float y, float width, float height){

        super(screen.getTextureAtlas().findRegion("wall"));

        destroyFlag = false;
        this.y = y;
        this.width = width;

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

        textureRegion = new TextureRegion(getTexture(), 1, 1, 43, 29);
        setBounds(1/2.5f,1/2.5f, 43 /2.5f, 29 / 2.5f);
        setRegion(textureRegion);

        shape.dispose();

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
    public void update() {
        setPosition(body.getPosition().x-width*2,body.getPosition().y-3*2);
    }

    @Override
    public void destroy() {
        destroyFlag = true;
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
    public Body getBody() {
        return body;
    }
}
