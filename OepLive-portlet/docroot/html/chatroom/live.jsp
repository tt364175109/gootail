<%@page import="com.oep.live.StringUtils"%>
<%@page import="com.oep.schoolfuction.model.PlanOrganization"%>
<%@page import="com.oep.schoolfuction.service.PlanOrganizationLocalServiceUtil"%>
<%@page import="com.liferay.portal.model.Organization"%>
<%@page import="java.util.List"%>
<%@page import="com.liferay.portal.service.OrganizationLocalServiceUtil"%>
<%@page import="com.liferay.portal.service.UserLocalServiceUtil"%>
<%@page import="com.liferay.portal.model.User"%>
<%@page import="org.json.JSONObject"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@page import="com.liferay.portal.util.PortalUtil"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<portlet:defineObjects />
<%
	long planId = ParamUtil.getLong(request, "planId");
	User u = PortalUtil.getUser(request);
	List<PlanOrganization> planOrgs = PlanOrganizationLocalServiceUtil.getPlanOrgsByPlanId(planId);
	JSONObject jb = new JSONObject();
	jb.put("role", 1);
	jb.put("userId", u.getUserId()+"");
	jb.put("userName", u.getFirstName());
	
	long userOrgId = 0;
	String groupName = "";
	List<Organization> orgs = OrganizationLocalServiceUtil.getUserOrganizations(u.getUserId());
	for(Organization org : orgs){
		for(PlanOrganization planOrg : planOrgs){
			if(org.getOrganizationId() == planOrg.getOrgId()){
				userOrgId = planOrg.getOrgId();
				groupName = org.getName();
			}
		}
	}
	jb.put("groupName", groupName);
	if(userOrgId != 0){
		long[] userIds = UserLocalServiceUtil.getOrganizationUserIds(userOrgId);
		jb.put("classMateUserIds", StringUtils.arrayToString(userIds));
	}
	jb.put("teacherUserIds", "");
	pageContext.setAttribute("jb", jb.toString());
%>

<style>
textarea:focus {
    outline: none !important;
    border-color: #ddd !important;
    box-shadow:none !important;
    webkit-box-shadow:none !important;
    moz-box-shadow:none !important;
}
textarea{
	resize: none;
}
.chat-main{
	width: 100%;
	height: 100%;
}
.chatPanel-l{
	width: calc(100% - 402px);
	height: 100%;
	float: left;
}
.chatPanel-r{
	width: 400px;
	height: 100%;
	float: right;
	border: 1px solid #c3bbbb33;
}
.outputmsg{
	width: 100%;
	height: calc(100% - 62px);
	background: #fbfbfb6b;
}
.layui-tab-item{
	width: calc(100% - 40px);
	height: 100%;
	padding: 0px 20px 0px 20px;
	word-break:break-all;
}
.inputmsg{
	width: 100%;
	height: 60px;
}
.box{
    overflow-y: scroll;
    overflow-x: hidden;
}
.scrollbar{
}

/*滚动条整体样式*/
.box::-webkit-scrollbar {
    width: 4px;
    height: 1px;
}
/*滚动条滑块*/
.box::-webkit-scrollbar-thumb {
    border-radius: 4px;
    -webkit-box-shadow: inset 0 0 5px rgba(0,0,0,0.2);
    background: #535353;
}
/*滚动条轨道*/
.box::-webkit-scrollbar-track {
    -webkit-box-shadow: inset 0 0 1px rgba(0,0,0,0);
    border-radius: 4px;
    background: #ccc;
}
#inputMessage{
	width: calc(100% - 134px);
	height: calc(100% - 10px);
}
#sendBtn{
	width: 120px;
	height: 60px;
	float: right;
	background: #FF6600 !important;
}
.layui-tab-title li{
	width: 165px !important;
	line-height: 40px;
}
.layui-tab{
	height: 100%;
	margin: 0px !important;
}
.layui-tab-content{
	padding: 0px;
	height: calc(100% - 40px) !important;
}
#userSpan{
	color: #2b94ff;
}
#tm{
	color: #b6da5e;
}

.users{
	margin: 0px !important;
	padding: 5px 20px 5px 20px !important;
}
.user{
	line-height: 50px !important;
	text-align: left;
}
.uon{
	color: red;
}
.uoff{
	color: gray;
}
.un{
	color: #2b94ff;
}
</style>
<portlet:defineObjects />
	<div class="chat-main">
		<div class="chatPanel-l">
			<video width="100%" height="100%" controls>
			    <source src="movie.mp4" type="video/mp4">
			    <source src="movie.ogg" type="video/ogg">
			    	您的浏览器不支持 video 标签。
			</video>
		</div>
		<div class="chatPanel-r">
			<div class="outputmsg">
				<div class="layui-tab layui-tab-brief" lay-filter="docDemoTabBrief">
				  <ul class="layui-tab-title" style="margin: 0px">
				    <li class="layui-this">聊天信息</li>
				    <li>班级信息</li>
				  </ul>
				  <div class="layui-tab-content">
				    <div class="layui-tab-item layui-show box" id="chatPanel">
				    	<div class="scrollbar"></div>
				    </div>
				    <div class="layui-tab-item box">
				    	<div class="scrollbar"></div>
				    	<ul class="users">
				    		<li class="user"><span class="uon">在线:</span><span class="un">杨丹</span></li>
				    		<li class="user"><span class="uoff">离线:</span><span class="un">杨丹</span></li>
				    		<li class="user"><span class="uoff">离线:</span><span class="un">杨丹</span></li>
				    		<li class="user"><span class="uoff">离线:</span><span class="un">杨丹</span></li>
				    		<li class="user"><span class="uoff">离线:</span><span class="un">杨丹</span></li>
				    		<li class="user"><span class="uoff">离线:</span><span class="un">杨丹</span></li>
				    		<li class="user"><span class="uoff">离线:</span><span class="un">杨丹</span></li>
				    		<li class="user"><span class="uoff">离线:</span><span class="un">杨丹</span></li>
				    	</ul>
				    </div>
				  </div>
				</div>
			</div>
			<div class="inputmsg">
				<textarea id="inputMessage" rows="" cols=""></textarea>
				<input id="sendBtn" type="button" value="发送消息" onclick="getMessage()" />
			</div>
		</div>
	</div>
	<script>
	 var $;
	 var layer;
	layui.use(['element','layer'], function(){
	  $ = layui.jquery;
	  var element = layui.element; 
	  layer = layui.layer;
	});
	</script>
    <script type="text/javascript">
    	var outputMessage = document.getElementById("chatPanel");
        // websocket实现聊天,客户端需要做的事情,总结起来,其实就是如下几件
        // 获取连接  new WebSocket() (获取服务端连接)    
        //服务端地址和请求类型
        var wsUrl = "ws://localhost:8080/OepLive-portlet/olwsServer";
        //客户端与服务端建立连接 ，建立连接以后，他会出发一个ws.onopen事件
        var ws = new WebSocket(wsUrl);

        //连接成功后 ，提示浏览器客户端输入昵称
        ws.onopen = function() {
            ws.send('${jb }');
        }

        //客户端收到服务器发送的消息 
        ws.onmessage = function(message) {
            //alert(message.data);
            //message.data
            //获取以后，在客户端显示
            outputMessage.innerHTML = outputMessage.innerHTML + message.data;
            var divscll = document.getElementById('chatPanel');
            divscll.scrollTop = divscll.scrollHeight;
        }

        //获取某个用户输入的聊天内容，并发送服务端，让服务端广播给所有人
        function getMessage() {
            var inputMessage = $("#inputMessage").val();
            console.log("填写的消息："+inputMessage);
            if (typeof (inputMessage) == "undefined" || inputMessage == "") {
            	layer.msg("请输入您要发送的消息");
            } else {
                ws.send(inputMessage);
                $("#inputMessage").val("");
            }

        }

        //当关闭页面或者用户退出时,会执行一个ws.close()方法 
        window.onbeforeunload = function() {
            ws.close();//该方法会触发后台的标注了OnClose的方法
        }

        //按回车发送消息
        document.onkeyup = function(e) {
            if (e.keyCode == 13) {
                getMessage();
            }
        }
    </script>


