package kr.co.aicc.modules.settings.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.aicc.modules.settings.domain.Code;
import kr.co.aicc.modules.settings.dto.DtlCode;
import kr.co.aicc.modules.settings.dto.GrpCode;
import kr.co.aicc.modules.settings.repository.SettingsDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CodeServiceImpl implements CodeService {
    private final SettingsDao settingsDao;
    private final ModelMapper modelMapper;
    
	@Override
	public List<GrpCode> findCodeGrpList(GrpCode grpDode) {
		return settingsDao.findCodeGrpList(grpDode);
	}

	@Override
	public List<DtlCode> findCodeDtlList(DtlCode dtlCode) {
		return settingsDao.findCodeDtlList(dtlCode);
	}

	@Override
	public int createCodeGrp(GrpCode grpCode) {
		return settingsDao.createCodeGrp(grpCode);
	}

	@Override
	public int createCodeDtl(DtlCode dtlCode) {
		return settingsDao.createCodeDtl(dtlCode);
	}

	@Override
	public int updateCodeGrp(GrpCode grpCode) {
		return settingsDao.updateCodeGrp(grpCode);
	}

	@Override
	public int updateCodeDtl(DtlCode dtlCode) {
		return settingsDao.updateCodeDtl(dtlCode);
	}

	@Override
	public int deleteCodeGrp(GrpCode grpCode) {
		settingsDao.deleteCodeGrpByDtl(grpCode);
		return settingsDao.deleteCodeGrp(grpCode);
	}

	@Override
	public int deleteCodeDtl(DtlCode dtlCode) {
		return settingsDao.deleteCodeDtl(dtlCode);
	}

	@Override
	public int findCodeGrpListCnt(GrpCode grpCode) {
		return settingsDao.findCodeGrpListCnt(grpCode);
	}

	@Override
	public int findCodeDtlListCnt(DtlCode dtlCode) {
		return settingsDao.findCodeDtlListCnt(dtlCode);
	}

}
