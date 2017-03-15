package mte.crasmonitoring.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by eli on 22/11/2016.
 */

public class RemoteUser {
    @SerializedName("picture")
    private String picture;
    @SerializedName("name")
    private String name;
    @SerializedName("_id")
    private String id;
    @SerializedName("mail")
    private String mail;
    private boolean status;

    public RemoteUser(String name, String id, String mail, String picture, boolean status){
        this.id = id;
        this.mail = mail;
        this.name = name;
        this.picture = picture;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RemoteUser that = (RemoteUser) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return 0;
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

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
