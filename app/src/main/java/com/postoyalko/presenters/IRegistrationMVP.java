package com.postoyalko.presenters;

/**
 * Created by aleksei on 26.03.2019.
 */

public interface IRegistrationMVP {
    interface IRegistrationPresenter {
        void registrationButtonWasClicked(String email, String password);
        String[] loginTextChanged(CharSequence s);
        String passwordTextChanged(CharSequence s);
    }

    interface IRegistrationView {
        void showToast(String message);
    }

}
