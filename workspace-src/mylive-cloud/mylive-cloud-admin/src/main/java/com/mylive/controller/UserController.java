package com.mylive.controller;

import com.mylive.api.consumer.WebClient;
import com.mylive.entity.query.UserInfoQuery;
import com.mylive.entity.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
@Validated
@Slf4j
public class UserController extends ABaseController {

    @Resource
    private WebClient webClient;

    //加载用户
    @RequestMapping("/loadUser")
    public ResponseVO loadUser(UserInfoQuery userInfoQuery) {
        return getSuccessResponseVO(webClient.loadUser(userInfoQuery));
    }

    //更改用户状态
    @RequestMapping("/changeStatus")
    public ResponseVO changeStatus(String userId,Integer status) {
        webClient.changeStatus(userId,status);
        return getSuccessResponseVO(null);
    }
}
