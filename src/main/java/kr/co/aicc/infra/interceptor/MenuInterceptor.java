package kr.co.aicc.infra.interceptor;

import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.account.service.UserAccount;
import kr.co.aicc.modules.common.service.CodeCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class MenuInterceptor implements HandlerInterceptor {
    private final CodeCacheService codeCacheService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (modelAndView != null && !isRedirectView(modelAndView)) {
            Account account;
            if (null == authentication || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
                account = null;
            } else {
                account = ((UserAccount) authentication.getPrincipal()).getAccount();
            }
            modelAndView.addObject("lnbSel", codeCacheService.findResSel(request.getRequestURI()));	//현재 선택된 메뉴
            modelAndView.addObject("lnbList", codeCacheService.findResList(account));	//메뉴 리스트
        }
    }

    private boolean isRedirectView(ModelAndView modelAndView) {
    	if (modelAndView.getViewName() != null) {
            return modelAndView.getViewName().startsWith("redirect:") || modelAndView.getView() instanceof RedirectView;
    	} else {
            return modelAndView.getView() instanceof RedirectView;
    	}
    }
}
