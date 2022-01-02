package com.rydzwr.snake;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

public final class GameModeManager
{
    private static GameModeManager instance;

    private HashMap<String, Class> gameModes;
    private GameMode currentGameMode;

    public GameModeManager()
    {
        this.gameModes = new HashMap<String, Class>();
    }

    public void registerGameMode(String name, Class gameModeClass)
    {
        if (name.isBlank() || gameModeClass == null || !GameMode.class.isAssignableFrom(gameModeClass))
            throw new IllegalArgumentException("New GM tag or class empty or invalid.");

        gameModes.put(name, gameModeClass);
    }

    public GameMode getCurrentGameMode()
    {
        return this.currentGameMode;
    }

    public void setCurrentGameMode(String name)
    {
        Class<GameMode> gameModeClass = gameModes.get(name);

        if (gameModeClass == null)
            throw new IllegalArgumentException("No Such Game Mode");

        GameMode newMode = null;

        try
        {
            newMode = gameModeClass.getDeclaredConstructor().newInstance();
        }
        catch (InvocationTargetException e) {}
        catch (InstantiationException e) {}
        catch (IllegalAccessException e) {}
        catch (NoSuchMethodException e) {}

        if (newMode == null)
            throw new IllegalArgumentException("GM creation failed!");

        currentGameMode = newMode;
        currentGameMode.init();
    }

    public void update()
    {
        if (currentGameMode != null)
            currentGameMode.update();
    }

    public void draw(GraphicsContext gc)
    {
        if (currentGameMode != null)
            currentGameMode.draw(gc);
    }

    public void onKeyDown(KeyEvent event)
    {
        if (currentGameMode != null)
            currentGameMode.onKeyDown(event);
    }

    public void onKeyUp(KeyEvent event)
    {
        if (currentGameMode != null)
            currentGameMode.onKeyUp(event);
    }

    public void onMouseUp(MouseEvent event)
    {
        if (currentGameMode != null)
            currentGameMode.onMouseUp(event);
    }

    public void onMouseDown(MouseEvent event)
    {
        if (currentGameMode != null)
            currentGameMode.onMouseDown(event);
    }

    public static GameModeManager getInstance()
    {
        if (instance == null)
            instance = new GameModeManager();

        return instance;
    }
}
