package com.rydzwr.snake;

import java.awt.*;
import java.util.ArrayList;

public abstract class BoardObject extends GameObject
{
    public abstract ArrayList<Point> getOccupiedCords();
}
