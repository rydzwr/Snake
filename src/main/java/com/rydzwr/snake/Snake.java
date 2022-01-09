package com.rydzwr.snake;

import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.util.ArrayList;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

public class Snake extends BoardObject
{
    private final int startLength = 3;
    private final long normalInterval = 200;
    private final long fastInterval = 100;

    private ArrayList<Point> snakeBody;
    private Point snakeHead;
    private Dir currentDirection;
    private Board board;

    private long timeSinceMovement = 0;
    private long movementInterval;

    @Override
    public String getTag()
    {
        return super.getTag();
    }

    @Override
    public ArrayList<Point> getOccupiedCords()
    {
        return new ArrayList<Point>(snakeBody.subList(1, snakeBody.size() -1));
    }

    @Override
    public void init()
    {
        currentDirection = Dir.RIGHT;
        snakeBody = new ArrayList<>();
        movementInterval = normalInterval;
    }

    @Override
    public void postInit()
    {
        board = (Board)GameObject.findUnique("Board");
        int mapSize = board.getMapSize();

        for (int i = 0; i < startLength; i++)
            snakeBody.add(new Point(mapSize / 2 - i, mapSize / 2));

        snakeHead = snakeBody.get(0);

        this.setTag("Snake");
        this.setzIndex(1);
        board.registerBoardObject(this.getUniqueId(), this);
    }

    @Override
    public void onKeyDown(KeyEvent event)
    {
        KeyCode code = event.getCode();

        if (code == KeyCode.SHIFT)
            movementInterval = fastInterval;

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
        KeyCode code = event.getCode();

        if (code == KeyCode.SHIFT)
            movementInterval = normalInterval;
    }

    @Override
    public void update(long deltaTime)
    {
        if (timeSinceMovement >= movementInterval)
        {
            timeSinceMovement = 0;
            moveSnake();
            eatFood();
        }

        timeSinceMovement += deltaTime;
    }

    @Override
    public void draw(GraphicsContext gc)
    {
        double squareSizePx = board.getSquareSizePx();
        Point screenCoords = board.getScreenCoords(snakeHead);

        gc.setFill(Color.web("09B730"));
        gc.fillRoundRect(screenCoords.getX(), screenCoords.getY(), squareSizePx, squareSizePx, 35, 35);

        for (int i = 1; i < snakeBody.size(); i++)
        {
            screenCoords = board.getScreenCoords(snakeBody.get(i));
            gc.fillRoundRect(screenCoords.getX(), screenCoords.getY(), squareSizePx, squareSizePx, 20, 20);
        }
    }

    public boolean isDead()
    {
        String tag = board.checkSquareOccupied(snakeHead).getTag();
        return (tag == "Snake" || tag == "out" || tag == "Obstacle");
    }

    private void eatFood()
    {
        String tag = board.checkSquareOccupied(snakeHead).getTag();

        if (tag == "Food")
        {
            snakeBody.add(new Point(-1, -1));
           // GameObject.destroy("Food" + board.getScore());
           // GameObject.create("Food" + (board.getScore() + 1), Food.class);
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
