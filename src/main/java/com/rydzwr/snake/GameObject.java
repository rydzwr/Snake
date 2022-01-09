package com.rydzwr.snake;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;

public abstract class GameObject
{
    private String uniqueId;
    private String tag;
    private boolean idSet = false;
    private boolean isGarbage = false;
    private int zIndex = 0;

    public boolean isGarbage()
    {
        return isGarbage;
    }

    public void setGarbage(boolean garbage)
    {
        isGarbage = garbage;
    }

    public int getzIndex()
    {
        return zIndex;
    }

    public void setzIndex(int zIndex)
    {
        this.zIndex = zIndex;
        GameModeManager.getInstance().getCurrentGameMode().setDepthDirty();
    }

    public String getUniqueId()
    {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId)
    {
        if (!idSet)
        {
            this.uniqueId = uniqueId;
            idSet = true;
        }
    }

    public String getTag()
    {
        return tag;
    }

    public void setTag(String tag)
    {
        this.tag = tag;
    }

    public void init() { };

    public void postInit() { };

    public void update(long deltaTime) { };

    public void draw(GraphicsContext gc) { };

    public void onKeyDown(KeyEvent event) {}

    public void onKeyUp(KeyEvent event) {}

    public void onMouseUp(MouseEvent event) {}

    public void onMouseDown(MouseEvent event) {}

    public static GameObject create(Class objClass)
    {
        if (objClass == null || !GameObject.class.isAssignableFrom(objClass))
            throw new IllegalArgumentException("New GO tag or class empty or invalid.");

        GameObject newObject = null;

        try
        {
            newObject = (GameObject)objClass.getDeclaredConstructor().newInstance();
        }
        catch (InvocationTargetException e) {}
        catch (InstantiationException e) {}
        catch (IllegalAccessException e) {}
        catch (NoSuchMethodException e) {}

        if (newObject == null)
            throw new IllegalArgumentException("GO creation failed!");

        GameModeManager.getInstance().getCurrentGameMode().addGameObject(newObject);

        return newObject;
    }

    public static void destroy(GameObject obj)
    {
        GameModeManager.getInstance().getCurrentGameMode().removeGameObject(obj);
    }

    public static ArrayList<GameObject> findAll(String tag)
    {
        return GameModeManager.getInstance().getCurrentGameMode().findGameObjects(tag);
    }

    public static GameObject findUnique(String tag)
    {
        return GameModeManager.getInstance().getCurrentGameMode().findUniqueObject(tag);
    }
}
