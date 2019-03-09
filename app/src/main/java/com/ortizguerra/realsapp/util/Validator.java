package com.ortizguerra.realsapp.util;

import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Pattern;

public class Validator {

    public Validator() {
    }

    public static void setError(EditText editText, String errorString) {

        editText.setError(errorString);

    }
    public static void setError(TextView textView, String errorString) {

        textView.setError(errorString);

    }

    public static void clearError(EditText editText) {

        editText.setError(null);

    }
    public static void clearError(TextView textView) {

        textView.setError(null);

    }

    public static boolean onlyLetters(EditText editText) {
        boolean isValid=true;
        int limit=9;
        String text = editText.getText().toString();
        for (int i=0;i<=limit;i++){
            if (text.contains(String.valueOf(i))){
                isValid=false;
            }
        }
        return isValid;
    }
    public static boolean isNotEmpty(EditText editText) {
        boolean isValid=true;
        if (editText.getText().toString().isEmpty())
            isValid=false;
        return isValid;
    }
    public static boolean isSamePassword(EditText passOne, EditText passTwo){
        boolean isSame=false;
        if (passOne.getText().toString().equals(passTwo.getText().toString()))
            isSame=true;
        return isSame;

    }
    public static boolean isLessThan(EditText editText, int size){
        boolean isLessThan=false;
        if (editText.getText().toString().length()<size){
            isLessThan=true;
        }
        return isLessThan;
    }
    public static boolean isGreaterThan(EditText editText, int size){
        boolean isGreaterThan=false;
        if (editText.getText().toString().length()>size){
            isGreaterThan=true;
        }
        return isGreaterThan;
    }
    public static boolean checkEmail(EditText editText){
        final Pattern EMAIL_REGEX = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", Pattern.CASE_INSENSITIVE);
        return EMAIL_REGEX.matcher(editText.getText().toString()).matches();
    }

}
