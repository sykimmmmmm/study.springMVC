/**
 * 
 */
package kr.letech.study.cmmn.board.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.letech.study.cmmn.board.dao.BoardDAO;
import kr.letech.study.cmmn.board.service.IBoardService;
import kr.letech.study.cmmn.board.service.IReplyService;
import kr.letech.study.cmmn.board.vo.BoardVO;
import kr.letech.study.cmmn.file.service.IFileService;
import kr.letech.study.cmmn.file.vo.FileVO;
import kr.letech.study.cmmn.utils.UserUtils;
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
@Service
@Slf4j
@RequiredArgsConstructor
public class BoardServiceImpl implements IBoardService{

	private final IFileService fileService;
	private final IReplyService replyService;
	private final BoardDAO boardDAO;
	
	private final String FILE_DIV = "board";

	@Override
	public List<BoardVO> selectBoardList(String searchType, String searchValue) {
		BoardVO boardVO = new BoardVO();
		if("title".equals(searchType) && StringUtils.isNotBlank(searchValue)) {
			boardVO.setBoardTitle(searchValue);
		}
		if("author".equals(searchType) && StringUtils.isNotBlank(searchValue)) {
			boardVO.setRgstId(searchValue);
		}
		
		return boardDAO.selectBoardList(boardVO);
	}
	
	@Transactional
	@Override
	public int insertBoard(BoardVO boardVO) {
		MultipartFile[] boFiles = boardVO.getBoFiles();
		if(boFiles != null && StringUtils.isNotBlank(boFiles[0].getOriginalFilename())) {
			String fileGrpId = fileService.insertFile(boFiles, null, this.FILE_DIV);
			boardVO.setFileGrpId(fileGrpId);
		}
		
		return boardDAO.insertBoard(boardVO);
	}
	
	@Transactional
	@Override
	public int updateBoard(BoardVO boardVO) {
		List<Integer> deleteFileNoList = boardVO.getDeleteFileNoList();
		if(deleteFileNoList != null && !deleteFileNoList.isEmpty()) {
			for(int fileNo : deleteFileNoList) {
				FileVO fileVO = new FileVO();
				fileVO.setFileGrpId(boardVO.getFileGrpId());
				fileVO.setFileNo(fileNo);
				fileService.deleteFileOne(fileVO);
			}
		}
		MultipartFile[] boFiles = boardVO.getBoFiles();
		if(boFiles != null && StringUtils.isNotBlank(boFiles[0].getOriginalFilename())) {
			String fileGrpId = fileService.insertFile(boFiles, boardVO.getFileGrpId(), FILE_DIV);
			boardVO.setFileGrpId(fileGrpId);
		}
		boardVO.setUpdtId(UserUtils.getUserId());
		return boardDAO.updateBoard(boardVO);
	}
	
	@Transactional
	@Override
	public int deleteBoard(BoardVO boardVO) {
		int status = 0;
		int[] boardDeleteNos = boardVO.getDeleteBoardNos();
		// 목록 리스트에서 게시글 삭제
		if(boardDeleteNos != null && boardDeleteNos.length > 0) {
			BoardVO vo = new BoardVO();
			for(int boardId : boardDeleteNos) {
				vo = this.selectBoardOnly(boardId);
				if(vo != null) {
					status = this.deleteBoard(vo);
				}
			}
		// 상세 페이지에서 삭제
		}else {
			
			if(StringUtils.isNotBlank(boardVO.getFileGrpId())) {
				fileService.deleteFileAll(boardVO.getFileGrpId());
			}
			boardVO.setUpdtId(UserUtils.getUserId());
			replyService.deleteReplyAll(boardVO);
			status = boardDAO.deleteBoard(boardVO);
		}
		
		return status;
	}
	
	@Override
	public BoardVO selectBoardOne(int boardId) {
		return boardDAO.selectBoardOne(boardId);
	}
	
	@Override
	public BoardVO selectBoardOnly(int boardId) {
		return boardDAO.selectBoardOnly(boardId);
	}
	
}
