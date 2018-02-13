package com.consensys.demoweb.content;

import com.consensys.demoweb.auth.Account;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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
    private String path;
    private String contentType;


    public Account getOwner() {
        return owner;
    }

    public void setOwner(Account owner) {
        this.owner = owner;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
