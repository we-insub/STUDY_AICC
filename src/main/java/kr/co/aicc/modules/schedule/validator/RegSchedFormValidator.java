package kr.co.aicc.modules.schedule.validator;

import kr.co.aicc.modules.schedule.dto.RegProgramForm;
import kr.co.aicc.modules.schedule.dto.SchedForm;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class RegSchedFormValidator implements Validator {

    private final ReloadableResourceBundleMessageSource messageSource;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SchedForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SchedForm schedForm = (SchedForm) target;
        if (schedForm.getMode().equals("edit")) {
            errors.rejectValue("progNm", "SchedForm.progNm.NotBlank", messageSource.getMessage("SchedForm.progNm.NotBlank", null, null));
        }

    }
}
