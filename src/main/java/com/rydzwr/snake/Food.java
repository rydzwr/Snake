package com.rydzwr.snake;

import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import javafx.scene.paint.Color;

public class Food extends BoardObject
{
    private Point position;
    private Board board;
    private int weight;
    private boolean superFood;
    private boolean runningFood;

    private final double maxRadius = 0.3;
    private final double baseRadius = 1;
    private final double pulseSpeed = 200;
    private double time;
    private double currentRadius;

    public int getWeight()
    {
        return weight;
    }

    public void setType(boolean superFood, boolean runningFood, int weight)
    {
        this.weight = weight;
        this.superFood = superFood;
        this.runningFood = runningFood;
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
    public void init()
    {
        time = 0;
        currentRadius = baseRadius;
    }

    @Override
    public void postInit()
    {
        board = (Board) GameObject.findUnique("Board");
        Random random = new Random();

        int mapSize = board.getMapSize();

        boolean validPosition = true;
        int foodX, foodY;

        do
        {
            foodX = random.nextInt(mapSize);
            foodY = random.nextInt(mapSize);

            if (board.checkSquareOccupied(new Point(foodX, foodY)) != null)
                validPosition = false;

        } while (!validPosition);

        position = new Point(foodX, foodY);
        super.setTag("Food");
        board.registerBoardObject(getUniqueId(), this);
    }

    @Override
    public void update(long deltaTime)
    {
        if (superFood)
        {
            currentRadius = baseRadius + Math.sin(time / pulseSpeed) * maxRadius;
            time += deltaTime;
        }
    }

    @Override
    public void draw(GraphicsContext gc)
    {
        Point screenCoords = board.getScreenCoords(position);
        double squareSizePx = board.getSquareSizePx();
        double radius = squareSizePx * currentRadius;

        if (superFood)
            gc.setFill(Color.GOLD);
        else
            gc.setFill(Color.RED);

        gc.fillOval(screenCoords.getX() - (radius / 2) + (squareSizePx / 2), screenCoords.getY() - (radius / 2) + (squareSizePx / 2), radius, radius);
    }
}
