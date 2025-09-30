package kr.letech.study.cmmn.rest;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import kr.letech.study.cmmn.user.vo.UserVO;
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
 *  2025-09-26  KSY			최초 생성
 */
@Slf4j
@Service
public class RestTemplateService {
	
	@Autowired
	private RestTemplate restTemplate;
	private final String BASE_URL = "http://localhost:8081";
	private final Gson gson = new Gson();
	
	/**
	 * 유저 등록 처리
	 * @param userVO
	 * @return
	 */
	public int insertOrUpdateUser(UserVO userVO) {
		// url
		URI uri = UriComponentsBuilder.fromUriString(BASE_URL)
					 .path("/api/users")
					 .build()
					 .toUri();
		
		// 헤더 정보 생성
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		
		// 리퀘스트 엔티티생성
		RequestEntity<UserVO> userEntity = RequestEntity.post(uri).body(userVO);
		ResponseEntity<?> response = restTemplate.exchange(userEntity, String.class);
		
		
		// 응답 본문 파싱
		String responseBody = (String) response.getBody();
		Map<String, Object> body = gson.fromJson(responseBody, new TypeToken<Map<String, Object>>() {}.getType());
		log.info("응답 {}",body);
		String status = (String) body.get("status");
		log.info(HttpStatus.OK.name());
		int result = 0;
		if(HttpStatus.OK.name().equals(status)) {
			result = 1;
		}else {
			result = -1;
			log.error((String) body.get("msg"));
		}
		
		return result;
	}
	
	/**
	 * 유저 수정 & 논리삭제 처리
	 * @param userVO
	 * @return
	 */
	public int updateOrDeleteUser(UserVO userVO) {
		// url
		URI uri = UriComponentsBuilder.fromUriString(BASE_URL)
					 .path("/api/users/"+userVO.getUserId())
					 .build()
					 .toUri();
		
		// 헤더 정보 생성
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		
		// 리퀘스트 엔티티생성
		RequestEntity<UserVO> userEntity = RequestEntity.post(uri).body(userVO);
		ResponseEntity<?> response = restTemplate.exchange(userEntity, String.class);
		
		
		// 응답 본문 파싱
		String responseBody = (String) response.getBody();
		Map<String, Object> body = gson.fromJson(responseBody, new TypeToken<Map<String, Object>>() {}.getType());
		log.info("응답 {}",body);
		String status = (String) body.get("status");
		log.info(HttpStatus.OK.name());
		int result = 0;
		if(HttpStatus.OK.name().equals(status)) {
			result = 1;
		}else {
			result = -1;
			log.error((String) body.get("msg"));
		}
		
		return result;
	}

	/**
	 * 유저정보 및 파일 정보 가져오기
	 * @param inputUserId
	 * @return
	 */
	public UserVO findUser(String inputUserId) {
		URI uri = UriComponentsBuilder.fromUriString(BASE_URL)
					.path("/api/users/"+inputUserId)
					.build()
					.toUri();
		
		// 헤더 정보 생성
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		String response = restTemplate.getForObject(uri, String.class);
		
		UserVO user = gson.fromJson(response, UserVO.class);
		
		log.info("user : {}",user);
		
		return user;
	}

	/**
	 * 유저권한 코드만 처리
	 * @param inputUserId
	 * @return
	 */
	public List<String> findUserAuthByUserId(String inputUserId) {
		URI uri = UriComponentsBuilder.fromUriString(BASE_URL)
				.path("/api/users/"+inputUserId+"/auth")
				.build()
				.toUri();
	
		// 헤더 정보 생성
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		String response = restTemplate.getForObject(uri, String.class);
		
		List<String> authList = gson.fromJson(response, new TypeToken<ArrayList<String>>(){}.getType());
		
		log.info("authList : {}",authList);
		
		return authList;
	}

	/**
	 * 유저정보 + 가지고있는 파일 정보 + 권한정보
	 * @param userVO
	 * @return
	 */
	public UserVO findAllUserWithFilesAndAuths(UserVO userVO) {
		// url
		URI uri = UriComponentsBuilder.fromUriString(BASE_URL)
					 .path("/api/users/"+userVO.getUserId())
					 .build()
					 .toUri();
		
		// 헤더 정보 생성
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		String response = restTemplate.getForObject(uri, String.class);
		
		UserVO user = gson.fromJson(response, UserVO.class);
		
		log.info("user : {}",user);
		
		return user;
	}

	/**
	 * 유저 정보만 가져오기
	 * @param userId
	 * @return
	 */
	public UserVO findUserOnly(String userId) {
		// url
		URI uri = UriComponentsBuilder.fromUriString(BASE_URL)
					 .path("/api/users/"+userId)
					 .build()
					 .toUri();
		
		// 헤더 정보 생성
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		String response = restTemplate.getForObject(uri, String.class);
		
		UserVO user = gson.fromJson(response, UserVO.class);
		
		log.info("user : {}",user);
		
		return user;
	}

	/**
	 * @param rollbackUser
	 */
	public void rollbackUser(UserVO rollbackUser) {
		// url
		URI uri = UriComponentsBuilder.fromUriString(BASE_URL)
					 .path("/api/users/"+rollbackUser.getUserId()+"/revert")
					 .build()
					 .toUri();
		
		// 헤더 정보 생성
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		
		// 리퀘스트 엔티티생성
		RequestEntity<UserVO> userEntity = RequestEntity.put(uri).body(rollbackUser);
		restTemplate.exchange(userEntity, String.class);
		
		
	}
	
	
}
