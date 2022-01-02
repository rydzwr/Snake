package com.rydzwr.snake;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.ToIntBiFunction;

public abstract class GameMode
{
    private HashMap<String, GameObject> gameObjects;

    public GameMode()
    {
        this.gameObjects = new HashMap<String, GameObject>();
    }

    public void init()
    {
        for (GameObject o : gameObjects.values())
            o.init();
    }

    public void update()
    {
        for (GameObject o : gameObjects.values())
            o.update();
    }

    public void draw(GraphicsContext gc)
    {
        for (GameObject o : gameObjects.values())
            o.draw(gc);
    }

    public void onKeyDown(KeyEvent event)
    {
        for (GameObject o : gameObjects.values())
            o.onKeyDown(event);
    }

    public void onKeyUp(KeyEvent event)
    {
        for (GameObject o : gameObjects.values())
            o.onKeyUp(event);
    }

    public void onMouseUp(MouseEvent event)
    {
        for (GameObject o : gameObjects.values())
            o.onMouseUp(event);
    }

    public void onMouseDown(MouseEvent event)
    {
        for (GameObject o : gameObjects.values())
            o.onMouseDown(event);
    }

    public void addGameObject(String tag, GameObject object)
    {
        gameObjects.put(tag, object);
    }

    public void removeGameObject(String tag)
    {
        gameObjects.remove(tag);
    }

    public GameObject findGameObject(String tag)
    {
        return gameObjects.get(tag);
    }
}
