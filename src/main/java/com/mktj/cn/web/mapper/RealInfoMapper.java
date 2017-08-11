package com.mktj.cn.web.mapper;

import com.mktj.cn.web.dto.RealInfoDTO;
import com.mktj.cn.web.po.RealInfo;
import com.mktj.cn.web.vo.RealInfoVo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RealInfoMapper {

    RealInfoDTO realInfoToRealInfoDTO(RealInfo realInfo);

    RealInfo realInfoToVoRealInfo(RealInfoVo realInfoVo);

    List<RealInfoDTO> realInfoToRealInfoDTOList(List<RealInfo> realInfoList);

    List<RealInfoDTO> realInfoToRealInfoDTOList(Iterable<RealInfo> realInfoList);
}