package com.mktj.cn.web.controller;

import com.mktj.cn.web.po.User;
import com.mktj.cn.web.properties.WechatMpProperties;
import com.mktj.cn.web.service.UserService;
import com.mktj.cn.web.util.GenerateRandomCode;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpUserQuery;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <pre>
 *  注意：此contorller 实现WxMpMenuService接口，仅是为了演示如何调用所有菜单相关操作接口，
 *      实际项目中无需这样，根据自己需要添加对应接口即可
 * </pre>
 *
 * @author Binary Wang(https://github.com/binarywang)
 */
@Controller
@RequestMapping("/api/wechat/user/")
public class WeLoginController extends WxMpUserQuery {
    @Autowired
    private DaoAuthenticationProvider daoAuthenticationProvider;
    private final static Logger log = LoggerFactory.getLogger(WeLoginController.class);
    @Autowired
    private WechatMpProperties wechatMpProperties;
    @Autowired
    private WxMpService wxService;
    @Autowired
    private UserService userServiceImp;

    @ApiOperation(value = "用户登录")
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        GenerateRandomCode generateRandomCode =new GenerateRandomCode();
        String connectUrl = wxService.oauth2buildAuthorizationUrl( "http://www.jinhuishengwu.cn/api/wechat/user/weLoginCallback", "snsapi_userinfo", generateRandomCode.generate(5));
        response.sendRedirect(connectUrl);
    }

    @ApiOperation(value = "用户登录回调")
    @RequestMapping(value = "/weLoginCallback", method = RequestMethod.GET)
    public void weLoginCallback(@RequestParam("code") String code, @RequestParam("state") String state, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxService.oauth2getAccessToken(code);
            WxMpUser wxMpUser = wxService.oauth2getUserInfo(wxMpOAuth2AccessToken, "zh_CN");
            User user = userServiceImp.regWxUser(wxMpUser);
            Authentication token = new UsernamePasswordAuthenticationToken(user.getAppId(), "123456");
            Authentication result =daoAuthenticationProvider.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(result);
            response.sendRedirect("http://www.jinhuishengwu.cn/u.html");
        } catch (WxErrorException e) {
            log.error(e.getMessage(), e);
        }
    }

}
