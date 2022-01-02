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
    private static ArrayList<Pair<String, GameObject>> newObjects = new ArrayList<>();
    private static ArrayList<String> garbage = new ArrayList<String>();

    public abstract void init();

    public abstract void update();

    public abstract void draw(GraphicsContext gc);

    public void onKeyDown(KeyEvent event) {}

    public void onKeyUp(KeyEvent event) {}

    public void onMouseUp(MouseEvent event) {}

    public void onMouseDown(MouseEvent event) {}

    public static GameObject create(String tag, Class objClass)
    {
        if (tag.isBlank() || objClass == null || !GameObject.class.isAssignableFrom(objClass))
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

        newObjects.add(new Pair<>(tag, newObject));

        return newObject;
    }

    public static void destroy(String tag)
    {
        garbage.add(tag);
    }

    public static GameObject find(String tag)
    {
        return GameModeManager.getInstance().getCurrentGameMode().findGameObject(tag);
    }

    public static void collectGarbage()
    {
        for (String tag : garbage)
            GameModeManager.getInstance().getCurrentGameMode().removeGameObject(tag);

        garbage.clear();
    }

    public static void registerNewObjects()
    {
        for (Pair<String, GameObject> pair : newObjects)
            GameModeManager.getInstance().getCurrentGameMode().addGameObject(pair.getKey(), pair.getValue());

        for (Pair<String, GameObject> pair : newObjects)
            pair.getValue().init();

        newObjects.clear();
    }
}
