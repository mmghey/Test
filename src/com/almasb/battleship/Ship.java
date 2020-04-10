package com.almasb.battleship;

import javafx.scene.Parent;

public class Ship extends Parent {
    public int type;
    public boolean vertical = true;
    public int w;
    public int h;

    private int health;

    public Ship(int type, boolean vertical) {
        this.type = type;
        if (type == 1) {
            w = 1;
            h = 1;
        } else if (type == 2) {
            w = 1; h = 2;
        } else if (type == 3) {
            w = 2; h = 2;
        } else if (type == 4) {
            w = 3; h = 3;
        }
        this.vertical = vertical;
        if(!vertical) {
            int swp = w;
            w = h;
            h = swp;
        }
        health = w * h;

        /*VBox vbox = new VBox();
        for (int i = 0; i < type; i++) {
            Rectangle square = new Rectangle(30, 30);
            square.setFill(null);
            square.setStroke(Color.BLACK);
            vbox.getChildren().add(square);
        }

        getChildren().add(vbox);*/
    }

    public void hit() {
        health--;
    }

    public boolean isAlive() {
        return health > 0;
    }
}