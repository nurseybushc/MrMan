package com.oscarboking.mrman.listeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.oscarboking.mrman.Player;
import com.oscarboking.mrman.Wall;
import com.oscarboking.mrman.Wizard;

/**
 * Created by Boking on 2016-07-20.
 */
public class CollisionDetector implements ContactListener {

    private boolean fixtureAisPlayer;
    private Player player;
    private Wizard wizard;
    private Wall wall;
    private String itemInfo = "Swipe/hold to drop";
    public com.oscarboking.mrman.sceens.GameScreen screen;
    public World world;

    public CollisionDetector(World world, com.oscarboking.mrman.sceens.GameScreen screen){
        this.world = world;
        this.screen = screen;
    }

    @Override
    public void beginContact(Contact contact) {

        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA == null || fixtureB == null)
            return;
        if (fixtureA.getUserData() == null || fixtureB.getUserData() == null)
            return;


        if (isBoltContact(fixtureA, fixtureB) || isSpikeContact(fixtureA, fixtureB)) {
            if (fixtureAisPlayer) {
                player = (Player) fixtureA.getUserData();
            } else {
                player = (Player) fixtureB.getUserData();
            }
            player.hitRegistered();
        }
        if(isWallContact(fixtureA,fixtureB)){
            if (fixtureAisPlayer) {
                player = (Player) fixtureA.getUserData();
                wall = (Wall) fixtureB.getUserData();
            } else {
                player = (Player) fixtureB.getUserData();
                wall = (Wall) fixtureA.getUserData();
            }
            Gdx.app.log("sd","player Y: " + player.getY() +"player height: " + player.getHeight()+ " wall Y: " + wall.getY());
            if(player.getY()+player.getHeight()>wall.getY() && player.getY() < wall.getY()+wall.getHeight()){
                player.hitRegistered();
            }

        }
        if (isWizardContact(fixtureA,fixtureB)){
            if (fixtureAisPlayer) {
                player = (Player) fixtureA.getUserData();
                wizard = (Wizard) fixtureB.getUserData();
            } else {
                player = (Player) fixtureB.getUserData();
                wizard = (Wizard) fixtureA.getUserData();
            }

            if(player.isUsing()){
                Gdx.app.log("HIT","KILLED WIZARD");
                wizard.destroy();
            }else{
                Gdx.app.log("HIT","DEATH BY WIZARD");
                player.hitRegistered();
            }
        }
        if(isPistolDropContact(fixtureA,fixtureB)){
            if (fixtureAisPlayer) {
                player = (Player) fixtureA.getUserData();
            } else {
                player = (Player) fixtureB.getUserData();
            }
            player.equipItem(new com.oscarboking.mrman.Pistol(world,screen,player));
            player.showInfoText(itemInfo,3);
        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public boolean isBoltContact(Fixture a,Fixture b){
        fixtureAisPlayer = (a.getUserData() instanceof Player);
        return (a.getUserData() instanceof Player && b.getUserData() instanceof com.oscarboking.mrman.WizardBolt);
    }

    public boolean isSpikeContact(Fixture a,Fixture b){
        fixtureAisPlayer = (a.getUserData() instanceof Player);
        return (a.getUserData() instanceof Player && b.getUserData() instanceof com.oscarboking.mrman.Spikes);
    }

    public boolean isWizardContact(Fixture a,Fixture b){
        fixtureAisPlayer = (a.getUserData() instanceof Player);
        return (a.getUserData() instanceof Player && b.getUserData() instanceof Wizard);
    }

    public boolean isWallContact(Fixture a,Fixture b){
        fixtureAisPlayer = (a.getUserData() instanceof Player);
        return (a.getUserData() instanceof Player && b.getUserData() instanceof Wall);
    }

    public boolean isPistolDropContact(Fixture a, Fixture b){
        fixtureAisPlayer = (a.getUserData() instanceof Player);
        return (a.getUserData() instanceof Player && b.getUserData() instanceof com.oscarboking.mrman.PistolDrop);
    }
}