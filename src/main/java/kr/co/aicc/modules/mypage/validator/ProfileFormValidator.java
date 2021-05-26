package kr.co.aicc.modules.mypage.validator;

import kr.co.aicc.modules.mypage.dto.ProfileForm;
import kr.co.aicc.modules.mypage.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class ProfileFormValidator implements Validator {

    private final MypageService mypageService;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(ProfileForm.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
    	ProfileForm profileForm = (ProfileForm) object;
//        if (profileForm.getPasswd().equals(profileForm.getPasswd2()) == false) {
//            errors.rejectValue("passwd", "ProfileForm.passwd.NotEquals");
//            errors.rejectValue("passwd2", "ProfileForm.passwd.NotEquals");
//        }
        
        // 첨부파일 확인
//        if (signUpForm.getFile().isEmpty()) {
//        	errors.rejectValue("file", "SignUpForm.file.IsEmpty");
//        }       	
    }
}
