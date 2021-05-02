package com.conway.board;

import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {

    //grid is a two dimentional array with x and y arrays
    static boolean[][] grid = new boolean[8][8];
    public static final int CELLSIZE = 50;


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        super.setSize(400, 400);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(new Color(0xFF666666, true));

        //draws cool chessboard entirely through logic
        int x = 0, y = 0;

        while (x < 8) {
            while (y<8) {
                grid[x][y] = true;
                y += 2;
            }
            x++;
            if (x%2 == 0) {
                y = 0;
            }
            else {
                y = 1;
            }
        }

        System.out.println(numberOfLivingCells());
        System.out.println(getCellNeighbors(1,1));

        //this method will hopefully draw the cells
        int cellx = 0, celly = 0;

        while (cellx < 8) {
            while (celly < 8) {
                if (grid[cellx][celly]) {
                    g2d.fillRect(cellx*CELLSIZE, celly*CELLSIZE, CELLSIZE, CELLSIZE);
                }
                celly++;
            }
            celly = 0;
            cellx++;
        }
    }
    //returns alive neighbors of a specified cell
    public static int getCellNeighbors(int x, int y) {
        int neighbors = 0;
        if (x < 8 && x > -1 && y < 8 && y > -1) {
            if (x < 7) {
                if (grid[x + 1][y]) {
                    neighbors++;
                }
            }
            if (x < 7 && y > 0) {
                if (grid[x + 1][y - 1]) {
                    neighbors++;
                }
            }
            if (x < 7 && y < 7) {
                if (grid[x + 1][y + 1]) {
                    neighbors++;
                }
            }
            if (x > 0 && y > 0) {
                if (grid[x - 1][y - 1]) {
                    neighbors++;
                }
            }
            if (x > 0) {
                if (grid[x - 1][y]) {
                    neighbors++;
                }
            }
            if (x > 0 && y < 7) {
                if (grid[x - 1][y + 1]) {
                    neighbors++;
                }
            }
            if (y < 7) {
                if (grid[x][y + 1]) {
                    neighbors++;
                }
            }
            if (y > 0) {
                if (grid[x][y - 1]) {
                    neighbors++;
                }
            }
        }
        return neighbors;
    }

    //returns number of living cells
    public static int numberOfLivingCells() {
        int livingCells = 0;

        for (boolean[] array : grid) {
            for (boolean cell : array) {
                if (cell) {
                    livingCells++;
                }
            }
        }

        return livingCells;
    }

}