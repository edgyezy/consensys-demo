package com.consensys.demo.web.content;

import com.consensys.demo.web.auth.Account;

import javax.persistence.*;

/**
 * Created by Calvin Ngo on 13/2/18.
 */

@Entity
public class UserContent {


    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private Account owner;

    private String fileName;
    private String contentId;
    private String contentType;
    private Long contentLength;

    public Account getOwner() {
        return owner;
    }

    public void setOwner(Account owner) {
        this.owner = owner;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getContentLength() {
        return contentLength;
    }

    public void setContentLength(Long contentLength) {
        this.contentLength = contentLength;
    }
}
