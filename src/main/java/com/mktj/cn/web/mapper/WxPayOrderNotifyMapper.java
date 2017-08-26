package com.mktj.cn.web.mapper;

import com.github.binarywang.wxpay.bean.result.WxPayOrderNotifyResult;
import com.mktj.cn.web.dto.UserDTO;
import com.mktj.cn.web.po.User;
import com.mktj.cn.web.po.WxPayOrderNotify;
import com.mktj.cn.web.vo.UserVo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WxPayOrderNotifyMapper {
    WxPayOrderNotify WxPayOrderNotifyResultToWxPayOrderNotify(WxPayOrderNotifyResult wxPayOrderNotifyResult);
}