/**
 * 
 */
package kr.letech.study.cmmn.user.controller;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.letech.study.cmmn.code.service.ICodeService;
import kr.letech.study.cmmn.code.vo.CodeVO;
import kr.letech.study.cmmn.sec.aop.AuthCheck;
import kr.letech.study.cmmn.user.service.IUserService;
import kr.letech.study.cmmn.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 
 * </pre>
 *  
 * << 개정이력 >>
 *   
 *  수정일      수정자		수정내용
 *  ------------------------------------------------
 *  2025-09-12  KSY			최초 생성
 *  						리스트, 생성, 상세페이지 완성
 *  2025-09-15  KSY			수정, 삭제 완성
 */
@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/cmmn/user")
public class UserController {

	private final IUserService userService;
	private final ICodeService codeService;
	
	@AuthCheck
	@RequestMapping(value = "/userList.do", method = RequestMethod.GET)
	public String userList(UserVO userVO,Model model) {
		log.info("리스트 요청");
		log.info("파람, {}", userVO);
		
		List<UserVO> userList = userService.selectUserList(userVO);
		log.info("userList : {}", userList);
		model.addAttribute("userList", userList);
		return "cmmn/user/userList.tiles";
	}
	
	@RequestMapping(value="/userRegisterForm.do", method = RequestMethod.GET)
	public String userRegistForm(Model model) {
		List<CodeVO> codeList = codeService.selectCodeList("AUTH");
		
		model.addAttribute("codeList", codeList);
		return "cmmn/user/userRegisterForm.tiles";
	}
	
	@RequestMapping(value="/userRegister.do", method = RequestMethod.POST)
	public String userRegister(UserVO userVO, Model model) {
		log.info("등록요청 userVO -> {}",userVO);
		String path = null;
		
		int status = userService.insertUser(userVO);
		
		if(status > 0) {
			path = "redirect:/";
		}else {
			List<CodeVO> codeList = codeService.selectCodeList("AUTH");
			
			model.addAttribute("codeList", codeList);
			model.addAttribute("msg", "등록처리 중 오류가 발생했습니다.");
			path = "cmmn/user/userRegisterForm.tiles";
		}
		
		return path;
	}
	
	@AuthCheck
	@RequestMapping(value = "/userDetail.do", method = RequestMethod.GET)
	public String userDetail(UserVO userVO, Model model) {
		log.info("상세페이지 userVO -> {}", userVO);
		
		UserVO targetVO = userService.selectUserOne(userVO);
		
		model.addAttribute("userVO", targetVO);
		return "cmmn/user/userDetail.tiles";
	}
	
	@AuthCheck
	@RequestMapping(value="/userUpdateForm.do", method = RequestMethod.GET)
	public String userUpdateForm(UserVO userVO, Model model) {
		log.info("update form -> {}", userVO);
		List<CodeVO> codeList = codeService.selectCodeList("AUTH");
		UserVO targetVO = userService.selectUserOne(userVO);
		
		model.addAttribute("codeList", codeList);
		model.addAttribute("userVO", targetVO);
		return "cmmn/user/userUpdateForm.tiles";
	}
	
	@RequestMapping(value="/userUpdate.do", method = RequestMethod.POST)
	public String userUpdate(UserVO userVO, Model model) {
		log.info("업데이트 로직 userVO -> {}", userVO);
		String path = null;
		
		int status = userService.updateUser(userVO);
		if(status > 0) {
			path = "redirect:/cmmn/user/userDetail.do?userId="+userVO.getUserId();
		}else {
			model.addAttribute("userVO", userVO);
			model.addAttribute("msg", "수정 처리 중 오류가 발생했습니다.");
			path = "cmmn/user/userUpdateForm.tiles";
		}
		
		return path;
	}
	
	@RequestMapping(value="/userDelete.do", method = RequestMethod.POST)
	public String userDelete(UserVO userVO, RedirectAttributes ra) {
		log.info("논리적 삭제 로직 userVO -> {}", userVO);
		String path = null;
		// 리뷰 - 논리적 삭제여서 update지만 의미를 명확하게 하기위해 delete문구를 사용하는게 좋다
		int status = userService.deleteUser(userVO);
		if(status > 0) {
			SecurityContextHolder.clearContext();
			path = "redirect:/";
		}else {
			ra.addFlashAttribute("msg", "삭제 처리 중 오류가 발생했습니다");
			path = "redirect:/cmmn/user/userDetail.do?userId="+userVO.getUserId();
		}
		
		return path;
	}
	
}
