package com.example.songiang.readebookandmanga.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class Comic implements Serializable {

    private String name;
    private String image;
    private String newestChapter;
    private String artist;
    private String detailUrl;
    private Map<String, String> chapter;
    public Comic() {
        chapter = new LinkedHashMap<>();
    }


    public void addChap(String key, String value) {
        chapter.put(key,value);
    }
    public Map getMap()
    {
        return chapter;
    }
    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNewestChapter() {
        return newestChapter;
    }

    public void setNewestChapter(String newestChapter) {
        this.newestChapter = newestChapter;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
