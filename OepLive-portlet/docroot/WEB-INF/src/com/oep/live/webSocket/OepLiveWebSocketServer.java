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

import org.json.JSONObject;

@ServerEndpoint("/olwsServer")
public class OepLiveWebSocketServer {
    private static final HashMap<String, Object> connectMap = new HashMap<String, Object>();
   private static final HashMap<String, String> userMap = new HashMap<String, String>();

    // 服务端收到客户端连接请求，连接成功后会执行此方法
    @OnOpen
    public void start(Session session) {
    	LiveObject lo = new LiveObject();
    	lo.setSession(session);
    	lo.setFirstFlag(true);
        connectMap.put(session.getId(), lo);
    }

    // 接收客户端发送的消息
    @OnMessage
    public void chat(String clientMessage, Session session) {

    	LiveObject client = (LiveObject)connectMap.get(session.getId());

        // 判断客户端是不是第一次传值
        if (client.isFirstFlag()) {
        	JSONObject jbol = new JSONObject(clientMessage);
        	client.setUserName(jbol.getString("userName"));
        	client.setUserId(jbol.getString("userId"));
        	client.setGroupName(jbol.getString("groupName"));
        	client.setRole(jbol.getInt("role"));
        	client.setClassMateUserIds(jbol.getString("classMateUserIds").split(","));
        	System.out.println("同班同学userIds："+jbol.getString("classMateUserIds"));
        	System.out.println("同班同学userIds："+jbol.getString("teacherUserIds"));
        	client.setTeacherUserIds(jbol.getString("teacherUserIds").split(","));
            userMap.put(client.getUserId(), session.getId());
            String message = htmlMessage("系统消息", client.getUserName() + "进入了直播间");

            for(String cmUserId : client.getClassMateUserIds()){
            	String cmSessionId = userMap.get(cmUserId);
            	LiveObject cmlo = (LiveObject)connectMap.get(cmSessionId);
            	try {
	            	if(cmlo!=null){
	            		cmlo.getSession().getBasicRemote().sendText(message);
	            	}
            	} catch (IOException e) {
                    e.printStackTrace();
                }
            }
            for(String tUserId : client.getTeacherUserIds()){
            	String tSessionId = userMap.get(tUserId);
            	LiveObject tlo = (LiveObject)connectMap.get(tSessionId);
            	try {
            		if(tlo!=null){
            			tlo.getSession().getBasicRemote().sendText(message);
            		}
            	} catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            client.setFirstFlag(false);
        } else {
            String message = htmlMessage(client.getUserName(), clientMessage);
            for(String cmUserId : client.getClassMateUserIds()){
            	String cmSessionId = userMap.get(cmUserId);
            	LiveObject cmlo = (LiveObject)connectMap.get(cmSessionId);
            	try {
            		if(cmlo!=null){
            			cmlo.getSession().getBasicRemote().sendText(message);
            		}
            	} catch (IOException e) {
                    e.printStackTrace();
                }
            }
            for(String tUserId : client.getTeacherUserIds()){
            	String tSessionId = userMap.get(tUserId);
            	LiveObject tlo = (LiveObject)connectMap.get(tSessionId);
            	try {
            		if(tlo!=null){
            			tlo.getSession().getBasicRemote().sendText(message);
            		}
            	} catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // ws.clos事件,会触发后台的标注OnClose的方法
    @OnClose
    public void close(Session session) {
    	LiveObject client = (LiveObject)connectMap.get(session.getId());
    	
        String message = htmlMessage("系统消息", client.getUserName() + "退出了直播间");
        userMap.remove(client.getUserId());
        connectMap.remove(session.getId());
        
        for(String cmUserId : client.getClassMateUserIds()){
        	String cmSessionId = userMap.get(cmUserId);
        	LiveObject cmlo = (LiveObject)connectMap.get(cmSessionId);
        	try {
        		if(cmlo!=null)
        			cmlo.getSession().getBasicRemote().sendText(message);
        	} catch (IOException e) {
                e.printStackTrace();
            }
        }
        for(String tUserId : client.getTeacherUserIds()){
        	String tSessionId = userMap.get(tUserId);
        	LiveObject tlo = (LiveObject)connectMap.get(tSessionId);
        	try {
        		if(tlo!=null)
        			tlo.getSession().getBasicRemote().sendText(message);
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
