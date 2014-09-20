package core;

import java.net.UnknownHostException;

public class Updateview extends Database{
	
	public void startWebsocket() throws UnknownHostException{ 
		super.startWebsocket("9000");
	}
}
