package com.conway.main;

import com.conway.board.Board;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Display implements ActionListener, MouseListener, MouseMotionListener {
    public static Canvas canvas;
    public static boolean leftMouseDown, rightMouseDown;
    public static int mouseX, mouseY;

    private final String title;
    private final int screen_width;
    private final int screen_height;
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
        JFrame frame = new JFrame(title);
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

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            rightMouseDown = true;
        }
        else {
            leftMouseDown = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            rightMouseDown = false;
        }
        else {
            leftMouseDown = false;
        }
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
