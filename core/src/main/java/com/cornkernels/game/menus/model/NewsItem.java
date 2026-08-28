package com.cornkernels.game.menus.model;

public class NewsItem {
    private String id;
    private String title;
    private String date;
    private String body;
    private boolean isRead;
    private String type;

    public NewsItem(String id, String title, String date, String body, String type) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.body = body;
        this.type = type;
        this.isRead = false;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getBody() {
        return body;
    }

    public String getType() {
        return type;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
