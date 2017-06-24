package distribution;

public class ChatImpl implements IChat{
	private String chat;

	@Override
	public void append(String msg) throws Throwable {
		// TODO Auto-generated method stub
		chat += msg;
	}

}
