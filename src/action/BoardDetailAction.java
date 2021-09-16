package action;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import svc.BoardDetailService;
import vo.ActionForward;
import vo.BoardBean;

public class BoardDetailAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//파라미터로 전송된 상세 내용을 볼 글 번호를 얻어와
		int board_num = Integer.parseInt(request.getParameter("board_num"));
		//페이지 번호를 받음(상세 내용보기 후 원래 페이지로 돌아가기 위해)
		String page = request.getParameter("page");
		
		BoardDetailService boardDetailService = new BoardDetailService();
		
		//글번호로 해당글을 찾아서 조회수 1증가 + 글번호로 글 하나의 정보를 조회새서 BoardBean객체를 반환
		BoardBean article = boardDetailService.getArticle(board_num);
		
		ActionForward forward = new ActionForward();
		request.setAttribute("article", article);
		request.setAttribute("page", page);
		
		forward.setPath("/board/qna_board_view.jsp");
		
		return forward;//isRedirect = false이므로 디스패치 방식
	}

}
