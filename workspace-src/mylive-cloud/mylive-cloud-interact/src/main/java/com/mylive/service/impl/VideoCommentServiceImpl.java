package com.mylive.service.impl;

import com.mylive.api.consumer.VideoClient;
import com.mylive.entity.constants.Constants;
import com.mylive.entity.enums.CommentTopTypeEnum;
import com.mylive.entity.enums.PageSize;
import com.mylive.entity.enums.ResponseCodeEnum;
import com.mylive.entity.enums.UserActionTypeEnum;
import com.mylive.entity.po.UserInfo;
import com.mylive.entity.po.VideoComment;
import com.mylive.entity.po.VideoInfo;
import com.mylive.entity.query.SimplePage;
import com.mylive.entity.query.VideoCommentQuery;
import com.mylive.entity.vo.PaginationResultVO;
import com.mylive.exception.BusinessException;
import com.mylive.mappers.VideoCommentMapper;
import com.mylive.service.VideoCommentService;
import com.mylive.utils.StringTools;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * 评论 业务接口实现
 */
@Service("videoCommentService")
public class VideoCommentServiceImpl implements VideoCommentService {

    @Resource
    private VideoCommentMapper<VideoComment, VideoCommentQuery> videoCommentMapper;

    @Resource
    private VideoClient videoClient;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<VideoComment> findListByParam(VideoCommentQuery param) {
        if (param.getLoadChildren() != null && param.getLoadChildren()) {
            return this.videoCommentMapper.selectListWithChildren(param);
        }
        return this.videoCommentMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(VideoCommentQuery param) {
        return this.videoCommentMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResultVO<VideoComment> findListByPage(VideoCommentQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<VideoComment> list = this.findListByParam(param);
        PaginationResultVO<VideoComment> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(VideoComment bean) {
        return this.videoCommentMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<VideoComment> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.videoCommentMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<VideoComment> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.videoCommentMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(VideoComment bean, VideoCommentQuery param) {
        StringTools.checkParam(param);
        return this.videoCommentMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(VideoCommentQuery param) {
        StringTools.checkParam(param);
        return this.videoCommentMapper.deleteByParam(param);
    }

    /**
     * 根据CommentId获取对象
     */
    @Override
    public VideoComment getVideoCommentByCommentId(Integer commentId) {
        return this.videoCommentMapper.selectByCommentId(commentId);
    }

    /**
     * 根据CommentId修改
     */
    @Override
    public Integer updateVideoCommentByCommentId(VideoComment bean, Integer commentId) {
        return this.videoCommentMapper.updateByCommentId(bean, commentId);
    }

    /**
     * 根据CommentId删除
     */
    @Override
    public Integer deleteVideoCommentByCommentId(Integer commentId) {
        return this.videoCommentMapper.deleteByCommentId(commentId);
    }

    /**
     * 发表评论
     */
    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public void postComment(VideoComment comment, Integer replyCommentId) {
        VideoInfo videoInfo = videoClient.getVideoInfoByVideoId(comment.getVideoId());
        if (videoInfo == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (videoInfo.getInteraction() != null && videoInfo.getInteraction().contains(Constants.ZERO.toString())) {
            throw new BusinessException("UP主已关闭评论区");
        }
        if (replyCommentId != null) {
            VideoComment replyComment = this.getVideoCommentByCommentId(replyCommentId);
            if (replyComment == null || replyComment.getVideoId().equals(comment.getVideoId())) {
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }
            if (replyComment.getpCommentId() == 0) {
                comment.setpCommentId(replyComment.getCommentId());
            } else {
                comment.setpCommentId(replyComment.getpCommentId());
                comment.setReplyUserId(replyComment.getUserId());
            }
            UserInfo userInfo = videoClient.getUserInfoByUserId(replyComment.getUserId());
            comment.setReplyNickName(userInfo.getNickName());
            comment.setReplyAvatar(userInfo.getAvatar());
        } else {
            comment.setpCommentId(0);
        }
        comment.setPostTime(new Date());
        comment.setVideoUserId(videoInfo.getUserId());
        this.videoCommentMapper.insert(comment);
        if (comment.getpCommentId() == 0) {
            this.videoClient.updateCountInfo(comment.getVideoId(), UserActionTypeEnum.VIDEO_COMMENT.getField(), Constants.ONE);
        }
    }

    /**
     * 置顶评论
     */
    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public void topComment(Integer commentId, String userId) {
        this.cancelTopComment(commentId, userId);
        VideoComment videoComment = new VideoComment();
        videoComment.setTopType(CommentTopTypeEnum.TOP.getType());
        videoCommentMapper.updateByCommentId(videoComment, commentId);
    }

    /**
     * 取消置顶评论
     */
    @Override
    public void cancelTopComment(Integer commentId, String userId) {
        VideoComment dbVideoComment = this.getVideoCommentByCommentId(commentId);
        if (dbVideoComment == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        VideoInfo videoInfo = videoClient.getVideoInfoByVideoId(dbVideoComment.getVideoId());
        if (videoInfo == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (!videoInfo.getUserId().equals(userId)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        VideoComment videoComment = new VideoComment();
        videoComment.setTopType(CommentTopTypeEnum.NO_TOP.getType());

        VideoCommentQuery videoCommentQuery = new VideoCommentQuery();
        videoCommentQuery.setVideoId(dbVideoComment.getVideoId());
        videoCommentQuery.setTopType(CommentTopTypeEnum.TOP.getType());
        videoCommentMapper.updateByParam(videoComment, videoCommentQuery);
    }

    /**
     * 删除评论
     */
    @Override
    public void deleteComment(Integer commentId, String userId) {
        VideoComment comment = videoCommentMapper.selectByCommentId(commentId);
        if (comment == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        VideoInfo videoInfo = videoClient.getVideoInfoByVideoId(comment.getVideoId());
        if (videoInfo == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (userId != null && !videoInfo.getUserId().equals(userId) && !comment.getUserId().equals(userId)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        videoCommentMapper.deleteByCommentId(commentId);
        if (comment.getpCommentId() == 0) {
            videoClient.updateCountInfo(comment.getVideoId(), UserActionTypeEnum.VIDEO_COMMENT.getField(), -Constants.ONE);
            //删除子评论
            VideoCommentQuery videoCommentQuery = new VideoCommentQuery();
            videoCommentQuery.setpCommentId(commentId);
            videoCommentMapper.deleteByParam(videoCommentQuery);
        }
    }
}