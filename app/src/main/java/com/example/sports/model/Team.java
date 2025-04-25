package com.example.sports.model;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private String name;
    private List<Player> players;
    private int score;

    public Team(String name) {
        this.name = name;
        this.players = new ArrayList<>();
        this.score = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public int getScore() {
        return score;
    }

    public void increaseScore() {
        score++;
    }

    public void resetScore() {
        score = 0;
    }
}