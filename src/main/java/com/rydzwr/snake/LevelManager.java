package com.rydzwr.snake;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.Random;

public class LevelManager extends GameObject
{
    private final double obstacleMultiplier = 10;
    private final int foodPerLevel = 10;
    private final int multiFoodCount = 3;
    private final int livesPerLevel = 3;
    private final double superFoodChance = 0.5;

    private Snake player;
    private int currentLevel;
    private int playerScore;
    private int currentMultipleFood;
    private int levelScore;
    private int foodOnBoard;
    private int livesLeft;
    private int foodIteration;

    public int getCurrentLevel()
    {
        return currentLevel;
    }

    public int getPlayerScore()
    {
        return playerScore;
    }

    @Override
    public void init()
    {
        setTag("LevelManager");
        currentLevel = 1;
        playerScore = 0;
        livesLeft = livesPerLevel;
    }

    @Override
    public void postInit()
    {
        nextLevel();
    }

    @Override
    public void update(long deltaTime)
    {
        if (player.isDead())
        {
            resetCurrentLevel();
            livesLeft--;

            if (livesLeft == 0)
                GameModeManager.getInstance().setCurrentGameMode("GameOver");
        }
    }

    @Override
    public void draw(GraphicsContext gc)
    {
        drawScore(gc);
    }

    private void nextLevel()
    {
        cleanUpObstacles();

        int obstacleCount = (int) (Math.sqrt(currentLevel) * obstacleMultiplier);

        for (int i = 0; i < obstacleCount; i++)
            GameObject.create(Obstacle.class);

        playerScore += levelScore;
        resetCurrentLevel();
    }

    private void resetCurrentLevel()
    {
        Random random = new Random();
        currentMultipleFood = random.nextInt(foodPerLevel);
        levelScore = 0;
        foodIteration = 0;

        cleanUpFood();
        generateFood();
        resetSnake();

        SnakeApplication.getInstance().setGamePaused(true);
    }

    private void resetSnake()
    {
        GameObject snake = GameObject.findUnique("Snake");
        if (snake != null)
            GameObject.destroy(snake);
        player = (Snake)GameObject.create(Snake.class);
    }

    private void cleanUpObstacles()
    {
        ArrayList<GameObject> oldObstacles = GameObject.findAll("Obstacle");

        for (GameObject oldObstacle : oldObstacles)
            GameObject.destroy(oldObstacle);
    }

    private void cleanUpFood()
    {
        ArrayList<GameObject> oldFood = GameObject.findAll("Food");

        for (GameObject gameObject : oldFood)
            GameObject.destroy(gameObject);

        foodOnBoard = 0;
    }

    private void generateFood()
    {
        boolean multipleFood = (foodIteration == currentMultipleFood);
        int foodCount = multipleFood ? multiFoodCount : 1;
        foodOnBoard = foodCount;
        foodIteration++;

        for (int i = 0; i < foodCount; i++)
        {
            Food food = (Food) GameObject.create(Food.class);
            food.setType(false, false, 1);
        }

        Random random = new Random();
        if (random.nextDouble() < superFoodChance)
        {
            Food food = (Food) GameObject.create(Food.class);
            food.setType(true, false, random.nextInt(1) + 2);
        }
    }

    public void foodEaten(BoardObject foodObj)
    {
        Food food = (Food)foodObj;
        levelScore += food.getWeight();
        GameObject.destroy(food);

        if (food.isSuperFood())
            return;

        foodOnBoard--;

        if (levelScore >= foodPerLevel)
            nextLevel();
        else if (foodOnBoard == 0)
            generateFood();
    }

    private void drawScore(GraphicsContext gc)
    {
        ScreenManager screen = ScreenManager.getInstance();

        double screenWidth = screen.getScreenWidthPx();
        double screenHeight = screen.getScreenHeightPx();

        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Digital-7", 35));
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.TOP);
        gc.fillText("Score: " + (playerScore + levelScore), screenWidth * 0.01, screenHeight * 0.01);
        gc.fillText("Lives Left: " + livesLeft, screenWidth * 0.01, screenHeight * 0.01 + 50);
        gc.setFont(new Font("Digital-7", 20));
        gc.fillText("Press SHIFT To Speed Up", screenWidth * 0.01, screenHeight * 0.01 + 120);
        gc.fillText("Press SPACE to pause", screenWidth * 0.01, screenHeight * 0.01 + 150);
    }
}
