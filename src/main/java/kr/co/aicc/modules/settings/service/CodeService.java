package kr.co.aicc.modules.settings.service;

import java.util.List;

import kr.co.aicc.modules.settings.dto.DtlCode;
import kr.co.aicc.modules.settings.dto.GrpCode;

public interface CodeService {

    public List<GrpCode> findCodeGrpList(GrpCode grpCode);

    public int findCodeGrpListCnt(GrpCode grpCode);
    
    public List<DtlCode> findCodeDtlList(DtlCode dtlCode);

    public int findCodeDtlListCnt(DtlCode dtlCode);
    
    public int createCodeGrp(GrpCode grpCode);
    
    public int createCodeDtl(DtlCode dtlCode);

    public int updateCodeGrp(GrpCode grpCode);
    
    public int updateCodeDtl(DtlCode dtlCode);

    public int deleteCodeGrp(GrpCode grpCode);
    
    public int deleteCodeDtl(DtlCode dtlCode);
}
