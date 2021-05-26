package kr.co.aicc.modules.mypage.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.mypage.dto.ProfileForm;

@Repository
@Mapper
public interface MypageMapper {
	
	public ProfileForm findProfileByMemNo(Long memNo);
	
	public int updateProfile(Account account);

	public int profileDelete(Account account);
}
