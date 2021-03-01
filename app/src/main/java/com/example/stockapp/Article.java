package com.example.stockapp;

public class Article {
    String ntitle;
    String source;
    String image;
    String ago;
    String link;
    String imglink;

    public Article() {

    }

    public String getTitle() {
        return ntitle;
    }

    public void setTitle(String ntitle) {
        this.ntitle = ntitle;
    }
    public String getAgo() {
        return ago;
    }

    public void setAgo(String ago) {
        this.ago= ago;
    }

    public String getBody() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
    public String getImgLink() {
        return imglink;
    }

    public void setImgLink(String imglink) {
        this.imglink = imglink;
    }
}
