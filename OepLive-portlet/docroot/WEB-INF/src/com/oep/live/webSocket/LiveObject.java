package com.oep.live.webSocket;

import javax.websocket.Session;


public class LiveObject {
	private String userId;
	private String userName;
	private String groupName;
	private String[] teacherUserIds;
	private String[] classMateUserIds;
	private boolean firstFlag;
    private Session session;
    //0代表学生，1代表教师
    private int role;
    
    public boolean isFirstFlag() {
		return firstFlag;
	}
	public void setFirstFlag(boolean firstFlag) {
		this.firstFlag = firstFlag;
	}
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String[] getTeacherUserIds() {
		return teacherUserIds;
	}
	public void setTeacherUserIds(String[] teacherUserIds) {
		this.teacherUserIds = teacherUserIds;
	}
	public String[] getClassMateUserIds() {
		return classMateUserIds;
	}
	public void setClassMateUserIds(String[] classMateUserIds) {
		this.classMateUserIds = classMateUserIds;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
}
