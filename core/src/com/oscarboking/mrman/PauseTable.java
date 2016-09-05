package com.oscarboking.mrman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * Created by boking on 2016-09-05.
 */
public class PauseTable extends Table {

    public Label pauseLabel;
    public Label scoreLabel;
    public Label resumeLabel;

    private BitmapFont font;
    private Label.LabelStyle textStyle;


    public PauseTable(int score){

        font = new BitmapFont(Gdx.files.internal("fonts/gamefont.fnt"));
        textStyle = new Label.LabelStyle(font, Color.WHITE);

        pauseLabel = new Label("Paused",textStyle);
        scoreLabel = new Label(Integer.toString(score), textStyle);
        resumeLabel = new Label("Tap to resume!",textStyle);
        pauseLabel.setFontScale(2);
        scoreLabel.setFontScale(2);
        resumeLabel.setFontScale(2);

        this.add(pauseLabel).row();
        this.add(scoreLabel).padTop(100).row();
        this.add(resumeLabel).padTop(200).row();

    }
}
