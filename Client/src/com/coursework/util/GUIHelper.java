package com.coursework.util;

import javafx.scene.control.TextField;

public class GUIHelper {
    public static void resetText(TextField... fields){
        for(TextField text : fields){
            text.setText("");
        }
    }
}
