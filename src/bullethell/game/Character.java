package bullethell.game;

import bullethell.graphic.Canvas;
import bullethell.math.Vec2f;

import static bullethell.graphic.Canvas.*;
import static org.lwjgl.glfw.GLFW.*;

public abstract class Character extends Entity{
    public Canvas canvas;

    public boolean shooting;

    private long wait; //for invulnerability time;

    public final int damage;

    protected Character(float x, float y, float scale, float velocity, Vec2f direction,
                        int startingFrame, int numberOfFrames, long frameInterval, float hitRadius,
                        long shootInterval, int damage){
        super(x, y, scale, velocity, direction, startingFrame, numberOfFrames, frameInterval, hitRadius,
                3, shootInterval, -1);

        this.damage = damage;

        shooting = false;

        wait = System.currentTimeMillis();
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

        if(x - hitRadius < MIN_X) x = 0f + hitRadius;
        else if(x + hitRadius > MAX_X) x = 9f - hitRadius;
        if(y - hitRadius < MIN_Y) y = 0f + hitRadius;
        else if(y + hitRadius > MAX_Y) y = 16f - hitRadius;
    }

    public void input(long windowID){
        direction.reset();
        if(glfwGetKey(windowID, GLFW_KEY_UP) == GLFW_PRESS) direction.y += 1f;
        if(glfwGetKey(windowID, GLFW_KEY_DOWN) == GLFW_PRESS) direction.y -= 1f;
        if(glfwGetKey(windowID, GLFW_KEY_RIGHT) == GLFW_PRESS) direction.x += 1f;
        if(glfwGetKey(windowID, GLFW_KEY_LEFT) == GLFW_PRESS) direction.x -= 1f;
    }

    public boolean die(){
        long now = System.currentTimeMillis();
        if(now - wait < 1000) return false;
        wait = now;
        return --life == 0;
    }

    @Override
    public void render(float alpha){
        super.render(alpha);

        bullets.render(alpha);
    }

    public void drawLives(){
        for(int i = 0; i < life; i++) renderer.drawTextureSquare(-0.95f + 0.1f * i, 0.95f, 0.05f, startingFrame);
    }
}
