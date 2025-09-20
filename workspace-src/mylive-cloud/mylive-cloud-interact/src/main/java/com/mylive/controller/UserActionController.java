package com.mylive.controller;

import com.mylive.annotation.GlobalInterceptor;
import com.mylive.annotation.RecordUserMessage;
import com.mylive.entity.constants.Constants;
import com.mylive.entity.enums.MessageTypeEnum;
import com.mylive.entity.po.UserAction;
import com.mylive.entity.vo.ResponseVO;
import com.mylive.service.UserActionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Api(tags = "用户行为(点赞、投币、收藏)controller")
@RestController
@RequestMapping("/userAction")
@Validated
public class UserActionController extends ABaseController {

    @Resource
    private UserActionService userActionService;

    @ApiOperation("用户行为(点赞、投币、收藏)")
    @RequestMapping("/doAction")
    @GlobalInterceptor(checkLogin = true)
    @RecordUserMessage(messageType = MessageTypeEnum.LIKE)
    public ResponseVO doAction(@NotEmpty String videoId,
                               @NotNull Integer actionType,
                               @Max(2) @Min(1) Integer actionCount,
                               Integer commentId) {
        UserAction userAction = new UserAction();
        userAction.setVideoId(videoId);
        userAction.setUserId(getTokenUserInfoDto().getUserId());
        userAction.setActionType(actionType);
        actionCount = actionCount == null ? Constants.ONE : actionCount;
        userAction.setActionCount(actionCount);
        commentId = commentId == null ? Constants.ZERO : commentId;
        userAction.setCommentId(commentId);
        userActionService.saveAction(userAction);
        return getSuccessResponseVO(null);
    }
}
