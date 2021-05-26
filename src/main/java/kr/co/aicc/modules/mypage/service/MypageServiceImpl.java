package kr.co.aicc.modules.mypage.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.co.aicc.infra.config.AppProperties;
import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.account.repository.AccountDao;
import kr.co.aicc.modules.mypage.dto.ProfileForm;
import kr.co.aicc.modules.mypage.repository.MypageDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MypageServiceImpl implements MypageService {
    private final MypageDao mypageDao;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AppProperties appProperties;
    private final AccountDao accountDao;
    
	@Override
	public ProfileForm findProfileByMemNo(Long memNo) {
		ProfileForm profile = mypageDao.findProfileByMemNo(memNo);
		profile.setBrthdayY(profile.getBrthday().substring(0,4));
		profile.setBrthdayM(profile.getBrthday().substring(4,6));
		profile.setBrthdayD(profile.getBrthday().substring(6,8));
		
		profile.setPtblTelNoF(profile.getPtblTelNo().substring(0,3));
		profile.setPtblTelNoM(profile.getPtblTelNo().substring(3,7));
		profile.setPtblTelNoL(profile.getPtblTelNo().substring(7,11));

		if (!"".equals(profile.getSysFileNm()) && profile.getSysFileNm() != null) {
			String path = profile.getFilePath().replace(appProperties.getUploadDirProfile(), "/file/image/profile");
			profile.setSysFileNm(path + "/" + profile.getSysFileNm());
		}
		return profile;
	}

	@Override
	public int updateProfile(ProfileForm profileForm) {
		int result = 0;
        Account account = modelMapper.map(profileForm, Account.class);
        account.setPasswd(passwordEncoder.encode(account.getPasswd()));
        account.setBrthday(profileForm.getBrthday().replaceAll("-", ""));	//생년월일
        account.setPtblTelNo(profileForm.getPtblTelNoF() + profileForm.getPtblTelNoM() + profileForm.getPtblTelNoL());	//휴대전화번호

        result = mypageDao.updateProfile(account);
		ProfileForm info = mypageDao.findProfileByMemNo(account.getMemNo());

        if ((!"".equals(profileForm.getFileData()) && profileForm.getFileData() != null)) {
	    	Account fileInfo = fileUpload(profileForm);
	    	fileInfo.setMemId(account.getMemId());
	    	fileInfo.setMemNo(account.getMemNo());
	    	if (accountDao.updateMemFileInfo(fileInfo) > 0) {
	    		fileDelete(info);
	    	}
	    }

		return result;
	}


    /**
     * 파일 업로드
     * @param
     * @return
     */
	public Account fileUpload(ProfileForm profileForm) {
		Account fileInfo = new Account();

		String seqNo = String.valueOf(profileForm.getMemNo());
		String path = appProperties.getUploadDirProfile()  + "/" + getTodayPath();

		try {
			File file = new File(path);
			if (!file.exists()) {
				log.debug("파일 디렉토리가 없음");
				if (file.mkdirs()) {
					log.debug("디렉토리 생성 성공");
				}
			}			
			String base64Data = profileForm.getFileData().substring(profileForm.getFileData().indexOf(",")+1);
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
	
	public String getTodayPath() {
		String path = "";
		Calendar cal = Calendar.getInstance();
		String year  = String.valueOf(cal.get(Calendar.YEAR));
		String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
		String day   = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        
		path = year + "/" + month + "/" + day;
		
        return path;
	}
	
	public void fileDelete(ProfileForm info) {

		String filePath = info.getFilePath() + "/" + info.getSysFileNm();
		String tFilePath = info.getFilePath() + "/" + info.getThumbSysFileNm();
		
		File file = new File(filePath);
		File tFile = new File(tFilePath);

		if (file.exists()) {
			if (file.delete()) {
				log.debug("## 파일삭제 성공 -> " + filePath);
			} else {
				log.debug("## 파일삭제 실패 -> " + filePath);
			}
		} else {
			log.debug("## 파일이 존재하지 않습니다. -> " + filePath);
		}

		if (tFile.exists()) {
			if (tFile.delete()) {
				log.debug("## 썸네일 파일삭제 성공 -> " + tFilePath);
			} else {
				log.debug("## 썸네일 파일삭제 실패 -> " + tFilePath);
			}
		} else {
			log.debug("## 해당 썸네일 파일이 존재 하지 않습니다. -> " + tFilePath);
		}
	}

	@Override
	public int profileDelete(Account account) {
		account.setStat("03");	//탈퇴
		account.setWdlRsnCd("99"); //기타
		return mypageDao.profileDelete(account);

	}
}
