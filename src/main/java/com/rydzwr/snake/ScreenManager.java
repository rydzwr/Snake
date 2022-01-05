package com.rydzwr.snake;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Screen;
import javafx.stage.Stage;

public final class ScreenManager
{
    private static ScreenManager instance;

    private GraphicsContext gc;
    private int screenWidthPx;
    private int screenHeightPx;

    public GraphicsContext getGC()
    {
        return this.gc;
    }

    public int getScreenWidthPx()
    {
        return this.screenWidthPx;
    }

    public int getScreenHeightPx()
    {
        return this.screenHeightPx;
    }

    public void createScreen(Stage stage, boolean fullscreen)
    {
        stage.setTitle("Snake");
        stage.setFullScreen(fullscreen);

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getBounds();

        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());

        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> onWindowResize(stage);
        ChangeListener<Boolean> stageMaxListener = (observable, oldValue, newValue) ->
        {
            onWindowResize(stage);
            SnakeApplication.getInstance().setGamePaused(true);
        };

        stage.widthProperty().addListener(stageSizeListener);
        stage.heightProperty().addListener(stageSizeListener);
        stage.maximizedProperty().addListener(stageMaxListener);
        stage.iconifiedProperty().addListener(stageMaxListener);

        screenWidthPx = (int)bounds.getWidth();
        screenHeightPx = (int)bounds.getHeight();

        Group root = new Group();
        Canvas canvas = new Canvas(screenWidthPx, screenHeightPx);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

        onWindowResize(stage);

        this.gc = canvas.getGraphicsContext2D();
    }

    public void onWindowResize(Stage stage)
    {
        Scene scene = stage.getScene();
        screenWidthPx = (int)scene.getWidth();
        screenHeightPx = (int)scene.getHeight();
    }

    public static ScreenManager getInstance()
    {
        if (instance == null)
            instance = new ScreenManager();

        return instance;
    }
}
