//답변쓰기 폼을 출력 요청을 처리하는 Action 클래스
package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import svc.BoardDetailService;
import vo.ActionForward;
import vo.BoardBean;

public class BoardReplyAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//답변글 등록 처리를 한 후 원래 있던 페이지로 되돌아가야 하기 때문에  파라미터로 전송된 페이지 번호를 얻어오고
		String nowPage = request.getParameter("page");
		
		//답변을 달기 위한 원글의 글번호를 얻어와
		int board_num = Integer.parseInt(request.getParameter("board_num"));
		
		BoardDetailService boardDetailService = new BoardDetailService();
		BoardBean article = boardDetailService.getArticle(board_num);
		/* ★원글의 정보를 조회하는 이유?
		 * 원글의  그룹(board_re_ref)이나 들여쓰기(board_re_lev)와 위치(board_re_seq)를 알아야 답변글을 원글 아래에 제대로 표시할 수 있으므로
		 * 
		 * 뒤에서 BoardDAO 클래스의 insertReplyArticle()메서드를 호출하여
		 * (642p그림참조)글쓴이,비밀번호,제목,내용만 답변글에 관한 정보이고
		 * 나머지 값들은 원글(=article)의 내용 그대로를 사용하여 수정하여 답변글을 board테이블에 추가할것이다 
		 */
		ActionForward forward = new ActionForward();
		request.setAttribute("page", nowPage);
		request.setAttribute("article", article);//원글정보
		
		forward.setPath("/board/qna_board_reply.jsp");
		return null;
	}

}
