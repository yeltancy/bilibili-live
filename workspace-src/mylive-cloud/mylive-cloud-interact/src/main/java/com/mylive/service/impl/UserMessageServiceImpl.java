package com.mylive.service.impl;

import com.mylive.api.consumer.VideoClient;
import com.mylive.entity.dto.UserMessageCountDto;
import com.mylive.entity.dto.UserMessageExtendDto;
import com.mylive.entity.enums.MessageReadTypeEnum;
import com.mylive.entity.enums.MessageTypeEnum;
import com.mylive.entity.enums.PageSize;
import com.mylive.entity.po.UserMessage;
import com.mylive.entity.po.VideoComment;
import com.mylive.entity.po.VideoInfo;
import com.mylive.entity.po.VideoInfoPost;
import com.mylive.entity.query.*;
import com.mylive.entity.vo.PaginationResultVO;
import com.mylive.mappers.UserMessageMapper;
import com.mylive.mappers.VideoCommentMapper;
import com.mylive.service.UserMessageService;
import com.mylive.utils.JsonUtils;
import com.mylive.utils.StringTools;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * 用户消息表 业务接口实现
 */
@Service("userMessageService")
public class UserMessageServiceImpl implements UserMessageService {

    @Resource
    private UserMessageMapper<UserMessage, UserMessageQuery> userMessageMapper;

    @Resource
    private VideoClient videoClient;

    @Resource
    private VideoCommentMapper<VideoComment, VideoCommentQuery> videoCommentMapper;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<UserMessage> findListByParam(UserMessageQuery param) {
        return this.userMessageMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(UserMessageQuery param) {
        return this.userMessageMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResultVO<UserMessage> findListByPage(UserMessageQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<UserMessage> list = this.findListByParam(param);
        PaginationResultVO<UserMessage> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(UserMessage bean) {
        return this.userMessageMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<UserMessage> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.userMessageMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<UserMessage> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.userMessageMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(UserMessage bean, UserMessageQuery param) {
        StringTools.checkParam(param);
        return this.userMessageMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(UserMessageQuery param) {
        StringTools.checkParam(param);
        return this.userMessageMapper.deleteByParam(param);
    }

    /**
     * 根据MessageId获取对象
     */
    @Override
    public UserMessage getUserMessageByMessageId(Integer messageId) {
        return this.userMessageMapper.selectByMessageId(messageId);
    }

    /**
     * 根据MessageId修改
     */
    @Override
    public Integer updateUserMessageByMessageId(UserMessage bean, Integer messageId) {
        return this.userMessageMapper.updateByMessageId(bean, messageId);
    }

    /**
     * 根据MessageId删除
     */
    @Override
    public Integer deleteUserMessageByMessageId(Integer messageId) {
        return this.userMessageMapper.deleteByMessageId(messageId);
    }

    /**
     * 保存用户消息列表中应发送的消息
     */
    @Override
    @Async
    public void saveUserMessage(String videoId, String sendUserId, MessageTypeEnum messageTypeEnum, String content, Integer replyCommentId) {
        VideoInfo videoInfo = videoClient.getVideoInfoByVideoId(videoId);
        if (null == videoInfo) {
            return;
        }
        UserMessageExtendDto extendDto = new UserMessageExtendDto();
        extendDto.setMessageContent(content);
        String userId = videoInfo.getUserId();
        //收藏、点赞，已经记录的不再记录
        if (ArrayUtils.contains(new Integer[]{MessageTypeEnum.LIKE.getType(), MessageTypeEnum.COLLECTION.getType()}, messageTypeEnum.getType())) {
            UserMessageQuery userMessageQuery = new UserMessageQuery();
            userMessageQuery.setUserId(userId);
            userMessageQuery.setVideoId(videoId);
            userMessageQuery.setMessageType(messageTypeEnum.getType());
            Integer count = this.findCountByParam(userMessageQuery);
            if (count > 0) {
                return;
            }
            UserMessage userMessage = new UserMessage();
            userMessage.setUserId(userId);
            userMessage.setVideoId(videoId);
            userMessage.setReadType(MessageReadTypeEnum.NO_READ.getType());
            userMessage.setCreateTime(new Date());
            userMessage.setMessageType(messageTypeEnum.getType());
            userMessage.setSendUserId(sendUserId);
            //评论特殊处理
            if (replyCommentId != null) {
                VideoComment comment = videoCommentMapper.selectByCommentId(replyCommentId);
                if (null != comment) {
                    userId = comment.getUserId();
                    extendDto.setMessageContentReply(comment.getContent());
                }
            }
            if(userId.equals(sendUserId)){
                return;
            }
            //系统消息特殊处理
            if(MessageTypeEnum.SYS == messageTypeEnum){
                VideoInfoPost videoInfoPost = videoClient.getVideoInfoPostByVideoId(videoId);
                extendDto.setAuditStatus(videoInfoPost.getStatus());
            }
            userMessage.setUserId(userId);
            userMessage.setExtendJson(JsonUtils.convertObj2Json(extendDto));
            this.add(userMessage);
        }
    }

    /**
     * 消息通知里分类展示未读消息(点赞、评论、系统消息等)
     */
    @Override
    public List<UserMessageCountDto> getMessageTypeNoReadCount(String userId) {
        return this.userMessageMapper.getMessageTypeNoReadCount(userId);
    }
}