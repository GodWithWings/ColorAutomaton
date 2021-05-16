package com.conway.main;

import com.conway.board.Board;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Display implements ActionListener, MouseListener, MouseMotionListener {
    private JFrame frame;
    public static Canvas canvas;
    public static boolean mouseDown;
    public static int mouseX, mouseY;

    private String title;
    private int screen_width, screen_height;
    JButton button;

    public Display(String title, int screen_width, int screen_height) {
        // using 'this' keyword to differentiate between variables in class and variables for constructor
        this.title = title;
        this.screen_width = screen_width;
        this.screen_height = screen_height;
        createDisplay();
    }

    private void createDisplay() {
        // this method will set the parameters for the window
        frame = new JFrame(title);
        frame.setSize(screen_width, screen_height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        //button to toggle editing mode
        button = new JButton();
        button.setBounds(800, 0, 50, 800);
        button.addActionListener(this);
        frame.add(button);

        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(screen_width, screen_height));
        canvas.setMaximumSize(new Dimension(screen_width, screen_height));
        canvas.setMinimumSize(new Dimension(screen_width, screen_height));
        canvas.setBackground(new Color(208, 208, 208));
        canvas.setFocusable(false);
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);

        frame.add(canvas);
        frame.pack();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public JFrame getFrame() {
        return frame;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (Board.editing && Display.mouseDown) {
            int x = (Display.mouseX - (Display.mouseX % Board.CELLSIZE))/Board.CELLSIZE;
            int y = (Display.mouseY - (Display.mouseY % Board.CELLSIZE))/Board.CELLSIZE;
            if (x < 80 && x > -1 && y < 80 && y > -1) {
                Board.grid[x][y] = true;
                if (x < 79) {
                    Board.grid[x+1][y] = true;
                }
                if (x < 79 && y > 0) {
                    Board.grid[x + 1][y - 1] = true;
                }
                if (x < 79 && y < 79) {
                    Board.grid[x + 1][y + 1] = true;
                }
                if (x > 0 && y > 0) {
                    Board.grid[x - 1][y - 1] = true;
                }
                if (x > 0) {
                    Board.grid[x - 1][y] = true;
                }
                if (x > 0 && y < 79) {
                    Board.grid[x - 1][y + 1] = true;
                }
                if (y < 79) {
                    Board.grid[x][y + 1] = true;
                }
                if (y > 0) {
                    Board.grid[x][y - 1] = true;
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseDown = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseDown = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == button) {
            Board.editing = !Board.editing;
//            if (button.getText().equals("Start Simulation")) {
//                button.setText("Switch to Edit Mode");
//            }
//            else {
//                button.setText("Start Simulation");
//            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
