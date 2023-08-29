package com.ray.service.member;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
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

import com.ray.controller.MemberFactory;
import com.ray.dao.MemberCRUD;
import com.ray.dao.MemberDAO;
import com.ray.etc.UploadedFile;
import com.ray.service.MemberService;
import com.ray.vo.Member;

public class modifyImgService implements MemberService {

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
		String userImg = "";

		System.out.println(userId);
		System.out.println(userId);

		// 파일이 저장될 공간의 경로, 사이즈 등의 환경설정 정보를 가지고 있는 객체
		DiskFileItemFactory factory = new DiskFileItemFactory(MEMORY_THRESHOLD, saveFileDir);

		// 실제 request로 넘겨져온 매개변수를 통해 파일을 upload 처리 할 객체
		ServletFileUpload sfu = new ServletFileUpload(factory);

		UploadedFile uf = null;

		try {
			List<FileItem> lst = sfu.parseRequest(request);
			System.out.println(lst);

			for (FileItem item : lst) {
				System.out.println(item);

				if (item.isFormField()) { // 파일이 아닌 일반 데이터이다.
					if (item.getFieldName().equals("userId")) {
						userId = item.getString(encoding);
					}
				} else if (item.isFormField() == false && item.getName() != "") { // 업로드된 파일인 경우
					// 파일을 저장해야 하는데 파일의 이름이 중복되는 경우가 있기 때문에...
					// 아래의 처리를 한다.

					// 1) 중복되지 않을 새이름으로 저장.
					uf = getNewFileName(item, realPath, userId);

					File uploadFile = new File(realPath + File.separator + uf.getNewFeileName());
					try {
						item.write(uploadFile); // 업로드된 파일을 하드디스크에 저장
					} catch (Exception e) {
						// 업로드된 파일이 실제 저장될 때 예외 발생
						e.printStackTrace();

						mf.setRedirect(true);
						mf.setWhereIsGo(request.getContextPath() + "/member/register.jsp?status=fail");

						return mf;
					}
					
					MemberDAO mdao = MemberCRUD.getInstance();
					int result = -1;
					if (uf != null) { // 업로드된 파일이 있는 경우
						uf.setNewFeileName("memberImg/" + uf.getNewFeileName());
		
					}

				}
			}

		} catch (FileUploadException e) {
			e.printStackTrace();
			mf.setRedirect(true);
			mf.setWhereIsGo(request.getContextPath()+"/member/register.jsp?status=fail");
			
			return mf;
		}

		return null;
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
