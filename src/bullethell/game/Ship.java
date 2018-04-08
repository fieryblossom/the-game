package bullethell.game;

import bullethell.math.Vec2f;

public class Ship extends Entity{
    public Ship(float x, float y){
        super(0.2f,0.2f,0,2,500);

        this.x = x;
        this.y = y;

        velocity = new Vec2f(0f,0f);
    }

    @Override
    public void update(){
        updateAnimation();
    }
}
