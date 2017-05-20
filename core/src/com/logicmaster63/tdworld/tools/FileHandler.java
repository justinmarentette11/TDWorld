package com.logicmaster63.tdworld.tools;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.google.common.reflect.ClassPath;
import com.logicmaster63.tdworld.TDWorld;
import com.logicmaster63.tdworld.map.Curve;
import com.logicmaster63.tdworld.map.Spawn;
import com.logicmaster63.tdworld.map.Strait;
import com.logicmaster63.tdworld.map.Track;
import com.logicmaster63.tdworld.screens.GameScreen;

import java.io.*;
import java.util.*;

public class FileHandler {

    private static List<Disposable> disposables = new ArrayList<Disposable>();

    public static List<Vector3> loadTrack(BufferedReader data, GameScreen screen) {
        if (data == null)
            return null;
        List<Track> track = new ArrayList<Track>();
        List<Vector3> path = new ArrayList<Vector3>();
        try {
            String line = "";
            int parsed[] = Tools.trippleParseInt(data.readLine());
            screen.setSpawnPos(new Vector3(parsed[0], parsed[1], parsed[2]));
            parsed = Tools.trippleParseInt(data.readLine());
            path.add(new Vector3(parsed[0], parsed[1], parsed[2]));
            line = data.readLine();
            while (!"@".equals(line)) {
                int speeed = Integer.parseInt(data.readLine());
                if ("|".equals(line)) { //Strait
                    line = data.readLine();
                    track.add(new Strait(speeed, new Vector3(Tools.trippleParseInt(line)[0], Tools.trippleParseInt(line)[1], Tools.trippleParseInt(line)[2])));
                } else if (")".equals(line)) { //Curve
                    parsed = Tools.trippleParseInt(data.readLine());
                    Vector3 k0 = new Vector3(parsed[0], parsed[1], parsed[2]);
                    parsed = Tools.trippleParseInt(data.readLine());
                    Vector3 k1 = new Vector3(parsed[0], parsed[1], parsed[2]);
                    parsed = Tools.trippleParseInt(data.readLine());
                    Vector3 k2 = new Vector3(parsed[0], parsed[1], parsed[2]);
                    track.add(new Curve(speeed, k0, k1, k2));
                } else {
                    Gdx.app.log("Error", "Track type '" + line + "'not found");
                    return path;
                }
                line = data.readLine();
            }
            for (Track t : track) {
                path.addAll(t.getPoints(TDWorld.getRes()));
                //System.out.println(t.getPoints(TDWorld.res));
                //System.out.println(path);
            }
            parsed = Tools.trippleParseInt(data.readLine());
            path.add(new Vector3(parsed[0], parsed[1], parsed[2]));
            data.close();
        } catch (IOException e) {
            Gdx.app.log("Error", e.toString());
        }
        return path;
    }

    public static BufferedReader getReader(String path) {
        return new BufferedReader(new InputStreamReader(Gdx.files.internal(path).read()));
    }

    public static HashMap<String, Class<?>> loadClasses(String pakage) {
        HashMap<String, Class<?>> classes = new HashMap<String, Class<?>>();
        if(Gdx.app.getType() == Application.ApplicationType.Android) {
            Gdx.app.error("Classpath", TDWorld.classGetter.getClasses(pakage).toString());
            for(Class clazz: TDWorld.classGetter.getClasses(pakage))
                classes.put(clazz.getSimpleName(), clazz);
            //getClasspathClasses(TDWorld.context, "com.logicmaster63.tdworld");
        } else {
            //Gdx.app.error("Classpath", new ArrayList<Class>(Arrays.asList(getAllClasses("com.logicmaster63.tdworld"))).toString());
            final ClassLoader loader = Thread.currentThread().getContextClassLoader();
            try {
                for (final ClassPath.ClassInfo info : ClassPath.from(loader).getTopLevelClasses()) {
                    if (info.getPackageName().startsWith(pakage))
                        classes.put(info.getSimpleName(), Class.forName(pakage + "." + info.getSimpleName()));
                }


            } catch (Exception e) {
                Gdx.app.error("Loading Classes", e.toString());
            }
        }
        return classes;
    }

    public static void loadPlanet(BufferedReader data, GameScreen screen) {
        //if(data == null)
        int coords[] = {0, 0, 0};
        if (data == null)
            return;
        try {
            screen.setHasPlanetModel(Boolean.parseBoolean(data.readLine()));
            coords = Tools.trippleParseInt(data.readLine());
            screen.setPlanetName(data.readLine());
            data.close();
        } catch (IOException e) {
            Gdx.app.log("Error", e.toString());
        }
        screen.setPlanetSize(new Vector3(coords[0], coords[1], coords[2]));
    }

    public static void loadDependencies(Map<String, Class<?>> classes) {
        int length = classes.size();
        List<Dependency> dependencies = null;
        List<Class<?>> classArray = new ArrayList<Class<?>>(classes.values());
        for (int i = 0; i < length; i++) {
            try {
                dependencies = (ArrayList<Dependency>) classArray.get(i).getMethod("getDependencies").invoke(null);
                if (dependencies != null)
                    for (Dependency dependency : dependencies) {
                    if(!classes.containsKey(dependency.getName())) {
                        classes.put(dependency.getName(), dependency.getClazz());
                        Gdx.app.log("Dependency Loaded", dependency.getName() + ".class");
                    }
                    }
            } catch (NoSuchMethodException e) {

            } catch (Exception e) {
                Gdx.app.log("Error", "FileHandler:150: " + e.toString());
            }
        }
        System.out.println(classes);
    }

    public static void loadFonts(Map<String, BitmapFont> fonts) {
        //FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/sourcesans.ttf"));
        //FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        //parameter.size = 64;
        //fonts.put("pixelade", new BitmapFont(Gdx.files.internal("font/pixelade.fnt"),false));
        //fonts.put("moonhouse", new BitmapFont(Gdx.files.internal("font/moonhouse.fnt"),false));
        //fonts.put("moonhouse", generator.generateFont(parameter));
        //parameter.size = 8;
        Texture texture = new Texture(Gdx.files.internal("font/moonhouse64.png"), true); // true enables mipmaps
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        fonts.put("moonhouse64", new BitmapFont(Gdx.files.internal("font/moonhouse64.fnt"), new TextureRegion(texture), false));
        texture = new Texture(Gdx.files.internal("font/moonhouse32.png"), true); // true enables mipmaps
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        fonts.put("moonhouse32", new BitmapFont(Gdx.files.internal("font/moonhouse32.fnt"), new TextureRegion(texture), false));
        texture = new Texture(Gdx.files.internal("font/moonhouse16.png"), true); // true enables mipmaps
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        fonts.put("moonhouse16", new BitmapFont(Gdx.files.internal("font/moonhouse16.fnt"), new TextureRegion(texture), false));

        texture = new Texture(Gdx.files.internal("font/sourcesans64.png"), true); // true enables mipmaps
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        fonts.put("ui64", new BitmapFont(Gdx.files.internal("font/sourcesans64.fnt"), new TextureRegion(texture), false));
        texture = new Texture(Gdx.files.internal("font/sourcesans32.png"), true); // true enables mipmaps
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        fonts.put("ui32", new BitmapFont(Gdx.files.internal("font/sourcesans32.fnt"), new TextureRegion(texture), false));
        texture = new Texture(Gdx.files.internal("font/sourcesans16.png"), true); // true enables mipmaps
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        fonts.put("ui16", new BitmapFont(Gdx.files.internal("font/sourcesans16.fnt"), new TextureRegion(texture), false));
        texture = new Texture(Gdx.files.internal("font/sourcesans8.png"), true); // true enables mipmaps
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        fonts.put("ui8", new BitmapFont(Gdx.files.internal("font/sourcesans8.fnt"), new TextureRegion(texture), false));
        //fonts.put("ui", generator.generateFont(parameter));
        //generator = new FreeTypeFontGenerator(Gdx.files.internal("font/sourcesans.ttf"));
        //parameter.size = 32;
        //fonts.put("ui16", generator.generateFont(parameter));
        //generator.dispose();
        //fonts.get("ui").getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

    }

    public static List<Spawn> loadSpawns(BufferedReader data) {
        if (data == null)
            return null;
        List<Spawn> spawns = new ArrayList<Spawn>();
        try {
            int spawnNum = Integer.parseInt(data.readLine());
            for (int i = 0; i < spawnNum; i++) {
                int repeatNum = Integer.parseInt(data.readLine());
                String name = data.readLine();
                int delay = Integer.parseInt(data.readLine());
                for (int j = 0; j < repeatNum; j++)
                    spawns.add(new Spawn(name, delay));
                data.close();
            }
        } catch (IOException e) {
            Gdx.app.log("Error", e.toString());
        }
        return spawns;
    }

    public static void addDisposables(Disposable... d) {
        disposables.addAll(Arrays.asList(d));
    }

    public static void dispose() {
        if (disposables != null)
            for (Disposable d : disposables)
                d.dispose();
    }
}
