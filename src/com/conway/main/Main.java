package com.conway.main;

import com.conway.board.Board;

import javax.swing.*;

public class Main {

    public static void main(String[] arg){
        Board board = new Board();
        JFrame jframe = new JFrame("Conway's Game of Life");
        jframe.setSize(414,437);
        jframe.setResizable(false);
        jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jframe.setVisible(true);
        jframe.add(board);
    }

}
