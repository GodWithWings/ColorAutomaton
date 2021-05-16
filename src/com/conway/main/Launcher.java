package com.conway.main;

import com.conway.board.Board;

public class Launcher {
    // we are running the game class over here
    public static void main(String[] args) {
        Board board = new Board("Conway's Game of Life", 850, 800);
        board.start();
    }
}
