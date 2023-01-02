package io.github.lama06.zombies.monster;

import io.github.lama06.zombies.ZombiesGame;
import io.github.lama06.zombies.util.ComponentContainer;
import io.github.lama06.zombies.util.EntityPosition;
import org.bukkit.entity.Entity;

public abstract class Monster<T extends Monster<T, E>, E extends Entity> {
    protected final ZombiesGame game;
    protected final ComponentContainer components = new ComponentContainer();
    protected final MonsterType<T, E> type;
    protected E entity;

    public Monster(
            final ZombiesGame game, final MonsterType<T, E> type, final EntityPosition position
    ) {
        this.game = game;
        this.type = type;

        entity = game.getWorld().spawn(position.toLocation(game.getWorld()), type.entityType());

        initComponents();
        onSpawned();
    }

    public abstract void initComponents();

    public void onSpawned() {
    }

    public void remove() {
        entity.remove();
    }

    public ComponentContainer getComponents() {
        return components;
    }

    public ZombiesGame getGame() {
        return game;
    }

    public E getEntity() {
        return entity;
    }
}