package com.rydzwr.snake;

import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import javafx.scene.paint.Color;

public class Obstacle extends GameObject implements IBoardObject
{
    private Random random = new Random();

    private Point position;
    boolean validPosition = true;
    int obstacleX, obstacleY;
    private Board board = (Board) GameObject.find("Board");
    int mapSize = board.getMapSize();

    public Point getPosition()
    {
        return position;
    }

    @Override
    public ArrayList<Point> getOccupiedCords()
    {
        ArrayList<Point> cords = new ArrayList<Point>();
        cords.add(position);
        return cords;
    }

    @Override
    public String getTag()
    {
        return super.getTag();
    }

    @Override
    public void postInit()
    {
        chooseAndGenerateRandomObstacle();
    }

    @Override
    public void draw(GraphicsContext gc)
    {
        Point screenCoords = board.getScreenCoords(position);
        double squareSizePx = board.getSquareSizePx();

        gc.setFill(Color.GRAY);
        gc.fillRoundRect(screenCoords.getX(), screenCoords.getY(), squareSizePx, squareSizePx, 35, 35);
    }

    void chooseAndGenerateRandomObstacle()
    {
        int randomN = random.nextInt(3);

        if (randomN == 0)
            generatePointObstacle();
        if (randomN == 1)
            generateLShapedObstacle();
        if (randomN == 2)
            generateWallObstacle();
    }

    public void generatePointObstacle()
    {
        do
        {
            obstacleX = random.nextInt(mapSize);
            obstacleY = random.nextInt(mapSize);

            if (board.checkSquareOccupied(new Point(obstacleX, obstacleY)) != null)
                validPosition = false;

        } while (!validPosition);

        position = new Point(obstacleX, obstacleY);
        super.setTag("Obstacle");
        board.registerBoardObject(getUniqueId(), this);
    }

    public void generateLShapedObstacle()
    {
        int leftArmLength = random.nextInt(5) + 1;
        int rightArmLength = random.nextInt(5) + 1;

        do
        {
            obstacleX = random.nextInt(mapSize);
            obstacleY = random.nextInt(mapSize);

            if (board.checkSquareOccupied(new Point(obstacleX, obstacleY)) != null)
                validPosition = false;

        } while (!validPosition);

        for (int i = 0; i < rightArmLength; i++)
        {
            position = new Point(obstacleX, obstacleY + i);
            position = new Point(obstacleX + 1, obstacleY);
        }

        super.setTag("LShapedObstacle");
        board.registerBoardObject(getUniqueId(), this);
    }

    public void generateWallObstacle()
    {
        int randomN = random.nextInt(1);
        int wallLength = random.nextInt(10) + 1;

        do
        {
            obstacleX = random.nextInt(mapSize);
            obstacleY = random.nextInt(mapSize);

            if (board.checkSquareOccupied(new Point(obstacleX, obstacleY)) != null)
                validPosition = false;

        } while (!validPosition);

        if (randomN == 0)
        {
            for (int i = 0; i < wallLength; i++)
            {
                position = new Point(obstacleX, obstacleY + i);
            }
        }
        else
        {
            for (int i = 0; i < wallLength; i++)
            {
                position = new Point(obstacleX + i, obstacleY);
            }
        }

        super.setTag("WallObstacle");
        board.registerBoardObject(getUniqueId(), this);
    }
}
