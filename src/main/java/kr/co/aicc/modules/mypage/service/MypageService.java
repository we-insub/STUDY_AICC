package kr.co.aicc.modules.mypage.service;

import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.mypage.dto.ProfileForm;

public interface MypageService {
	
	public ProfileForm findProfileByMemNo(Long memNo);
	
	public int updateProfile(ProfileForm profileForm);

	public int profileDelete(Account account);

}
