package kr.co.aicc.modules.settings.service;

import kr.co.aicc.infra.config.AppProperties;
import kr.co.aicc.modules.dashboard.domain.Schedule;
import kr.co.aicc.modules.settings.dto.ChannelForm;
import kr.co.aicc.modules.settings.repository.SettingsDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChannelSettingServiceImpl implements ChannelSettingService {
    private final SettingsDao settingsDao;
    private final AppProperties appProperties;

    @Override
    public List<ChannelForm> getChannelList(ChannelForm channelForm) {
    	List<ChannelForm> chnlList = settingsDao.getChannelList(channelForm);
    	
    	for (int i=0; i<chnlList.size(); i++) {
    		if (!"".equals(chnlList.get(i).getThumbSysFileNm()) && chnlList.get(i).getThumbSysFileNm() != null) {
    			String path = chnlList.get(i).getFilePath().replace(appProperties.getUploadDirChannel(), "/file/image/channel");
    			chnlList.get(i).setThumbSysFileNm(path + "/" + chnlList.get(i).getThumbSysFileNm());
    		}
    	}
        return chnlList;
    }

	@Override
	public int getChannelListCnt(ChannelForm channelForm) {
		return settingsDao.getChannelListCnt(channelForm);
	}

	@Override
	public int createChannel(ChannelForm channelForm) {
		int result = 0;
		result = settingsDao.createChannel(channelForm);
    	Long chnlNo = settingsDao.getChnlNo(channelForm);
    	
        if (!channelForm.getPicFile().isEmpty()) {
        	ChannelForm fileInfo = fileUpload(channelForm.getPicFile(), String.valueOf(chnlNo));
        	fileInfo.setChnlNo(chnlNo);
        	fileInfo.setChgr(channelForm.getChgr());
        	settingsDao.updateChnlFileInfo(fileInfo);
        }
        
		return result;
	}

	@Override
	public int updateChannel(ChannelForm channelForm) {
		int result = 0;
		result = settingsDao.updateChannel(channelForm);
		
    	ChannelForm param = new ChannelForm();
    	param.setChnlNo(channelForm.getChnlNo());
    	ChannelForm info = settingsDao.getChannelList(param).get(0);
    	
        if (!channelForm.getPicFile().isEmpty()) {
        	ChannelForm fileInfo = fileUpload(channelForm.getPicFile(), String.valueOf(channelForm.getChnlNo()));
        	fileInfo.setChnlNo(channelForm.getChnlNo());
        	fileInfo.setChgr(channelForm.getChgr());
	    	if (settingsDao.updateChnlFileInfo(fileInfo) > 0) {
	    		fileDelete(info);
	    	}
        }
        
		return result;
	}

	@Override
	public int deleteChannel(ChannelForm channelForm) {
		return settingsDao.deleteChannel(channelForm);
	}

    @Override
    public Schedule getMemberScheduleList(Schedule schedule) {
        return settingsDao.selectMemberSchedule(schedule);
    }


    /**
     * 파일 업로드
     * @param
     * @return
     */
	public ChannelForm fileUpload(MultipartFile multipartFile, String seqNo) {
		ChannelForm fileInfo = new ChannelForm();

		String path = appProperties.getUploadDirChannel() + "/" + getTodayPath();

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
			String extName = fileNm.substring(fileNm.lastIndexOf(".")+1, fileNm.length());
			String sysFileNm = getRandomNm("CHANNEL_", extName);

			log.debug("fileNm : " + fileNm);
			log.debug("sysFileNm : " + sysFileNm);
			log.debug("extName : " + extName);
			log.debug("fileSize : " + fileSize);

			writeFile(multipartFile, path, sysFileNm);

	        //썸네일 생성
	        int width = 100;
	        int height = 24;
	        File thumb = new File(path + "/" + "T-" + sysFileNm);
			InputStream in = multipartFile.getInputStream();
			BufferedImage originalImage = ImageIO.read(in);
	        Thumbnails.of(originalImage).size(width ,height).outputFormat(extName).toFile(thumb);
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
	
	public void fileDelete(ChannelForm info) {

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

}
