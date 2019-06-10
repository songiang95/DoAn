package com.example.songiang.readebookandmanga.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NonNls;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity(tableName = "favorite_comic")
public class Comic implements Serializable {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    private String name;
    @ColumnInfo(name = "image")
    private String image;
    @Ignore
    private String artist;
    @ColumnInfo(name = "detail_url")
    private String detailUrl;
    @Ignore
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

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }



}
