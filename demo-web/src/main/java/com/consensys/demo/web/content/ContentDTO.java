package com.consensys.demo.web.content;

/**
 * Created by Calvin Ngo on 14/2/18.
 */
public class ContentDTO {

    private String contentId;
    private String fileName;
    private String location;

    public ContentDTO(UserContent content) {
        contentId = content.getContentId();
        fileName = content.getFileName();
        location = "/content/" + contentId;
    }

    public String getContentId() {
        return contentId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getLocation() {
        return location;
    }


}
