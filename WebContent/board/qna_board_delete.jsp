<!-- [삭제] 요청 시 글의 비밀번호를 입력하는 화면을 보여주는 뷰페이지 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
//삭제 후 다시 원래 페이지로 돌아와서 삭제되었는지 확인
String nowPage = (String)request.getAttribute("page");
int board_num = (Integer)request.getAttribute("board_num");
%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<style>
	#passForm{
		margin: auto;
		width: 400px;
		bordar: 1px solid orange;
	}
</style>
<body>
<section id ="passForm">
	<form action="boardDeletePro.bo?board_num=<%=board_num%>" method="post" name="deleteForm">
		<input type = "hidden" name="page" value="<%=nowPage%>">
		<table>
			<tr align="center">
				<td>글 비밀번호</td>
				<td><input type="password" name="board_paass"></td>
			</tr>
			<tr align="center">
				<td><input type="submit" value="삭제"></td> &nbsp;&nbsp;
				<td><input type="button" value="돌아가기" onclick="javascript:histoy.go(-1)"></td>
			</tr>
		</table>
	
	</form>
</section>
</body>
</html>