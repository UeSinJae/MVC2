<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="vo.Dog" %>

<%
	Dog article = (Dog)request.getAttribute("article");
%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<style type="text/css">
	#registForm{
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
<section id = "registForm">
	<h2>게시판 글 등록</h2>
	<!-- [수정]버튼을 클릭 시 boardModifyPro.bo를 요청하여 프론트컨트롤러로 이동 -->
	<form action="boardModifyPro" method="post" enctype="multipart/form-data" name="boardform">
		<table>                                   
			<tr>
				<td class="td_left"><label for="board_name">글쓴이</label></td>
				<td class="td_right"><input type="text" name="board_name" id="board_name" value="<%=article.getBoard_name()%>"></td>
			</tr>
			<tr>
				<td class="td_left"><label for="board_pass">비밀번호</label></td>
				<td class="td_right"><input type="password" name="board_pass" id="board_pass" size="21" required="required"></td>
			</tr>
			<tr>
				<td class="td_left"><label for="board_subject">제목</label></td>
				<td class="td_right"><input type="text" name="board_subject" id="board_subject"  value="<%=article.getBoard_subject()%>"></td>
			</tr>
			<tr>
				<td class="td_left"><label for="board_content">내용</label></td>
				<td class="td_right"><textarea name="board_content" id="board_content" cols="40" rows="15" value="<%=article.getBoard_content()%>"></textarea></td>
			</tr>
		</table>
	<section id="commandCell"> 
		<a href="javaescript:modifyboard()">[수정]</a>%nbsp;%nbsp;
		<a href="javaescript:history.back()">[뒤로]</a>
	</section>		
	</form>
</section>

</body>
</html>