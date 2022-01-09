package com.rydzwr.snake;

import java.awt.*;
import java.util.ArrayList;

public class OutOfBoardObject extends BoardObject
{
    @Override
    public String getTag()
    {
        return "out";
    }

    @Override
    public ArrayList<Point> getOccupiedCords()
    {
        throw new IllegalArgumentException("Called Get Occupied Cords Of Out Of Board Object");
    }
}
