package com.rydzwr.snake;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Timer;
import java.util.TimerTask;

public class SnakeApplication extends Application
{
    private static SnakeApplication instance;

    private AnimationTimer timer;
    private long previousNow = 0;
    private boolean isPaused = false;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        instance = this;

        ScreenManager screenManager = ScreenManager.getInstance();
        GameModeManager gmManager = GameModeManager.getInstance();

        screenManager.createScreen(primaryStage, false);
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

        timer = new AnimationTimer()
        {
            @Override
            public void handle(long now)
            {
                long deltaTime = (previousNow == 0) ? 0 : (now - previousNow) / 1000000;
                update(deltaTime);
                draw();
                previousNow = now;
            }
        };

        timer.start();
    }

    @Override
    public void stop()
    {
        timer.stop();
    }

    public boolean getGamePaused()
    {
        return isPaused;
    }

    public void setGamePaused(boolean value)
    {
        isPaused = value;
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
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, screen.getScreenWidthPx(), screen.getScreenHeightPx());

        GameModeManager.getInstance().draw(gc);
    }

    private void update(long deltaTime)
    {
        GameModeManager.getInstance().processGameObjects();
        if (!isPaused)
            GameModeManager.getInstance().update(deltaTime);
    }

    public static SnakeApplication getInstance()
    {
        return instance;
    }

    public static void main(String[] args) { launch(args); }
}
