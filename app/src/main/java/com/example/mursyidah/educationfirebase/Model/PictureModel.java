package com.example.mursyidah.educationfirebase.Model;

/**
 * Created by mursyidah on 13/11/2017.
 */

public class PictureModel {

    private String title, desc, image, video;

    public PictureModel(){

    }



    public PictureModel(String title, String desc, String image) {
        this.title = title;
        this.desc = desc;
        this.image = image;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
