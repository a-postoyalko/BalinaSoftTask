package com.postoyalko.utilities;

import android.content.Context;
import android.util.Patterns;

import com.postoyalko.R;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by aleksei on 22.03.2019.
 */

public class AccountUtils {

    private static final String[] DOMAIN_NAMES = {"gmail.com","gmail.co.uk","yahoo.com","mail.ru"};
    public static final char AT_SYMBOL = '@';
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 20;
    public static final int MIN_LOGIN_LENGTH = 4;
    public static final int MAX_LOGIN_LENGTH = 32;

    public static String[] getEmails (String enteredString) {
        String[] emails = new String[AccountUtils.DOMAIN_NAMES.length];
        String requiredString = enteredString.substring(0, enteredString.indexOf(AT_SYMBOL));
        for (int i = 0; i < emails.length; i++) {
            emails[i] = requiredString + AT_SYMBOL + AccountUtils.DOMAIN_NAMES[i];
        }
        return emails;
    }

    public static boolean isValidEmail(String email){
        if (email.length() <= MIN_LOGIN_LENGTH || email.length() >= MAX_LOGIN_LENGTH )
            return false;
        String domain;
        if (email.indexOf(AT_SYMBOL) >= 0) {
            domain = email.substring(email.indexOf(AT_SYMBOL) + 1, email.length());
            Patterns.DOMAIN_NAME.matcher(domain).matches();
            try {
                InetAddress domainAddress = InetAddress.getByName(domain);
                return Patterns.DOMAIN_NAME.matcher(domain).matches() && Patterns.EMAIL_ADDRESS.matcher(email).matches();
            } catch (UnknownHostException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public static boolean isValidPassword(String password) {
        return MIN_PASSWORD_LENGTH <= password.length() && password.length() <= MAX_PASSWORD_LENGTH;
    }

    public static String getInvalidPasswordMessage(Context context) {
        return context.getString(R.string.invalid_password, MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH);
    }

    public static String getInvalidEmailMessage(Context context, String email) {
        if (email.length() == 0 ||email.charAt(0) == AT_SYMBOL)
            return context.getString(R.string.empty_email);
        if (email.length() <= MIN_LOGIN_LENGTH || email.length() >= MAX_LOGIN_LENGTH )
            return context.getString(R.string.login_lenght, MIN_LOGIN_LENGTH, MAX_LOGIN_LENGTH);
        return context.getString(R.string.invalid_email);
    }
}
