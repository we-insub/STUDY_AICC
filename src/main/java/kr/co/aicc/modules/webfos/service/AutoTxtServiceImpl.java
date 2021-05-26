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
 * @package kr.co.aicc.infra.config
 * @file SecurityConfig.java
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
 * 2020. 06. 18	developer		 최초생성
 * -------------------------------------------------------------------------------
 */
package kr.co.aicc.modules.webfos.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.mozilla.universalchardet.UniversalDetector;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import kr.co.aicc.infra.config.AppProperties;
import kr.co.aicc.infra.exception.BizException;
import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.webfos.domain.AutoTxt;
import kr.co.aicc.modules.webfos.dto.AutoTxtInfo;
import kr.co.aicc.modules.webfos.dto.SearchInfo;
import kr.co.aicc.modules.webfos.repository.WebfosDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AutoTxtServiceImpl implements AutoTxtService {

	private final WebfosDao webfosDao;
	private final AppProperties appProperties;
	
	@Override
	public String getWordOfAutoTxt(String key, Long memNo) {
		return webfosDao.findByKeywordAndMemNoMemAutoTxt(key, memNo);
	}
	
	@Override
	public String createWordOfAutoTxt(Long memNo, String key, String value, String overwrite) {
		String result = "Success";
		
		try {
			webfosDao.createMemAutoTxt(memNo, key, value, overwrite);
		} catch (DuplicateKeyException e) {
			result = "DuplicateKey";
		} catch (Exception e) {
			result = "Fail";
			log.error(e.getMessage());
		}
		
		return result;
	}
	
	@Override
	public String updateWordOfAutoTxt(Long memNo, String key, String value) {
		String result = "Success";
		
		try {
			webfosDao.updateMemAutoTxt(memNo, key, value);
		} catch (Exception e) {
			result = "Fail";
			log.error(e.getMessage());
		}
		
		return result;
	}
	
	@Override
	public String deleteWordOfAutoTxt(Long memNo, String key) {
		String result = "Success";
		
		try {
			webfosDao.deleteMemAutoTxt(memNo, key);
		} catch (Exception e) {
			result = "Fail";
			log.error(e.getMessage());
		}
		
		return result;
	}
	
	@Override
	public List<AutoTxtInfo> getAutoTxtInfoList(Long memNo) {
		List<AutoTxtInfo> autoTxtInfoList = new ArrayList<AutoTxtInfo>();
		
		List<AutoTxt> autoTxtList = webfosDao.findByMemNoMemAutoTxt(memNo);
		
		autoTxtList.forEach(autoTxt -> {
			autoTxtInfoList.add(AutoTxtInfo.builder().memAutotxtNo(autoTxt.getMemAutotxtNo()).memNo(autoTxt.getMemNo())
					.keyword(autoTxt.getKeyword()).autotxt(autoTxt.getAutotxt()).build());
		});
		
		return autoTxtInfoList;
	}
	
	@Override
	public List<AutoTxtInfo> getAutoTxtInfoList(SearchInfo searchInfo) {
		List<AutoTxtInfo> autoTxtInfoList = new ArrayList<AutoTxtInfo>();
		
		List<AutoTxt> autoTxtList = webfosDao.findBySearchInfoMemAutoTxt(searchInfo);
		
		autoTxtList.forEach(autoTxt -> {
			autoTxtInfoList.add(AutoTxtInfo.builder().memAutotxtNo(autoTxt.getMemAutotxtNo()).memNo(autoTxt.getMemNo())
					.keyword(autoTxt.getKeyword()).autotxt(autoTxt.getAutotxt()).build());
		});
		
		return autoTxtInfoList;
	}
	
	@Override
	public void autoTxtUpload(Account account, MultipartFile uploadFile) {
		File localFile = null;
		try {
			
			String path = appProperties.getUploadDirAutotxt();
			
			localFile = new File(path + uploadFile.getOriginalFilename());
    		uploadFile.transferTo(localFile);
    		
    		if (localFile.getTotalSpace() > 0) {
    			try (Stream<String> stream = Files.lines(Paths.get(localFile.getPath()), StandardCharsets.UTF_8)) {
    				log.debug("stream : {}", stream.toString());
    				stream.forEach(e -> {
    					if (!e.contains(";") && !e.contains("*")) {
    						String[] array  = e.split("\t");
    						log.debug("array size : {}", array.length);
    						if (array.length == 2) {
    							webfosDao.createMemAutoTxt(account.getMemNo(), array[0].trim(), array[1], "I");
    						}
    					}
    				});
    			}
    		}
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		} finally {
			if (!ObjectUtils.isEmpty(localFile) && localFile.exists()) {
				localFile.delete();
			}
		}
	}
	
	@Override
	public void rerunAutoTxtUpload(Account account, MultipartFile uploadFile) {
		File localFile = null;
		try {
			
			String path = appProperties.getUploadDirAutotxt();
			
			localFile = new File(path + uploadFile.getOriginalFilename());
    		uploadFile.transferTo(localFile);

    		String enc = this.findFileEncoding(localFile);
    		if (localFile.getTotalSpace() > 0) {
    			if("UTF-8".equals(enc) || "EUC-KR".equals(enc)) {
        			this.createMemAutoTxt(localFile, enc, account.getMemNo());
    			}
    		}
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		} finally {
			if (!ObjectUtils.isEmpty(localFile) && localFile.exists()) {
				localFile.delete();
			}
			
		}
		
	}
	
	private void createMemAutoTxt(File localFile, String charset, Long memNo) throws IOException {
		Stream<String> stream = Files.lines(Paths.get(localFile.getPath()), Charset.forName(charset));
		log.debug("stream : {}", stream.toString());
		stream.forEach(e -> {
			if (!e.contains(";") && !e.contains("*")) {
				String[] array  = e.split("\t");
				log.debug("array size : {}", array.length);
				if (array.length == 2) {
					String dup = webfosDao.findByKeywordAndMemNoMemAutoTxt(array[0].trim(), memNo);
					if(dup == null) {
						webfosDao.createMemAutoTxt(memNo, array[0].trim(), array[1], "I");
					} else {
						throw new BizException();
					}
				}
			}
		});
	}
	
	public String findFileEncoding(File file) throws IOException {
	
		byte[] buf = new byte[4096];
	    java.io.FileInputStream fis = new java.io.FileInputStream(file);
	    String encoding = null;
		try {
		    UniversalDetector detector = new UniversalDetector(null);
		 
		    int nread;
		    while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
		      detector.handleData(buf, 0, nread);
		    }
		    detector.dataEnd();
		 
		    encoding = detector.getDetectedCharset();
		} finally {
			fis.close();
		}
	    
	    return encoding;
	}
	
	@Override
	public String autoTxtDownload(Account account) {
		StringBuffer result = new StringBuffer();
		
		List<AutoTxt> autoTxtList = webfosDao.findByMemNoMemAutoTxt(account.getMemNo());
		
		if (!ObjectUtils.isEmpty(autoTxtList)) {
			result.append("* Webfos\tAutoTxts");
			result.append("\n");
			
			autoTxtList.forEach(autoTxt -> {
				result.append(autoTxt.getKeyword());
				result.append("\t");
				result.append(autoTxt.getAutotxt());
				result.append("\n");
			});
		}
		
		return result.toString();
	}
}
