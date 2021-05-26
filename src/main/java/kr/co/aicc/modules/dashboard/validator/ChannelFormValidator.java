package kr.co.aicc.modules.dashboard.validator;

import kr.co.aicc.modules.account.dto.SignUpForm;
import kr.co.aicc.modules.dashboard.dto.ChannelForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class ChannelFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(ChannelForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ChannelForm channelForm = (ChannelForm) target;

    }

}
