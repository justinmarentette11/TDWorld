package com.logicmaster63.tdgalaxy.projectiles.basic;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.utils.IntMap;
import com.logicmaster63.tdgalaxy.constants.Effects;
import com.logicmaster63.tdgalaxy.constants.Types;
import com.logicmaster63.tdgalaxy.entity.Entity;
import com.logicmaster63.tdgalaxy.projectiles.Projectile;
import com.logicmaster63.tdgalaxy.tools.Asset;

import java.util.ArrayList;
import java.util.EnumSet;

public class Bullet extends Projectile {

    public static final int HP = 20;
    public static final float LIFETIME = 10;
    public static final EnumSet<Types> TYPES = EnumSet.of(Types.sharp);
    public static final btCollisionShape SHAPE = new btSphereShape(10);
    public static final float SPEED = 300;

    public Bullet(Vector3 pos, Vector3 velocity, int hp, int health, float speed, EnumSet<Types> types, EnumSet<Effects> effects, ModelInstance model, btCollisionShape shape, boolean isTower, btCollisionWorld world, IntMap<Entity> entities, float lifetime) {
        super(pos, velocity, hp, health, speed, types, effects, model, shape, isTower, world, entities, lifetime);
    }

    public Bullet(Vector3 pos, Vector3 velocity, ModelInstance model, boolean isTower, btCollisionWorld world, IntMap<Entity> entities) {
        super(pos, velocity, HP, HP, SPEED, TYPES, EnumSet.noneOf(Effects.class), model, SHAPE, isTower, world, entities, LIFETIME);
    }

    public static ArrayList<Asset> getAssets() {
        ArrayList<Asset> assets = new ArrayList<com.logicmaster63.tdgalaxy.tools.Asset>();
        assets.add(new Asset("theme/basic/projectile/Bullet.g3db", Model.class));
        return assets;
    }

    @Override
    public void collision(Entity entity) {

    }

    @Override
    public void tick(float delta) {
        super.tick(delta);
    }
}
