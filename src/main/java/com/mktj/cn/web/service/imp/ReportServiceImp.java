package com.mktj.cn.web.service.imp;

import com.mktj.cn.web.dto.EntryDTO;
import com.mktj.cn.web.enumerate.OrderStatus;
import com.mktj.cn.web.enumerate.RoleType;
import com.mktj.cn.web.po.Order;
import com.mktj.cn.web.po.User;
import com.mktj.cn.web.repositories.OrderRepository;
import com.mktj.cn.web.repositories.TeamOrganizationRepository;
import com.mktj.cn.web.repositories.UserRepository;
import com.mktj.cn.web.service.ReportService;
import com.mktj.cn.web.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.util.calendar.CalendarUtils;
import sun.util.locale.provider.CalendarDataUtility;

import java.util.*;

/**
 * @author zhanwang
 * @create 2017-08-19 1:18
 **/
@Service
public class ReportServiceImp implements ReportService {
    @Autowired
    TeamOrganizationRepository teamOrganizationRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public Map<String, Long> analysisMemberDistribution(String teamCode) {
        List<Object[]> list = teamOrganizationRepository.analysisMemberDistribution(teamCode);
        return fillResult(list);
    }

    @Override
    public Map<String, Long> analysisImmediateMemberDistribution(String phone, String teamCode) {
        User user = userRepository.findByPhone(phone);
        List<Object[]> list = teamOrganizationRepository.analysisImmediateMemberDistribution(user, teamCode);
        return fillResult(list);
    }

    @Override
    public Map<String, Long> analysisNewMemberDistribution(String teamCode) {
        List<Object[]> list = teamOrganizationRepository.analysisNewMemberDistribution(teamCode);
        return fillResult(list);
    }

    @Override
    public Map<String, List<EntryDTO<String, Long>>> analysisOrderVolume(String phone) {
        User user = userRepository.findByPhone(phone);
        Map<String, Long> ordinaryOrderSaleVolume = fillVolumeResult(orderRepository.analysisOrdinaryOrderSaleVolume(user, OrderStatus.已支付, DateUtil.getYearBeginDate(), new Date()));
        List<Order> orderList = user.getServiceOrderList();
        List<Long> orderIds = new ArrayList<>();
        if (orderList.size() > 0) {
            orderList.forEach(order -> orderIds.add(order.getId()));
        }
        Map<String, Long> serviceOrderSaleVolume = fillVolumeResult(orderRepository.analysisServiceOrderSaleVolume(orderIds, OrderStatus.已支付, DateUtil.getYearBeginDate(), new Date()));
        List<String> monthList = DateUtil.getYTDMonth();
        Map<String, List<EntryDTO<String, Long>>> map = new HashMap<>();
        map.put("个人进货量", fillAnalysisOrderVolume(ordinaryOrderSaleVolume, monthList));
        map.put("个人销货量", fillAnalysisOrderVolume(serviceOrderSaleVolume, monthList));
        return map;
    }

    @Override
    public Map<String, List<EntryDTO<String, Double>>> analysisOrderShare(String phone) {
        User user = userRepository.findByPhone(phone);
        Map<String, Long> ordinaryOrderSaleVolume = fillVolumeResult(orderRepository.analysisOrdinaryOrderSaleVolume(user, OrderStatus.已支付, DateUtil.getYearBeginDate(), new Date()));
        List<Order> orderList = user.getServiceOrderList();
        List<Long> orderIds = new ArrayList<>();
        if (orderList.size() > 0) {
            orderList.forEach(order -> orderIds.add(order.getId()));
        }
        Map<String, Long> serviceOrderSaleVolume = fillVolumeResult(orderRepository.analysisServiceOrderSaleVolume(orderIds, OrderStatus.已支付, DateUtil.getYearBeginDate(), new Date()));
        List<String> monthList = DateUtil.getYTDMonth();
        Map<String, List<EntryDTO<String, Double>>> map = new HashMap<>();
        map.put("个人进货量环比", fillAnalysisOrderShare(ordinaryOrderSaleVolume, monthList));
        map.put("个人销货量环比", fillAnalysisOrderShare(serviceOrderSaleVolume, monthList));
        return map;
    }

    private List<EntryDTO<String, Double>> fillAnalysisOrderShare(Map<String, Long> serviceOrderSaleVolume, List<String> monthList) {
        List<EntryDTO<String, Double>> orderSaleShareList = new ArrayList<>();
        List<EntryDTO<String, Long>> entryDTOList = fillAnalysisOrderVolume(serviceOrderSaleVolume, monthList);
        for (int i = 1; i < entryDTOList.size(); i++) {
            EntryDTO<String, Long> volume = entryDTOList.get(i);
            EntryDTO<String, Long> pre_volume = entryDTOList.get(i - 1);
            Double share = Double.valueOf((volume.getValue() - pre_volume.getValue()) / volume.getValue());
            orderSaleShareList.add(new EntryDTO<>(volume.getKey(),share));
        }
        return orderSaleShareList;
    }

    private List<EntryDTO<String, Long>> fillAnalysisOrderVolume(Map<String, Long> serviceOrderSaleVolume, List<String> monthList) {
        List<EntryDTO<String, Long>> orderSaleVolumeList = new ArrayList<>();
        monthList.forEach(month -> {
            EntryDTO entryDTO;
            if (serviceOrderSaleVolume.containsKey(month)) {
                entryDTO = new EntryDTO(month, serviceOrderSaleVolume.get(month));
            } else {
                entryDTO = new EntryDTO(month, 0);
            }
            orderSaleVolumeList.add(entryDTO);
        });
        return orderSaleVolumeList;
    }

    private Map<String, Long> fillVolumeResult(List<Object[]> list) {
        Map<String, Long> result = new HashMap<>();
        list.forEach(objects -> {
            String month;
            if (Integer.parseInt((String) objects[0]) < 10) {
                month = "0" + (String) objects[0];
            } else {
                month = (String) objects[0];
            }
            result.put(month, (Long) objects[1]);
        });
        return result;
    }

    private Map<String, Long> fillResult(List<Object[]> list) {
        Map<String, Long> result = new HashMap<>();
        result.put(RoleType.天使.getName(), Long.valueOf(0));
        result.put(RoleType.合伙人.getName(), Long.valueOf(0));
        result.put(RoleType.准合伙人.getName(), Long.valueOf(0));
        result.put(RoleType.高级合伙人.getName(), Long.valueOf(0));
        list.forEach(objects -> {
            RoleType roleType = (RoleType) objects[0];
            if (result.containsKey(roleType.getName())) {
                result.put(roleType.getName(), (Long) objects[1]);
            }
        });
        return result;
    }
}
