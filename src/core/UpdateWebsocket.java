package core;


import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketAdapter;
import org.java_websocket.WebSocketFactory;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.framing.FrameBuilder;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class UpdateWebsocket extends WebSocketServer {
	private static int counter = 0;
	private static int portnumber = 9000;
	private static LinkedList<WebSocket> linkedWS = new LinkedList<WebSocket>(); 
	private volatile static UpdateWebsocket singletonUV = null;

	public static UpdateWebsocket getInstance(int port) throws UnknownHostException{
		portnumber = port;
		if(singletonUV == null){
			synchronized (UpdateWebsocket.class) {
				if(singletonUV == null){
					singletonUV = new UpdateWebsocket(port, new Draft_17());
					singletonUV.start();
				}
			}
		}
		return singletonUV;
	}

	private UpdateWebsocket( int port , Draft d ) throws UnknownHostException {
		super( new InetSocketAddress( port ), Collections.singletonList( d ) );
	}

	private UpdateWebsocket( InetSocketAddress address, Draft d ) {
		super( address, Collections.singletonList( d ) );
	}

	public void sendMessage(String message){
		for(WebSocket ws : linkedWS){
			ws.send(message);
		}
	}
	
	@Override
	public void onOpen( WebSocket conn, ClientHandshake handshake ) {
		counter++;
		linkedWS.add(conn);
		System.out.println( "///////////Opened connection number" + counter );
	}

	@Override
	public void onClose( WebSocket conn, int code, String reason, boolean remote ) {
		linkedWS.remove(conn);
		System.out.println( "closed" );
	}

	@Override
	public void onError( WebSocket conn, Exception ex ) {
		linkedWS.remove(conn);
		System.out.println( "Error:" );
		ex.printStackTrace();
	}
	
	@Override
	public void onMessage( WebSocket conn, String message ) {
		for(WebSocket ws : linkedWS){
			ws.send(message);
		}
//		conn.send( message );
	}

	@Override
	public void onMessage( WebSocket conn, ByteBuffer blob ) {
		for(WebSocket ws : linkedWS){
			ws.send(blob);
		}
//		conn.send( blob );
	}

	public void onWebsocketMessageFragment( WebSocket conn, Framedata frame ) {
		FrameBuilder builder = (FrameBuilder) frame;
		builder.setTransferemasked( false );
		conn.sendFrame( frame );
	}
}
