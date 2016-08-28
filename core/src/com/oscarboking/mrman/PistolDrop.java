package com.oscarboking.mrman;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.oscarboking.mrman.sceens.GameScreen;

/**
 * Created by Boking on 2016-08-25.
 */
public class PistolDrop extends Sprite implements Spawnable {

    private TextureRegion textureRegion;
    private World world;

    protected Body body;
    protected BodyDef bodyDef;

    protected PolygonShape shape;
    protected FixtureDef fixtureDef;

    boolean destroyFlag;

    public PistolDrop(final World world, final GameScreen screen, final float x, final float y){

        super(screen.getTextureAtlas().findRegion("wizard"));

        destroyFlag = false;

        this.world = world;
        shape = new PolygonShape();
        shape.setAsBox(1 * 2, 1 * 2);

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y + 1);
        bodyDef.fixedRotation = true;

        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 5;

        this.body = world.createBody(bodyDef);
        this.body.createFixture(fixtureDef).setUserData(this);

        textureRegion = new TextureRegion(getTexture(), 46, 1, 18, 22);
        setBounds(46 / 3f, 1 / 3f, 18 / 3f, 22 / 3f);
        setRegion(textureRegion);
        shape.dispose();
    }

    @Override
    public void update() {
        rotate(5);
        setOriginCenter();
        setPosition(body.getPosition().x-1*2,body.getPosition().y-1*2);
    }

    @Override
    public void destroy() {
        destroyFlag = true;
    }

    @Override
    public void redraw(SpriteBatch batch) {

    }

    @Override
    public BodyDef getBodyDef() {
        return null;
    }

    @Override
    public boolean isFlaggedForKill() {
        return destroyFlag;
    }
}
