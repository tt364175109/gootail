package com.oep.live.webSocket;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/wsServer")
public class WebSocketServer {
	private boolean firstFlag = true;
    private Session session;
    private String userName;
    // 记录此次聊天室的服务端有多少个连接:有用户发送消息，需要把所有连接都开通，把此消息发送给其他所有在线的用户
    // key代表此次客户端的session，value代表此次连接对象
    private static final HashMap<String, Object> connectMap = new HashMap<String, Object>();
    // 保存所有的用户昵称信息
    // key是sessionId，value：用户名
    private static final HashMap<String, String> userMap = new HashMap<String, String>();

    // 服务端收到客户端连接请求，连接成功后会执行此方法
    @OnOpen
    public void start(Session session) {
        // 保存好当前的连接
        this.session = session;
        // 把当前连接放入connectMap中
        connectMap.put(session.getId(), this);
    }

    // 接收客户端发送的消息
    @OnMessage
    public void chat(String clientMessage, Session session) {

    	WebSocketServer client = null;

        // 判断客户端是不是第一次传值
        if (firstFlag) {
            this.userName = clientMessage;
            // 将新进来的用户保存到用户map中：
            userMap.put(session.getId(), userName);
            // 往客户端发送消息：构造发送给客户端的提示信息
            String message = htmlMessage("系统消息", userName + "进入了直播间");

            // 将消息广播给所有的用户（所有的用户都在userMap中）
            for (String connectKey : connectMap.keySet()) {
                client = (WebSocketServer) connectMap.get(connectKey);
                // 给对应的web端发送一个文本信息message
                try {
                    client.session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 输入昵称以后，就代表firstFlag = false;
            firstFlag = false;
        } else {
            // 往客户端发送消息：构造发送给客户端的提示信息
            String message = htmlMessage(userMap.get(session.getId()), clientMessage);
            // 将消息广播给所有的用户（所有的用户都在userMap中）
            for (String connectKey : connectMap.keySet()) {
                client = (WebSocketServer) connectMap.get(connectKey);
                // 给对应的web端发送一个文本信息message
                try {
                    client.session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // ws.clos事件,会触发后台的标注OnClose的方法
    @OnClose
    public void close(Session session) {
        // 当某个用户退出时,对其他用户进行广播
        String message = htmlMessage("系统消息", userMap.get(session.getId()) + "退出了直播间");
        userMap.remove(session.getId());
        connectMap.remove(session.getId());
        WebSocketServer client = null;
        // 将消息广播给所有的用户
        for (String connectKey : connectMap.keySet()) {
            client = (WebSocketServer) connectMap.get(connectKey);
            // 给对应的web短发送一个文本信息（message）
            try {
                client.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public String htmlMessage(String userName, String message) {
        StringBuffer messageBuffer = new StringBuffer();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
		messageBuffer.append("<p id='tm'>"+sf.format(new Date())+"</p>");
		messageBuffer.append("<p><span id='userSpan'>"+userName+":</span>&nbsp;&nbsp;"+message+"</p>");
       /* messageBuffer.append("<div class='record_item'>");
        messageBuffer.append("<p class='record_item_time'>");
        messageBuffer.append("<span>" + sf.format(new Date()) + "</span>");
        messageBuffer.append("</p>");
        messageBuffer.append("<div class='record_item_txt'>");
        messageBuffer.append("<span class='avatar'>" + userName + "</span>");
        messageBuffer.append("<p>");
        messageBuffer.append("<span class='txt'>" + message + "</span>");
        messageBuffer.append("</p>");
        messageBuffer.append("</div>");
        messageBuffer.append("</div>");*/
        return messageBuffer.toString();
    }

}
