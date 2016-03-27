package com.socket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.servlet.TransferServlet;
import com.transfer.Transfer;




@ServerEndpoint("/websocket")
public class MyWebSocket {

	private static int onlineCount = 0;

	private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<MyWebSocket>();

	private Session session;

	@OnOpen
	public void onOpen(Session session){
		this.session = session;
		webSocketSet.add(this);
		addOnlineCount();           //在线数加1
		System.out.println("有新连接加入!当前在线人数为" + getOnlineCount());
	}

	@OnClose
	public void onClose(){
		webSocketSet.remove(this);  //从set中删除
		webSocketSet.remove("");
		subOnlineCount();           //在线数减1   
		System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println("来自客户端的消息:" + message);

		//将数据变成json格式
//		String message1 = TransferServlet.data + ",\"Message\":" + message + "}]";
				
		String s = TransferServlet.data + ","+ message;
		
		//群发消息
		for(MyWebSocket item: webSocketSet){             
			try {
				//将List发送给客户端
				item.sendMessage(s);
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
		}
	}

	@OnError
	public void onError(Session session, Throwable error){
		System.out.println("发生错误");
		error.printStackTrace();
	}

	public void sendMessage(String data) throws IOException{
		this.session.getBasicRemote().sendText(data);;
	}

	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	public static synchronized void addOnlineCount() {
		MyWebSocket.onlineCount++;
	}

	public static synchronized void subOnlineCount() {
		MyWebSocket.onlineCount--;
	}

}