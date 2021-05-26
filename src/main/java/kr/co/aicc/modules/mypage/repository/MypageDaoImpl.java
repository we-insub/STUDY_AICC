package kr.co.aicc.modules.mypage.repository;

import org.springframework.stereotype.Repository;

import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.mypage.dto.ProfileForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MypageDaoImpl implements MypageDao {
    private final MypageMapper mypageMapper;
	
	@Override
	public ProfileForm findProfileByMemNo(Long memNo) {
		return mypageMapper.findProfileByMemNo(memNo);
	}

	@Override
	public int updateProfile(Account account) {
		return mypageMapper.updateProfile(account);
	}

	@Override
	public int profileDelete(Account account) {
		return mypageMapper.profileDelete(account);
	}

}
