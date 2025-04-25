package com.example.sports.model;

public enum Action {
    SERVE("发球"),
    RECEIVE("接发球"),
    FOREHAND("正手击球"),
    BACKHAND("反手击球"),
    NET_APPROACH("上网"),
    SCORE("得分");

    private final String displayName;

    Action(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}