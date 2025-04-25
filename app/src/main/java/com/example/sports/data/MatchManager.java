package com.example.sports.data;

import com.example.sports.model.Match;
import com.example.sports.model.Player;
import com.example.sports.model.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchManager {
    private static MatchManager instance;

    private List<Match> matches;
    private Map<String, Team> teams;
    private Match currentMatch;

    private MatchManager() {
        matches = new ArrayList<>();
        teams = new HashMap<>();
    }

    public static synchronized MatchManager getInstance() {
        if (instance == null) {
            instance = new MatchManager();
        }
        return instance;
    }

    // 创建新队伍
    public Team createTeam(String name) {
        Team team = new Team(name);
        teams.put(name, team);
        return team;
    }

    // 获取队伍
    public Team getTeam(String name) {
        return teams.get(name);
    }

    // 获取所有队伍
    public List<Team> getAllTeams() {
        return new ArrayList<>(teams.values());
    }

    // 创建新比赛
    public Match createMatch(Team team1, Team team2) {
        Match match = new Match(team1, team2);
        matches.add(match);
        currentMatch = match;
        return match;
    }

    // 获取当前比赛
    public Match getCurrentMatch() {
        return currentMatch;
    }

    // 设置当前比赛
    public void setCurrentMatch(Match match) {
        this.currentMatch = match;
    }

    // 获取所有比赛
    public List<Match> getAllMatches() {
        return matches;
    }

    // 添加球员到队伍
    public Player addPlayerToTeam(String playerName, Team team) {
        Player player = new Player(playerName, team);
        team.addPlayer(player);
        return player;
    }

    // 从队伍移除球员
    public void removePlayerFromTeam(Player player, Team team) {
        team.removePlayer(player);
    }

    // 检查是否可以开始比赛
    public boolean canStartMatch(Match match) {
        return match.getTeam1().getPlayers().size() >= 2 &&
               match.getTeam2().getPlayers().size() >= 2;
    }
}