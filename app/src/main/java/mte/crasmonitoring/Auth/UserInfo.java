package mte.crasmonitoring.Auth;

import com.google.gson.annotations.SerializedName;

/**
 * Created by eli on 18/11/2016.
 */

public class UserInfo {
    private String name;
    @SerializedName("mail")
    private String mail;
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("picture")
    private String picture;

    public UserInfo(String displayName, String email, String uid, String pictureUri) {
        this.name = displayName;
        if(name == null)
            name = "testUser";
        this.mail = email;
        this.user_id = uid;
        this.picture = pictureUri;
    }

    public String getDisplayName() {
        return name;
    }

    public String getEmail() {
        return mail;
    }

    public String getUid() {
        return user_id;
    }

    public void setDisplayName(String displayName) {
        this.name = displayName;
    }

    public void setEmail(String email) {
        this.mail = email;
    }

    public void setUid(String uid) {
        this.user_id = uid;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
