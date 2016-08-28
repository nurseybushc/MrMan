package com.oscarboking.mrman;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;
import com.oscarboking.mrman.sceens.GameScreen;

/**
 * Created by Boking on 2016-08-24.
 */
public class Pistol extends Sprite implements Usable {

    private World world;

    public com.oscarboking.mrman.Bullet bullet;

    public GameScreen screen;

    public com.oscarboking.mrman.Player player;


    public Pistol(final World world, final GameScreen screen, com.oscarboking.mrman.Player player){
        this.world = world;
        this.screen = screen;
        this.player = player;
    }

    @Override
    public void use(int screenY,int screenX) {
        bullet = new com.oscarboking.mrman.Bullet(world,screen,player.getX(),player.getY(),screenY);
        screen.getObjectInWorld().add(bullet);
    }

    @Override
    public float getCooldown() {
        return 0;
    }
}
