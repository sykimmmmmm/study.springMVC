<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!-- 공통변수 처리 -->
<c:set var="CONTEXT_PATH" value="${pageContext.request.contextPath}" scope="application"/>
<c:set var="RESOURCES_PATH" value="${CONTEXT_PATH}/resources" scope="application"/>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="description" content="">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
	<script type="text/javascript">
		var CONTEXT_PATH = "${CONTEXT_PATH}";
		var RESOURCES_PATH = "${RESOURCES_PATH}";
	</script>
	<link rel="stylesheet" href="${RESOURCES_PATH}/css/common.css">
    <title><tiles:insertAttribute name="title" /></title>
  </head>
  
  <body>
  	<div class='wrap'>
  		<tiles:insertAttribute name="header" />
		  <div class='content'>  	
  			<tiles:insertAttribute name="left"/>
	  		<div class="page_content">
	  			<tiles:insertAttribute name="body"/>
	  		</div>
  		  </div>
  		<tiles:insertAttribute name="foot" />
  	</div>
  </body>
</html>