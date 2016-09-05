package com.oscarboking.mrman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

/**
 * Created by boking on 2016-09-05.
 */
public class GameOverTable extends Table {

    public Label gameOverLabel;
    public Label scoreLabel;
    public Label highScoreLabel;
    public Label restartLabel;

    private Skin skin;
    private BitmapFont font;
    private Label.LabelStyle textStyle;

    private Preferences prefs;

    public GameOverTable(int score){

        prefs = Gdx.app.getPreferences("My Preferences");
        font = new BitmapFont(Gdx.files.internal("fonts/gamefont.fnt"));
        skin = new Skin();
        textStyle = new Label.LabelStyle(font, Color.WHITE);

        gameOverLabel = new Label("Game Over!",textStyle);
        scoreLabel = new Label(Integer.toString(score), textStyle);
        highScoreLabel = new Label("Highscore: " + Integer.toString(prefs.getInteger("highscore"),0),textStyle);
        restartLabel = new Label("Tap to restart!",textStyle);
        gameOverLabel.setFontScale(3);
        scoreLabel.setFontScale(3);
        highScoreLabel.setFontScale(2);
        restartLabel.setFontScale(3);

        if(prefs.getInteger("highscore")<score){
            prefs.putInteger("highscore", score);
            prefs.flush();
            highScoreLabel.setText(Integer.toString(score));
        }

        this.add(gameOverLabel).row();
        this.add(scoreLabel).padTop(100).row();
        this.add(highScoreLabel).padTop(100).row();
        this.add(restartLabel).padTop(150).row();

    }
}
