package by.belyahovich.entity;

import java.security.SecureRandom;
import java.util.Random;

public abstract class Creature extends Entity {

    private static final long MAX_SPEED = 3;
    private static final long MAX_HP = 10;

    private final long speed;
    private final long hp;

    abstract void move();

    public Creature() {
        SecureRandom secureRandom = new SecureRandom();
        speed = secureRandom.nextLong(MAX_SPEED) + 1;
        hp = secureRandom.nextLong(MAX_HP) + 1;
    }

    public long getSpeed() {
        return speed;
    }

    public long getHp() {
        return hp;
    }
}
