package com.codecool.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RadioCharts {

    Connection connection;

    public RadioCharts(String url, String user, String password) {
        try {
            this.connection =  DriverManager.getConnection(
                    url,user,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public String getMostPlayedSong(){
        String SQL = "SELECT * FROM music_broadcast";
        try {
            ResultSet rs = connection.createStatement().executeQuery(SQL);
            Map<Song,Integer> songs = new HashMap<>();
            if (!rs.next()) {
                return "";
            }
            else {
                do {
                    Song song = new Song(rs.getString(2), rs.getInt(3));
                    int count = songs.getOrDefault(song, 0);
                    songs.put(song, count + song.getTimesAired());
                } while (rs.next());
            }
            Map.Entry<Song,Integer> mostPlayed = null;
            for (Map.Entry<Song,Integer> song : songs.entrySet())
            {
                if (mostPlayed == null || song.getValue().compareTo(mostPlayed.getValue()) > 0)
                {
                    mostPlayed = song;
                }
            }
            return mostPlayed.getKey().getTitle();
        } catch (SQLException e) {
            return "";
        }
    }
    public String getMostActiveArtist(){
        String SQL = "SELECT * FROM music_broadcast";
        try {
            ResultSet rs = connection.createStatement().executeQuery(SQL);
            Map<Artist,Integer> artists = new HashMap<>();
            List<Song> songs = new ArrayList<>();
            if (!rs.next()) {
                return "";
            }
            else {
                do {
                    Artist artist = new Artist(rs.getString(1));
                    Song song = new Song(rs.getString(2), rs.getInt(3));
                    if(!songs.contains(song)) {
                        int count = artists.getOrDefault(artist, 0);
                        artists.put(artist, count + 1);
                        songs.add(song);
                    }
                } while (rs.next());
            }
            Map.Entry<Artist,Integer> mostActive = null;
            for (Map.Entry<Artist,Integer> artist : artists.entrySet())
            {
                if (mostActive == null || artist.getValue().compareTo(mostActive.getValue()) > 0)
                {
                    mostActive = artist;
                }
            }
            return mostActive.getKey().getName();
        } catch (SQLException e) {
            return "";
        }
    }
}
