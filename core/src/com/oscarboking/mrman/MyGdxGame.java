package com.oscarboking.mrman;

import com.badlogic.gdx.Game;
import com.oscarboking.mrman.sceens.MainMenu;

public class MyGdxGame extends Game {

    private Game game;
    public static final String TITLE = "LEGEND OF MR MAN", VERSION ="0.0.0.0";

    @Override
	public void create () {
        game=this;
		Settings.update();
		if(Settings.isMusicEnabled()) {
			Settings.playMenuMusic();
		}
        setScreen(new MainMenu(game));
	}

	@Override
	public void render () {
	super.render();
    }
	
	@Override
	public void dispose () {
        super.dispose();
	}
}
