package kr.co.aicc.modules.account.service;

import kr.co.aicc.infra.config.AppProperties;
import kr.co.aicc.infra.enums.CommonEnum.MemStat;
import kr.co.aicc.infra.exception.MemberNotApprovedException;
import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.account.repository.AccountDao;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service("userDetailsService")
@Transactional
@RequiredArgsConstructor
@EqualsAndHashCode
public class UserDetailsServiceImpl implements UserDetailsService, Serializable {
    private final AccountDao accountDao;
    private final ModelMapper modelMapper;
    private final ReloadableResourceBundleMessageSource messageSource;
    private final AppProperties appProperties;

    @Override
    public UserDetails loadUserByUsername(String memId) throws UsernameNotFoundException {
        Account account = accountDao.findByMemId(memId);
        log.debug("account : {}", account);
        if (account != null) {
            if (!"".equals(account.getFilePath()) && account.getFilePath() != null) {
        		account.setReFilePath(account.getFilePath().replace(appProperties.getUploadDirProfile(), "/file/image/profile"));        	
            }
        }
        // 회원존재확인
        if (account == null) {
            throw new UsernameNotFoundException(memId);
        }

        // 정회원 승인을 받지 않은 경우 오류 리턴
//        if ("Y".equalsIgnoreCase(account.getEmailAuthYn())) {
//	        if (!MemStat.MEM_NORMAL.value().equals(account.getStat())) {
//	            log.info("================ 정회원 승인을 받지 않은 경우 오류 리턴");
//	            throw new MemberNotApprovedException(
//	                    messageSource.getMessage("AICC_1102_400", null, null));
//	        }
//	    }

        // Role
        Set<String> memRoles = account.getMemRoles()
                .stream()
                .map(userRole -> userRole.getRoleNm())
                .collect(Collectors.toSet());
        log.debug("=====> [UserDetails] memId : {}, memNm : {}, roleNm : {}",
                account.getMemId(), account.getMemNm(), memRoles);

        List<GrantedAuthority> collect =
                memRoles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new UserAccount(account, collect);
    }

}