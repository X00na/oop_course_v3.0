package com.coursework.util;

import javafx.scene.control.TextField;

public class Checker {
    public static boolean checkString(TextField text){
        if(!validateString(text.getText())){
            text.setStyle("-fx-prompt-text-fill: black; -fx-background-color: rgba(255, 0, 0, 0.4);");
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkInt(TextField text){
        if(!validateInt(text.getText())){
            text.setStyle("-fx-text-inner-color: red;");
            return false;
        }

        return true;
    }

    public static boolean validateString(String s){
        return s != null && !s.isEmpty();
    }

    public static boolean validateInt(String s){
        try {
            Integer.parseInt(s);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}
