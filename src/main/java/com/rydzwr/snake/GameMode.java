package com.rydzwr.snake;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Comparator;

public abstract class GameMode
{
    private ArrayList<GameObject> gameObjects;
    private ArrayList<GameObject> newObjects;
    private boolean depthDirty;
    private int currentGOId = 0;

    public GameMode()
    {
        this.gameObjects = new ArrayList<>();
        this.newObjects = new ArrayList<>();
        this.depthDirty = false;
    }

    public void init()
    {
        for (GameObject o : gameObjects)
            o.init();
    }

    public void update(long deltaTime)
    {
        for (GameObject o : gameObjects)
            o.update(deltaTime);
    }

    public void draw(GraphicsContext gc)
    {
        for (GameObject o : gameObjects)
            o.draw(gc);
    }

    public void onKeyDown(KeyEvent event)
    {
        for (GameObject o : gameObjects)
            o.onKeyDown(event);
    }

    public void onKeyUp(KeyEvent event)
    {
        for (GameObject o : gameObjects)
            o.onKeyUp(event);
    }

    public void onMouseUp(MouseEvent event)
    {
        for (GameObject o : gameObjects)
            o.onMouseUp(event);
    }

    public void onMouseDown(MouseEvent event)
    {
        for (GameObject o : gameObjects)
            o.onMouseDown(event);
    }

    public void setDepthDirty()
    {
        this.depthDirty = true;
    }

    public void sortByDepth()
    {
        if (depthDirty)
        {
            gameObjects.sort(Comparator.comparing(GameObject::getzIndex));
            depthDirty = false;
        }
    }

    public void addGameObject(GameObject object)
    {
        object.setUniqueId(Integer.toString(currentGOId));
        object.init();
        newObjects.add(object);
        depthDirty = true;
        currentGOId++;
    }

    public void removeGameObject(GameObject obj)
    {
        obj.setGarbage(true);
        depthDirty = true;
    }

    public ArrayList<GameObject> findGameObjects(String tag)
    {
        ArrayList<GameObject> results = new ArrayList<>();
        for (GameObject gameObject : gameObjects)
        {
            if ((gameObject.getTag() == tag) && (!gameObject.isGarbage()))
                results.add(gameObject);
        }
        
        return results;
    }

    public GameObject findUniqueObject(String tag)
    {
        ArrayList<GameObject> results = this.findGameObjects(tag);
        return (results.size() == 0) ? null : results.get(0);
    }

    public void collectGarbage()
    {
        for (int i = 0; i < gameObjects.size(); i++)
        {
            if (gameObjects.get(i).isGarbage())
                gameObjects.remove(i);
        }
    }

    public void registerNewObjects()
    {
        for (GameObject newObject : newObjects)
            gameObjects.add(newObject);

        for (GameObject newObject: newObjects)
            newObject.postInit();

        newObjects.clear();
    }
}
