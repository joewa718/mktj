package com.mktj.cn.web.mapper;

import com.github.binarywang.wxpay.bean.result.WxPayOrderNotifyResult;
import com.mktj.cn.web.po.OAuthInfo;
import com.mktj.cn.web.po.WxPayOrderNotify;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OauthInfoMapper {
    OAuthInfo WxMpOAuth2AccessTokenToOAuthInfo(WxMpOAuth2AccessToken oAuthInfo);
}