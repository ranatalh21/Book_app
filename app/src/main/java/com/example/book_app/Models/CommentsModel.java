package com.example.book_app.Models;

public class CommentsModel {

    private String Comment;

    private String publisher;

    public CommentsModel() {
    }

    public CommentsModel(String comment, String publisher) {
        Comment = comment;
        this.publisher = publisher;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}


