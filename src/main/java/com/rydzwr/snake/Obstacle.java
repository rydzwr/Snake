package com.rydzwr.snake;

import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import javafx.scene.paint.Color;

public class Obstacle extends GameObject implements IBoardObject
{
    private Point position;
    private Board board;

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
        board = (Board) GameObject.find("Board");
        Random random = new Random();

        int mapSize = board.getMapSize();

        boolean validPosition = true;
        int obstacleX, obstacleY;

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

    @Override
    public void draw(GraphicsContext gc)
    {
        Point screenCoords = board.getScreenCoords(position);
        double squareSizePx = board.getSquareSizePx();

        gc.setFill(Color.GRAY);
        gc.fillRoundRect(screenCoords.getX(), screenCoords.getY(), squareSizePx, squareSizePx, 35, 35);
    }
}