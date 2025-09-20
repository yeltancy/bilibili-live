package com.mylive.admin.controller;

import com.mylive.entity.query.UserInfoQuery;
import com.mylive.entity.query.VideoCommentQuery;
import com.mylive.entity.query.VideoDanmuQuery;
import com.mylive.entity.vo.PaginationResultVO;
import com.mylive.entity.vo.ResponseVO;
import com.mylive.service.UserInfoService;
import com.mylive.service.VideoCommentService;
import com.mylive.service.VideoDanmuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/user")
@Validated
@Slf4j
public class UserController extends ABaseController {

    @Resource
    private UserInfoService userInfoService;

    //加载用户
    @RequestMapping("/loadUser")
    public ResponseVO loadUser(UserInfoQuery userInfoQuery) {
        userInfoQuery.setOrderBy("join_time desc");
        return getSuccessResponseVO(userInfoService.findListByPage(userInfoQuery));
    }

    //更改用户状态
    @RequestMapping("/changeStatus")
    public ResponseVO changeStatus(String userId,Integer status) {
        userInfoService.changeUserStatus(userId,status);
        return getSuccessResponseVO(null);
    }
}
