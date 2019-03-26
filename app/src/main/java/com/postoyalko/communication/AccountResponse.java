package com.postoyalko.communication;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aleksei on 21.03.2019.
 */

public class AccountResponse {

    @SerializedName("status")
    private int status;

    @SerializedName("data")
    private AccountData data;

    @SerializedName("error")
    private String error;

    @SerializedName("valid")
    private ValidationMessage validationMessage;

    public ValidationMessage getValidationMessage() {
        return validationMessage;
    }

    public void setValidationMessage(ValidationMessage validationMessage) {
        this.validationMessage = validationMessage;
    }


    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public AccountData getData() {
        return data;
    }

    public void setData(AccountData data) {
        this.data = data;
    }

    public class AccountData {
        @SerializedName("login")
        private String login;
        @SerializedName("token")
        private String token;
        @SerializedName("userId")
        private int userId;

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }

    public class ValidationMessage {

        @SerializedName("field")
        private String field;
        @SerializedName("message")
        private String message;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
