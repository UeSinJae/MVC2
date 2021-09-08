//DB로 SQL구문을 전송하는 클래스
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.tomcat.dbcp.dbcp2.PStmtKey;

import static db.JdbcUtil.*;//static:모든 메서드가 메모리에 올라옴

import com.mysql.cj.xdevapi.PreparableStatement;

import vo.BoardBean;

public class BoardDAO {
	Connection con;//멤버변수(전역변수 : 전체 메서드에서 사용 가능)
	PreparedStatement pstmt = null;
	ResultSet rs =null;
	/* 싱글톤 패턴 : BoardDAO객체 단 1개만 생성
	 * 이유? 외부 클래스에서 처음 생성된 BoardDAO객체를 공유해서 사용하도록 하기 위해
	 * 
	 */
	private BoardDAO(){}
	
	private static BoardDAO boardDAO;
	//static으로 만든 이유 : 객체를 생성하기 전에 메모리에 올라간 getInstance()메서드를 통해서만 BoardDAO객체를 1개만 만들도록 하기 위해
	public static BoardDAO getInstance(){
		if(boardDAO == null) {//객체가 없으면
			boardDAO = new BoardDAO();//객체 생성
		}
		
		return boardDAO;//있으면 기존객체의 주소 리턴
	}
	
	public void setConnection(Connection con){//Connection객체를 받아 DB연결
		this.con = con;
	}
	
	//1. 글 등록
	public int insertArticle(BoardBean article){
		int num = 0;
		String sql ="";
		int insertCount=0;
		try {
//		pstmt = con.prepareStatement("select max(board_num) from board");
		//오라클 : NVL(),NVL2   MYSQL : IFNULL();
		pstmt = con.prepareStatement("select IFNULL(max(board_num),0)+1 from board");
		rs = pstmt.executeQuery();
		
//		if(rs.next()) num = rs.getInt(1)+1;
//		else num=1;//null이면 1
		
		if(rs.next()); num=rs.getInt(1);//수정
		
		
			sql ="insert into board values(?,?,?,?,?,?,?,?,?,?,now())";//now() = 오라클 sysdate는 ()없음
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.setString(2, article.getBoard_name());//글쓴이
			pstmt.setString(3, article.getBoard_pass());//비밀번호
			pstmt.setString(4, article.getBoard_subject());//제목
			pstmt.setString(5, article.getBoard_content());//내용
			pstmt.setString(6, article.getBoard_file());//첨부파일
			
			pstmt.setInt(7, num);//답변글 등록할 때 원글과 답변글을 같은 그룹으로 묶기 위해 사용함(그룹번호가 같으면 같은 그룹임)
			pstmt.setInt(8, 0);//들어쓰기 : 얼마만큼 안쪽으로 들어가 글이 시작될 것인지를 결정해주는 값(0으로초기화.0은원글)
			pstmt.setInt(9, 0);//정렬 : 원글에서 답변글이 몇번째 아래에 놓일 것인지 위치를 결정해주는 값(0으로초기화)
			pstmt.setInt(10, 0);//조회수(0으로 초기화)
			//11 : now() : 오늘날짜가 들어감
			
			insertCount = pstmt.executeUpdate();//업데이트가 성공하면 1을 리턴받음
		} catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("boardInsert 에러 : "+e);//e:예외종류+예외메세지
		}finally {
			close(pstmt);
			close(rs);
		}
		return insertCount;
	}
	
	//2.게시판 전체 글의 개수 구하여 반환
	public int selectListCount() {
		int listCount=0;
		try {
			pstmt = con.prepareStatement("select count(*) form board");
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				listCount = rs.getInt(1);//조회한 전제 글의 수
			}
		} catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("getListCount 에러 : "+e);//e:예외종류+예외메세지
		}finally {
			close(rs);
			close(pstmt);
		}
		return listCount;
	}
	
	public ArrayList<BoardBean> selectArticleList(int page, int limit){
		ArrayList<BoardBean> articleList = new ArrayList<BoardBean>();
		/* board_re_ref : 같은 수는 같은 그룹
		 *                (원글이 글번호가 3이면 답변글도 모두 3) 
		 * board_re_seq : 원글에서 답변글이 몇 번째 아래에 놓일 것인지 위치를 결정해 주는 값
		 * 
		 */
		                                                                             //limi 10,10 :11행부터 10개
		String sql = "select * from board order by board_re_ref desc, board_re_seq asc limit ?, 10";
		/*
		 * startrow변수에 해당 페이지에 출력되어애 하는 시작 레코드의 index번호를 구하여 저장
		 * (예)아래 페이지 번호 중 2를 클릭하면 page가 2가 되어(2-1)*10=10
		 */
		
		int startrow = (page-1)*10;//10이지만 읽기 시작하는 row번호는 11이 됨
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, startrow);//11부터 10개의 레코드 조회(답변글 포함해서)
			rs = pstmt.executeQuery();
			
			BoardBean boardBean = null;
			while(rs.next()) {
				boardBean = new BoardBean();//기본값으로 채워진 것을 조회한 결과값으로 변경함
				
				boardBean.setBoard_num(rs.getInt("board_num"));//글번호
				boardBean.setBoard_name(rs.getString("board_name"));//글작성자 (주의 : 글 비밀번호 제외)
				boardBean.setBoard_subject(rs.getString("board_subject"));//글제목
				boardBean.setBoard_content(rs.getString("board_content"));//글내용
				boardBean.setBoard_file(rs.getString("board_file"));//첨부파일
				
				boardBean.setBoard_re_ref(rs.getInt("board_re_ref"));//관련글번호
				boardBean.setBoard_re_lev(rs.getInt("board_re_lev"));//답변레벨
				boardBean.setBoard_re_seq(rs.getInt("board_re_seq"));//관련글 중 출력순서
				
				boardBean.setBoard_readcount(rs.getInt("board_readcount"));//조회수
				boardBean.setBoard_date(rs.getDate("board_date"));//작성일
				
				articleList.add(boardBean);
			}
		} catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("getBoardList 에러 : "+e);//e:예외종류+예외메세지
		}finally {
			close(rs);
			close(pstmt);
		}
		return articleList;
	}
	
	
	
	

}
