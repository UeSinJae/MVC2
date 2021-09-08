package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import action.Action;
import action.BoardWriteProAction;
import vo.ActionForward;

/**
 * Servlet implementation class BoardFrontController
 */
//확장자가 bo이면 무조건 BoardFrontController로 이동하여 doProcess()메서드 실행함
@WebServlet("*.bo")//마지막 url이 *.bo로 끝나는 요청을 매핑
public class BoardFrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BoardFrontController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doProcess(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doProcess(request, response);
	}
	
	//이 서블릿으로 들어오는 get,post방식의 모든 요청은 doProcess()를 호출하여 처리
	protected void doProcess(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		request.setCharacterEncoding("utf-8");//반드시 첫줄
		
		//요청객체로부터 프로젝트명+파일경로까지 가져옴 (예)/프로젝트명/boardWriteForm.bo
		String RequestURI = request.getRequestURI();
		//요청 URL : http://localhost:8090/프로젝트명/boardWriteForm.bo
		//요청 URI : /프로젝트명/boardWriteFrom.bo
		
		//요청객체로부터 프로젝트path만 가져옴(예)/프로젝트
		String contextPath = request.getContextPath();
		
		/* URI에서 contextPath길이만큼 잘라낸 나머지 문자열
		 * /프로젝트명/boardWriteFrom.bo - /프로젝트명 = /boardWritrForm.bo
		 */
		String command = RequestURI.substring(contextPath.length());//index 8~끝까지 부분 문자열을 반환해준다
		
		/* 요청이 파악되면 해당 요청을 처리하는 각 Action클래스를 사용해서 요청 처리
		 * 각 요청에 해당하는 ACtion클래스 객체들을 다형성을 이용해서 동일한 타입(Action)으로 참조하기 위해
		 * Action 인터페이스 타입의 변수 선언(574p 참조)
		 */
		Action action = null;
		ActionForward forward= null;
		/* 글쓰기 페이지를 열어주는 요청인 경우는 특별한 비지니스 로직을 실행할 필요없이
		 * 글쓰기 할 수 있는 뷰페이지로 포워딩
		 */
		if(command.equals("/boardWriteForm.bo")) {//사용자가 글 등록하는 폼화면 요청이면
			forward = new ActionForward();
			forward.setPath("/board/qna_board_write.jsp");//디스패치 방식
		}
		else if(command.equals("/boardWritePro.bo")) {//사용자가 입력한 자료들을 DB에 추가하는 요청이면
			action = new BoardWriteProAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(command.equals("/boardList.bo")) {//테이블  목록 보여주기 요청이면
			action = new BoardWriteProAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/******************************************************************
		 *포워딩
		 * 
		 *******************************************************************/
		
		if(forward != null) {
			if(forward.isRedirect()) {//isRedirect=true : 주소변경(새요청), request 공유 못
				response.sendRedirect(forward.getPath());//응답-리다이렉트 방식
				
			}else {//isRedirect=false : 디스패치 방식
				//RequestDispatcher dispatcher = request.getRequestDispatcher(forward.getPath());
				//dispatcher.forward(request, response);//기존의 요청, 기존의 응답을 그대로 보내므로 주소가 그대로임.request공유
				request.getRequestDispatcher(forward.getPath()).forward(request, response);
				
			}
		}
		
	}
	
}











