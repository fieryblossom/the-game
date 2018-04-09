package bullethell.game;

import bullethell.game.explosions.ExplosionWithNoName;
import bullethell.math.Vec2f;

public abstract class Enemy extends Solid{
    protected Enemy(float x, float y, float scale, float velocity, Vec2f direction,
          int startingFrame, int numberOfFrames, long frameInterval, float hitRadius){
        super(x,y,scale,velocity,direction,startingFrame,numberOfFrames,frameInterval,hitRadius);
    }

    @Override
    public void update(){
        move();
        updateAnimation();
    }

    protected abstract void move();

    public Explosion explode(){
        return new ExplosionWithNoName(x,y);
    }
}