package com.mylive.admin.controller;

import com.mylive.entity.enums.StatisticsTypeEnum;
import com.mylive.entity.po.StatisticsInfo;
import com.mylive.entity.query.StatisticsInfoQuery;
import com.mylive.entity.query.UserInfoQuery;
import com.mylive.entity.vo.ResponseVO;
import com.mylive.service.StatisticsInfoService;
import com.mylive.service.UserInfoService;
import com.mylive.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/index")
@Validated
@Slf4j
public class IndexController extends ABaseController {

    @Resource
    private StatisticsInfoService statisticsInfoService;

    @Resource
    private UserInfoService userInfoService;

    //获取数据统计
    @RequestMapping("/getActualTimeStatisticsInfo")
    public ResponseVO getActualTimeStatisticsInfo() {
        String preDate = DateUtil.getBeforeDayDate(1);
        StatisticsInfoQuery query = new StatisticsInfoQuery();
        query.setStatisticsDate(preDate);
        List<StatisticsInfo> preDayData = statisticsInfoService.findListTotalInfoByParam(query);

        Integer userCount = userInfoService.findCountByParam(new UserInfoQuery());
        preDayData.forEach(item -> {
            if (StatisticsTypeEnum.FANS.getType().equals(item.getDataType())) {
                item.setStatisticsCount(userCount);
            }
        });

        Map<Integer, Integer> preDayDataMap = preDayData.stream().collect(Collectors.toMap(StatisticsInfo::getDataType, StatisticsInfo::getStatisticsCount, (item1, item2) -> item2));

        Map<String, Integer> totalCountInfo = statisticsInfoService.getStatisticsInfoActualTime(null);
        Map<String, Object> result = new HashMap<>();
        result.put("preDayData", preDayDataMap);
        result.put("totalCountInfo", totalCountInfo);

        return getSuccessResponseVO(result);
    }

    //获取七天数据统计(用作流线图)
    @RequestMapping("/getWeekStatisticsInfo")
    public ResponseVO getWeekStatisticsInfo(Integer dataType) {
        List<String> dateList = DateUtil.getBeforeDates(7);
        StatisticsInfoQuery query = new StatisticsInfoQuery();
        query.setDataType(dataType);
        query.setStatisticsDateStart(dateList.get(0));
        query.setStatisticsDateEnd(dateList.get(dateList.size() - 1));
        query.setOrderBy("statistics_date asc");

        List<StatisticsInfo> statisticsInfoList = null;
        if (!StatisticsTypeEnum.FANS.getType().equals(dataType)) {
            statisticsInfoList = statisticsInfoService.findListTotalInfoByParam(query);
        } else {
            statisticsInfoList = statisticsInfoService.findUserCountTotalInfoByParam(query);
        }
        Map<String, StatisticsInfo> dataMap = statisticsInfoList.stream().collect(Collectors.toMap(item -> item.getStatisticsDate(), Function.identity(), (data1, data2) -> data2));

        List<StatisticsInfo> resultDataList = new ArrayList<>();
        for (String date : dateList) {
            StatisticsInfo dataItem = dataMap.get(date);
            if (dataItem == null) {
                dataItem = new StatisticsInfo();
                dataItem.setStatisticsCount(0);
                dataItem.setStatisticsDate(date);
            }
            resultDataList.add(dataItem);
        }
        return getSuccessResponseVO(statisticsInfoList);
    }
}
