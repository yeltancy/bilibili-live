package com.mylive.api.provider;

import com.mylive.entity.constants.Constants;
import com.mylive.entity.po.UserAction;
import com.mylive.entity.query.UserActionQuery;
import com.mylive.entity.query.VideoDanmuQuery;
import com.mylive.entity.vo.PaginationResultVO;
import com.mylive.service.UserActionService;
import com.mylive.service.VideoDanmuService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(Constants.INNER_API_PREFIX + "/userAction")
public class UserActionApi {
    @Resource
    private UserActionService userActionService;

    @RequestMapping("/getUserActionList")
    public List<UserAction> getUserActionList(@RequestBody UserActionQuery actionQuery) {
        return userActionService.findListByParam(actionQuery);
    }

}
