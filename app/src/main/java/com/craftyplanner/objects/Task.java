package com.craftyplanner.objects;

public class Task {
    private String text;
    private String status;

    public Task(String text, String status){
        this.text = text;
        this.status = status;
    }

    public String getText() {
        return text;
    }
    public String getStatus() {
        return status;
    }

    public void setText(String text) {
        this.text = text;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
