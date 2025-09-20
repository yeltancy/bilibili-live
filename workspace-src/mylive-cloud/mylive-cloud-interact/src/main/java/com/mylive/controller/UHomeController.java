package com.mylive.controller;

import com.mylive.entity.enums.UserActionTypeEnum;
import com.mylive.entity.query.UserActionQuery;
import com.mylive.entity.vo.PaginationResultVO;
import com.mylive.entity.vo.ResponseVO;
import com.mylive.service.UserActionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.*;

@Api(tags = "个人主页controller")
@RestController
@RequestMapping("/uhome")
@Validated
public class UHomeController extends ABaseController {

    @Resource
    private UserActionService userActionService;

    @ApiOperation("加载收藏列表")
    @RequestMapping("/loadUserCollection")
    public ResponseVO loadUserCollection(@NotEmpty String userId, Integer pageNo) {
        UserActionQuery actionQuery = new UserActionQuery();
        actionQuery.setActionType(UserActionTypeEnum.VIDEO_COLLECT.getType());
        actionQuery.setUserId(userId);
        actionQuery.setPageNo(pageNo);
        actionQuery.setOrderBy("action_time desc");
        actionQuery.setQueryVideoInfo(true);
        PaginationResultVO resultVO = userActionService.findListByPage(actionQuery);

        return getSuccessResponseVO(resultVO);
    }
}
