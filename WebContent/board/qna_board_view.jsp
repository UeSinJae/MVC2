<!-- 글 하나의 상세정보를 보여주는 뷰페이지 641p 그림참조 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="vo.*" %>
<%
	Dog article = (Dog)request.getAttribute("article");
String nowPage = (String)request.getAttribute("page");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
	#articleForm{
		margin: auto;
		width : 500px;
		height : 500px;
		bordar : 1ps solid red;
	}
	h2{
		text-align: center;
	}
	#basicInfoArea{
		text-align: center;
		height: 40px;
	}
	#articleContentArea{
		text-align: center;
		margin: auto;
		overflow:auto;/*자동스크롤바생성*/
		height: 350px;
		margin-top: 20px;
		background : orange;
	}
	#commandList{/*답변,수정,삭제,목록*/
		text-align: center;
		margin: auto;
		width:500px;
	}
</style>
</head>
<body>
<section id="articleForm">
	<h2>글 내용 상세보기</h2>
	<section id="basicInfoArea">
		제목 : <%=article.getBoard_subject() %>
		첨부파일 : <%if(article.getBoard_file() != null) {%>
					<a href="board/file_down.jsp?filename=<%= %>"><%=article.getBoard_file() %></a>
				<%} %>
	</section>
	<section id="articleContentArea">
		<%=article.getBoard_content() %>
	</section>
	
	<section id ="commandList">
		<!-- [답변][삭제] 두 파라미터 값을 전송하는 이유
		1. 글 번호 : board테이블에서 원글을 조회하기 위해
			★원글을 조회하는 이유?
			원글의  그룹(board_re_ref)이나 들여쓰기(board_re_lev)와 위치(board_re_seq)를 알아야 답변글을 원글 아래에 제대로 표시할 수 있으므로
		★2. 현재 페이지 번호 : 답변글 등록을 처리한 후 원래 있던 페이지로 되돌아가야 하기 때문에 -->
		
		<a href="boardReplyForm.bo?board_num=<%=article.getBoard_num()%>& page=<%=nowPage%>">[답변]</a>
		<a href="boardModifyForm.bo?board_num=<%=article.getBoard_num()%>">[수정]</a>
		<a href="boardDeleteForm.bo?board_num=<%=article.getBoard_num()%>& page=<%=nowPage%>">[삭제]</a>
		
		<!-- 파라미터로 혀재페이지번호를 전송 -->
		<a href="boardList.bo?page=<%=nowPage%>">[목록]</a>
	</section>
	
</section>
</body>
</html>











