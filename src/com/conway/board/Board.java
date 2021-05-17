package com.conway.board;

import com.conway.main.Display;

import javax.sound.sampled.LineUnavailableException;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class Board implements Runnable{

    boolean[][] secondGrid = new boolean[numberOfPossibleCells][numberOfPossibleCells];
    public static double timePerTick = 20000000;
    public static boolean editing = true;
    private Display display;
    public String title;
    public int screen_width, screen_height;

    private boolean running = false;
    private Thread thread;

    public static boolean[][] grid = new boolean[80][80];
    public static final int CELLSIZE = 10;

    public static final int numberOfPossibleCells = grid.length;

    // these arguments that the Board class needs are the same ones that the Display class needs
    public Board(String title, int screen_width, int screen_height) {
        this.title = title;
        this.screen_height = screen_height;
        this.screen_width = screen_width;
    }

    public synchronized void start() {
        // if it's running, then don't do the next lines of code in this method
        if (running) return;

        running = true;
        thread = new Thread(this);
        thread.start();

    }

    //initializes cells manually
    private void init() throws LineUnavailableException {
        // this creates the frame
        display = new Display(title, screen_width, screen_height);

        //draws cool checkerboard pattern entirely through logic
//        int x = 0, y = 0;
//
//        while (x < 80) {
//            while (y<80) {
//                grid[x][y] = true;
//                y += 2;
//            }
//            x++;
//            if (x%2 == 0) {
//                y = 0;
//            }
//            else {
//                y = 1;
//            }
//        }
    }

    // the rules for the game of life
    private void update() {

        for (int y = 0; y < numberOfPossibleCells; y++) {
            for (int x = 0; x < numberOfPossibleCells; x++) {
                int aliveNeighbors = getCellNeighbors(x, y);

                if (grid[x][y]) {
                    //underpopulation
                    if (aliveNeighbors < 2) {
                        secondGrid[x][y] = false;
                    }
                    //overpopulation
                    else if (aliveNeighbors > 3) {
                        secondGrid[x][y] = false;
                    }
                }
                else {
                    //reproduction
                    if (aliveNeighbors == 3) {
                        secondGrid[x][y] = true;
                    }
                }
            }
        }
        for (int y = 0; y < numberOfPossibleCells; y++) {
            for (int x = 0; x < numberOfPossibleCells; x++) {
                grid[x][y] = secondGrid[x][y];
            }
        }

    }

    // this method draws all the cells to the canvas
    private void render() {

        BufferStrategy bs = display.getCanvas().getBufferStrategy();
        if (bs == null) {
            display.getCanvas().createBufferStrategy(3);
            return;
        }
        Graphics2D g2d = (Graphics2D) bs.getDrawGraphics();

        // this clears the canvas
        g2d.clearRect(0, 0, screen_width, screen_height);

        // this is where you tell everything u want to draw
        g2d.setColor(new Color(0xFFAFAFAF, true));

        //this draws a grid
        if (editing) {
            for (int i = 0; i < numberOfPossibleCells; i++) {
                g2d.drawLine(i * CELLSIZE, 0, i * CELLSIZE, CELLSIZE * numberOfPossibleCells);
            }
            for (int i = 0; i < numberOfPossibleCells; i++) {
                g2d.drawLine(0, i * CELLSIZE, CELLSIZE * numberOfPossibleCells, i * CELLSIZE);
            }
        }

        //left click for smaller brush
        if (Board.editing && Display.leftMouseDown) {
            int x = (Display.mouseX - (Display.mouseX % Board.CELLSIZE))/Board.CELLSIZE;
            int y = (Display.mouseY - (Display.mouseY % Board.CELLSIZE))/Board.CELLSIZE;
            if (x < 80 && x > -1 && y < 80 && y > -1) {
                secondGrid[x][y] = true;
            }
        }

        //right click for bigger brush
        if (Board.editing && Display.rightMouseDown) {
            int x = (Display.mouseX - (Display.mouseX % Board.CELLSIZE))/Board.CELLSIZE;
            int y = (Display.mouseY - (Display.mouseY % Board.CELLSIZE))/Board.CELLSIZE;
            if (x < 80 && x > -1 && y < 80 && y > -1) {
                secondGrid[x][y] = true;
                if (x < 79) {
                    secondGrid[x+1][y] = true;
                }
                if (x < 79 && y > 0) {
                    secondGrid[x + 1][y - 1] = true;
                }
                if (x < 79 && y < 79) {
                    secondGrid[x + 1][y + 1] = true;
                }
                if (x > 0 && y > 0) {
                    secondGrid[x - 1][y - 1] = true;
                }
                if (x > 0) {
                    secondGrid[x - 1][y] = true;
                }
                if (x > 0 && y < 79) {
                    secondGrid[x - 1][y + 1] = true;
                }
                if (y < 79) {
                    secondGrid[x][y + 1] = true;
                }
                if (y > 0) {
                    secondGrid[x][y - 1] = true;
                }
            }
        }

        //sets color for living cells
        g2d.setColor(new Color(0xFF666666, true));

        //this method will hopefully draw the cells
        int cellx = 0, celly = 0;

        while (cellx < numberOfPossibleCells) {
            while (celly < numberOfPossibleCells) {
                if (secondGrid[cellx][celly]) {
                    g2d.fillRect(cellx * CELLSIZE, celly * CELLSIZE, CELLSIZE, CELLSIZE);
                }
                celly++;
            }
            celly = 0;
            cellx++;
        }
        // stuff that needs to be done for drawing, don't mind it
        bs.show();
        g2d.dispose();
    }

    public void run() {
        try {
            init();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        // all of these variables are to maintain delta time, don't worry if you don't understand it

        double delta = 0;
        long now;
        long lastTime = System.nanoTime();
        long timer = 0;

        // this updates the variables and draws all the cells to the screen
        while (running) {

            timePerTick = 20000000;
            if (!editing) {
                timePerTick *= 10;
            }

            now = System.nanoTime();
            delta += (now - lastTime) / timePerTick;
            timer += now - lastTime;
            lastTime = now;

            if (delta >= 1) {
                render();
                if (!editing) {
                    update();
                }
                delta--;
            }

            if (timer >= 1000000000) {
                timer = 0;
            }
        }

        stop();
    }

    public synchronized void stop() {
        // if it's not running, then don't do the next lines of code in this method
        if (!running) return;

        // stops the thread properly
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //returns the number of neighbors a cell has
    public static int getCellNeighbors(int x, int y) {
        int neighbors = 0;
        if (x < 80 && x > -1 && y < 80 && y > -1) {
            if (x < 79) {
                if (grid[x + 1][y]) {
                    neighbors++;
                }
            }
            if (x < 79 && y > 0) {
                if (grid[x + 1][y - 1]) {
                    neighbors++;
                }
            }
            if (x < 79 && y < 79) {
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
            if (x > 0 && y < 79) {
                if (grid[x - 1][y + 1]) {
                    neighbors++;
                }
            }
            if (y < 79) {
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

}
