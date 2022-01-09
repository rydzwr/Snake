package com.rydzwr.snake;

import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import javafx.scene.paint.Color;

public class Obstacle extends BoardObject
{
    private Random random;
    private ArrayList<Point> positions;
    boolean validPosition;
    int obstacleX, obstacleY;
    private Board board;
    int mapSize;

    @Override
    public ArrayList<Point> getOccupiedCords()
    {
        return positions;
    }

    @Override
    public String getTag()
    {
        return super.getTag();
    }

    @Override
    public void init()
    {
        positions = new ArrayList<>();
        random = new Random();
        validPosition = true;
    }

    @Override
    public void postInit()
    {
        board = (Board) GameObject.findUnique("Board");
        mapSize = board.getMapSize();

        findFirstPoint();
        generateObstacle();

        super.setTag("Obstacle");
        board.registerBoardObject(getUniqueId(), this);
    }

    @Override
    public void draw(GraphicsContext gc)
    {
        double squareSizePx = board.getSquareSizePx();
        gc.setFill(Color.GRAY);

        for (Point position : positions)
        {
            Point screenCoords = board.getScreenCoords(position);
            gc.fillRoundRect(screenCoords.getX(), screenCoords.getY(), squareSizePx, squareSizePx, 35, 35);
        }
    }

    void generateObstacle()
    {
        int randomN = random.nextInt(3);

        if (randomN == 0)
            generatePointObstacle();
        else if (randomN == 1)
            generateLShapedObstacle();
        else if (randomN == 2)
            generateWallObstacle();
    }

    public void generatePointObstacle()
    {
        positions.add(new Point(obstacleX, obstacleY));
    }

    public void generateLShapedObstacle()
    {
        int leftArmLength = random.nextInt(5) + 1;
        int rightArmLength = random.nextInt(5) + 1;

        for (int i = 0; i < rightArmLength; i++)
        {
            Point point = new Point(obstacleX, obstacleY + i);
            if (board.checkSquareOccupied(point) == null)
                positions.add(point);
        }

        for (int i = 0; i < leftArmLength; i++)
        {
            Point point = new Point(obstacleX + i, obstacleY);
            if (board.checkSquareOccupied(point) == null)
                positions.add(point);
        }
    }

    public void generateWallObstacle()
    {
        int randomN = random.nextInt(1);
        int wallLength = random.nextInt(10) + 1;

        if (randomN == 0)
        {
            for (int i = 0; i < wallLength; i++)
            {
                Point point = new Point(obstacleX, obstacleY + i);
                if (board.checkSquareOccupied(point) == null)
                    positions.add(point);
            }
        }
        else
        {
            for (int i = 0; i < wallLength; i++)
            {
                Point point = new Point(obstacleX + i, obstacleY);
                if (board.checkSquareOccupied(point) == null)
                    positions.add(point);
            }
        }
    }

    public void findFirstPoint()
    {
        do
        {
            obstacleX = random.nextInt(mapSize);
            obstacleY = random.nextInt(mapSize);

            if (board.checkSquareOccupied(new Point(obstacleX, obstacleY)) != null)
                validPosition = false;

        } while (!validPosition);
    }
}
