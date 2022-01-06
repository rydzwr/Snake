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
    private ArrayList<String> garbage;
    private boolean depthDirty;

    public GameMode()
    {
        this.gameObjects = new ArrayList<>();
        this.newObjects = new ArrayList<>();
        this.garbage = new ArrayList<>();
        this.depthDirty = false;
    }

    public void init()
    {
        for (GameObject o : gameObjects)
            o.init();
    }

    public void update()
    {
        for (GameObject o : gameObjects)
            o.update();
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
        if (findGameObject(object.getUniqueId()) != null)
            throw new IllegalArgumentException("GameObject uniqueId duplicated: " + object.getUniqueId());

        newObjects.add(object);
        depthDirty = true;
    }

    public void removeGameObject(String uniqueId)
    {
        garbage.add(uniqueId);
        depthDirty = true;
    }

    public GameObject findGameObject(String uniqueId)
    {
        GameObject obj = null;
        for (GameObject gameObject : gameObjects)
        {
            if (gameObject.getUniqueId() == uniqueId)
                obj = gameObject;
        }

        if (obj == null)
            return null;

        for (String garbageId : garbage)
        {
            if (garbageId == obj.getUniqueId())
                return null;
        }
        
        return obj;
    }

    public void collectGarbage()
    {
        for (int i = 0; i < gameObjects.size(); i++)
        {
            if (garbage.contains(gameObjects.get(i).getUniqueId()))
                gameObjects.remove(i);
        }

        garbage.clear();
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
