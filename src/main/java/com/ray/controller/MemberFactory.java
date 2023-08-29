package com.ray.controller;

import com.ray.service.MemberService;
import com.ray.service.member.ConfirmMailCodeService;
import com.ray.service.member.DuplicateUserIdService;
import com.ray.service.member.LoginMemberService;
import com.ray.service.member.LogoutMemberService;
import com.ray.service.member.MyPageService;
import com.ray.service.member.RegisterMemberService;
import com.ray.service.member.SendMailService;
import com.ray.service.member.modifyEmailService;
import com.ray.service.member.modifyImgService;

public class MemberFactory {
	private static MemberFactory instance = null;
	
	private boolean isRedirect;  // redirect 할것인지 말것인지 
	private String whereIsGo;  // 어느 view단으로 이동할것인지
	
	private MemberFactory() { }
	
	public static MemberFactory getInstance() {
		if (instance == null) {
			instance = new MemberFactory();
		}
		
		return instance;
	}
	
	
	
	
	
	public boolean isRedirect() {
		return isRedirect;
	}

	public void setRedirect(boolean isRedirect) {
		this.isRedirect = isRedirect;
	}

	public String getWhereIsGo() {
		return whereIsGo;
	}

	public void setWhereIsGo(String whereIsGo) {
		this.whereIsGo = whereIsGo;
	}

	// command를 매개변수로 받아 해당 기능을 수행하는 객체를 반환함.
	public MemberService getService(String command) {
		MemberService result = null;
		
		if (command.equals("/member/duplicateUserId.mem")) {
			// 아이디 중복 검사를 할 수 있는 객체를 만들어서 반환
			result = new DuplicateUserIdService();
			
		} else if (command.equals("/member/registerMember.mem")) {
			// 회원 가입을 할 수 있는 객체를 만들어서 반환
			result = new RegisterMemberService();
		} else if (command.equals("/member/sendMail.mem")) {
			// 인증코드를 메일로 보내는 기능을 할 수 잇는 객체를 만들어서 반환
			result = new SendMailService();
		} else if (command.equals("/member/confirmCode.mem")) {
			result = new ConfirmMailCodeService();
		} else if (command.equals("/member/login.mem")) {
			result = new LoginMemberService();
		} else if (command.equals("/member/logout.mem")) {
			result = new LogoutMemberService();
		} else if (command.equals("/member/myPage.mem")) {
			result = new MyPageService();
		}else if (command.equals("/member/modifyEmail.mem")) {
			result = new modifyEmailService();
		}else if (command.equals("/member/modifyImg.mem")) {
			result = new modifyImgService();
		}
		
		return result;
	}
}
