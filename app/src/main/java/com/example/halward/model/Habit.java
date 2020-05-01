package com.example.halward.model;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;
import java.util.Date;

public class Habit implements Serializable, Comparable<Habit> {
    private String name;
    private String description;
    private int duration;
    private boolean isActive;
    private String image;
    private String key;
    @DocumentId
    private String documentName; //save document name directly in FireBase
    private Date startDate;
    private Date endDate;
    private  boolean isDone;
    private String tag;

    public Habit() {
        key = Double.toString( Math.random());
        startDate = new Date();
        isDone = false;
    }


    public Habit(String name, int duration, String image) {
        this.name = name;
        this.duration = duration;
        this.image = image;
    }

    public Habit(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Habit habit) {
        return 0;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
