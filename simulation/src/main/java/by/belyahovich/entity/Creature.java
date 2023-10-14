package by.belyahovich.entity;

import java.security.SecureRandom;

public abstract class Creature extends Entity {

    private static final long MAX_SPEED = 3;
    private static final long MAX_HP = 10;

    private final long speed;
    private final long hp;

    public Creature() {
        SecureRandom secureRandom = new SecureRandom();
        speed = secureRandom.nextLong(MAX_SPEED - 1) + 1;//1 - MAX_SPEED
        hp = secureRandom.nextLong(MAX_HP);//1 - MAX_HP
    }

    abstract void move();

    public long getSpeed() {
        return speed;
    }

    public long getHp() {
        return hp;
    }
}
