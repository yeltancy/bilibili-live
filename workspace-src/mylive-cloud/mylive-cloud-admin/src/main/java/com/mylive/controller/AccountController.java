package com.mylive.controller;

import com.mylive.component.RedisComponent;
import com.mylive.entity.config.AppConfig;
import com.mylive.entity.constants.Constants;
import com.mylive.entity.vo.ResponseVO;
import com.mylive.exception.BusinessException;
import com.mylive.utils.StringTools;
import com.wf.captcha.ArithmeticCaptcha;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户信息 Controller
 */
@RestController
@RequestMapping("/account")
@Validated
public class AccountController extends ABaseController {
    @Resource
    private RedisComponent redisComponent;

    @Resource
    private AppConfig appConfig;

    /*    使用HttpSession存储验证码与注册
        @RequestMapping("/checkCode")
        public ResponseVO checkCode(HttpSession session) {
            ArithmeticCaptcha captcha = new ArithmeticCaptcha(100, 42);
            String code = captcha.text();
            session.setAttribute("checkCode", code);
            String checkCodeBase64 = captcha.toBase64();
            return getSuccessResponseVO(checkCodeBase64);
        }
        @RequestMapping("/register")
        public ResponseVO register(HttpSession session,String checkCode){
            String myCheckCode = (String) session.getAttribute("checkCode");
            return getSuccessResponseVO(myCheckCode.equals(checkCode));
        }*/

    @RequestMapping("/checkCode")
    public ResponseVO checkCode() {
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(100, 42);
        String code = captcha.text();
        String checkCodeKey = redisComponent.saveCheckCode(code);
        String checkCodeBase64 = captcha.toBase64();
        Map<String, String> result = new HashMap<>();
        result.put("checkCode", checkCodeBase64);
        result.put("checkCodeKey", checkCodeKey);
        return getSuccessResponseVO(result);
    }

    @RequestMapping("/login")
    public ResponseVO login(HttpServletRequest request, HttpServletResponse response,
                            //@RequestHeader("token") String token,
                            @NotEmpty String account,
                            @NotEmpty String password,
                            @NotEmpty String checkCodeKey,
                            @NotEmpty String checkCode) {
        try {
            if (!checkCode.equalsIgnoreCase(redisComponent.getCheckCode(checkCodeKey))) {
                throw new BusinessException("验证码错误");
            }
            if (!account.equals(appConfig.getAdminAccount()) || !password.equals(StringTools.encodeByMd5(appConfig.getAdminPassword()))) {
                throw new BusinessException("账号或密码错误");
            }
            String token = redisComponent.saveTokenInfo4Admin(account);
            saveToken2Cookie(response, token);
            return getSuccessResponseVO(account);
        } finally {
            redisComponent.cleanCheckCode(checkCodeKey);
            //TODO
            //到时候用前端传来的token把这段删了
            Cookie[] cookies = request.getCookies();
            if(null != cookies){
                String token = null;
                for (Cookie cookie : cookies) {
                    if (Constants.TOKEN_ADMIN.equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
                //这段保留
                if (!StringTools.isEmpty(token)) {
                    redisComponent.cleanToken4Admin(token);
                }
            }
        }
    }

    @RequestMapping("/logout")
    public ResponseVO logout(HttpServletResponse response) {
        cleanCookie(response);
        return getSuccessResponseVO(null);
    }
}