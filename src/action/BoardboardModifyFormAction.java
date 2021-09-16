//글 수정 폼 보기 요청을 처리하는 action클래스
package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import svc.BoardDetailService;
import vo.ActionForward;
import vo.BoardBean;

public class BoardboardModifyFormAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//수정대상이 되는 글 번호를 얻어와 정수로 변환
		int board_num = Integer.parseInt(request.getParameter("board_num"));
		

		BoardDetailService boardDetailService = new BoardDetailService();
		BoardBean article = boardDetailService.getArticle(board_num);
		
		ActionForward forward = new ActionForward();
		request.setAttribute("article", article);
		forward.setPath("/board/qna_board_modify.jsp");
		
		return forward;
	}

}
