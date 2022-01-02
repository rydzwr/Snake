package com.rydzwr.snake;

public class PlayGameMode extends GameMode
{
    @Override
    public void init()
    {
        GameObject.create("Board", Board.class);
        GameObject.create("Snake", Snake.class);
        GameObject.create("Food0", Food.class);
        super.init();
    }
}
