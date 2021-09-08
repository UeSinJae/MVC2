/*
 * 글 등록 요청을 처리하는 비지니스 로직을 구현
 */
package svc;

import vo.BoardBean;

import static db.JdbcUtil.*;

import java.sql.Connection;

import dao.BoardDAO;

public class BoardWriteProService {
	//기본생성자
	
	public boolean registArticle(BoardBean boardBean){
		// 커넥션풀에서 DB를 연결하기 위한 Connection객체를 얻어와
		Connection con = getConnection();//JdbcUtil.getConnection();//클래스명.static메서드 호출
		
		//BoardDAO : 싱글톤 패턴-단 1개의 객체만 생성하여 외부클래스에서 공유하여 사용하도록
		BoardDAO boardDAO = BoardDAO.getInstance();
		
		//BoardDAO객체에서 DB작업을 할 때 사용하도록 매개값으로 설정함
		boardDAO.setConnection(con);
		
		//DB의 board테이블에 사용자가 입력한 값들로(BoardBean객체) 글 추가->성공하면 1리턴받음
		int insertCount = boardDAO.insertArticle(boardBean);
		
		boolean isWriteSuccess = false;//글 등록 성공 여부
		if(insertCount > 0) {//글 동록 성공
			commit(con);//트랜잭션 완료
			isWriteSuccess = true;
		}else {
			rollback(con);//트랜젹스 취소
		}
			
		close(con);
		return isWriteSuccess;
	}
	
}
