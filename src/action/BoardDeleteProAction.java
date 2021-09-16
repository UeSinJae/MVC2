//글 삭제 처리 요청을 처리하는 Action클래스
package action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import svc.BoardDeleteProService;
import vo.ActionForward;
import vo.BoardBean;

public class BoardDeleteProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = new ActionForward();
		
		int board_num =Integer.parseInt(request.getParameter("board_num"));
		String nowPage = request.getParameter("page");
		String pass = request.getParameter("board_pass");
		BoardDeleteProService boardDeleteProService = new BoardDeleteProService();
		boolean isRightUser = boardDeleteProService.isArticleWriter(board_num, pass);
		
		if(isRightUser) {
			//글 수정을 요청한 사용자가 글을 작성한 사용자가 아니면
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('수정할 권한이 없습니다.');");
			out.println("history.back();");
			out.println("</script>");
		}else {
			
			boolean isRemoveSuccess = boardDeleteProService.removeArticle(board_num);
			
			if(!isRemoveSuccess) {//수정 작업을 실패하면 
				response.setContentType("text/html;charset=utf-8");
				PrintWriter out = response.getWriter();
				
				out.println("<script>");
				out.println("alert(삭제 실패);");
				out.println("history.back();");
				out.println("</script>");
			}else {//수정작업을 성공하면
				forward = new ActionForward();
				forward.setRedirect(true);//리다이렉트 방식(=새요청)으로
				//삭제가 제대로 되었는지 확인하기 위해 전체 목록 보기 요청(현재 페이지 번호 전송)
				forward.setPath("boardList.bo?page="+nowPage);
			}
			
		}
		return forward;
	}

}
