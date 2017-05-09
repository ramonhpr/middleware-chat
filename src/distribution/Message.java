package distribution;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {
	private MessageHeader header;
	private Message body;
	
	public class MessageHeader implements Serializable {
		private String magic;
		private int version;
		private boolean byteOrder;
		private int messageType;
		private long messageSize;
	}
	
	public class MessageBody implements Serializable {
		private RequestHeader requestHeader;
		private RequestBody requestBody;
		private ReplyHeader replyHeader;
		private ReplyBody replyBody;
	}
	
	public class RequestHeader implements Serializable {
		private String context;
		private int requestId;
		private boolean responseExpected;
		private int objectKey;
		private String operation;
	}
	
	public class RequestBody implements Serializable {
		private ArrayList<Object> parameters = new ArrayList<Object>();		
	}
	
	public class ReplyHeader implements Serializable {
		private String serviceContext;
		private int requestId;
		private int replyStatus;
	}
	
	public class ReplyBody implements Serializable {
		private Object operationResult;
	}
}
