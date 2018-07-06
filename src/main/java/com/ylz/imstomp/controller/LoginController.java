package com.ylz.imstomp.controller;

import com.baomidou.kisso.AuthToken;
import com.baomidou.kisso.SSOHelper;
import com.baomidou.kisso.Token;
import com.ylzinfo.framework.sso.util.SSODataUtils;
import com.ylzinfo.framework.sys.domain.CurrentUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@CrossOrigin
@Controller
public class LoginController {
    @Value("${sso.defined.private_key}")
    private String privateKey;

    // 查看登录信息
    @ResponseBody
    @RequestMapping("/token")
    public AuthToken token(HttpServletRequest request, HttpServletResponse response) {
        String msg = "暂未登录";
        AuthToken ssoToken = SSOHelper.askCiphertext(request, response, privateKey);

        Token token = SSOHelper.getToken(request);

        System.out.println("token = " + token.getId());

        CurrentUser currentUser = SSODataUtils.getCurrentUser(token);

        System.out.println(currentUser.getAllFunctionList());
        System.out.println(currentUser.getItselfFunctionList());

        if (null != ssoToken) {
            msg = "登录信息 ip=" + ssoToken.getIp();
            msg += "， id=" + ssoToken.getId();
        }
        return ssoToken;
    }

    @RequestMapping(value = "/login")
    public String login(Map<String, Object> map, HttpServletRequest request) {

        Token token = SSOHelper.getToken(request);

        if (null != token) {
            CurrentUser currentUser = SSODataUtils.getCurrentUser(token);

            map.put("userName", currentUser.getUsername());
        }

        return "login";
    }

    @RequestMapping(value = "/loginIm")
    public String loginIm(Map<String, Object> map, HttpServletRequest request) {

        Token token = SSOHelper.getToken(request);

        if (null != token) {
            CurrentUser currentUser = SSODataUtils.getCurrentUser(token);

            map.put("userName", currentUser.getUsername());
        }

        return "login_im";
    }

}