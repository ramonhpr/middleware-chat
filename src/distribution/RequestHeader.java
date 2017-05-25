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
public class RequestHeader implements Serializable {
    private String context;
    private int requestId;
    private boolean responseExpected;
    private int objectKey;
    private String operation;

    String getOperation() {
        return operation;
    }
}