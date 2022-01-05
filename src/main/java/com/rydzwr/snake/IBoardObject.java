package com.rydzwr.snake;

import java.awt.*;
import java.util.ArrayList;

public interface IBoardObject
{
    ArrayList<Point> getOccupiedCords();
    String getTag();
}
