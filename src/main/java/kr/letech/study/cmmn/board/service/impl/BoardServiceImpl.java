/**
 * 
 */
package kr.letech.study.cmmn.board.service.impl;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
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

	/**
	 * 특정 기준으로 게시글 불러오기
	 * @param searchType (title, author)
	 * @param searchValue
	 */
	@Override
	public List<BoardVO> selectBoardList(String searchType, String searchValue) {
		BoardVO boardVO = new BoardVO();
		// 제목 과 작성자를 기준으로 검색
		if("title".equals(searchType) && StringUtils.isNotBlank(searchValue)) {
			boardVO.setBoardTitle(searchValue);
		}
		if("author".equals(searchType) && StringUtils.isNotBlank(searchValue)) {
			boardVO.setRgstId(searchValue);
		}
		
		return boardDAO.selectBoardList(boardVO);
	}
	
	/**
	 * 게시글 등록
	 * @param boardVO (boardTitle, boardContent, rgstId, boFiles[])
	 */
	@Transactional
	@Override
	public void insertBoard(BoardVO boardVO) {
		MultipartFile[] boFiles = boardVO.getBoFiles();
		// 파일이 있거나 해당파일의 이름이 '' 이 아닐시 파일서비스 호출 후 파일 그룹번호 반환하기 
		if(!ArrayUtils.isEmpty(boFiles) && StringUtils.isNotBlank(boFiles[0].getOriginalFilename())) {
			String fileGrpId = fileService.insertFile(boFiles, null, this.FILE_DIV);
			boardVO.setFileGrpId(fileGrpId);
		}
			
		boardDAO.insertBoard(boardVO);
	}
	
	/**
	 * 게시글 업데이트
	 * @param boardVO (boardTitle, boardContent, fileGrpId, deleteFileNoList, boFiles[])
	 */
	@Transactional
	@Override
	public void updateBoard(BoardVO boardVO) {
		int[] deleteFileNos = boardVO.getDeleteBoardNos();
//		// 특정 파일 삭제
		if(deleteFileNos != null && deleteFileNos.length > 0) {
			for(int fileNo : deleteFileNos) {
				FileVO fileVO = new FileVO();
				fileVO.setFileGrpId(boardVO.getFileGrpId());
				fileVO.setFileNo(fileNo);
				fileService.deleteFileOne(fileVO);
			}
		}
//		// 파일이 있을 시 파일 추가
		MultipartFile[] boFiles = boardVO.getBoFiles();
		if(!ArrayUtils.isEmpty(boFiles) && StringUtils.isNotBlank(boFiles[0].getOriginalFilename())) {
			String fileGrpId = fileService.insertFile(boFiles, boardVO.getFileGrpId(), FILE_DIV);
			boardVO.setFileGrpId(fileGrpId);
		}
		boardVO.setUpdtId(UserUtils.getUserId());
		boardDAO.updateBoard(boardVO);
	}
	
	/**
	 * 게시글 삭제
	 * @param boardVO (boardId, [deleteBoardNos])
	 */
	@Transactional
	@Override
	public void deleteBoard(BoardVO boardVO) {
		int[] boardDeleteNos = boardVO.getDeleteBoardNos();
		// 목록 리스트에서 게시글 삭제
		if(boardDeleteNos != null && boardDeleteNos.length > 0) {
			BoardVO vo = new BoardVO();
			for(int boardId : boardDeleteNos) {
				vo = this.selectBoardOnly(boardId);
				if(vo != null) {
					this.deleteBoard(vo);
				}
			}
		// 상세 페이지에서 삭제
		}else {
			if(boardVO.getBoardId() == 0) {
				return;
			}
			if(StringUtils.isNotBlank(boardVO.getFileGrpId())) {
				fileService.deleteFileAll(boardVO.getFileGrpId());
			}
			boardVO.setUpdtId(UserUtils.getUserId());
			replyService.deleteReplyAll(boardVO);
			boardDAO.deleteBoard(boardVO);
		}
		
	}
	
	/**
	 * 특정 게시글 가져오기(게시글, 첨부파일, 댓글에 대한 모든 정보)
	 * @param boardId
	 */
	@Override
	public BoardVO selectBoardOne(int boardId) {
		// 게시글, 첨부파일, 댓글에 대한 모든 정보 가져오기
		return boardDAO.selectBoardOne(boardId);
	}
	
	/**
	 * 특정 게시글 가져오기(게시글, 첨부파일에 대한 모든 정보)
	 * @param boardId
	 */
	@Override
	public BoardVO selectBoardOnly(int boardId) {
		// 게시글, 첨부파일에 대한 정보만 가져오기
		return boardDAO.selectBoardOnly(boardId);
	}
	
}
