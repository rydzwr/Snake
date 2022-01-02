package com.rydzwr.snake;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class Board extends GameObject
{
    private final int mapSize = 100;
    private final double mapMargin = 0.02;
    private double mapOffsetX = 0;
    private double mapOffsetY = 0;
    private int score = 0;

    public int getSquareSizePx()
    {
        ScreenManager screen = ScreenManager.getInstance();
        double shorterDim = Math.min(screen.getScreenWidthPx(), screen.getScreenHeightPx());
        return (int)((shorterDim - 2 * mapMargin * shorterDim) / (double)mapSize);
    }

    public double getMapOffsetX()
    {
        return mapOffsetX;
    }

    public double getMapOffsetY()
    {
        return mapOffsetY;
    }

    public int getMapSize()
    {
        return mapSize;
    }

    public int getScore()
    {
        return score;
    }

    public void incrementScore(int amount)
    {
        this.score = score + amount;
    }

    @Override
    public void init()
    {

    }

    @Override
    public void update()
    {

    }

    @Override
    public void draw(GraphicsContext gc)
    {
        ScreenManager screen = ScreenManager.getInstance();

        double squareSizePx = getSquareSizePx();
        mapOffsetX = screen.getScreenWidthPx() / 2 - (mapSize * squareSizePx) / 2;
        mapOffsetY = screen.getScreenHeightPx() / 2 - (mapSize * squareSizePx) / 2;

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, screen.getScreenWidthPx(), screen.getScreenHeightPx());

        gc.setStroke(Color.GRAY);
        gc.setLineWidth(1);
        gc.strokeRect(mapOffsetX, mapOffsetY, mapSize * squareSizePx, mapSize * squareSizePx);

        drawScore(gc);
    }

    private void drawScore(GraphicsContext gc)
    {
        ScreenManager screen = ScreenManager.getInstance();

        double screenWidth = screen.getScreenWidthPx();
        double screenHeight = screen.getScreenHeightPx();

        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Digital-7", 35));
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.TOP);
        gc.fillText("Score: " + score, screenWidth * 0.01, screenHeight * 0.01);
        gc.setFont(new Font("Digital-7", 20));
        gc.fillText("Press SHIFT To Speed Up", screenWidth * 0.01, screenHeight * 0.01 + 70);
        gc.fillText("Press SPACE to pause", screenWidth * 0.01, screenHeight * 0.01 + 100);
    }
}
