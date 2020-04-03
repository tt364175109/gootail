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
    // ��¼�˴������ҵķ�����ж��ٸ�����:���û�������Ϣ����Ҫ���������Ӷ���ͨ���Ѵ���Ϣ���͸������������ߵ��û�
    // key����˴οͻ��˵�session��value����˴����Ӷ���
    private static final HashMap<String, Object> connectMap = new HashMap<String, Object>();
    // �������е��û��ǳ���Ϣ
    // key��sessionId��value���û���
    private static final HashMap<String, String> userMap = new HashMap<String, String>();

    // ������յ��ͻ��������������ӳɹ����ִ�д˷���
    @OnOpen
    public void start(Session session) {
        // ����õ�ǰ������
        this.session = session;
        // �ѵ�ǰ���ӷ���connectMap��
        connectMap.put(session.getId(), this);
    }

    // ���տͻ��˷��͵���Ϣ
    @OnMessage
    public void chat(String clientMessage, Session session) {

    	WebSocketServer client = null;

        // �жϿͻ����ǲ��ǵ�һ�δ�ֵ
        if (firstFlag) {
            this.userName = clientMessage;
            // ���½������û����浽�û�map�У�
            userMap.put(session.getId(), userName);
            // ���ͻ��˷�����Ϣ�����췢�͸��ͻ��˵���ʾ��Ϣ
            String message = htmlMessage("ϵͳ��Ϣ", userName + "������ֱ����");

            // ����Ϣ�㲥�����е��û������е��û�����userMap�У�
            for (String connectKey : connectMap.keySet()) {
                client = (WebSocketServer) connectMap.get(connectKey);
                // ����Ӧ��web�˷���һ���ı���Ϣmessage
                try {
                    client.session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // �����ǳ��Ժ󣬾ʹ���firstFlag = false;
            firstFlag = false;
        } else {
            // ���ͻ��˷�����Ϣ�����췢�͸��ͻ��˵���ʾ��Ϣ
            String message = htmlMessage(userMap.get(session.getId()), clientMessage);
            // ����Ϣ�㲥�����е��û������е��û�����userMap�У�
            for (String connectKey : connectMap.keySet()) {
                client = (WebSocketServer) connectMap.get(connectKey);
                // ����Ӧ��web�˷���һ���ı���Ϣmessage
                try {
                    client.session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // ws.clos�¼�,�ᴥ����̨�ı�עOnClose�ķ���
    @OnClose
    public void close(Session session) {
        // ��ĳ���û��˳�ʱ,�������û����й㲥
        String message = htmlMessage("ϵͳ��Ϣ", userMap.get(session.getId()) + "�˳���ֱ����");
        userMap.remove(session.getId());
        connectMap.remove(session.getId());
        WebSocketServer client = null;
        // ����Ϣ�㲥�����е��û�
        for (String connectKey : connectMap.keySet()) {
            client = (WebSocketServer) connectMap.get(connectKey);
            // ����Ӧ��web�̷���һ���ı���Ϣ��message��
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
