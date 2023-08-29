package com.ray.service.member;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

import com.ray.controller.MemberFactory;
import com.ray.dao.MemberCRUD;
import com.ray.dao.MemberDAO;
import com.ray.etc.UploadedFile;
import com.ray.service.MemberService;
import com.ray.vo.Member;

public class RegisterMemberService implements MemberService {

	private static final int MEMORY_THRESHOLD = 1024 * 1024 * 5; // 하나의 파일블럭의 버퍼 사이즈 5MB
	private static final int MAX_FILE_SIZe = 1024 * 1024 * 10; // 최대 파일 사이즈 10MB
	private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 15; // 최대 request 사이즈 15MB

	@Override
	public MemberFactory executeService(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		MemberFactory mf = MemberFactory.getInstance();
		
		
		
		// 파일과 함께 데이터를 받았다면 request.getParameter()로 데이터를 수집하면 안된다.(!)
		
		String uploadDir = "\\memberImg";
		// 실제 파일이 저장될 물리적 경로(서버의 경로가 바뀌어도.. 바뀐물리적경로를 얻어오게 된다)
		String realPath = request.getSession().getServletContext().getRealPath(uploadDir);
		System.out.println(realPath);
		
	
		
		String encoding = "utf-8"; // 인코딩 방식
		
		
		File saveFileDir = new File(realPath); // 파일이 실제 저장될 경로에대한 파일객체 생성
		
		String userId = "";
		userId = request.getParameter("userId");
		
		String userPwd = "";
		String email = "";
		String userImg = "";
		
		// 파일이 저장될 공간의 경로, 사이즈 등의 환경설정 정보를 가지고 있는 객체
		DiskFileItemFactory factory = new DiskFileItemFactory(MEMORY_THRESHOLD, saveFileDir);
		
		// 실제 request로 넘겨져온 매개변수를 통해 파일을 upload 처리 할 객체
		ServletFileUpload sfu = new ServletFileUpload(factory);
		
		UploadedFile uf = null;
		
		try {
			List<FileItem> lst = sfu.parseRequest(request);
			
//			FileItem 속성에서
//			1) name값이 null이 아니면 파일
//			2) 파일이면 name속성의 값이 업로드된 파일이름(확장자)
//			3) isFormField의 값이 true이면 파일이 아닌 데이터
//			   isFormField의 값이 false이면 파일
//			4) FieldName의 값이 보내온 데이터의 input 태그의 name속성값
			for (FileItem item : lst) {
				System.out.println(item);
	
				if (item.isFormField()) { // 파일이 아닌 일반 데이터이다.
					if(item.getFieldName().equals("userId")) {
						userId = item.getString(encoding);
					} else if(item.getFieldName().equals("userPwd")) {
						userPwd = item.getString(encoding);
					} else if(item.getFieldName().equals("userEmail")) {
						email = item.getString(encoding);
					}
				} else if (item.isFormField() == false && item.getName() != "") { // 업로드된 파일인 경우
					// 파일을 저장해야 하는데 파일의 이름이 중복되는 경우가 있기 때문에...
					// 아래의 처리를 한다.
					 
					// 1) 중복되지 않을 새이름으로 저장.
					uf = getNewFileName(item, realPath, userId);
					
					
					// 2) 파일명(순서).확장자로 새파일이름을 만들고 싶은 경우
//					uf = makeNewFileNameWithNumbering(item, realPath);
						
					
					File uploadFile = new File(realPath + File.separator + uf.getNewFeileName());
					try {
						item.write(uploadFile); // 업로드된 파일을 하드디스크에 저장
					} catch (Exception e) {
						// 업로드된 파일이 실제 저장될 때 예외 발생
						e.printStackTrace();
						
						mf.setRedirect(true);
						mf.setWhereIsGo(request.getContextPath()+"/member/register.jsp?status=fail");
						
						return mf;
					}
					
					// 만약 이미지파일을 따로 저장하지 않고 base64문자열로 처리하여 저장하고 싶다면...
//					makeImgtoBase64String(realPath + File.separator + uf.getNewFeileName());
				}
			}
				
		} catch (FileUploadException e) {
			// 파일이 업로드 될 때 예외 발생
			e.printStackTrace();
			
			mf.setRedirect(true);
			mf.setWhereIsGo(request.getContextPath()+"/member/register.jsp?status=fail");
			
			return mf;
			
		} 
		
		
		MemberDAO mdao = MemberCRUD.getInstance();
		int result = -1;
		try {
			if (uf != null) { // 업로드된 파일이 있는 경우
				uf.setNewFeileName("memberImg/" + uf.getNewFeileName());
				result = mdao.registerMemberWithFile(uf, new Member(userId, userPwd, email, null, -1, -1), 
						"회원가입", 100);	
			} else { // 업로드된 파일이 없는 경우
				result = mdao.registerMember(new Member(userId, userPwd, email, null, -1, -1), "회원가입", 100);
			}
			
			if (result == 0) {
				System.out.println("회원가입 all 성공");
			}
		} catch(NamingException | SQLException e) {
			// DB에 저장할 때 나오는 예외
			e.printStackTrace();
			
			// 1) 업로드된 파일이 있다면 지워야 합니다.
			if (uf != null) {
			
				String without = uf.getNewFeileName().substring("memberImg/".length());
				System.out.println(without);
				File deleteFile = new File(realPath + File.separator + without);
				deleteFile.delete(); // 파일 지움
			}
			
			mf.setRedirect(true);
			mf.setWhereIsGo(request.getContextPath()+"/member/register.jsp?status=fail");
			
			return mf;
		}
		
		
		mf.setRedirect(true);
		mf.setWhereIsGo(request.getContextPath() + "/index.jsp?status=success");
		
		
		return mf;
	}

	private void makeImgtoBase64String(String uploadedFile) {
		System.out.println(uploadedFile);
		
		// base64 문자열 : 이진데이터파일을 읽어서 A-Za-z0-9+/ 문자의 조합으로 바꾼것
		
		// 인코딩(파일 -> 문자열)
		String result = null;
		File upFile = new File(uploadedFile);
		try {
			byte[] file = FileUtils.readFileToByteArray(upFile); // 업로드된 파일을 읽어 byte[]로 바꿔줌
			result = Base64.getEncoder().encodeToString(file); // 인코딩된 base64 문자열
			
			System.out.println(result); 
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String realPath = "D:\\lecture\\JSP\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\JSPMiniProject\\memberImg";
		
		// 디코딩 (문자열-> 파일)
		String encodedStr = "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";
		
		byte[] encodedArr = Base64.getDecoder().decode(encodedStr);
		File f = new File(realPath + File.separator + "aaaaaa.png");
		try {
			FileUtils.writeByteArrayToFile(f, encodedArr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//return result;
		
		
	}

	private UploadedFile makeNewFileNameWithNumbering(FileItem item, String realPath) {
		String originalFileName = item.getName(); // 업로드된 원본 이름
		String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
		
		// ex)  abc.jpg -> abc(1).jpg -> abc(2).jpg -> abc(3).jpg
		
		
		while(duplicateFileName(originalFileName, realPath)) {
			
		}
		
		return null;
	}

	private boolean duplicateFileName(String originalFileName, String realPath) {
		boolean result = false;
		
		File realPathFile = new File(realPath);
		for (File f : realPathFile.listFiles()) {
			if (f.getName().equals(originalFileName)) {
				result = true;
			}
		}
		
		
		return result;
	}

	private UploadedFile getNewFileName(FileItem item, String realPath, String userId) {
		String uuid = UUID.randomUUID().toString();

		String originalFileName = item.getName(); // 업로드된 원본 이름
		String ext = originalFileName.substring(originalFileName.lastIndexOf("."));

		String newFileName = "";
		if (item.getSize() > 0) {
			// 파일 이름이 중복되지 않게 처리하기 위해 아래처럼 처리하자
			// 예) "userId_UUID.확장자";
			newFileName = userId + "_" + uuid + ext;
		}

		UploadedFile uf = new UploadedFile(originalFileName, ext, newFileName, item.getSize());

		System.out.println(uf.toString());

		return uf;
	}

}
