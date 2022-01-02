package com.rydzwr.snake;

import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.util.Random;
import javafx.scene.paint.Color;

public class Food extends GameObject
{
    private Point position;
    private Board board;

    public Point getPosition()
    {
        return position;
    }

    @Override
    public void init()
    {
        Snake snake = (Snake) GameObject.find("Snake");
        board = (Board) GameObject.find("Board");
        Random random = new Random();

        int mapSize = board.getMapSize();

        boolean validPosition = true;
        int foodX, foodY;

        do
        {
            foodX = random.nextInt(mapSize);
            foodY = random.nextInt(mapSize);

            if (snake.positionInsideSnake(new Point(foodX, foodY)))
                validPosition = false;

        } while (!validPosition);

        position = new Point(foodX, foodY);
    }

    @Override
    public void update()
    {

    }

    @Override
    public void draw(GraphicsContext gc)
    {
        double squareSizePx = board.getSquareSizePx();
        double mapOffsetXPx = board.getMapOffsetX();
        double mapOffsetYPx = board.getMapOffsetY();

        gc.setFill(Color.RED);
        gc.fillRoundRect(position.getX() * squareSizePx + mapOffsetXPx,
                position.getY() * squareSizePx + mapOffsetYPx, squareSizePx, squareSizePx, 35, 35);
    }
}
