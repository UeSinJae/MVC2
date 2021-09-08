package action;

import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//파일을 업로드하기 위해서 cos.jar를 lid폴더에 추가해야한다
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import vo.ActionForward;
import vo.BoardBean;
import svc.BoardWriteProService;

public class BoardWriteProAction implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward=null;
		
		
		int fileSize=5*1024*1024;//한 번에 업로드할 수 있는 파일 크기 5M
		
		//먼저, upload 폴더 만들기
		//파일이 업로드 될 서버상의 실제 디렉토리 경로
		ServletContext context = request.getServletContext();
		String realFolder = context.getRealPath("/boardUpload");
		
		MultipartRequest multi =  new MultipartRequest(request,realFolder,fileSize, "utf-8",
				new DefaultFileRenamePolicy());
		        //파일이름 중복처리를 위한 객체(예)a.txt가 있다면 a1.txt로 자동으로 변경되어 업로드됨

		//새로 동록할 글 정보를 저장할 BoardBean클래스(=DTO)
		BoardBean boardBean = new BoardBean();
		//기본값으로 채워진 BoardBean객체를 사용자가 입력한 정보로 채움
		boardBean.setBoard_name(multi.getParameter("board_name"));
		boardBean.setBoard_pass(multi.getParameter("board_pass"));
		boardBean.setBoard_subject(multi.getParameter("board_subject"));
		boardBean.setBoard_content(multi.getParameter("board_content"));
		boardBean.setBoard_file(multi.getOriginalFileName((String)multi.getFileNames().nextElement()));
		
		//새로운 글(boardBean)을 동록하는 BoardWriteProServlce 객체 생성 후
		BoardWriteProService boardWriteProService = new BoardWriteProService();
		//객체 안의 registArticle()메소드로 DB연결,BoardBean객체 DB의 board테이블에 추가
		boolean isWriteSuccess = boardWriteProService.registArticle(boardBean);
		
		//추가 후 성공하면 true, 실패하면 false 리턴
		if(!isWriteSuccess) {//isWriteSuccess==false 실패
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('등록실패');");
			out.println("history.back();");
			out.println("</script>");
		}else {//글등록 성공 시
			forward = new ActionForward();
			forward.setRedirect(true);//리다이렉트(=새요청)  기본값false:디스패치
			forward.setPath("boardList.bo");//글 전체 목록 보기 요청하면 다시 프론트컨트롤러로 이동..
		}
		
		return forward;
	}

}
