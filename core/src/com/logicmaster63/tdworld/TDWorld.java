package com.logicmaster63.tdworld;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.logicmaster63.tdworld.screens.GameScreen;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TDWorld extends Game {

	private static final Map<String, Integer> TYPES;
	private static int res = 10;
    private static float sensitivity = 0.5f;
	public static final boolean isDebugging = true;
	private static final List<String> themes = new ArrayList<String>();
	private static Map<String, BitmapFont> fonts;
	private static GameScreen gameScreen;
	static {
		TYPES = new HashMap<String, Integer>();
		TYPES.put("ice", 1);
		TYPES.put("fire", 2);
		TYPES.put("sharp", 4);
        themes.add("basic");
	}

	@Override
	public void create() {
		Bullet.init();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/moonhouse.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        fonts = new HashMap<String, BitmapFont>();
        //fonts.put("pixelade", new BitmapFont(Gdx.files.internal("font/pixelade.fnt"),false));
        //fonts.put("moonhouse", new BitmapFont(Gdx.files.internal("font/moonhouse.fnt"),false));
        fonts.put("moonhouse", generator.generateFont(parameter));
        //generator = new FreeTypeFontGenerator(Gdx.files.internal("font/pixelade.ttf"));
        //fonts.put("pixelade", generator.generateFont(parameter));
        generator.dispose();

        gameScreen = new GameScreen(this, 0, themes.get(0));
		setScreen(gameScreen);
	}

	@Override
	public void dispose() {
	    List<BitmapFont> fontArray = new ArrayList<BitmapFont>(fonts.values());
	    for(BitmapFont font: fontArray)
		    font.dispose();
	}

    public static int getRes() {
        return res;
    }

    public static void setRes(int res) {
        TDWorld.res = res;
    }

    public static float getSensitivity() {
        return sensitivity;
    }

    public static void setSensitivity(float sensitivity) {
        TDWorld.sensitivity = sensitivity;
    }

    public static Map<String, BitmapFont> getFonts() {
        return fonts;
    }

    public static Map<String, Integer> getTYPES() {
        return TYPES;
    }
}
