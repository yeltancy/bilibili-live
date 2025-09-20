package com.mylive.service;

import com.mylive.entity.dto.UserMessageCountDto;
import com.mylive.entity.enums.MessageTypeEnum;
import com.mylive.entity.po.UserMessage;
import com.mylive.entity.query.UserMessageQuery;
import com.mylive.entity.vo.PaginationResultVO;

import java.util.List;


/**
 * 用户消息表 业务接口
 */
public interface UserMessageService {

    /**
     * 根据条件查询列表
     */
    List<UserMessage> findListByParam(UserMessageQuery param);

    /**
     * 根据条件查询列表
     */
    Integer findCountByParam(UserMessageQuery param);

    /**
     * 分页查询
     */
    PaginationResultVO<UserMessage> findListByPage(UserMessageQuery param);

    /**
     * 新增
     */
    Integer add(UserMessage bean);

    /**
     * 批量新增
     */
    Integer addBatch(List<UserMessage> listBean);

    /**
     * 批量新增/修改
     */
    Integer addOrUpdateBatch(List<UserMessage> listBean);

    /**
     * 多条件更新
     */
    Integer updateByParam(UserMessage bean, UserMessageQuery param);

    /**
     * 多条件删除
     */
    Integer deleteByParam(UserMessageQuery param);

    /**
     * 根据MessageId查询对象
     */
    UserMessage getUserMessageByMessageId(Integer messageId);


    /**
     * 根据MessageId修改
     */
    Integer updateUserMessageByMessageId(UserMessage bean, Integer messageId);


    /**
     * 根据MessageId删除
     */
    Integer deleteUserMessageByMessageId(Integer messageId);

    /**
     * 用户消息列表中应发送的消息
     */
    void saveUserMessage(String videoId, String sendUserId, MessageTypeEnum messageTypeEnum, String content, Integer replyCommentId);

    /**
     * 消息通知里分类展示未读消息(点赞、评论、系统消息等)
     */
    List<UserMessageCountDto> getMessageTypeNoReadCount(String userId);
}