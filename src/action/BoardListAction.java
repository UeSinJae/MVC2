package action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import svc.BoardListService;
import vo.ActionForward;
import vo.BoardBean;
import vo.PageInfo;

public class BoardListAction implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
	
			
		
		
		//클리한 페이지 번호와 한페이지 당 출력될 글의 개수 10을 매개값으로 전송하여 해당페이지의 출력될  글 목록을 얻어옴
		int page=1;//출력될 페이지의 기본값으로 1페이지 설정
		int limit=10;//한 페이지 당 출력될 글의 개수를 10개로 설정
		
		//출력될 페이지가 파라미터로 전송되었으면 (아래의 페이지 중 2클릭)
		if(request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));//문자열 page를 연산할 수 있으므로
		}
		
		BoardListService boardListService = new BoardListService();
		int listCount = boardListService.getListCount();//게시판의 총 글의 개수를 얻어오고
		//listCount를 이용하여 총 페이지 수 계산 (예) 11.0/10 = 1.1+0.95= 2.05=> 2페이지, 21.0/10=2.1+0.95=3.05=>3페이지
		int maxPage = (int)((double)listCount/limit+0.95);
		
		/*******************************************
		 * 현재 페이지에 보여줄 시작 페이지 수 (1)
		 * [이전] 1 2 3 4 5 6 7 8 9 10[다음]
		 * [이전] 11 12 13 14 15 16 17 18 19 20[다음]
		 ********************************************/
		int startPage = ((int)((double)page/10+0.9))*10+1;
		
		//현재페이지에 보여줄 마지막 페이지 수(10,20,30 등)
		int endPage = startPage+10-1;//startPage=1일때 : 1+10-1=10
		                             //startPage=11일때 : 11+10-1 = 20
		/* 전체 페이지가 15라면 [이전]11 12 13 14 15
		 * endPage(20) > 총 페이지 수(15)보다 크면 endPage=15로 설정
		 * 
		 */
		if(endPage > maxPage) endPage=maxPage;
		
		
		PageInfo pageInfo = new PageInfo();
		pageInfo.setPage(page);//페이지번호
		pageInfo.setListCount(listCount);
		pageInfo.setMaxPage(maxPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
		
		//클릭한 페이지 번호와 한 페이지당 출력될 글의 개수 10을 매개값으로 전송하여 해당 페이지의 출력될 글 목록을 얻어옴
		ArrayList<BoardBean> articleList= boardListService.getArticleList(page,limit);
		/* 위에서 구한 페이징에 관한 정보를 저장한 PageInfo 객체(=DTO)와
		 * 출력될 글목록 정보가 담긴 ArrayList<BoardBean>객체를
		 * 미리 request영역에 공유 시킨 후
		 * 디스패치 방식(기존의 request 그대로)으로 포워딩함 
		 */
		
		request.setAttribute("pageInfo", pageInfo);
		request.setAttribute("articleList", articleList);
		
		ActionForward forward = new ActionForward();
		forward.setPath("/board/qna_board_list.jsp");
		
		return null;
	}
	
	
}	
