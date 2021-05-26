/*
 * AICC (AI Caption Center) version 1.0
 *
 *  Copyright ⓒ 2020 sorizava corp. All rights reserved.
 *
 *  This is a proprietary software of sorizava corp, and you may not use this file except in
 *  compliance with license agreement with sorizava corp. Any redistribution or use of this
 *  software, with or without modification shall be strictly prohibited without prior written
 *  approval of sorizava corp, and the copyright notice above does not evidence any actual or
 *  intended publication of such software.
 *
 * @package kr.co.aicc.modules.account.service
 * @file AccountServiceImpl.java
 * @author developer
 * @since 2020. 06. 08
 * @version 1.0
 * @title AICC
 * @description
 *
 *
 * << 개정이력 (Modification Information) >>
 *
 *   수정일		수정자	수정내용
 * -------------------------------------------------------------------------------
 * 2020. 06. 08	developer		 최초생성
 * -------------------------------------------------------------------------------
 */
package kr.co.aicc.modules.account.service;

import kr.co.aicc.infra.config.AppProperties;
import kr.co.aicc.infra.external.mail.EmailMessage;
import kr.co.aicc.infra.external.mail.EmailService;
import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.account.domain.LoginDetail;
import kr.co.aicc.modules.account.domain.Terms;
import kr.co.aicc.modules.account.dto.FindAccount;
import kr.co.aicc.modules.account.dto.SignUpForm;
import kr.co.aicc.modules.account.repository.AccountDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.codec.binary.Base64;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountDao accountDao;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final TemplateEngine templateEngine;
    private final AppProperties appProperties;
    private final EmailService emailService;
	private final SessionRegistry sessionRegistry;

    /**
     * 회원가입처리
     * @param signUpForm
     * @return
     */
    @Override
    public Account signUp(SignUpForm signUpForm) {
        Account account = modelMapper.map(signUpForm, Account.class);
        account.setPasswd(passwordEncoder.encode(account.getPasswd()));
        account.setBrthday(signUpForm.getBrthday().replaceAll("-", ""));	//생년월일
        account.setPtblTelNo(signUpForm.getPtblTelNoF() + signUpForm.getPtblTelNoM() + signUpForm.getPtblTelNoL());	//휴대전화번호
        account.generateEmailAuthToken();
        account.setStat("04");	//회원상태 - 승인대기
        account.setStatDtl("101");	//회원상태 - 정상회원
        account.setEmailAuthYn("N"); //이메일인증여부

        log.debug("account: {}",account);
        accountDao.insertMember(account);

        // 회원별 약관 동의 insert
    	int i;
    	String[] termsList = signUpForm.getTerms().split(",");

    	Long memNo = accountDao.findByMemId(signUpForm.getMemId()).getMemNo();

    	for (i=0; i<termsList.length; i++) {
        	Terms terms = new Terms();
        	terms.setTncNo(Long.parseLong(termsList[i]));
        	terms.setMemNo(memNo);
        	terms.setAgreeYn("Y");

    		accountDao.insertTermsList(terms);
    	}
    	account.setMemNo(memNo);
    	accountDao.insertMemRole(account);
        sendSignUpConfirmEmail(account);

        signUpForm.setMemNo(memNo);
        if ((!"".equals(signUpForm.getFileData()) && signUpForm.getFileData() != null)) {
	    	Account fileInfo = fileUpload(signUpForm);
	    	fileInfo.setMemId(account.getMemId());
	    	fileInfo.setMemNo(account.getMemNo());
	    	accountDao.updateMemFileInfo(fileInfo);
	    }

        return null;
    }


    /**
     * 파일 업로드
     * @param
     * @return
     */
	public Account fileUpload(SignUpForm signUpForm) {
		Account fileInfo = new Account();

		String seqNo = String.valueOf(signUpForm.getMemNo());
		String path = appProperties.getUploadDirProfile()  + "/" + getTodayPath();

		try {
			File file = new File(path);
			if (!file.exists()) {
				log.debug("파일 디렉토리가 없음");
				if (file.mkdirs()) {
					log.debug("디렉토리 생성 성공");
				}
			}
			
	        //base64 -> 파일 변환
			String base64Data = signUpForm.getFileData().substring(signUpForm.getFileData().indexOf(",")+1);
	        byte[] decode = Base64.decodeBase64(base64Data);
			
			String extName = "png";
			String fileNm = seqNo + "_PROFILE_";
			String sysFileNm = getRandomNm(fileNm, extName);
			String fileSize = "";
			
	        File base64file = new File(path + "/" + sysFileNm);	        	        

	        FileOutputStream fos = new FileOutputStream(base64file);
	        fos.write(decode);
	        fos.close();
	        
	        fileSize = String.valueOf(base64file.length());
			log.debug("fileNm ::: " + fileNm);
			log.debug("extName ::: " + extName);
			log.debug("sysFileNm ::: " + sysFileNm);
			log.debug("fileSize ::: " + fileSize);

	        //썸네일 생성
	        int width = 46;
	        int height = 60;
	        File thumb = new File(path + "/" + "T-" + sysFileNm);
	        BufferedImage originalImage = ImageIO.read(new File(path + "/" + sysFileNm));
	        Thumbnails.of(originalImage).size(width ,height).outputFormat(extName).toFile(thumb);	        

			fileInfo.setFileNm(fileNm); //프로필파일명
			fileInfo.setSysFileNm(sysFileNm); //프로필시스템파일명
			fileInfo.setFilePath(path); //프로필파일경로
			fileInfo.setFileSize(fileSize); //프로필파일사이즈
			fileInfo.setThumbSysFileNm(thumb.getName()); //썸네일시스템파일명
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return fileInfo;
	}
	
    /**
     * 파일 업로드
     * @param
     * @return
     */
	public Account fileUpload2(MultipartFile multipartFile, String seqNo) {
		Account fileInfo = new Account();

		String path = appProperties.getUploadDirProfile() + "/" + seqNo;

		try {
			File file = new File(path);
			if (!file.exists()) {
				log.debug("파일 디렉토리가 없음");
				if (file.mkdirs()) {
					log.debug("디렉토리 생성 성공");
				}
			}

			String fileNm = multipartFile.getOriginalFilename();
			String fileSize = Long.toString(multipartFile.getSize());
			String extName = fileNm.substring(fileNm.lastIndexOf("."), fileNm.length());
			String sysFileNm = getRandomNm("PROFILE_", extName);

			log.debug("fileNm : " + fileNm);
			log.debug("sysFileNm : " + sysFileNm);
			log.debug("extName : " + extName);
			log.debug("fileSize : " + fileSize);

			writeFile(multipartFile, path, sysFileNm);

	        File thumb = new File(path + "/" + "T-" + sysFileNm);
			InputStream in = multipartFile.getInputStream();
			BufferedImage originalImage = ImageIO.read(in);
	        Thumbnails.of(originalImage).size(150 ,100).outputFormat("png").toFile(thumb);
			in.close();

			fileInfo.setFileNm(fileNm); //프로필파일명
			fileInfo.setSysFileNm(sysFileNm); //프로필시스템파일명
			fileInfo.setFilePath(path); //프로필파일경로
			fileInfo.setFileSize(fileSize); //프로필파일사이즈
			fileInfo.setThumbSysFileNm(thumb.getName()); //썸네일시스템파일명

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return fileInfo;
	}

    /**
     * 파일 랜덤이름 생성
     * @param
     * @return
     */
	private String getRandomNm(String pre, String extName) {
		String random = UUID.randomUUID().toString();

		return pre + random + "." + extName;
	}

    /**
     * 파일 write
     * @param
     * @return
     */
	public void writeFile(MultipartFile multipartFile, String path, String sysFileNm) throws IOException{
		byte[] data = multipartFile.getBytes();

		FileOutputStream fos = new FileOutputStream(path + "/" + sysFileNm);
		fos.write(data);
		fos.close();
	}

    /**
     * 이메일로 회원정보조회
     * @return
     */
    @Override
    public Account findByEmail(Account account) {
        return accountDao.findByEmail(account);
    }

    /**
     * 회원가입 - 이메일 인증완료
     * @return
     */
    @Override
    public void completeSignUp(Account account) {
        account.completeSignUp();
        accountDao.updateCheckEmailToken(account);
    }

    /**
     * 회원가입 이메일인증 전송
     * @param account
     */
    @Override
    public void sendSignUpConfirmEmail(Account account) {
        Context context = new Context();
        context.setVariable("memNm", account.getMemNm());
        context.setVariable("link", "/account/check_email_token?token=" + account.getEmailAuthTkn() +
                "&memId=" + account.getMemId());
        context.setVariable("nickname", account.getMemNm());
        context.setVariable("linkName", "메일 인증");
//        context.setVariable("message", "자막센터(AICC)를 사용하려면 링크를 클릭하세요.");
        context.setVariable("host", appProperties.getHost());
        String message = templateEngine.process("mail/signup_template", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(account.getMemId())
                .subject("자막센터(AICC), 회원 가입 인증")
                .message(message)
                .build();

        emailService.sendEmail(emailMessage);
    }

    /**
     * 이메일 인증메일 재전송
     * @param account
     */
	@Override
	public void resendConfirmEmail(Account account) {
		sendSignUpConfirmEmail(account);
		// 인증번호 전송 일자 업데이트
		accountDao.updateEmailTokenIssueDt(account);
	}

    @Async
    @Override
    public void insertLoginDetail(LoginDetail loginDetail) {
        try {
            accountDao.insertLoginDetail(loginDetail);
        } catch (Exception e){
            log.error("insertLoginDetail : {}", e);
        }
    }

    /**
     * 회원가입 약관 불러오기
     * @param
     * @return
     */
	@Override
	public List<Terms> selectTermsList() {
		Terms param = new Terms();
		param.setDelYn("N");
		param.setTncGb("SINGUP");
		param.setDispYn("Y");
		List<Terms> list = accountDao.selectTermsList(param);

		for (int i=0; i<list.size(); i++) {
			list.get(i).setTncDesc(list.get(i).getTncDesc().replaceAll("\r\n", "<br>"));
		}
		return list;
	}

    /**
     * 회원가입 약관 필수값 체크
     * @param
     * @return
     */
	@Override
	public boolean checkReqTerms(SignUpForm signUpForm) {
		Terms param = new Terms();
		param.setDelYn("N");
		param.setTncGb("SINGUP");
		param.setDispYn("Y");
		param.setTncMandYn("Y");
		signUpForm.getReqTerms();

		String result = accountDao.selectReqTnc(param);
		return result.equals(signUpForm.getReqTerms());
	}

    /**
     * 아이디 중복 체크
     * @return
     */
	@Override
	public int checkByMemId(String memId) {
		return accountDao.checkByMemId(memId);
	}

    /**
     * 아이디, 비밀번호 찾기
     * @return
     */
	@Override
	public List<Account> findAccount(FindAccount findAccount) {
		return accountDao.findAccount(findAccount);
	}

	@Override
	public int updatePasswdReset(Account account) {
		int passwdLength = 10;
		String resetPasswd = createRandomPw(passwdLength);

		account.setPasswd(passwordEncoder.encode(resetPasswd));

		int result =  accountDao.updatePasswdReset(account);

		if (result > 0){
	        Context context = new Context();
	        context.setVariable("memNm", account.getMemNm());
	        context.setVariable("linkName", "생성 확인");
	        context.setVariable("resetPasswd", resetPasswd);
	        context.setVariable("host", appProperties.getHost());
	        String message = templateEngine.process("mail/find_account_template", context);

	        EmailMessage emailMessage = EmailMessage.builder()
	                .to(account.getMemId())
	                .subject("자막센터(AICC), 계정 비밀번호 초기화")
	                .message(message)
	                .build();

	        emailService.sendEmail(emailMessage);
		}

		return result;
	}

	public String createRandomPw(int length) {
		char charArr[] = new char[] {
			'1','2','3','4','5','6','7','8','9','0',
			'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
			'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
			'!','@','#','$','%','^','&','*','(',')'};

		int index = 0;
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < length; i++) {
			index = (int)(Math.random()*(charArr.length));
			sb.append(charArr[index]);
		}

		return sb.toString();
	}
	
	public String getTodayPath() {
		String path = "";
		Calendar cal = Calendar.getInstance();
		String year  = String.valueOf(cal.get(Calendar.YEAR));
		String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
		String day   = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        
		path = year + "/" + month + "/" + day;
		
        return path;
	}

	@Override
	public List<String> getActiveMembers() {
		List<String> activeMemberList = new ArrayList<String>();

		if(sessionRegistry.getAllPrincipals().size() != 0) {
			log.info("ACTIVE Member count : {}", sessionRegistry.getAllPrincipals().size());

			List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
			allPrincipals.stream().filter(principal -> principal instanceof User).forEach(principal -> {
				log.debug("user : {}", ((UserAccount) principal).getUsername());
				activeMemberList.add(((UserAccount) principal).getUsername());
			});
		}
		return activeMemberList;
	}

	@Override
	public Account findByMemId(String memId) {
		return accountDao.findByMemId(memId);
	}
}
