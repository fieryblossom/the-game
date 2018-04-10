package bullethell.core;

import bullethell.game.Character;
import bullethell.game.characters.CharacterWithNoName;
import bullethell.game.enemies.EnemyWithNoName;
import bullethell.game.explosions.ExplosionWithNoName;
import bullethell.graphic.Renderer;
import bullethell.graphic.Window;
import bullethell.util.Bullets;
import bullethell.util.Explosions;
import bullethell.util.Solids;
import org.lwjgl.glfw.GLFWErrorCallback;

import java.util.concurrent.ThreadLocalRandom;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;

public class Game{
    private GLFWErrorCallback errorCallback;

    private Window window;

    private Renderer renderer;

    private Character player;
    private Solids enemies;
    private Bullets bullets;
    //private Solids powerUps;
    //private Solids destroyables;
    private Explosions explosions;

    public static void main(String[] args){
        new Game().start();
    }

    private void start(){
        init();
        loop();
        dispose();
    }

    private void init(){
        errorCallback = GLFWErrorCallback.createPrint();
        glfwSetErrorCallback(errorCallback);

        /* Initialize GLFW */
        if(!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW!");
        }

        /* Create GLFW window */
        window = new Window(500, 500, "Bullet Hell");

        renderer = new Renderer();
        renderer.init();

        player = new CharacterWithNoName(-0.5f, -0.5f);
        enemies = new Solids();
        bullets = new Bullets();
        explosions = new Explosions();

        enemies.add(new EnemyWithNoName(0.5f, 0.5f));
    }

    private void loop(){
        while(true){
            long start = System.currentTimeMillis();

            if(window.isClosing()) break;

            player.input(window.id);

            player.update();
            enemies.update();
            bullets.update();
            explosions.update();

            if(enemies.collided(player))
                explosions.add(new ExplosionWithNoName(
                        (float)ThreadLocalRandom.current().nextDouble(-1,+1),
                        (float)ThreadLocalRandom.current().nextDouble(-1,+1)
                ));

            player.render(renderer);
            enemies.render(renderer);
            bullets.render(renderer);
            explosions.render(renderer);

            renderer.draw();

            window.update();

            long now = System.currentTimeMillis();
            System.out.println("Time per frame: " + (now - start) + "ms");
            wait(now, (long) (1000f / 30f) - (now - start));
        }
    }

    private void dispose(){
        renderer.dispose();
        window.dispose();
    }

    private void wait(long start, long interval){
        while(System.currentTimeMillis() - start < interval)
            try{
                Thread.sleep(1);
            } catch(InterruptedException ignored){
            }
    }
}
