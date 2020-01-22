package com.example.halward.model;

import java.io.Serializable;

public class Habit implements Serializable, Comparable<Habit> {
    private String name;
    private String description;
    private int duration;
    private int image;

    public Habit(){

    }
    public Habit(String name, int duration, int image){
        this.name = name;
        this.duration = duration;
        this.image = image;
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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
