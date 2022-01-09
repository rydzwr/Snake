package com.rydzwr.snake;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class LevelManager extends GameObject
{
    private int currentLevel;
    private int playerScore;
    private int currentLevelScore;

    public int getCurrentLevel()
    {
        return currentLevel;
    }

    public int getPlayerScore()
    {
        return playerScore;
    }

    public int getCurrentLevelScore()
    {
        return currentLevelScore;
    }

    @Override
    public void draw(GraphicsContext gc)
    {

    }

    @Override
    public void init()
    {
        currentLevel = 1;
        playerScore = 0;
        currentLevelScore = 0;
        nextLevel();
    }

    public void nextLevel()
    {
        ArrayList<GameObject> oldObstacles = GameObject.findAll("Obstacle");
        ArrayList<GameObject> oldFood = GameObject.findAll("Food");

        for (GameObject oldObstacle : oldObstacles)
        {
            GameObject.destroy(oldObstacle);
        }

        for (GameObject gameObject : oldFood)
        {
            GameObject.destroy(gameObject);
        }

        int obstacleCount = currentLevel;

    }
}
