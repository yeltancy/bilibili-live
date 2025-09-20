package com.mylive.aspect;

import com.mylive.annotation.RecordUserMessage;
import com.mylive.component.RedisComponent;
import com.mylive.entity.constants.Constants;
import com.mylive.entity.dto.TokenUserInfoDto;
import com.mylive.entity.enums.MessageTypeEnum;
import com.mylive.entity.enums.UserActionTypeEnum;
import com.mylive.entity.vo.ResponseVO;
import com.mylive.service.UserMessageService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Aspect
@Component
@Slf4j
public class UserMessageOperationAspect {

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private UserMessageService userMessageService;

    private static final String PARAMETERS_VIDEO_ID = "videoId";

    private static final String PARAMETERS_ACTION_TYPE = "actionType";

    private static final String PARAMETERS_REPLY_COMMENT_ID = "replyCommentId";

    private static final String PARAMETERS_AUDIT_REJECT_REASON = "reason";

    private static final String PARAMETERS_CONTENT = "content";

    @Around("@annotation(com.mylive.annotation.RecordUserMessage)")
    public ResponseVO interceptorDo(ProceedingJoinPoint point) throws Throwable {
        ResponseVO responseVO = (ResponseVO) point.proceed();
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        RecordUserMessage recordUserMessage = method.getAnnotation(RecordUserMessage.class);
        if (null != recordUserMessage) {
            saveMessage(recordUserMessage, point.getArgs(), method.getParameters());
        }
        return responseVO;
    }

    private void saveMessage(RecordUserMessage recordUserMessage, Object[] args, Parameter[] parameters) {
        String videoId = null;
        Integer actionType = null;
        Integer replyCommentId = null;
        String content = null;
        for (int i = 0; i < parameters.length; i++) {
            if (PARAMETERS_VIDEO_ID.equals(parameters[i].getName())) {
                videoId = (String) args[i];
            } else if (PARAMETERS_ACTION_TYPE.equals(parameters[i].getName())) {
                actionType = (Integer) args[i];
            } else if (PARAMETERS_REPLY_COMMENT_ID.equals(parameters[i].getName())) {
                replyCommentId = (Integer) args[i];
            } else if (PARAMETERS_CONTENT.equals(parameters[i].getName()) || PARAMETERS_AUDIT_REJECT_REASON.equals(parameters[i].getName())) {
                content = (String) args[i];
            }
        }
        MessageTypeEnum messageTypeEnum = recordUserMessage.messageType();
        if (UserActionTypeEnum.VIDEO_COLLECT.getType().equals(actionType)) {
            messageTypeEnum = MessageTypeEnum.COLLECTION;
        }
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        userMessageService.saveUserMessage(videoId, tokenUserInfoDto == null ? null : tokenUserInfoDto.getUserId(), messageTypeEnum, content, replyCommentId);
    }

    protected TokenUserInfoDto getTokenUserInfoDto() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(Constants.TOKEN_WEB);
        return redisComponent.getTokenInfo(token);
    }
}
