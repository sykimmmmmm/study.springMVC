/**
 * 
 */
package kr.letech.study.cmmn.user.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.letech.study.cmmn.file.service.IFileService;
import kr.letech.study.cmmn.file.vo.FileVO;
import kr.letech.study.cmmn.user.dao.UserDAO;
import kr.letech.study.cmmn.user.service.IUserService;
import kr.letech.study.cmmn.user.vo.UserAuthVO;
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
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
	
	private final UserDAO userDAO;
	private final IFileService fileService;
	private final PasswordEncoder passwordEncoder;
	
	private final String FILE_DIV = "user";
	
	@Override
	public List<UserVO> selectUserList(UserVO userVO) {
		return userDAO.selectUserList(userVO);
	}
	
	@Transactional
	@Override
	public int insertUser(UserVO userVO){
		int status = 0;
		// 입력받은 비밀번호 암호화처리 후 셋팅
		String encodedPw = passwordEncoder.encode(userVO.getUserPw());
		userVO.setUserPw(encodedPw);
		
		try {
			// 파일 존재할시 파일 등록 후 fileGrpId 맵핑
			MultipartFile[] boFiles = userVO.getBoFiles();
			if(boFiles != null && StringUtils.isNotBlank(boFiles[0].getOriginalFilename())) {
				String fileGrpId = fileService.insertFile(boFiles,null,this.FILE_DIV);
				userVO.setFileGrpId(fileGrpId);
			}
				
			// 유저 정보 등록
			status = userDAO.insertUser(userVO);
			
			// 체크된 권한 가져와서 등록하기
			List<String> authList = userVO.getAuthList();
			if(!authList.isEmpty() || authList.size() > 0) {
				//권한 등록혹은 업데이트
				UserAuthVO authVO = new UserAuthVO();
				authVO.setUserId(userVO.getUserId());
				authVO.setRgstId(userVO.getUserId());
				authVO.setUpdtDt(userVO.getUserId());
				for(String auth : authList) {
					authVO.setUserAuth(auth);
					userDAO.mergeUserAuth(authVO);
				}
				
			}
		} catch (Exception e) {
			status = -1;
		}
		return status;
	}
	
	@Override
	public UserVO selectUserOne(UserVO userVO) {
		UserVO vo = userDAO.selectUserOne(userVO);
		return vo;
	}
	
	@Transactional
	@Override
	public int updateUser(UserVO userVO) {
		int status = 0;
		try {
			// 업로드할 파일 이 있는지 확인 후 기존 파일 삭제 및 신규 파일 등록
			MultipartFile[] boFiles = userVO.getBoFiles();
			log.info("boFiles : {}",boFiles);
			if(boFiles != null && StringUtils.isNotBlank(boFiles[0].getOriginalFilename())) {
				// 기존에 존재하던 파일이 있는경우 파일 삭제
				if(StringUtils.isNotBlank(userVO.getDeleteFileNo())) {
					FileVO fileVO = new FileVO();
					fileVO.setFileGrpId(userVO.getFileGrpId());
					fileVO.setFileNo(Integer.parseInt(userVO.getDeleteFileNo()));
					fileVO.setUpdtId(userVO.getUserId());
					fileService.deleteFileOne(fileVO);
				}
				String fileGrpId = fileService.insertFile(boFiles, userVO.getFileGrpId(), FILE_DIV);
				userVO.setFileGrpId(fileGrpId);
			}
			
			// 비밀번호를 새로 입력했을 경우 암호화처리
			if(StringUtils.isNotBlank(userVO.getUserPw())) {
				String encodedPw = this.passwordEncoder.encode(userVO.getUserPw());
				userVO.setUserPw(encodedPw);
			}
			// 유저 정보 업데이트
			status = userDAO.updateUser(userVO);
			// 유저 권한 업데이트
			List<String> authList = userVO.getAuthList();
			if(!authList.isEmpty() || authList.size() > 0) {
				
				//권한 전부 삭제 처리
				userVO.setUpdtId(userVO.getUserId());
				userDAO.deleteUserAuth(userVO);
				
				//권한 추가 혹은 수정
				UserAuthVO authVO = new UserAuthVO();
				authVO.setUserId(userVO.getUserId());
				authVO.setRgstId(userVO.getUserId());
				authVO.setUpdtDt(userVO.getUserId());
				for(String auth : authList) {
					authVO.setUserAuth(auth);
					userDAO.mergeUserAuth(authVO);
				}
			 }
		} catch (Exception e) {
			status = -1;
		}
		return status;
	}
	
	@Transactional
	@Override
	public int deleteUser(UserVO userVO) {
		int status = 0;
		try {
			userVO.setUpdtId(userVO.getUserId());
			String fileGrpId = userVO.getFileGrpId();
			// 파일 삭제
			fileService.deleteFileAll(fileGrpId);
			// 권한 삭제
			userDAO.deleteUserAuth(userVO);
			// 유저 정보 삭제
			status = userDAO.deleteUser(userVO);
		} catch (Exception e) {
			status = -1;
		}
		return status;
	}
	
	@Override
	public List<UserVO> selectUserListTemp() {
		return userDAO.selectUserListTemp();
	}
}
