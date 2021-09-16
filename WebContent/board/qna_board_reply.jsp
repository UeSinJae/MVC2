<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="vo.Dog" %>
<%
	Dog article = (Dog)request.getAttribute("article");//원글정보
String nowPage = (String)request.getAttribute("page");//원래페이지로 돌아가기 위한 페이지
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<style type="text/css">
	#writeForm{
		margin: auto;
		width:500px;
		height: 610px;
		border:1px solid red;
	}
	h2{text-align: center;}
	table{
		margin: auto;
		width:450px;
	}
	
	/*테이블의 왼쪽셀*/
	.td_left{
		width:150px;
		background: orange;
	}
	
	/*테이블의 오른쪽셀*/
	.td_right{
		width:300px;
		background:skyblue;
	}
	#commandcall{
		text-align: center;
	}
	
</style>
<body>
<section id="writeForm">
	<h2>게시판 글 등록(답변)</h2>
		<form action="boardReplyPro.bo" method="post" name="boardform">
			<!-- hidden 이유?[답변글등록]을 클릭하면 아래 2개의 값도 함께 보여줄 필요없이 전송하기 위해
			page:원래있던 페이지로 돌아가기 위해,board_num : 원글 조회하기 위해 -->
			<input type="hidden" name="page" value="nowPage">
			<input type="hidden" name="board_num" value="">
			<table>
				<tr>
					<td class="td_left"><label for="board_name">글쓴이</label></td>
					<td class="td_right">
					<input type="text" name="board_name" id="board_name">
					</td>
				</tr>
				<tr>
					<td class="td_left"><label for="board_pass">비밀번호</label></td>
					<td class="td_right">
					<input type="password" name="board_pass" id="board_pass">
					</td>
				</tr>
				<tr>
					<td class="td_left"><label for="board_subject">제목</label></td>
					<td class="td_right">
					<input type="text" name="board_subject" id="board_subject">
					</td>
				</tr>
				<tr>
					<td class="td_left"><label for="board_content">내용</label></td>
					<td class="td_right">
					<textarea name="board_content" id="board_content" cols="40" rows="15">
					</textarea>
					</td>
				</tr>
			</table>
			<section id="commandCell">
				<input type="submit" value="답변글등록">&nbsp;&nbsp; 
				<input type="reset" value="다시쓰기"> 
			</section>
		</form>
</section>
</body>
</html>















