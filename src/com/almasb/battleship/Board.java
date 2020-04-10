package com.almasb.battleship;

import java.lang.reflect.Array;
import java.util.*;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Board extends Parent {
    public static final int CELLS_IN_X_AXSIS = 20;
    public static final int CELLS_IN_Y_AXSIS = 20;
    public static final int BOARD_SIZE = 350;

    private VBox rows = new VBox();
    private boolean enemy = false;
    public int ships = 11;

    public Board(boolean enemy, EventHandler<? super MouseEvent> handler) {
        this.enemy = enemy;
        for (int y = 0; y < CELLS_IN_Y_AXSIS; y++) {
            HBox row = new HBox();
            for (int x = 0; x < CELLS_IN_X_AXSIS; x++) {
                Cell c = new Cell(x, y, this);
                c.setOnMouseClicked(handler);
                row.getChildren().add(c);
            }

            rows.getChildren().add(row);
        }

        getChildren().add(rows);
    }

    public boolean placeShip(Ship ship, int x, int y) {
        if (canPlaceShip(ship, x, y)) {
            int length = ship.h;
            int width = ship.w;
            for (int j = x ; j < x + width ; j++)
            {
                for (int i = y; i < y + length; i++) {
                    Cell cell = getCell(j, i);
                    cell.ship = ship;
                    if (!enemy) {
                        cell.setFill(Color.WHITE);
                        cell.setStroke(Color.GREEN);
                    }
                    else { //Test
                        cell.setFill(Color.WHITE);
                        cell.setStroke(Color.BLUE);
                    }
                }
            }

            return true;
        }

        return false;
    }

    public Cell getCell(int x, int y) {
        return (Cell) ((HBox) rows.getChildren().get(y)).getChildren().get(x);
    }

    private List<Cell> getNeighbors(int x, int y, Ship ship) {
        int width = ship.w;
        int length = ship.h;
        ArrayList<Point2D> points = new ArrayList<Point2D>();
        for (int i = x - 1 ; i < x + 1 + width ; i++) {
            points.add(new Point2D(i, y-1));
            points.add(new Point2D(i, y + length));
        }
        for (int i = y ; i < y + length ; i++) {
            points.add(new Point2D(x - 1, i));
            points.add(new Point2D(x + width, i));
        }
        List<Cell> neighbors = new ArrayList<Cell>();

        for (Point2D p : points) {
            if (isValidPoint(p)) {
                neighbors.add(getCell((int) p.getX(), (int) p.getY()));
            }
        }

        return neighbors;
    }

    private boolean canPlaceShip(Ship ship, int x, int y) {
        int width = ship.w;
        int length = ship.h;
        for (int j = x ; j < x + width ; j++)
        {
            for (int i = y; i < y + length; i++) {
                if (!isValidPoint(j, i))
                    return false;

                Cell cell = getCell(j, i);
                if (cell.ship != null)
                    return false;
            }
        }
        for (Cell neighbor : getNeighbors(x, y, ship)) {
            if (neighbor.ship != null)
                return false;
        }
        return true;
    }

    private boolean isValidPoint(Point2D point) {
        return isValidPoint(point.getX(), point.getY());
    }

    private boolean isValidPoint(double x, double y) {
        return x >= 0 && x < CELLS_IN_X_AXSIS && y >= 0 && y < CELLS_IN_Y_AXSIS;
    }
    private void removeShip (Ship ship) {
        List<Cell> shipCells = new ArrayList<Cell>();
        int firstX = 0;
        int firstY = 0;
        for (int xx = 0 ; xx < CELLS_IN_X_AXSIS ; xx++) {
            for (int yy = 0 ; yy < CELLS_IN_Y_AXSIS ; yy++) {
                if(getCell(xx,yy).ship == ship) {
                    shipCells.add(getCell(xx,yy));
                    if(shipCells.size() == 1) {
                        firstX = xx;
                        firstY = yy;
                    }
                }
            }
        }
        List<Cell> neighborsOfShip = getNeighbors(firstX,firstY,ship);
        for(Cell c : neighborsOfShip) {
            c.setFill(Color.ORANGERED);
            c.wasShot = true;
        }
    }
    public class Cell extends Rectangle {
        public int x, y;
        public Ship ship = null;
        public boolean wasShot = false;

        private Board board;

        public Cell(int x, int y, Board board) {
            super(BOARD_SIZE / CELLS_IN_X_AXSIS,BOARD_SIZE / CELLS_IN_X_AXSIS);
            this.x = x;
            this.y = y;
            this.board = board;
            setFill(Color.LIGHTGRAY);
            setStroke(Color.BLACK);
        }

        public boolean shoot() {
            wasShot = true;
            setFill(Color.BLACK);

            if (ship != null) {
                ship.hit();
                setFill(Color.RED);
                if (!ship.isAlive()) {
                    board.ships--;
                    removeShip(ship);
                }
                return true;
            }

            return false;
        }
    }
}