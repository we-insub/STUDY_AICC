package kr.co.aicc.modules.mypage.repository;

import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.mypage.dto.ProfileForm;

public interface MypageDao {
	
	public ProfileForm findProfileByMemNo(Long memNo);
	
	public int updateProfile(Account account);

	public int profileDelete(Account account);
}
