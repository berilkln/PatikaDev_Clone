package com.patikadev.Helper;

import javax.swing.*;
import java.awt.*;

public class Helper {


    public static int screenCenterPoint(String axis, Dimension size){
        int point;
        switch (axis) {
            case "x":
                point =(Toolkit.getDefaultToolkit().getScreenSize().width - size.width) /2;
                break;
            case "y":
                point =(Toolkit.getDefaultToolkit().getScreenSize().height - size.height) /2;
                break;
            default:
                point=0;

        }
        return point;
    }


    public static boolean isFieldEmpty(JTextField field){
        return field.getText().trim().isEmpty();

    }
    public static void showMessage(String str){  // Message Fields
        optionPageTR();
        String msg;
        String title;

        switch (str){
            case "fill":
                msg= "Please fill in all fields!";
                title = "Error Message";
                break;
            case "done":
                msg = "Transaction Successful!";
                title = "Sonuç";
                break;
            case "error":
                msg = "The Transaction Failed";
                title = "Hata";
                break;
            default:
                msg = str;
                title = "Message";
        }
        JOptionPane.showMessageDialog(null,msg, title, JOptionPane.INFORMATION_MESSAGE);

    }
    public static void optionPageTR(){
        UIManager.put("OptionPane.okButtonText","Ok");
    }

    
}

