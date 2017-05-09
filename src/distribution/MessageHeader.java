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
public class MessageHeader implements Serializable {
    private String magic;
    private int version;
    private boolean byteOrder;
    private int messageType;
    private long messageSize;

    MessageHeader(String miop, int i, boolean b, int i0, int i1) {
        this.magic = miop;
        version = i;
        byteOrder = b;
        messageType = i0;
        messageSize = i1;
    }
}