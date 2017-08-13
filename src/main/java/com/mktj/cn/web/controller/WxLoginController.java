package com.mktj.cn.web.controller;

import com.mktj.cn.web.configuration.WechatMpProperties;
import com.mktj.cn.web.service.UserService;
import com.mktj.cn.web.util.RoleType;
import com.mktj.cn.web.vo.UserVo;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMenuService;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpUserQuery;
import me.chanjar.weixin.mp.bean.menu.WxMpGetSelfMenuInfoResult;
import me.chanjar.weixin.mp.bean.menu.WxMpMenu;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
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
@RestController
@RequestMapping("/api/wechat/user/")
public class WxLoginController extends WxMpUserQuery {
    private final static Logger log = LoggerFactory.getLogger(WxLoginController.class);
    @Autowired
    private WechatMpProperties wechatMpProperties;
    @Autowired
    private WxMpService wxService;
    @Autowired
    private UserService userServiceImp;

    @ApiOperation(value = "用户登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(HttpServletResponse response) throws IOException {
        String authorizationUrl = wxService.oauth2buildAuthorizationUrl("http://122.152.208.113/api/wechat/user/login_callback", "snsapi_userinfo", "123456");
        response.sendRedirect(authorizationUrl);
    }

    @ApiOperation(value = "用户登录回调")
    @RequestMapping(value = "/login_callback", method = RequestMethod.POST)
    public void callback(@RequestParam("code") String code, @RequestParam("state") String state, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxService.oauth2getAccessToken(code);
            WxMpUser wxMpUser = wxService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
            userServiceImp.regWxUser(wxMpUser);
            response.sendRedirect(request.getContextPath() + "/api/user/login?username=" + wxMpUser.getOpenId() + "&password=123456");
        } catch (WxErrorException e) {
            log.error(e.getMessage(), e);
        }
    }
}
