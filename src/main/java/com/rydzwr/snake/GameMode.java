package com.rydzwr.snake;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.ToIntBiFunction;

public abstract class GameMode
{
    private HashMap<String, GameObject> gameObjects;
    private HashMap<String, GameObject> newObjects;
    private ArrayList<String> garbage;

    public GameMode()
    {
        this.gameObjects = new HashMap<String, GameObject>();
        this.newObjects = new HashMap<>();
        this.garbage = new ArrayList<>();
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

    public void addGameObject(String uniqueId, GameObject object)
    {
        newObjects.put(uniqueId, object);
    }

    public void removeGameObject(String uniqueId)
    {
        garbage.add(uniqueId);
    }

    public GameObject findGameObject(String uniqueId)
    {
        return gameObjects.get(uniqueId);
    }

    public void collectGarbage()
    {
        for (String uniqueId : garbage)
            gameObjects.remove(uniqueId);

        garbage.clear();
    }

    public void registerNewObjects()
    {
        for (Map.Entry<String, GameObject> entry : newObjects.entrySet()) {
            gameObjects.put(entry.getKey(), entry.getValue());
        }

        for (GameObject newObject: newObjects.values())
            newObject.postInit();

        newObjects.clear();
    }
}
