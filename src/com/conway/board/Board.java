package com.conway.board;

import com.conway.main.Display;

import javax.sound.sampled.LineUnavailableException;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class Board implements Runnable{

    public static boolean editing = true;
    private Display display;
    public String title;
    public int screen_width, screen_height;

    private boolean running = false;
    private Thread thread;

    private BufferStrategy bs;
    private Graphics2D g2d;

    public static boolean[][] grid = new boolean[80][80];
    public static final int CELLSIZE = 10;

    public static final int numberOfPossibleCells = grid.length;

    // these arguments that the Game class needs are the same ones that the Display class needs
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
        int cellx = 0, celly = 0;

        while (cellx < numberOfPossibleCells) {
            while (celly < numberOfPossibleCells) {
                //underpopulation
                if (getCellNeighbors(cellx, celly) < 2) {
                    grid[cellx][celly] = false;
                }

                //overpopulation
                if (getCellNeighbors(cellx, celly) > 3) {
                    grid[cellx][celly] = false;
                }

                //reproduction
                if (getCellNeighbors(cellx, celly) == 3) {
                    grid[cellx][celly] = true;
                }

                celly++;
            }
            celly = 0;
            cellx++;
        }
    }

    // this method draws all the cells to the canvas
    private void render() {
        bs = display.getCanvas().getBufferStrategy();
        if (bs == null) {
            display.getCanvas().createBufferStrategy(3);
            return;
        }
        g2d = (Graphics2D) bs.getDrawGraphics();

        // this clears the canvas
        g2d.clearRect(0, 0, screen_width, screen_height);

        // this is where you tell everything u want to draw
        g2d.setColor(new Color(0xFF666666, true));

        //this tedious function makes the paintbrish size bigger
        if (Board.editing && Display.mouseDown) {
            int x = (Display.mouseX - (Display.mouseX % Board.CELLSIZE))/Board.CELLSIZE;
            int y = (Display.mouseY - (Display.mouseY % Board.CELLSIZE))/Board.CELLSIZE;
            if (x < 80 && x > -1 && y < 80 && y > -1) {
                grid[x][y] = true;
                if (x < 79) {
                    grid[x+1][y] = true;
                }
                if (x < 79 && y > 0) {
                    grid[x + 1][y - 1] = true;
                }
                if (x < 79 && y < 79) {
                    grid[x + 1][y + 1] = true;
                }
                if (x > 0 && y > 0) {
                    grid[x - 1][y - 1] = true;
                }
                if (x > 0) {
                    grid[x - 1][y] = true;
                }
                if (x > 0 && y < 79) {
                    grid[x - 1][y + 1] = true;
                }
                if (y < 79) {
                    grid[x][y + 1] = true;
                }
                if (y > 0) {
                    grid[x][y - 1] = true;
                }
            }
        }

        //this method will hopefully draw the cells
        int cellx = 0, celly = 0;

        while (cellx < numberOfPossibleCells) {
            while (celly < numberOfPossibleCells) {
                if (grid[cellx][celly]) {
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

        double timePerTick = 0;
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();
        long timer = 0;

        // this updates the variables and draws all the cells to the screen
        while (running) {

            if (editing) {
                timePerTick = 10000000;
            }
            else {
                timePerTick = 200000000;
            }
            now = System.nanoTime();
            delta += (now - lastTime) / timePerTick;
            timer += now - lastTime;
            lastTime = now;

            if (delta >= 1) {
                if (!editing) {
                    update();
                }
                render();
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

    //returns the number of currently living cells
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
