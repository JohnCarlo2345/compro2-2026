package com.rpsgame.service;

public abstract class GameMove {
    private String moveName;


    public GameMove(String name) {
        this.moveName = name;
    }


    public String getMoveName() { return moveName; }
    public abstract int compare(GameMove other);
}


class Rock extends GameMove {
    public Rock() { super("Rock"); }
    @Override public int compare(GameMove other) {
        if (other instanceof Scissors) return 1;
        if (other instanceof Paper) return -1;
        return 0;
    }
}


class Paper extends GameMove {
    public Paper() { super("Paper"); }
    @Override public int compare(GameMove other) {
        if (other instanceof Rock) return 1;
        if (other instanceof Scissors) return -1;
        return 0;
    }
}


class Scissors extends GameMove {
    public Scissors() { super("Scissors"); }
    @Override public int compare(GameMove other) {
        if (other instanceof Paper) return 1;
        if (other instanceof Rock) return -1;
        return 0;
    }
}







