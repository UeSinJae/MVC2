<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MVC2 게시판</title>
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
</head>
<body>
<!-- 642p 그림 참조 -->
<section id = "writeForm">
	<h2>게시판 글 등록</h2>
	<!-- [등록]버튼을 클릭 시 boardWritePro.bo를 요청하여 프론트컨트롤러로 이동 -->
	<form action="boardWritePro.bo" method="post" enctype="multipart/form-data" name="boardform">
		<table>                                   
			<tr>
				<td class="td_left"><label for="board_name">글쓴이</label></td>
				<td class=""><input type="text" name="board_name" id="board_name" required="required"></td>
			</tr>
			<tr>
				<td class="td_left"><label for="board_pass">비밀번호</label></td>
				<td class=""><input type="password" name="board_pass" id="board_pass" size="21" required="required"></td>
			</tr>
			<tr>
				<td class="td_left"><label for="board_subject">제목</label></td>
				<td class=""><input type="text" name="board_subject" id="board_subject"  required="required"></td>
			</tr>
			<tr>
				<td class="td_left"><label for="board_content">내용</label></td>
				<td class=""><textarea name="board_content" id="board_content" cols="40" rows="15" required="required"></textarea></td>
			</tr>
			<tr>
				<td class="td_left"><label for="board_file">파일첨부</label></td>
				<td class=""><input type="file" name="board_file" id="board_file" required="required"></td>
			</tr>
		</table>
		<input type="submit" value="등록" id="commandcall" >
		<input type="reset" value="다시쓰기" id="commandcall">
		
	</form>
</section>
</body>
</html>




