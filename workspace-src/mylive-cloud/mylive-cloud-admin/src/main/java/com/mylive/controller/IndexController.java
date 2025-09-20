package com.mylive.controller;

import com.mylive.api.consumer.WebClient;
import com.mylive.entity.enums.StatisticsTypeEnum;
import com.mylive.entity.po.StatisticsInfo;
import com.mylive.entity.query.UserInfoQuery;
import com.mylive.entity.vo.ResponseVO;
import com.mylive.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
    private WebClient webClient;

    //获取数据统计
    @RequestMapping("/getActualTimeStatisticsInfo")
    public ResponseVO getActualTimeStatisticsInfo() {
        return getSuccessResponseVO(webClient.getActualTimeStatisticsInfo());
    }

    //获取七天数据统计(用作流线图)
    @RequestMapping("/getWeekStatisticsInfo")
    public ResponseVO getWeekStatisticsInfo(Integer dataType) {
        return getSuccessResponseVO(webClient.getWeekStatisticsInfo(dataType));
    }
}
