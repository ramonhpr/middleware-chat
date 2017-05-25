package distribution;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {
	private MessageHeader header;
	private MessageBody body;

    Message(MessageHeader messageHeader, MessageBody messageBody) {
        header = messageHeader;
        body = messageBody;
    }

    Message() {
        header = null;
        body = null;
    }

    MessageBody getBody() {
        return body;
    }
}