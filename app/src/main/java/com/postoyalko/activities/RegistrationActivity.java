package com.postoyalko.activities;

import android.graphics.Typeface;
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
import com.postoyalko.communication.AccountBody;
import com.postoyalko.communication.AccountResponse;
import com.postoyalko.communication.BalinaApiClient;
import com.postoyalko.font.AppFonts;
import com.postoyalko.utilities.AccountUtils;
import com.postoyalko.utilities.NetworkUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    private Button registrationButton;
    private ImageView logoImageView;
    private AutoCompleteTextView loginEditText;
    private EditText passwordEditText;
    private TextView loginTextView;
    private TextView passwordTextView;
    private FrameLayout frameLayout;
    private ImageView passwordDrawable;
    private ArrayAdapter<String> adapter;
    private AccountBody accountBody;
    private int maxHeight;
    private boolean passwordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
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
        accountBody = new AccountBody();
        Typeface typeface = AppFonts.getInstance(this);
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
                    if(!AccountUtils.isValidEmail(loginEditText.getText().toString())) {
                    loginEditText.requestFocus();
                    loginEditText.setError(AccountUtils.getInvalidEmailMessage(getApplicationContext(),loginEditText.getText().toString()));
                }
                else if(!AccountUtils.isValidPassword(passwordEditText.getText().toString())) {
                    passwordEditText.requestFocus();
                    passwordEditText.setError(AccountUtils.getInvalidPasswordMessage(getApplicationContext()));
                }
                else {
                    if (NetworkUtils.isNetworkAvailable(getApplicationContext())) {
                        accountBody.setLogin(loginEditText.getText().toString());
                        accountBody.setPassword(passwordEditText.getText().toString());
                        BalinaApiClient.getApi().registerAccount(accountBody).enqueue(new RegistrationCallback());
                    }
                    else {
                        Toast.makeText(getApplicationContext(), getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private class RegistrationCallback implements Callback<AccountResponse> {
        @Override
        public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
            AccountResponse accountResponse = response.body();
            String message = BalinaApiClient.LOGIN_ALREADY_USE_MESSAGE;
            if(accountResponse != null) {
                if(accountResponse.getStatus() == BalinaApiClient.SUCCESS) {
                    message = BalinaApiClient.SUCCESS_MESSAGE;
                }
            }
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
        @Override
        public void onFailure(Call<AccountResponse> call, Throwable t) {
            Toast.makeText(getApplicationContext(), BalinaApiClient.ERROR_MESSAGE, Toast.LENGTH_LONG).show();
        }
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
            int occurrences = 0;
            String enteredString = s.toString();

            for (char c : enteredString.toCharArray()) {
                if (c == AccountUtils.AT_SYMBOL) {
                    occurrences++;
                }
            }
            if (occurrences == 1) {
                adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item,
                        AccountUtils.getEmails(enteredString));
                loginEditText.showDropDown();
                loginEditText.setAdapter(adapter);
            } else if (occurrences == 0) {
                loginEditText.dismissDropDown();
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
            if (!AccountUtils.isValidPassword(s.toString())) {
                passwordEditText.setError(AccountUtils.getInvalidPasswordMessage(getApplicationContext()));
            }
        }
        @Override
        public void afterTextChanged(Editable s) {}
    }

}
