package com.mylive.admin.controller;

import com.mylive.component.RedisComponent;
import com.mylive.entity.dto.SysSettingDto;
import com.mylive.entity.query.UserInfoQuery;
import com.mylive.entity.vo.ResponseVO;
import com.mylive.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/setting")
@Validated
@Slf4j
public class SettingController extends ABaseController {

    @Resource
    private RedisComponent redisComponent;

    //加载设置
    @RequestMapping("/getSetting")
    public ResponseVO getSetting() {
        return getSuccessResponseVO(redisComponent.getSysSettingDto());
    }

    //保存设置
    @RequestMapping("/saveSetting")
    public ResponseVO saveSetting(SysSettingDto sysSettingDto) {
        redisComponent.saveSysSettingDto(sysSettingDto);
        return getSuccessResponseVO(null);
    }
}
