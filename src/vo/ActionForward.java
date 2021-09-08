/* 575p-JAVA Bean 클래스
 * 3.포워딩 정보를 저장할 수 있는 ActionForward클래스
 * 컨트롤러에서 클라이언트의 각 요청을 받아서 처리한 후 최종적으로 뷰 페이지(JSP)로 포워딩 처리 시 이동할 뷰 페이지의 URL과 포워딩 방식(디스패치나 리다이렉트)이 필요하다.
 * 이 두 정보를 편히라게 다루기 위해서 ActionForward클래스를 설계
 */
package vo;

public class ActionForward {
	//컨트롤러에서 요청을 처리한 후 포워딩 될 최종 뷰 페이지 url이 저장
	private String path = null;
	
	//포워딩 방식 저장. false이면 디스패치(기존요청), true이면 리다이렉트(재요청)
	private boolean isRedirect = false;

	//매개변수 없는 기본생성자
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isRedirect() {
		return isRedirect;
	}

	public void setRedirect(boolean isRedirect) {
		this.isRedirect = isRedirect;
	}
	
}
