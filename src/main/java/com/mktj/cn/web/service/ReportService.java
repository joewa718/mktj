package com.mktj.cn.web.service;/**
 * Created by zhanwa01 on 2017/8/19.
 */

import com.mktj.cn.web.dto.EntryDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author zhanwang
 * @create 2017-08-19 1:18
 **/
@Service
public interface ReportService {
    /**
     * 获取团队成员分布
     * @return
     */
    Map<String,Long> analysisMemberDistribution(String phone);
    /**
     * 获取团队直系成员分布
     * @return
     */
    Map<String,Long> analysisImmediateMemberDistribution(String phone);

   /**
     * 获取团队新成员分布
     * @return
     */
    Map<String,Long> analysisNewMemberDistribution(String phone);

    /**
     * 获取个人进货量和出货量
     * @param phone
     * @return
     */
    Map<String,List<EntryDTO<String,Long>>> analysisOrderVolume(String phone);
    /**
     * 获取个人进货量和出货量环比
     * @param phone
     * @return
     */
    Map<String, List<EntryDTO<String,Double>>> analysisOrderShare(String phone);

    Map<String, Long> getNewSeniorImmediateMemberDistribution(String phone);

    Map<String, Long> analysisSleepDistribution(String phone);

    Map<String, List<EntryDTO<String, Long>>> analysisImmediateTeamOrderSaleVolume(String phone);

    Map<String, List<EntryDTO<String, Double>>> analysisImmediateTeamOrderShare(String phone);
}
