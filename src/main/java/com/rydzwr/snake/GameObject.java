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

    public void update() { };

    public void draw(GraphicsContext gc) { };

    public void onKeyDown(KeyEvent event) {}

    public void onKeyUp(KeyEvent event) {}

    public void onMouseUp(MouseEvent event) {}

    public void onMouseDown(MouseEvent event) {}

    public static GameObject create(String uniqueId, Class objClass)
    {
        if (uniqueId.isBlank() || objClass == null || !GameObject.class.isAssignableFrom(objClass))
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

        newObject.setUniqueId(uniqueId);
        newObject.init();
        GameModeManager.getInstance().getCurrentGameMode().addGameObject(uniqueId, newObject);

        return newObject;
    }

    public static void destroy(String uniqueId)
    {
        GameModeManager.getInstance().getCurrentGameMode().removeGameObject(uniqueId);
    }

    public static GameObject find(String uniqueId)
    {
        return GameModeManager.getInstance().getCurrentGameMode().findGameObject(uniqueId);
    }
}
