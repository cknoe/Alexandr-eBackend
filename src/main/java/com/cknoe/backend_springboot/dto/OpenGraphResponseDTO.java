package com.cknoe.backend_springboot.dto;

public class OpenGraphResponseDTO {
    public String title;
    public String image;
    public String site;
    public String url;

    public OpenGraphResponseDTO(String title, String image, String site, String url) {
        this.title = title;
        this.image = image;
        this.site = site;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
