package com.rydzwr.snake;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class PlayGameMode extends GameMode
{
    private final long normalInterval = 200;
    private final long fastInterval = 100;

    private boolean gameStarted = false;
    private boolean gameOver = false;
    private boolean firstUpdate = true;

    private ScreenManager screen = ScreenManager.getInstance();
    private Snake player;

    @Override
    public void init()
    {
        GameObject.create("Board", Board.class);
        player = (Snake)GameObject.create("Snake", Snake.class);
        GameObject.create("Food0", Food.class);

        super.init();
    }

    @Override
    public void onKeyUp(KeyEvent event)
    {
        KeyCode code = event.getCode();

        if (code == KeyCode.SHIFT)
            SnakeApplication.getInstance().changeUpdateInterval(normalInterval);
        if (code == KeyCode.SPACE)
        {
            if (!gameStarted)
            {
                SnakeApplication.getInstance().setGamePaused(false);
                gameStarted = true;
            }
            else if (!gameOver)
                SnakeApplication.getInstance().setGamePaused(!SnakeApplication.getInstance().getGamePaused());
            else
                GameModeManager.getInstance().setCurrentGameMode("Play");
        }
        super.onKeyUp(event);
    }

    @Override
    public void onKeyDown(KeyEvent event)
    {
        KeyCode code = event.getCode();

        if (code == KeyCode.SHIFT)
            SnakeApplication.getInstance().changeUpdateInterval(fastInterval);

        super.onKeyDown(event);
    }

    @Override
    public void update()
    {
        super.update();

        if (firstUpdate)
        {
            SnakeApplication.getInstance().setGamePaused(true);
            firstUpdate = false;
        }

        if (player.isDead())
        {
            SnakeApplication.getInstance().setGamePaused(true);
            gameOver = true;
        }
    }

    @Override
    public void draw(GraphicsContext gc)
    {
        super.draw(gc);

        if (!gameStarted)
            drawStartText(gc);
        else if ((!gameOver) && (SnakeApplication.getInstance().getGamePaused()))
            drawGamePaused(gc);
        else if (gameOver)
            drawGameOver(gc);

    }

    private void drawStartText(GraphicsContext gc)
    {
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Digital-7", 50));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText("Press SPACE To Start", screen.getScreenWidthPx() / 2, screen.getScreenHeightPx() / 2);
    }

    private void drawGameOver(GraphicsContext gc)
    {
        gc.setFill(Color.RED);
        gc.setFont(new Font("Digital-7", 70));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText("Game Over!", screen.getScreenWidthPx() / 2, screen.getScreenHeightPx() / 2);
    }

    private void drawGamePaused(GraphicsContext gc)
    {
        gc.setFill(Color.RED);
        gc.setFont(new Font("Digital-7", 70));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText("Press SPACE To Start", screen.getScreenWidthPx() / 2, screen.getScreenHeightPx() / 2);
    }
}
