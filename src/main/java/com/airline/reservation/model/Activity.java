package com.airline.reservation.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Activity {

    private final StringProperty time;
    private final StringProperty activity;
    private final StringProperty user;
    private final StringProperty status;

    public Activity(String time, String activity, String user, String status) {
        this.time = new SimpleStringProperty(time);
        this.activity = new SimpleStringProperty(activity);
        this.user = new SimpleStringProperty(user);
        this.status = new SimpleStringProperty(status);
    }

    // Getters and setters for TableView binding
    public String getTime() {
        return time.get();
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public StringProperty timeProperty() {
        return time;
    }

    public String getActivity() {
        return activity.get();
    }

    public void setActivity(String activity) {
        this.activity.set(activity);
    }

    public StringProperty activityProperty() {
        return activity;
    }

    public String getUser() {
        return user.get();
    }

    public void setUser(String user) {
        this.user.set(user);
    }

    public StringProperty userProperty() {
        return user;
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public StringProperty statusProperty() {
        return status;
    }
}
