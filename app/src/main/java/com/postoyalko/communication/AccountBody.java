package com.postoyalko.communication;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aleksei on 21.03.2019.
 */

public class AccountBody {
    @SerializedName("login")
    private String login;
    @SerializedName("password")
    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
