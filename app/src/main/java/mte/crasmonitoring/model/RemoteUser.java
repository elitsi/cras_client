package mte.crasmonitoring.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by eli on 22/11/2016.
 */

public class RemoteUser {
    @SerializedName("picture")
    String picture;
    @SerializedName("name")
    String name;
    @SerializedName("_id")
    String id;
    @SerializedName("mail")
    String mail;

    public RemoteUser(String name, String id, String mail , String picture){
        this.id = id;
        this.mail = mail;
        this.name = name;
        this.picture = picture;
    }

    public String getID() {
        return id;
    }

    public String getMail() {
        return mail;
    }

    public String getName() {
        return name;
    }

    public String getPicture() {
        return picture;
    }

    public void getId(String _id) {
        this.id = _id;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
