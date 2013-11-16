/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package exceptions;

import java.util.List;

/**
 *
 * @author Miro
 */
public class ProcessingException extends RuntimeException {
    
    public final int MULTI_LINE = 1;
    public final int STRING_TYPE = 2;
    
    private List<String> errorList;
    private String message;
    private int type;
    
    public ProcessingException(List<String> errors){
        type = MULTI_LINE;
        errorList = errors;
    }
    
    public ProcessingException(String message){
        type = STRING_TYPE;
        this.message = message;
    }

    public List<String> getErrorList() {
        return errorList;
    }

    public int getType() {
        return type;
    }
}
