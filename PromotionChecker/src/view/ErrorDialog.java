/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package view;

import exceptions.ProcessingException;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Miro
 */
public class ErrorDialog {
    
    private JFrame frame;
    
    public ErrorDialog(JFrame frame){
        this.frame = frame;
    }
    
    public void showCriticalError(String message){
        JOptionPane.showMessageDialog(frame, "Nieznany błąd. \n" + message, "Błąd", JOptionPane.ERROR_MESSAGE);
    }
    
    public void showError(ProcessingException exception){
        switch(exception.getType()){
            case ProcessingException.MULTI_LINE :
                showError(exception.getErrorList());
                return;
            case ProcessingException.STRING_TYPE :
                showError(exception.getMessage());
        }
    }
    
    private void showError(List<String> list){
        String message = Arrays.toString(list.subList(0, list.size() > 10 ? 10 : list.size()).toArray());
        message = message.substring(1, message.length() - 1);
        if (list.size() > 10) {
            message += ", ...";
        }
        if (message.length() > 150){
            message = message.replaceAll(",", ", \n");
        }
        JOptionPane.showMessageDialog(frame, "Błąd podczas wczytywania lini:\n" + message, "Wczytywanie nie powiodło się", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showError(String message){
        JOptionPane.showMessageDialog(frame, message, "Błąd podczas wczytywania pliku", JOptionPane.ERROR_MESSAGE);
    }
}
