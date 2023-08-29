package com.ray.service.member;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.ray.controller.MemberFactory;
import com.ray.etc.SendMail;
import com.ray.service.MemberService;

public class SendMailService implements MemberService {

	@Override
	public MemberFactory executeService(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String userMailAddr = request.getParameter("tmpUserEmail");
		
		// 인증 코드를 만들고 인증 코드를 세션에 저장(나중에 코드확인버튼을 누르면 확인해야 하므로)
		String code = UUID.randomUUID().toString();
		request.getSession().setAttribute("authCode", code);
		
		
		System.out.println(userMailAddr + "로 메일(" + code + ")을 보내자");
		
		response.setContentType("application/json; charset=utf-8;");
		PrintWriter out = response.getWriter();
		
		Map<String, String> jsonMap = new HashMap<String, String>();
		
		try {
			SendMail.sendMail(userMailAddr, code);	
			jsonMap.put("status", "success");
		} catch (MessagingException e) {
			e.printStackTrace();
			jsonMap.put("status", "fail");
		}
		
		JSONObject json = new JSONObject(jsonMap);
		out.print(json.toJSONString());
		
		out.flush();
		out.close();
		
		return null;
	}

}
