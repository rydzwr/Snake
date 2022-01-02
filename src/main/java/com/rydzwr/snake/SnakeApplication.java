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
    private Timer updateTimer = new Timer();
    private Timer drawTimer = new Timer();

    @Override
    public void start(Stage primaryStage) throws Exception
    {
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

        TimerTask updateTask = new TimerTask()
        {
            public void run()
            {
                update();
            }
        };

        TimerTask drawTask = new TimerTask()
        {
            public void run()
            {
                draw();
            }
        };

        initGame();

        updateTimer.schedule(updateTask, 0, 200);
        drawTimer.schedule(drawTask, 0, 1000 / 60);
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
        GameModeManager.getInstance().update();
        GameObject.collectGarbage();
        GameObject.registerNewObjects();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
