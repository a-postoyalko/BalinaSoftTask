package com.postoyalko.activities;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.postoyalko.R;
import com.postoyalko.presenters.IRegistrationMVP;
import com.postoyalko.presenters.RegistrationPresenter;

public class RegistrationActivity extends AppCompatActivity implements IRegistrationMVP.IRegistrationView{

    private Button registrationButton;
    private ImageView logoImageView;
    private AutoCompleteTextView loginEditText;
    private EditText passwordEditText;
    private TextView loginTextView;
    private TextView passwordTextView;
    private FrameLayout frameLayout;
    private ImageView passwordDrawable;
    private ArrayAdapter<String> adapter;
    private int maxHeight;
    private boolean passwordVisible = false;

    private RegistrationPresenter registrationPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        registrationPresenter = RegistrationPresenter.getRegistrationPresenter(this, getApplicationContext());
        registrationButton = (Button) findViewById(R.id.registrationButton);
        getWindow().setBackgroundDrawableResource(R.drawable.bg);
        logoImageView = (ImageView) findViewById(R.id.logoImageView);
        logoImageView.setVisibility(View.VISIBLE);
        loginEditText = (AutoCompleteTextView) findViewById(R.id.loginEditText);
        loginEditText.addTextChangedListener(new LoginEditorListener());
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        passwordEditText.addTextChangedListener(new PasswordEditorListener());
        loginTextView = (TextView) findViewById(R.id.loginTextView);
        passwordTextView = (TextView) findViewById(R.id.passwordTextView);
        passwordDrawable = (ImageView) findViewById(R.id.passwordDrawable);
        Typeface typeface = registrationPresenter.getFont();
        loginTextView.setTypeface(typeface);
        passwordTextView.setTypeface(typeface);
        loginEditText.setTypeface(typeface);
        passwordEditText.setTypeface(typeface);
        passwordDrawable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!passwordVisible) {
                    passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                if(passwordVisible) {
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                passwordVisible = !passwordVisible;
            }
        });
        frameLayout = (FrameLayout)findViewById(R.id.registrationFrameLayout);
        frameLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                logoImageView.setVisibility(View.GONE);
                if (oldBottom == 0 || bottom == maxHeight ) {
                    maxHeight = bottom;
                    logoImageView.setVisibility(View.VISIBLE);
                }
            }
        });
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = loginEditText.getText().toString();
                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {
                        String checkEmailMessage = registrationPresenter.checkEmail(email);
                        return checkEmailMessage;
                    }

                    @Override
                    protected void onPostExecute(String checkEmailMessage) {
                        super.onPostExecute(checkEmailMessage);
                        String checkPasswordMessage = registrationPresenter.checkPassword(passwordEditText.getText().toString());
                        if (checkEmailMessage != null) {
                            loginEditText.requestFocus();
                            loginEditText.setError(checkEmailMessage);
                        } else if (checkPasswordMessage != null) {
                            passwordEditText.requestFocus();
                            passwordEditText.setError(checkPasswordMessage);
                        } else {
                            registrationPresenter.registrationButtonWasClicked(loginEditText.getText().toString(), passwordEditText.getText().toString());
                        }
                    }
                }.execute();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loginEditText.clearFocus();
        passwordEditText.clearFocus();
    }

    private class LoginEditorListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String[] emails = registrationPresenter.loginTextChanged(s);
            if (emails != null) {
                adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item,
                        emails);
                if (!adapter.isEmpty()) {
                    loginEditText.showDropDown();
                    loginEditText.setAdapter(adapter);
                }
            }
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    private class PasswordEditorListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(registrationPresenter.passwordTextChanged(s) != null)
                passwordEditText.setError(registrationPresenter.passwordTextChanged(s));

        }
        @Override
        public void afterTextChanged(Editable s) {}
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}
