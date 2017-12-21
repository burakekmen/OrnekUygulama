package com.burakekmen.ornekuygulama.models;

import java.io.Serializable;

/****************************
 * Created by Burak EKMEN   |
 * 20.12.2017               |
 * ekmen.burak@hotmail.com  |
 ***************************/

public class PhotoModel implements Serializable {

    private String id;
    private String secret;
    private String server;
    private String farm;

    public PhotoModel(String id, String secret, String server, String farm) {
        this.id = id;
        this.secret = secret;
        this.server = server;
        this.farm = farm;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return "http://farm" + farm + ".static.flickr.com/" + server + "/" + id + "_" + secret + ".jpg";
    }

}