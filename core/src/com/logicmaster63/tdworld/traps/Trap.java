package com.logicmaster63.tdworld.traps;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.logicmaster63.tdworld.object.Object;

import java.util.Map;

public abstract class Trap extends Object{

    public Trap(Vector3 pos, int hp, int health, int types, ModelInstance instance, btCollisionShape shape, int effects, btCollisionWorld world, Map<Integer, Object> objects, boolean isTemplate) {
        super(pos, hp, health, types, effects, instance, shape, world, objects, isTemplate);
    }

    public abstract void collision(Object object);
}
