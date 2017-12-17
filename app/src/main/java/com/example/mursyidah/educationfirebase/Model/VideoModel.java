package com.example.mursyidah.educationfirebase.Model;

/**
 * Created by mursyidah on 26/11/2017.
 */

public class VideoModel {

    private String Vtitle, Vdesc, video;

    public VideoModel(){

    }

    public VideoModel(String Vtitle, String Vdesc, String video) {
        this.Vtitle = Vtitle;
        this.Vdesc = Vdesc;
        this.video = video;
    }

    public String getVtitle() {
        return Vtitle;
    }

    public void setVtitle(String vtitle) {
        Vtitle = vtitle;
    }

    public String getVdesc() {
        return Vdesc;
    }

    public void setVdesc(String vdesc) {
        Vdesc = vdesc;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

}
