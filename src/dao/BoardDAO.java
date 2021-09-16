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
	
	//3.해당 페이지에 출력될 글 목록을 DB에서 조회하여 ArrayList<BoardBean>객체 반환
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
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("getBoardList 에러 : "+e);//e:예외종류+예외메세지
		}finally {
			close(rs);
			close(pstmt);
		}
		return articleList;
	}
	
	//4. 글번호로 해당글을 조회하여 조회수 1증가
	public int updateReadCount(int board_num) {
		int updateCount = 0;//지역변수 초기화
		String sql = "update board set board_readcount=board_readcount+1 where board_num="+board_num;
		try {
			pstmt = con.prepareStatement(sql);
			updateCount = pstmt.executeUpdate();
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("setReadCountUpdate 에러 : "+e);//e:예외종류+예외메세지
		}finally {
//			close(rs);
			close(pstmt);
		}
		return updateCount;
	}
	
	//5.글번호로 글 하나의 정보를 조회해서 BoardBean객체로 반환
	public BoardBean selectArticle(int board_num) {
		BoardBean boardBean = null;
		
//		String sql = "select * from board where board_num="+board_num;
		String sql = "select * from board where board_num=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, board_num);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				boardBean = new BoardBean();//기본값으로 채워진 BoardBean객체를
				//조회한 결과값으로 채움
				boardBean.setBoard_num(rs.getInt("board_num"));
				boardBean.setBoard_name(rs.getString("board_name"));
				boardBean.setBoard_subject(rs.getString("board_subject"));
				boardBean.setBoard_content(rs.getString("board_content"));
				boardBean.setBoard_file(rs.getString("board_file"));
				boardBean.setBoard_re_ref(rs.getInt("board_re_ref"));
				boardBean.setBoard_re_lev(rs.getInt("board_re_lev"));
				boardBean.setBoard_re_seq(rs.getInt("board_re_seq"));
				boardBean.setBoard_readcount(rs.getInt("board_readcount"));
				boardBean.setBoard_date(rs.getDate("board_date"));
			}
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("getDetail 에러 : "+e);//e:예외종류+예외메세지
		}finally {
			close(rs);
			close(pstmt);
		}
		
		return boardBean;
	}
	
	//6.답변글쓰기 폼에서 답변관련 내용을 담은 BoardBean객체를 board테이블에 추가
	//글쓴이,비밀번호,제목,내용만 답변글 정보이고 나머지 값(글번호,그룹,들여쓰기,위치)들은 원글의 내용 그대로
	public int insertReplyArticle(BoardBean article){
//		String board_max_sql = "select max(board_num) from board";//교재내용
		String board_max_sql = "select IFNULL(max(board_num),0)+1 from board";
		int num = 0;		
		String sql="";
		int re_ref = article.getBoard_re_ref();//원글의 그룹번호
		int re_seq = article.getBoard_re_seq();//원글에서 답변글이 몇번째 아래에 놓일 것인지 위치를 결정해주는 값
		int re_lev = article.getBoard_re_lev();//들여쓰기
		
		int insertCount = 0;
		
		try {
			pstmt=con.prepareStatement(board_max_sql);
			rs = pstmt.executeQuery();
			
	//		if(rs.next()) num = rs.getInt(1)+1;//->교재내용
	//		else num = 1;//아무글도 없으면 답변글번호를 1로시작->교재내용
			if(rs.next()) num=rs.getInt(1);//num:답변글번호
			
			/* ★★최신답변글을 원글 바로 아래로 위치하도록 하기 위해
			 * 원글의 답변글만 찾아서 board_re_seq를 각각 1증가
			 * 
			 * (예)원글 0     원글0      원글0
			 * --------------------
			 * 			      - 답변글1(최신답변)
			 *   답변글 1->답변글2->답변글2
			 *   답변글 1->답변글2->답변글2
			 * 
			 * (예2)
			 *     답변글 1     답변글1      답변들1
			 *--------------------
			 * 			      - 답변글2(최신답변)
			 *   답변글 2->답변글3->답변글3
			 *   답변글 2->답변글3->답변글3
			 * 
			 */
			sql="update board set board_re_seq=board_re_seq+1 where board_re_ref=? and board_re_seq > ?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, re_ref);//원글의 그룹번호
			pstmt.setInt(2, re_seq);//원글의 위치
			int updateCount = pstmt.executeUpdate();//업데이트가 성공하면 1을 리턴받음
			
			if(updateCount > 0) commit(con);
			else rollback(con);
			
			/********************답변글 등록*************************/
			re_lev=re_lev+1;//원글레벨(0)+1 원글레벨(1)+1
			re_seq=re_seq+1;//원글위치(0)+1 원글위치(1)+1
			
			//★ sql+=""; 하면안됨
			sql="insert into board values(?,?,?,?,?,?,?,?,?,?,now())";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, num);//위에서 계산한 답변글번호
			
			//아래 4개의 값(글쓴이,비번,제목,내용)은 전송받은 값을 그대로 가져와
			pstmt.setString(2, article.getBoard_name());
			pstmt.setString(3, article.getBoard_pass());
			pstmt.setString(4, article.getBoard_subject());
			pstmt.setString(5, article.getBoard_content());
			
			pstmt.setString(6,"");//답변글 폼에서 파일을 업로드하는 부분이 없음
			
			pstmt.setInt(7, re_ref);//원글과 같은 그룹번호
			pstmt.setInt(8, re_lev);//원글+1
			pstmt.setInt(9, re_seq);//원글+1(원글바로아래에위치ㄹ함)
			
			pstmt.setInt(10, 0);//죄회수
			
			insertCount = pstmt.executeUpdate();
		}catch (Exception e) {
			//e.printStackTrace();
			System.out.println("boardReply 에러 : "+e);//e:예외종류+예외메세지
		}finally {
			close(rs);
			close(pstmt);
		}
		return insertCount;
	}
	
	//7.(글번호와 입력한 비번을 매개값으로)현재 사용자가 글쓴이인지 확인 
	public boolean isArticleBoardWriter(int board_num, String pass) {
//		String board_sql = "select * from board where board_num=?";
		String board_sql = "select * from board where board_num="+board_num;
		boolean isWriter = false;
		try {
			pstmt = con.prepareStatement(board_sql);
//			pstmt.setInt(1, board_num);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				//사용자가 입려한 비번과 글번호로 조회한 비번이 같으면
				if(pass.equals(rs.getString("board_pass"))) {
					isWriter = true;
				}
			}
			
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("isBoardWriter 에러 : "+e);//e:예외종류+예외메세지
		}finally {
			close(rs);
			close(pstmt);
		}
		
		
		return isWriter;
	}

	//8.파라미터로 전송된 수정 정보인 article을 매개값으로 받아 글을 수정한다.
	public int updateArticle(BoardBean article) {
		String sql="update board set board_subject=?,board_content=? where board_num=?";
		int updateCount = 0;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, article.getBoard_subject());
			pstmt.setString(2, article.getBoard_content());
			pstmt.setInt(3, article.getBoard_num());
			
			updateCount = pstmt.executeUpdate();
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("BoardModify 에러 : "+e);//e:예외종류+예외메세지
		}finally {
//			close(rs);
			close(pstmt);
		}
		
		
		return updateCount;
	}

	//9.글삭제
	public int deleteArticle(int board_num) {
		String sql = "delete from board where board_num="+board_num;
		int deleteCount = 0;
		try {
			pstmt = con.prepareStatement(sql);
			deleteCount = pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("BoardDelete 에러 : "+e);//e:예외종류+예외메세지
		}finally {
//			close(rs);
			close(pstmt);
		}
		return deleteCount;
	}
	

}
















