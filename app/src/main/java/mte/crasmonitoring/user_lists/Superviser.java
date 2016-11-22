package mte.crasmonitoring.user_lists;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by eli on 22/11/2016.
 */

public class Superviser {
    @SerializedName("picture")
    String picture;
    @SerializedName("name")
    String name;
    @SerializedName("_id")
    String _id;
    @SerializedName("mail")
    String mail;

    public String get_id() {
        return _id;
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

    public void set_id(String _id) {
        this._id = _id;
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
