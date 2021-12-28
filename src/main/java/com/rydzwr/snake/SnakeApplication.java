package com.rydzwr.snake;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnakeApplication extends Application
{
    private static double width = 1200;
    private static double height = 1000;
    private static final double rows = (width / 20);
    private static final double columns = rows;
    private static final double squareSize = width / rows;
    private static final int right = 0;
    private static final int left = 1;
    private static final int up = 2;
    private static final int down = 3;

    private GraphicsContext gc;
    private List<Point> snakeBody = new ArrayList<>();
    private Point snakeHead;
    private int foodX;
    private int foodY;
    private boolean gameOver;
    private int currentDirection;
    private int score = 0;

    Random random = new Random();

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        primaryStage.setTitle("Snake");
        Group root = new Group();
        Canvas canvas = new Canvas(width, height);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        gc = canvas.getGraphicsContext2D();

        scene.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent event)
            {
                KeyCode code = event.getCode();
                if (code == KeyCode.RIGHT || code == KeyCode.D)
                {
                    if (currentDirection != left)
                    {
                        currentDirection = right;
                    }
                }
                else if (code == KeyCode.LEFT || code == KeyCode.A)
                {
                    if (currentDirection != right)
                    {
                        currentDirection = left;
                    }
                }
                else if (code == KeyCode.UP || code == KeyCode.W)
                {
                    if (currentDirection != down)
                    {
                        currentDirection = up;
                    }
                }
                else if (code == KeyCode.DOWN || code == KeyCode.S)
                {
                    if (currentDirection != up)
                    {
                        currentDirection = down;
                    }
                }
            }
        });

        for (int i = 0; i < 3; i++)
        {
            snakeBody.add(new Point(5, (int) rows / 2));
        }

        snakeHead = snakeBody.get(0);
        generateFood();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(130), e -> run(gc)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void run(GraphicsContext gc)
    {
        if (gameOver)
        {
            gc.setFill(Color.RED);
            gc.setFont(new Font("Digital-7", 70));
            gc.fillText("Game Over!", width / 3.5, height / 2);
            return;
        }
        drawBackground(gc);
        drawFood(gc);
        drawSnake(gc);
        drawScore();

        for (int i = snakeBody.size() - 1; i >= 1; i--)
        {
            snakeBody.get(i).x = snakeBody.get(i - 1).x;
            snakeBody.get(i).y = snakeBody.get(i - 1).y;
        }

        switch (currentDirection)
        {
            case right:
                moveRight();
                break;
            case left:
                moveLeft();
                break;
            case up:
                moveUp();
                break;
            case down:
                moveDown();
                break;
        }

        gameOver();
        eatFood();
    }

    private void drawBackground(GraphicsContext gc)
    {
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                if ((i + j) % 2 == 0)
                {
                    gc.setFill(Color.web("000000"));
                }
                else
                {
                    gc.setFill(Color.web("000000"));
                }
                gc.fillRect(i * squareSize, j * squareSize, squareSize - 0.5, squareSize - 0.5);
            }
        }
    }

    private void generateFood()
    {
        start:
        while (true)
        {
            foodX = (int) (Math.random() * rows);
            foodY = (int) (Math.random() * columns);

            for (Point snake : snakeBody)
            {
                if (snake.getX() == foodX && snake.getY() == foodY)
                {
                    continue start;
                }
            }

            break;
        }
    }

    private void drawFood(GraphicsContext gc)
    {
        gc.setFill(Color.RED);
        gc.fillRoundRect(foodX * squareSize, foodY * squareSize, squareSize, squareSize, 35, 35);
    }

    private void drawSnake(GraphicsContext gc)
    {
        gc.setFill(Color.web("09B730"));
        gc.fillRoundRect(snakeHead.getX() * squareSize, snakeHead.getY() * squareSize, squareSize - 1, squareSize - 1, 35, 35);

        for (int i = 1; i < snakeBody.size(); i++)
        {
            gc.fillRoundRect(snakeBody.get(i).getX() * squareSize, snakeBody.get(i).getY() * squareSize, squareSize - 1, squareSize - 1, 20, 20);
        }
    }
    private void moveRight()
    {
        snakeHead.x++;
    }

    private void moveLeft()
    {
        snakeHead.x--;
    }

    private void moveUp()
    {
        snakeHead.y--;
    }

    private void moveDown()
    {
        snakeHead.y++;
    }

    public void gameOver()
    {
        if (snakeHead.x < 0 || snakeHead.y < 0 || snakeHead.x * squareSize >= width || snakeHead.y * squareSize >= height)
        {
            gameOver = true;
        }

        // Destroy Itself
        for (int i = 1; i < snakeBody.size(); i++)
        {
            if (snakeHead.x == snakeBody.get(i).getX() && snakeHead.getY() == snakeBody.get(i).getY())
            {
                gameOver = true;
                break;
            }
        }
    }

    private void eatFood()
    {
        if (snakeHead.getX() == foodX && snakeHead.getY() == foodY)
        {
            snakeBody.add(new Point(-1, -1));
            generateFood();
            score += 1;
        }
    }

    private void drawScore()
    {
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Digital-7", 35));
        gc.fillText("Score: " + score, 10, 35);
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
