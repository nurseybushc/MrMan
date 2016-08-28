package com.oscarboking.mrman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;

/**
 * Created by Boking on 2016-07-25.
 */
public class Wizard extends Sprite implements Spawnable{

    private WizardBolt bolt;
    private TextureRegion textureRegion;
    private World world;

    protected Body body;
    protected BodyDef bodyDef;

    protected PolygonShape shape;
    protected FixtureDef fixtureDef;

    boolean destroyFlag;
    protected com.oscarboking.mrman.sceens.GameScreen screen;
    protected Fixture fixture;


    public Wizard(final World world, final com.oscarboking.mrman.sceens.GameScreen screen, final Body playerBody, final float x, final float y, final float width, final float height){

        super(screen.getTextureAtlas().findRegion("wizard"));

        this.screen = screen;
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

        fixtureDef.filter.categoryBits = com.oscarboking.mrman.sceens.GameScreen.WIZARD_BIT;
        fixtureDef.filter.maskBits = com.oscarboking.mrman.sceens.GameScreen.PLAYER_BIT | com.oscarboking.mrman.sceens.GameScreen.DEFAULT;

        this.body = world.createBody(bodyDef);
        fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);

        Filter newFilter = new Filter();
        newFilter.categoryBits = com.oscarboking.mrman.sceens.GameScreen.WIZARD_BIT;
        fixture.setFilterData(newFilter);

        textureRegion = new TextureRegion(getTexture(), 46, 1, 18, 22);
        setBounds(46 / 3f, 1 / 3f, 18 / 3f, 22 / 3f);
        setRegion(textureRegion);

        //Shoot bolt
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                Gdx.app.log("Wizard","SHOOTING");
                bolt = new WizardBolt(world,playerBody,screen,x-width,y+height/2,0.5f,0.5f);
                screen.getObjectInWorld().add(bolt);
            }
        }, 0.5f);
    }

    @Override
    public void update() {
        setPosition(body.getPosition().x-1*2,body.getPosition().y-2*2);
        if(bolt != null) {
            bolt.update();
        }
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

        Filter newFilter = new Filter();
        newFilter.categoryBits = com.oscarboking.mrman.sceens.GameScreen.DESTROYED_BIT;
        fixture.setFilterData(newFilter);

        world.destroyBody(body);
        destroyFlag = true;
        shape.dispose();
    }

    @Override
    public BodyDef getBodyDef() {
        return bodyDef;
    }

    @Override
    public boolean isFlaggedForKill() {
        return destroyFlag;
    }
}
