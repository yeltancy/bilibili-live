package com.mylive.controller;

import com.mylive.component.RedisComponent;
import com.mylive.entity.vo.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;

@Api(tags = "视频同时在线人数controller")
@RestController
@RequestMapping("/online")
@Validated
public class OnlineController extends ABaseController {

    @Resource
    private RedisComponent redisComponent;

    @ApiOperation("监听视频在线观看人数")
    @RequestMapping("/reportVideoPlayOnline")
    public ResponseVO reportVideoPlayOnline(@NotEmpty String fileId, @NotEmpty String deviceId) {
        return getSuccessResponseVO(redisComponent.reportVideoPlayOnline(fileId, deviceId));
    }
}
