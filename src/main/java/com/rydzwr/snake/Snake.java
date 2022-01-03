package com.rydzwr.snake;

import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.util.ArrayList;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

public class Snake extends GameObject
{
    private ArrayList<Point> snakeBody;
    private Point snakeHead;
    private final int startLength = 3;
    private Dir currentDirection;
    private Board board;

    @Override
    public void init()
    {
        board = (Board) GameObject.find("Board");

        int mapSize = ((Board)GameObject.find("Board")).getMapSize();

        currentDirection = Dir.RIGHT;
        snakeBody = new ArrayList<>();

        for (int i = 0; i < startLength; i++)
            snakeBody.add(new Point(mapSize / 2, mapSize / 2));

        snakeHead = snakeBody.get(0);
    }

    @Override
    public void onKeyDown(KeyEvent event)
    {
        KeyCode code = event.getCode();

        if (code == KeyCode.RIGHT || code == KeyCode.D)
        {
            if (currentDirection != Dir.LEFT)
                currentDirection = Dir.RIGHT;
        }
        else if (code == KeyCode.LEFT || code == KeyCode.A)
        {
            if (currentDirection != Dir.RIGHT)
                currentDirection = Dir.LEFT;
        }
        else if (code == KeyCode.UP || code == KeyCode.W)
        {
            if (currentDirection != Dir.DOWN)
                currentDirection = Dir.UP;
        }
        else if (code == KeyCode.DOWN || code == KeyCode.S)
        {
            if (currentDirection != Dir.UP)
                currentDirection = Dir.DOWN;
        }
    }

    @Override
    public void onKeyUp(KeyEvent event)
    {

    }

    @Override
    public void update()
    {
        moveSnake();
        eatFood();
    }

    @Override
    public void draw(GraphicsContext gc)
    {
        double squareSizePx = board.getSquareSizePx();
        double mapOffsetXPx = board.getMapOffsetX();
        double mapOffsetYPx = board.getMapOffsetY();

        gc.setFill(Color.web("09B730"));
        gc.fillRoundRect(snakeHead.getX() * squareSizePx + mapOffsetXPx,
                snakeHead.getY() * squareSizePx + mapOffsetYPx, squareSizePx, squareSizePx, 35, 35);

        for (int i = 1; i < snakeBody.size(); i++)
            gc.fillRoundRect(snakeBody.get(i).getX() * squareSizePx + mapOffsetXPx,
                    snakeBody.get(i).getY() * squareSizePx + mapOffsetYPx, squareSizePx, squareSizePx, 20, 20);
    }

    public boolean checkGameOver()
    {
        int mapSize = board.getMapSize();

        if (snakeHead.x < 0 || snakeHead.y < 0 || snakeHead.x >= mapSize || snakeHead.y >= mapSize)
            return true;

        // Check If Eating Itself
        for (int i = 1; i < snakeBody.size(); i++)
        {
            if (snakeHead.x == snakeBody.get(i).getX() && snakeHead.getY() == snakeBody.get(i).getY())
            {
                return true;
            }
        }

        return false;
    }

    private void eatFood()
    {
        Food food = (Food)GameObject.find("Food" + board.getScore());

        if (food == null)
            return;

        if (snakeHead.getX() == food.getPosition().getX() && snakeHead.getY() == food.getPosition().getY())
        {
            snakeBody.add(new Point(-1, -1));
            GameObject.destroy("Food" + board.getScore());
            GameObject.create("Food" + (board.getScore() + 1), Food.class);
            board.incrementScore(1);
        }
    }

    private void moveSnake()
    {
        for (int i = snakeBody.size() - 1; i >= 1; i--)
        {
            snakeBody.get(i).x = snakeBody.get(i - 1).x;
            snakeBody.get(i).y = snakeBody.get(i - 1).y;
        }

        switch (currentDirection)
        {
            case RIGHT:
                snakeHead.x++;
                break;
            case LEFT:
                snakeHead.x--;
                break;
            case UP:
                snakeHead.y--;
                break;
            case DOWN:
                snakeHead.y++;
                break;
        }
    }

    public boolean positionInsideSnake(Point position)
    {
        boolean isOutside = true;

        for (Point snake : snakeBody)
        {
            if (snake.getX() == position.getX() && snake.getY() == position.getY())
                isOutside = false;
        }

        return !isOutside;
    }
}
