package com.example.halward.model;

import java.io.Serializable;
import java.util.UUID;

public class Habit implements Serializable, Comparable<Habit> {
    private String name;
    private String description;
    private int duration;
    private String image;
    private UUID key;

    public Habit(){
key =UUID.randomUUID();
    }
    public Habit(String name, int duration, String image){
        this.name = name;
        this.duration = duration;
        this.image = image;
    }
    public Habit(String name){
        this.name = name;
    }

    @Override
    public int compareTo(Habit habit) {
        return 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
