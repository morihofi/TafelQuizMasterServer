package de.morihofi.tafelquizserver;

import io.undertow.Undertow;

import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.websockets.WebSocketConnectionCallback;
import io.undertow.websockets.core.AbstractReceiveListener;
import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;
import io.undertow.websockets.spi.WebSocketHttpExchange;

import static io.undertow.Handlers.path;
import static io.undertow.Handlers.resource;
import static io.undertow.Handlers.websocket;

import java.time.Instant;
import java.util.Enumeration;
import java.util.Properties;

import org.json.JSONObject;

public class Main {
	
	
	public static int port = 5559;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("[INFO] [SERVER] Starting up...");
		System.out.print("[INFO] [DATABASE] Loading H2 Driver...");
        try {
			Class.forName("org.h2.Driver");
			System.out.print(" Success\n");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.print(" Failed\n");
			//e.printStackTrace();
		}
        
        
        System.out.println("[INFO] [SERVER] Trying to start server on port " + port + "...");
        Undertow server = Undertow.builder()
                .addHttpListener(port, "0.0.0.0")
                .setHandler(path()
                        .addPrefixPath("/quizws", websocket(new WebSocketConnectionCallback() {

                            @Override
                            public void onConnect(WebSocketHttpExchange exchange, WebSocketChannel channel) {
                                channel.getReceiveSetter().set(new AbstractReceiveListener() {

                                    @Override
                                    protected void onFullTextMessage(WebSocketChannel channel, BufferedTextMessage message) {
                                        final String messageData = message.getData();
                                        for (WebSocketChannel session : channel.getPeerConnections()) {
                                        	
                                        	
                                        	/*
                                           if (session == channel) {
                                        	   //Nichts senden, da der Absender seine Nachricht sonst wieder zugeschickt bekommt
                                        	   
                                           } else {
                                        	   
	                                        	    
                             			         WebSockets.sendText(messageData, session, null);
                             			        
                             			        
                                        	  
                                           }
                                           
                                           */
                              		       	
                              		      WebSockets.sendText(messageData, session, null);
                                        }
                                    }
                                });
                                channel.resumeReceives();
                            }

                        }))
                        .addPrefixPath("/", resource(new ClassPathResourceManager(Main.class.getClassLoader(), Main.class.getPackage()))
                                .addWelcomeFiles("index.html")))
                .build();
        System.out.println("[INFO] [SERVER] Server is started at port " + port);
        server.start();
	}

}
