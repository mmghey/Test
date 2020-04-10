package com.almasb.battleship;

import java.util.Random;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import com.almasb.battleship.Board.Cell;

public class BattleshipMain extends Application {

    private boolean running = false;
    private Board enemyBoard, playerBoard;

    private int shipsToPlace = 11;

    private boolean enemyTurn = false;

    private Random random = new Random();


    private Parent createContent() {
        BorderPane root = new BorderPane();
        root.setPrefSize(800, 800);

        root.setRight(new Text("RIGHT SIDEBAR - CONTROLS"));

        enemyBoard = new Board(true, event -> {
            if (!running)
                return;

            Cell cell = (Cell) event.getSource();
            if (cell.wasShot)
                return;

            enemyTurn = !cell.shoot();

            if (enemyBoard.ships == 0) {
                System.out.println("YOU WIN");
                System.exit(0);
            }

            if (enemyTurn)
                enemyMove();
        });

        playerBoard = new Board(false, event -> {
            if (running)
                return;

            Cell cell = (Cell) event.getSource();
            if (playerBoard.placeShip(new Ship(passType(shipsToPlace), event.getButton() == MouseButton.PRIMARY), cell.x, cell.y)) {
                if (--shipsToPlace == 0) {
                    startGame();
                }
            }
        });

        VBox vbox = new VBox(50, enemyBoard, playerBoard);
        vbox.setAlignment(Pos.CENTER);

        root.setCenter(vbox);

        return root;
    }

    private void enemyMove() {
        while (enemyTurn) {
            int x = random.nextInt(Board.CELLS_IN_X_AXSIS);
            int y = random.nextInt(Board.CELLS_IN_Y_AXSIS);

            Cell cell = playerBoard.getCell(x, y);
            if (cell.wasShot)
                continue;

            enemyTurn = cell.shoot();

            if (playerBoard.ships == 0) {
                System.out.println("YOU LOSE");
                System.exit(0);
            }
        }
    }

    private void startGame() {
        // place enemy ships
        int type = 11;

        while (type > 0) {
            int x = random.nextInt(Board.CELLS_IN_X_AXSIS);
            int y = random.nextInt(Board.CELLS_IN_Y_AXSIS);

            if (enemyBoard.placeShip(new Ship(passType(type), Math.random() < 0.5), x, y)) {
                type--;
            }
        }

        running = true;
    }

    public int passType(int ships) {
        int currentType = 0;
        if(ships > 6) {
            currentType = 1;
        } else if (ships > 3) {
            currentType = 2;
        } else if (ships > 1) {
            currentType = 3;
        } else if (ships == 1) {
            currentType = 4;
        }
        return currentType;
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("Battleship");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
