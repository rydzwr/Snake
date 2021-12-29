package com.rydzwr.snake;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.security.Key;
import java.security.cert.PolicyNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

enum Dir
{
    UP, DOWN, RIGHT, LEFT
}

enum GameMode
{
    START, PLAY, GAMEOVER, STOP
}

public class SnakeApplication extends Application
{

    private Random random = new Random();
    private GameMode gameMode = GameMode.START;
    private List<Point> snakeBody = new ArrayList<>();
    private Dir currentDirection;
    private GraphicsContext gc;

    private double screenWidth;
    private double screenHeight;
    private double squareSizePx;
    private double mapOffsetXPx;
    private double mapOffsetYPx;
    private final int mapSize = 100;
    private final int startLength = 3;
    private final String startText = "Press SPACE To Start";
    private final String pressTab = "Press TAB To Pause Game";
    private final String pressShift = "Press SHIFT To SpeedUp Game";

    private Point snakeHead;
    private int foodX;
    private int foodY;
    private int score = 0;

    private Timeline timeline = new Timeline();
    private final int normalDuration = 200;
    private final int fastDuration = 100;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        primaryStage.setTitle("Snake");
        primaryStage.setFullScreen(true);

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        screenWidth = bounds.getWidth();
        screenHeight = bounds.getHeight();

        squareSizePx = Math.min(screenWidth, screenHeight) / (double) mapSize;

        mapOffsetXPx = screenWidth / 2 - (mapSize * squareSizePx) / 2;
        mapOffsetYPx = screenHeight / 2 - (mapSize * squareSizePx) / 2;

        Group root = new Group();
        Canvas canvas = new Canvas(screenWidth, screenHeight);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();

        gc = canvas.getGraphicsContext2D();

        scene.setOnKeyReleased(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent event)
            {
                handleInputUp(event);
            }
        });

        scene.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent event)
            {
                handleInputDown(event);
            }
        });

        playAnimation(normalDuration);
    }

    private void playAnimation(int speed)
    {
        timeline.stop();
        timeline.getKeyFrames().clear();
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(speed), e -> tick()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void initGame()
    {

    }

    private void resetGame()
    {
        score = 0;
        currentDirection = Dir.RIGHT;

        snakeBody = new ArrayList<>();

        for (int i = 0; i < startLength; i++)
            snakeBody.add(new Point(mapSize / 2, mapSize / 2));

        snakeHead = snakeBody.get(0);
        generateFood();
    }

    private void handleInputUp(KeyEvent event)
    {
        KeyCode code = event.getCode();

        if (gameMode == GameMode.PLAY)
        {
            if (code == KeyCode.SHIFT)
                playAnimation(normalDuration);
        }

        else if (gameMode == GameMode.START)
        {
            if (code == KeyCode.SPACE || code == KeyCode.ENTER)
            {
                resetGame();
                gameMode = GameMode.PLAY;
            }
        }

        else if (gameMode == GameMode.GAMEOVER)
        {
            if (code == KeyCode.SPACE || code == KeyCode.ENTER)
                gameMode = GameMode.START;
        }
    }

    public void handleInputDown(KeyEvent event)
    {
        KeyCode code = event.getCode();

        if (gameMode == GameMode.PLAY)
        {
            if (code == KeyCode.RIGHT || code == KeyCode.D)
            {
                if (currentDirection != Dir.LEFT)
                    currentDirection = Dir.RIGHT;
            }
            else if (code == KeyCode.LEFT || code == KeyCode.A)
            {
                if (currentDirection != Dir.RIGHT)
                    currentDirection = Dir.LEFT;
            }
            else if (code == KeyCode.UP || code == KeyCode.W)
            {
                if (currentDirection != Dir.DOWN)
                    currentDirection = Dir.UP;
            }
            else if (code == KeyCode.DOWN || code == KeyCode.S)
            {
                if (currentDirection != Dir.UP)
                    currentDirection = Dir.DOWN;
            }
            if (code == KeyCode.SHIFT)
                playAnimation(fastDuration);
            if (gameMode == GameMode.PLAY)
            {
                if (code == KeyCode.TAB)
                {
                    gameMode = GameMode.STOP;
                    timeline.stop();
                }
            }
            if (gameMode == GameMode.STOP)
            {
                if (code == KeyCode.TAB)
                {
                    gameMode = GameMode.PLAY;
                    timeline.play();
                }
            }
        }
    }

    private void tick()
    {
        update();
        draw();
    }

    private void draw()
    {
        drawBackground();

        if (gameMode == GameMode.START)
            drawStartText();
        else if (gameMode == GameMode.GAMEOVER)
            drawGameOver();
        else if (gameMode == GameMode.PLAY)
        {
            drawFood();
            drawSnake();
            drawScore();
        }
    }

    private void update()
    {
        if (gameMode != GameMode.PLAY)
            return;

        for (int i = snakeBody.size() - 1; i >= 1; i--)
        {
            snakeBody.get(i).x = snakeBody.get(i - 1).x;
            snakeBody.get(i).y = snakeBody.get(i - 1).y;
        }

        switch (currentDirection)
        {
            case RIGHT:
                snakeHead.x++;
                break;
            case LEFT:
                snakeHead.x--;
                break;
            case UP:
                snakeHead.y--;
                break;
            case DOWN:
                snakeHead.y++;
                break;
        }

        checkGameOver();
        eatFood();
    }

    private void generateFood()
    {
        boolean validPosition = true;

        do
        {
            foodX = random.nextInt(mapSize);
            foodY = random.nextInt(mapSize);

            for (Point snake : snakeBody)
            {
                if (snake.getX() == foodX && snake.getY() == foodY)
                    validPosition = false;
            }

        } while (!validPosition);
    }

    private void drawStartText()
    {
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Digital-7", 50));
        gc.fillText(startText, squareSizePx + mapOffsetXPx * 2, screenHeight / 2);
    }

    private void drawBackground()
    {
        gc.clearRect(0,0, screenWidth, screenHeight);
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, screenWidth, screenHeight);
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(10);
        gc.strokeRect(mapOffsetXPx, mapOffsetYPx, mapSize * squareSizePx, mapSize * squareSizePx);
    }

    private void drawFood()
    {
        gc.setFill(Color.RED);
        gc.fillRoundRect(foodX * squareSizePx + mapOffsetXPx, foodY * squareSizePx + mapOffsetYPx, squareSizePx, squareSizePx, 35, 35);
    }

    private void drawSnake()
    {
        gc.setFill(Color.web("09B730"));
        gc.fillRoundRect(snakeHead.getX() * squareSizePx + mapOffsetXPx, snakeHead.getY() * squareSizePx + mapOffsetYPx, squareSizePx, squareSizePx, 35, 35);

        for (int i = 1; i < snakeBody.size(); i++)
            gc.fillRoundRect(snakeBody.get(i).getX() * squareSizePx + mapOffsetXPx, snakeBody.get(i).getY() * squareSizePx + mapOffsetYPx, squareSizePx, squareSizePx, 20, 20);
    }

    private void drawScore()
    {
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Digital-7", 35));
        gc.fillText("Score: " + score, 10, 35);
    }

    private void drawGameOver()
    {
        gc.setFill(Color.RED);
        gc.setFont(new Font("Digital-7", 70));
        gc.fillText("Game Over!", screenWidth / 4.5, screenHeight / 2);
    }

    public void checkGameOver()
    {
        if (snakeHead.x < 0 || snakeHead.y < 0 || snakeHead.x >= mapSize || snakeHead.y >= mapSize)
            gameMode = GameMode.GAMEOVER;

        // Check If Eating Itself
        for (int i = 1; i < snakeBody.size(); i++)
        {
            if (snakeHead.x == snakeBody.get(i).getX() && snakeHead.getY() == snakeBody.get(i).getY())
            {
                gameMode = GameMode.GAMEOVER;
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

    public static void main(String[] args)
    {
        launch(args);
    }
}
