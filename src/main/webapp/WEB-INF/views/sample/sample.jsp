<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>Sample!</h1>
	<p> The time on the WAS server is ${serverTime}.</p>
	<p> The time on the DB server is ${sampleNow}.</p>
</body>
<script type="text/javascript">

/*
	const bodyEl = document.body;
	let bodyStart = null;
	let bodyEnd = null;
	let bodyHtml = null
	let time = 0;
	let timer = setInterval(() => {
			time += 1000;
			fetch("http://localhost:8080/sample")
			.then((resp) => resp.text())
			.then((result) => {
				bodyStart = result.indexOf("<body>")+"<body>".length;
				bodyEnd = result.indexOf("</body>");
				bodyHtml = result.substring(bodyStart,bodyEnd);
				bodyEl.innerHTML = bodyHtml;
			});
			
			if(time > 1000000){
				clearInterval(timer);
			}
		
	}, 1000);
*/
</script>
</html>