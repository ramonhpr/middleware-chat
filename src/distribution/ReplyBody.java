/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distribution;

import java.io.Serializable;

/**
 *
 * @author rhpr
 */
public class ReplyBody implements Serializable {
    private Object operationResult;
    
    Object getOperationResult() {
        return operationResult;
    }
}