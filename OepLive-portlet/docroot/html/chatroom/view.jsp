<%@page import="com.liferay.portal.util.PortalUtil"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<portlet:defineObjects />
<portlet:renderURL var="liveUrl"  windowState="pop_up">
	<portlet:param name="jspPage" value="/html/chatroom/live.jsp"/>
</portlet:renderURL>

<input type="button" value="进入直播间" id="toLive" >

<!-- 后面移走 -->
<script type="text/javascript">
layui.use(['element','layer'], function(){
  var $ = layui.jquery
  ,element = layui.element;
  var layer = layui.layer;
  $('#toLive').click(function(){
	  layer.open({
		  type: 2,
		  title: '云迪直播',
		  shadeClose: true,
		  shade: 0.8,
		  area: ['100%', '100%'],
		  content: '<%=liveUrl %>&<portlet:namespace/>planId=14918' //iframe的url
	  });
  })
  
});
</script>