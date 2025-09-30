/**
 * 
 */
package kr.letech.study.cmmn.user.service.impl;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.letech.study.cmmn.file.service.IFileService;
import kr.letech.study.cmmn.file.vo.FileVO;
import kr.letech.study.cmmn.rest.RestTemplateService;
import kr.letech.study.cmmn.user.dao.UserDAO;
import kr.letech.study.cmmn.user.service.IUserService;
import kr.letech.study.cmmn.user.vo.UserAuthVO;
import kr.letech.study.cmmn.user.vo.UserVO;
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
	private final UserDetailsService userDetailsService;
	private final RestTemplateService restTemplateService;
	
	private final String FILE_DIV = "user";
	
	@Override
	public List<UserVO> selectUserList(UserVO userVO) {
		return userDAO.selectUserList(userVO);
	}
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void insertUser(UserVO userVO){
		boolean restSuccess = false;
		UserVO rollbackUser = null;
		String fileGrpId = null;
		
		if(StringUtils.isBlank(userVO.getUserId())) {
			return;
		}
		// 입력받은 비밀번호 암호화처리 후 셋팅
		if(StringUtils.isNotBlank(userVO.getUserPw())) {
			String encodedPw = passwordEncoder.encode(userVO.getUserPw());
			userVO.setUserPw(encodedPw);
		}
		// 등록자 , 수정자 id 세팅
		String userId = userVO.getUserId();
		userVO.setRgstId(userId);
		userVO.setUpdtId(userId);
		userVO.setDelYn("N");
//		
		// 파일 존재할시 파일 등록 후 fileGrpId 맵핑
		MultipartFile[] boFiles = userVO.getBoFiles();
		if(!ArrayUtils.isEmpty(boFiles) && StringUtils.isNotBlank(boFiles[0].getOriginalFilename())) {
			fileGrpId = fileService.insertFile(boFiles,null,this.FILE_DIV);
			userVO.setFileGrpId(fileGrpId);
		}
		
		rollbackUser = this.restTemplateService.findUserOnly(userVO.getUserId());
		if(rollbackUser != null) {
			throw new RuntimeException("이미 존재하는 사용자");
		}
		// rest호출로 유저정보 등록해보기
		int response = restTemplateService.insertOrUpdateUser(userVO);
		if(response <= 0) {
			throw new RuntimeException();
		}else {
			restSuccess = true;
		}
		
		try {	
			// 체크된 권한 가져와서 등록하기
			List<String> authList = userVO.getAuthList();
			if(authList != null && !authList.isEmpty()) {
				//권한 등록
				UserAuthVO authVO = new UserAuthVO();
				authVO.setUserId(userVO.getUserId());
				authVO.setRgstId(userVO.getUserId());
				authVO.setUpdtId(userVO.getUserId());
				for(String auth : authList) {
					authVO.setDelYn("N");
					authVO.setUserAuth(auth);
					userDAO.mergeUserAuth(authVO);
				}
				
			}
		} catch (Exception e) {
			if(restSuccess) {
				if(rollbackUser == null) {
					rollbackUser = new UserVO();
					rollbackUser.setUserId(userVO.getUserId());
				}
				restTemplateService.rollbackUser(rollbackUser);
			}
			fileService.deleteFileAll(fileGrpId);
			throw e;
		}
			
	}
	
	@Override
	public UserVO selectUserOne(UserVO userVO) {
//		UserVO vo = userDAO.selectUserOne(userVO);
		UserVO vo = this.restTemplateService.findAllUserWithFilesAndAuths(userVO);
		return vo;
	}
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void updateUser(UserVO userVO) {
		boolean restSuccess = false;
		UserVO rollbackUser = this.restTemplateService.findUserOnly(userVO.getUserId());
		
			// 업로드할 파일 이 있는지 확인 후 기존 파일 삭제 및 신규 파일 등록
			MultipartFile[] boFiles = userVO.getBoFiles();
			log.info("boFiles : {}",boFiles);
			if(boFiles != null && StringUtils.isNotBlank(boFiles[0].getOriginalFilename())) {
//			 기존에 존재하던 파일이 있는경우 파일 삭제
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
			
			// 수정자id 설정
			userVO.setUpdtId(userVO.getUserId());
			
			// rest호출로 유저정보 등록해보기
			int response = restTemplateService.updateOrDeleteUser(userVO);
			
			if(response <= 0) {
				throw new RuntimeException();
			}else {
				// 수정 완료
				restSuccess = true;
			}
			
			try {
				// 유저 권한 업데이트
				List<String> authList = userVO.getAuthList();
				if(!authList.isEmpty() || authList.size() > 0) {
					
					//권한 전부 삭제 처리
					userVO.setUpdtId(userVO.getUserId());
					userDAO.deleteUserAuth(userVO);
					
					//권한 추가 혹은 수정
					UserAuthVO authVO = new UserAuthVO();
					authVO.setUserId(userVO.getUserId());
					authVO.setRgstId(UserUtils.getUserId());
					authVO.setUpdtId(UserUtils.getUserId());
					authVO.setUpdtDt(userVO.getUserId());
					for(String auth : authList) {
						authVO.setUserAuth(auth);
						userDAO.mergeUserAuth(authVO);
					}
			}
			// 헤더의 프로필이미지 동기화 및 권한을 반영 하기위한 authentication 갱신
			if(userVO.getUserId().equals(UserUtils.getUserId())) {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				UserDetails updatedUser = userDetailsService.loadUserByUsername(userVO.getUserId());
				
				UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(updatedUser, auth.getCredentials(), updatedUser.getAuthorities());
				
				SecurityContextHolder.getContext().setAuthentication(newAuth);
			}
		} catch (RuntimeException e) {
			if(restSuccess) {
				restTemplateService.rollbackUser(rollbackUser);
				// 헤더의 프로필이미지 동기화 및 권한을 반영 하기위한 authentication 갱신
				if(userVO.getUserId().equals(UserUtils.getUserId())) {
					Authentication auth = SecurityContextHolder.getContext().getAuthentication();
					UserDetails updatedUser = userDetailsService.loadUserByUsername(userVO.getUserId());
					
					UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(updatedUser, auth.getCredentials(), updatedUser.getAuthorities());
					
					SecurityContextHolder.getContext().setAuthentication(newAuth);
				}
			}
			throw e;
		}
	}
	
	@Transactional
	@Override
	public void deleteUser(UserVO userVO) {
		boolean restSuccess = false;
		UserVO rollbackUser = this.restTemplateService.findUserOnly(userVO.getUserId());
		
		userVO.setUpdtId(userVO.getUserId());
		String fileGrpId = userVO.getFileGrpId();
		// 파일 삭제
		fileService.deleteFileAll(fileGrpId);
		// 권한 삭제
		userDAO.deleteUserAuth(userVO);
		// 유저 정보 삭제
		userVO.setDelYn("Y");
		try {
			int status = restTemplateService.updateOrDeleteUser(userVO);
			
			if(status <= 0) {
				throw new RuntimeException();
			}else {
				restSuccess = true;
			}
			
			// 컨텍스트 정보 제거 로그아웃처리
			SecurityContextHolder.clearContext();
		} catch (RuntimeException e) {
			if(restSuccess) {
				restTemplateService.rollbackUser(rollbackUser);
			}
			throw e;
		}
		
	}
	
	@Override
	public List<UserVO> selectUserListTemp() {
		return userDAO.selectUserListTemp();
	}
}
