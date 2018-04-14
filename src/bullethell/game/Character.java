package bullethell.game;

import bullethell.graphic.Renderer;
import bullethell.math.Vec2f;
import bullethell.util.Timer;
import bullethell.util.lists.Bullets;

import static org.lwjgl.glfw.GLFW.*;

public abstract class Character extends Entity{
    private boolean shooting;

    private int lives;
    private long wait;

    public final int damage;

    protected Character(float x, float y, float scale, float velocity, Vec2f direction,
                        int startingFrame, int numberOfFrames, long frameInterval, float hitRadius,
                        long shootInterval, int damage){
        super(x, y, scale, velocity, direction, startingFrame, numberOfFrames, frameInterval, hitRadius,
                shootInterval, -1);

        this.damage = damage;

        shooting = false;

        lives = 3;
        wait = 0;
    }

    @Override
    public void update(float delta){
        super.update(delta);

        bullets.update(delta);

        if(shooting){
            long now = System.currentTimeMillis();
            if(last == -1){
                shoot();
                last = now;
            } else if(now - last >= shootInterval){
                shoot();
                last = now;
            }
        }
    }

    protected abstract void shoot();

    @Override
    protected void move(float delta){
        direction.normalize();
        x += delta * velocity * direction.x;
        y += delta * velocity * direction.y;

        if(x - hitRadius < -1f) x = -1f + hitRadius;
        else if(x + hitRadius > 1f) x = 1f - hitRadius;
        if(y - hitRadius < -1f) y = -1f + hitRadius;
        else if(y + hitRadius > 1f) y = 1f - hitRadius;
    }

    public void input(long windowID){
        direction.reset();
        if(glfwGetKey(windowID, GLFW_KEY_UP) == GLFW_PRESS) direction.y += 1f;
        if(glfwGetKey(windowID, GLFW_KEY_DOWN) == GLFW_PRESS) direction.y -= 1f;
        if(glfwGetKey(windowID, GLFW_KEY_RIGHT) == GLFW_PRESS) direction.x += 1f;
        if(glfwGetKey(windowID, GLFW_KEY_LEFT) == GLFW_PRESS) direction.x -= 1f;
    }

    public void setShooting(boolean shooting){
        this.shooting = shooting;
    }

    public boolean collided(Solid solid){
        return checkCollision(x - solid.x, y - solid.y, hitRadius + solid.hitRadius);
    }

    public boolean die(){
        long now = System.currentTimeMillis();
        if(now - wait < 1000) return false;
        wait = now;
        return --lives == 0;
    }

    @Override
    public void render(Renderer renderer, float alpha){
        super.render(renderer, alpha);

        bullets.render(renderer, alpha);

        for(int i = 0; i < lives; i++) renderer.drawTexture(-0.95f + 0.1f * i,0.95f, 0.05f, startingFrame);
    }
}
