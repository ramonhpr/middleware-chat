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
public class MessageBody implements Serializable {
    private RequestHeader requestHeader;
    private RequestBody requestBody;
    private ReplyHeader replyHeader;
    private ReplyBody replyBody;

    MessageBody(RequestHeader requestHeader, RequestBody requestBody, Object object, Object object0) {
        this.replyBody = replyBody;
        this.replyHeader = replyHeader;
        this.requestBody = requestBody;
        this.requestHeader = this.requestHeader;
    }

    ReplyBody getReplyBody() {
        return replyBody;
    }

    RequestHeader getRequestHeader() {
        return requestHeader;
    }
}