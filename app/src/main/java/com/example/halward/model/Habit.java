package com.example.halward.model;

import java.io.Serializable;

public class Habit implements Serializable, Comparable<Habit> {
    private String name;
    private String description;
    private int duration;
    private byte[] image;

    @Override
    public int compareTo(Habit habit) {
        return 0;
    }
}
