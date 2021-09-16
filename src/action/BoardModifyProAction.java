//글 수정 처리를 요청하는 action클래스
package action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import svc.BoardModifyProService;
import vo.ActionForward;
import vo.BoardBean;

public class BoardModifyProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = null;
		int board_num = Integer.parseInt(request.getParameter("board_num"));
		String pass = request.getParameter("board_pass");
		
		BoardBean article = null;
		
		
		//파라미터로 전송된 비밀번호를 사용해서 글 수정을 욫어한 사용자가 글을 작성한 사용자인지 확인
		BoardModifyProService boardModifyProService=new BoardModifyProService();
		boolean isRightUser = boardModifyProService.isArticleWriter(board_num, pass);
		
		if(isRightUser) {
			//글 수정을 요청한 사용자가 글을 작성한 사용자가 아니면
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('수정할 권한이 없습니다.');");
			out.println("history.back();");
			out.println("</script>");
		}else {
			//글 수정을 요청한 사용자가 글을 작성한 사용자가 맞으면
			article = new BoardBean();//기본값으로 채워진
			//사용자가 다시 입력한 파라미터값으로 채움
			article.setBoard_num(board_num);//이름은 수정안함
			article.setBoard_subject(request.getParameter("board_subject"));
			article.setBoard_content(request.getParameter("board_content"));
			
			boolean isModifySuccess = boardModifyProService.modifyArticle(article);
			
			if(!isModifySuccess) {//수정 작업을 실패하면 
				response.setContentType("text/html;charset=utf-8");
				PrintWriter out = response.getWriter();
				
				out.println("<script>");
				out.println("alert(답벼글 등록 실패);");
				out.println("history.back();");
				out.println("</script>");
			}else {//수정작업을 성공하면
				forward = new ActionForward();
				forward.setRedirect(true);//리다이렉트 방식(=새요청)으로(이미갱신
				forward.setPath("boardDetail.bo?board_num="+article.getBoard_num());//수정이 제대로 되었는지 확인하기 위해 하나의 글 상세보기 요청(글번호로 전송)
			}
			
		}
		
		return null;
	}

}
