package com.mygdx.tap.Utility;

public class HighScore {

    public int score;

    private static HighScore singleton = new HighScore();

    private HighScore() {

    }

    public static HighScore returnInstance() {
        return singleton;
    }

    public int addScore(int points) {
        score += points;
        return score;
    }

    public Integer getTotal() {
        return score;
    }

    public void setScore(int newScore) {
        score = newScore;
    }
}
