package com.rydzwr.snake;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Timer;
import java.util.TimerTask;

public class SnakeApplication extends Application
{
    private static SnakeApplication instance;

    private Timer updateTimer = new Timer();
    private Timer drawTimer = new Timer();

    private long FPS = 60;
    private long UpdateIntervalMS = 200;
    private boolean isPaused = false;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        instance = this;

        ScreenManager screenManager = ScreenManager.getInstance();
        GameModeManager gmManager = GameModeManager.getInstance();

        screenManager.createScreen(primaryStage);
        Scene scene = primaryStage.getScene();

        scene.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent event)
            {
                gmManager.onKeyDown(event);
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent event)
            {
                gmManager.onKeyUp(event);
            }
        });

        scene.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                gmManager.onMouseUp(event);
            }
        });

        scene.setOnMouseReleased(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                gmManager.onMouseDown(event);
            }
        });

        initGame();

        updateTimer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                update();
            }
        }, 0, UpdateIntervalMS);

        drawTimer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                draw();
            }
        }, 0, 1000 / FPS);
    }

    @Override
    public void stop()
    {
        updateTimer.cancel();
        drawTimer.cancel();
    }

    public boolean getGamePaused()
    {
        return isPaused;
    }

    public void setGamePaused(boolean value)
    {
        isPaused = value;
    }

    public void changeUpdateInterval(long intervalMS)
    {
        updateTimer.cancel();
        updateTimer.purge();

        updateTimer = new Timer();
        updateTimer.schedule(new TimerTask()
        {
            public void run()
            {
                update();
            }
        }, 0, intervalMS);
    }

    private void initGame()
    {
        GameModeManager gmManager = GameModeManager.getInstance();
        gmManager.registerGameMode("Play", PlayGameMode.class);
        gmManager.setCurrentGameMode("Play");
    }

    private void draw()
    {
        ScreenManager screen = ScreenManager.getInstance();
        GraphicsContext gc = screen.getGC();
        gc.clearRect(0,0, screen.getScreenWidthPx(), screen.getScreenHeightPx());
        GameModeManager.getInstance().draw(gc);
    }

    private void update()
    {
        GameObject.collectGarbage();
        GameObject.registerNewObjects();

        if (!isPaused)
            GameModeManager.getInstance().update();
    }

    public static SnakeApplication getInstance()
    {
        return instance;
    }

    public static void main(String[] args) { launch(args); }
}
