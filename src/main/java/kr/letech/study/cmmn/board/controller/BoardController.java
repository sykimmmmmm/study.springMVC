/**
 * 
 */
package kr.letech.study.cmmn.board.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import kr.letech.study.cmmn.board.service.IBoardService;
import kr.letech.study.cmmn.board.vo.BoardVO;
import kr.letech.study.cmmn.sec.aop.AuthCheck;
import kr.letech.study.cmmn.user.service.IUserService;
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
 *  2025-09-16  KSY			최초 생성
 */
@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value="/cmmn/board")
public class BoardController {

	private final IBoardService boardService;
	private final IUserService userService;
	
	@AuthCheck
	@RequestMapping(value = "/boardList.do", method = RequestMethod.GET)
	public String boardList(
			  @RequestParam(name = "searchType", required = false) String searchType
			, @RequestParam(name = "searchValue", required = false) String searchValue
			, Model model) {
		log.info("목록 불러오기 -> {}",searchType, searchValue );
		
		List<BoardVO> boardList = boardService.selectBoardList(searchType, searchValue);
		log.info("boardList -> {}", boardList);
		model.addAttribute("boardList", boardList);
		return "cmmn/board/boardList.tiles";
	}
	
	@AuthCheck
	@RequestMapping(value = "/boardRegisterForm.do", method = RequestMethod.GET)
	public String boardRegisterForm(Model model) {
		
		return "cmmn/board/boardRegisterForm.tiles";
	}
	
	@RequestMapping(value = "/boardRegister.do", method = RequestMethod.POST)
	public String boardRegister(BoardVO boardVO) {
		log.info("게시글 등록 -> {}", boardVO);
		String path = null;
		int status = boardService.insertBoard(boardVO);
		if(status > 0) {
			path = "redirect:/cmmn/board/boardDetail.do?boardId="+boardVO.getBoardId();
		}else {
			path = "redirect:/cmmn/board/boardRegisterForm.do";
		}
		return path;
	}
	
	@AuthCheck
	@RequestMapping(value = "/boardDetail.do", method = RequestMethod.GET)
	public String boardDetail(int boardId, Model model) {
		log.info("상세페이지 -> {}", boardId);
		BoardVO boardVO = boardService.selectBoardOne(boardId);
		log.error("boardVO -> {}",boardVO);
		model.addAttribute("boardVO", boardVO);
		return "cmmn/board/boardDetail.tiles";
	}
	
	@AuthCheck
	@RequestMapping(value = "/boardUpdateForm.do", method = RequestMethod.GET)
	public String boardUpdateForm(int boardId, Model model) {
		log.info("업데이트폼 요청 -> {}", boardId);
		BoardVO boardVO = boardService.selectBoardOnly(boardId);
		model.addAttribute("boardVO", boardVO);
		return "cmmn/board/boardUpdateForm.tiles";
	}
	
	@RequestMapping(value = "/boardUpdate.do", method = RequestMethod.POST)
	public String boardUpdate(BoardVO boardVO) {
		log.info("업데이트 요청 -> {}", boardVO);
		String path = null;
		int status = boardService.updateBoard(boardVO);
		if(status > 0) {
			path = "redirect:/cmmn/board/boardDetail.do?boardId="+boardVO.getBoardId();
		}else {
			path = "redirect:/cmmn/board/boardUpdateForm.do?boardId="+boardVO.getBoardId();
		}
		return path;
	}
	
	@RequestMapping(value = "/boardDelete.do", method = RequestMethod.POST)
	public String boardDelete(BoardVO boardVO) {
		log.info("삭제 요청 - > {}", boardVO);
		String path = null;
		int status = boardService.deleteBoard(boardVO);
		if(status > 0) {
			path = "redirect:/cmmn/board/boardList.do";
		}else {
			path = "redirect:/cmmn/board/boardDetail.do?boardId="+boardVO.getBoardId();
		}
		return path;
	}
}
