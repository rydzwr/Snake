package com.rydzwr.snake;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Board extends GameObject
{
    private HashMap<String, BoardObject> objects;

    private final int mapSize = 100;
    private final double mapMargin = 0.02;
    private double mapOffsetX = 0;
    private double mapOffsetY = 0;


    public double getSquareSizePx()
    {
        ScreenManager screen = ScreenManager.getInstance();
        double shorterDim = Math.min(screen.getScreenWidthPx(), screen.getScreenHeightPx());
        return (int)((shorterDim - 2 * mapMargin * shorterDim) / (double)mapSize);
    }

    public Point getScreenCoords(Point mapCoords)
    {
        double squareSizePx = getSquareSizePx();
        double x = mapOffsetX + squareSizePx * mapCoords.getX();
        double y = mapOffsetY + squareSizePx * mapCoords.getY();
        Point p = new Point();
        p.setLocation(x, y);
        return p;
    }

    public int getMapSize()
    {
        return mapSize;
    }

    @Override
    public void init()
    {
        objects = new HashMap<String , BoardObject>();
        this.setzIndex(-1);
        this.setTag("Board");
    }

    public void registerBoardObject(String name, BoardObject object)
    {
        objects.put(name ,object);
    }

    public void removeBoardObject(String name)
    {
        objects.remove(name);
    }

    public BoardObject checkSquareOccupied(Point cords)
    {
        if (cords.getY() < 0 || cords.getX() < 0 || cords.getY() > mapSize - 1 || cords.getX() > mapSize - 1)
            return new InvalidBoardObject("out");

        for (BoardObject object : objects.values())
        {
            ArrayList<Point> points = object.getOccupiedCords();

            for (Point point : points)
            {
                if (point.getX() == cords.getX() && point.getY() == cords.getY())
                    return object;
            }
        }

        return new InvalidBoardObject("null");
    }

    @Override
    public void draw(GraphicsContext gc)
    {
        ScreenManager screen = ScreenManager.getInstance();

        double squareSizePx = getSquareSizePx();
        mapOffsetX = screen.getScreenWidthPx() / 2 - (mapSize * squareSizePx) / 2;
        mapOffsetY = screen.getScreenHeightPx() / 2 - (mapSize * squareSizePx) / 2;

        gc.setFill(Color.GRAY);

        for (int i = -1; i < mapSize + 1; i++)
        {
            Point point = getScreenCoords(new Point(i,-1));
            Point point2 = getScreenCoords(new Point(i, mapSize));
            Point point3 = getScreenCoords(new Point(-1,i));
            Point point4 = getScreenCoords(new Point(mapSize , i));

            gc.fillRoundRect(point.getX(), point.getY(), squareSizePx, squareSizePx, 35, 35);
            gc.fillRoundRect(point2.getX(), point2.getY(), squareSizePx, squareSizePx, 35, 35);
            gc.fillRoundRect(point3.getX(), point3.getY(), squareSizePx, squareSizePx, 35, 35);
            gc.fillRoundRect(point4.getX(), point4.getY(), squareSizePx, squareSizePx, 35, 35);
        }
    }
}
