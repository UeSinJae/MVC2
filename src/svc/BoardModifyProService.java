//글 수정 처리를 요청을 구현한 service글래스
package svc;

import java.sql.Connection;

import dao.BoardDAO;
import vo.BoardBean;

import static db.JdbcUtil.*;

public class BoardModifyProService {
	
	public boolean isArticleWriter(int board_num, String pass) throws Exception{
		Connection con = getConnection();
		BoardDAO boardDAO = BoardDAO.getInstance();
		boardDAO.setConnection(con);
		
		//전송된 비번을 사용해서 글 수정 요청한 사용자가 글을 작성한 사용자인지 확인
		boolean isArticleWriter = boardDAO.isArticleBoardWriter(board_num,pass);
		
		close(con);
		return isArticleWriter;
	}

	public boolean modifyArticle(BoardBean article) throws Exception{
		Connection con = getConnection();
		BoardDAO boardDAO = BoardDAO.getInstance();
		boardDAO.setConnection(con);
		
		int updateCount = boardDAO.updateArticle(article);
		
		boolean isModifySuccess = false;
		if(updateCount > 0) {
			commit(con);
			isModifySuccess = true;
		}
		
		close(con);
		return isModifySuccess;
	}
	
}
