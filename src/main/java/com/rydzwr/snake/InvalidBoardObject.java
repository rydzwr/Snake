package com.rydzwr.snake;

import java.awt.*;
import java.util.ArrayList;

public class InvalidBoardObject extends BoardObject
{
    public InvalidBoardObject(String tag)
    {
        setTag(tag);
    }

    @Override
    public ArrayList<Point> getOccupiedCords()
    {
        throw new IllegalArgumentException("Called Get Occupied Cords Of Out Of Board Object");
    }
}
