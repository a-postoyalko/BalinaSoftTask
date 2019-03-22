package com.postoyalko.utilities;

import android.content.Context;

import com.postoyalko.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by aleksei on 22.03.2019.
 */

public class AccountUtils {

    private static final String[] DOMAIN_NAMES = {"gmail.com","gmail.co.uk","yahoo.com","mail.ru"};
    public static final char AT_SYMBOL = '@';
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 20;
    public static final String EMAIL_PATTERN = "^[-a-z0-9]+(?:\\.[-a-z0-9]+)*@(?:[a-z0-9]([-a-z0-9]{0,61}[a-z0-9])?\\.)*(?:co|com|edu|gov|info|net|org|[a-z][a-z])$";


    public static String[] getEmails (String enteredString) {
        String[] emails = new String[AccountUtils.DOMAIN_NAMES.length];
        String requiredString = enteredString.substring(0, enteredString.indexOf("@"));
        for (int i = 0; i < emails.length; i++) {
            emails[i] = requiredString + AT_SYMBOL + AccountUtils.DOMAIN_NAMES[i];
        }
        return emails;
    }

    public static boolean isValidEmail(String email){
        Pattern p = Pattern.compile(EMAIL_PATTERN);
        Matcher m = p.matcher(email);
        return m.matches();
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
        return context.getString(R.string.invalid_email);
    }
}
