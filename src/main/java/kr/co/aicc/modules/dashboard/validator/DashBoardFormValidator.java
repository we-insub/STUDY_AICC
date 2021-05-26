package kr.co.aicc.modules.dashboard.validator;

import kr.co.aicc.modules.dashboard.dto.ChannelForm;
import kr.co.aicc.modules.dashboard.dto.DashBoardForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class DashBoardFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(DashBoardForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        DashBoardForm dashBoardForm = (DashBoardForm) target;
    }
}
