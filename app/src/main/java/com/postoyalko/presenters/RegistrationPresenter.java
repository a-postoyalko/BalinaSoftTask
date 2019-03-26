package com.postoyalko.presenters;

import android.content.Context;
import android.graphics.Typeface;

import com.google.gson.Gson;
import com.postoyalko.R;
import com.postoyalko.communication.AccountBody;
import com.postoyalko.communication.AccountResponse;
import com.postoyalko.communication.BalinaApiClient;
import com.postoyalko.font.AppFonts;
import com.postoyalko.utilities.AccountUtils;
import com.postoyalko.utilities.NetworkUtils;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by aleksei on 26.03.2019.
 */

public class RegistrationPresenter implements IRegistrationMVP.IRegistrationPresenter {

    private static RegistrationPresenter registrationPresenter;
    private IRegistrationMVP.IRegistrationView mView;
    private Context context;
    private AccountBody accountBody;

    private RegistrationPresenter(IRegistrationMVP.IRegistrationView mView, Context context) {
        this.mView = mView;
        this.context = context;
        accountBody = new AccountBody();
    }

    public static RegistrationPresenter getRegistrationPresenter(IRegistrationMVP.IRegistrationView mView, Context context){
        if (registrationPresenter == null) {
            registrationPresenter = new RegistrationPresenter(mView , context);
        }
        return registrationPresenter;
    }

    @Override
    public void registrationButtonWasClicked(String email, String password) {
        if (NetworkUtils.isNetworkAvailable(context)) {
            accountBody.setLogin(email);
            accountBody.setPassword(password);
            BalinaApiClient.getApi().registerAccount(accountBody).enqueue(new RegistrationCallback());
        }
        else {
            mView.showToast(context.getString(R.string.check_internet_connection));
        }
    }

    @Override
    public String[] loginTextChanged(CharSequence s) {
        int occurrences = 0;
        String enteredString = s.toString();
        for (char c : enteredString.toCharArray()) {
            if (c == AccountUtils.AT_SYMBOL) {
                occurrences++;
            }
        }
        if (occurrences == 1) {
            return AccountUtils.getEmails(enteredString);
        }
        return null;
    }

    @Override
    public String passwordTextChanged(CharSequence s) {
        if (!AccountUtils.isValidPassword(s.toString())) {
            return AccountUtils.getInvalidPasswordMessage(context);
        }
        return null;
    }

    public Typeface getFont() {
        return AppFonts.getInstance(context);
    }

    public String checkEmail(String email) {
        if (isNetworkAvailable(context))
            if (AccountUtils.isValidEmail(email))
                return null;
            else
                return AccountUtils.getInvalidEmailMessage(context,email);
        else
            return context.getString(R.string.check_internet_connection);
    }

    public String checkPassword(String password) {
        if (AccountUtils.isValidPassword(password))
            return null;
        else
            return AccountUtils.getInvalidPasswordMessage(context);
    }

    public boolean isNetworkAvailable(Context applicationContext) {
        return NetworkUtils.isNetworkAvailable(context);
    }

    private class RegistrationCallback implements Callback<AccountResponse> {
        @Override
        public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
            AccountResponse accountResponse = response.body();
            if (accountResponse == null) {
                try {
                    accountResponse = new Gson().fromJson(response.errorBody().string(),AccountResponse.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            String message = null;
            if(accountResponse != null) {
                switch (accountResponse.getStatus()){
                    case  BalinaApiClient.SUCCESS:
                        message = BalinaApiClient.SUCCESS_MESSAGE;
                        break;
                    case BalinaApiClient.BAD_REQUEST:
                        if(accountResponse.getValidationMessage() != null) {
                            message = accountResponse.getValidationMessage().getField() + " "
                                + accountResponse.getValidationMessage().getMessage();
                        }
                        else
                            message = accountResponse.getError();
                        break;
                    default:
                        message = BalinaApiClient.ERROR_MESSAGE;
                }
            }
             mView.showToast(message);
        }
        @Override
        public void onFailure(Call<AccountResponse> call, Throwable t) {
            mView.showToast(BalinaApiClient.ERROR_MESSAGE);
        }
    }
}
