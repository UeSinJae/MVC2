package svc;

import static db.JdbcUtil.*;
import java.sql.Connection;
import java.util.ArrayList;

import dao.BoardDAO;
import vo.BoardBean;

public class BoardListService {
	//게시판의 총 글의 개수를 반환하는 메서드 
	public int getListCount() throws Exception {
		Connection con  = getConnection();
		BoardDAO boardDAO = BoardDAO.getInstance();
		boardDAO.setConnection(con);
		
		int listCount = boardDAO.selectListCount();
		
		close(con);//DB연결해제
		
		return listCount;
	}

	//페이지 번호와 한 페이지당 출력될 글의 개수10을 전송 받아 지정한 페이지에 출력될 글 목록을 ArrayList<BoardBean> 객체 반환
	public ArrayList<BoardBean> getArticleList(int page, int limit) {
		Connection con  = getConnection();
		BoardDAO boardDAO = BoardDAO.getInstance();
		boardDAO.setConnection(con);

		ArrayList<BoardBean> articleList = boardDAO.selectArticleList(page, limit);
		close(con);
		
		return articleList;
	}
	
	
}
