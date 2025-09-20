package com.mylive.api.provider;

import com.mylive.entity.constants.Constants;
import com.mylive.entity.query.VideoCommentQuery;
import com.mylive.entity.vo.PaginationResultVO;
import com.mylive.entity.vo.ResponseVO;
import com.mylive.service.VideoCommentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(Constants.INNER_API_PREFIX + "/comment")
public class VideoCommentApi {
    @Resource
    private VideoCommentService videoCommentService;

    //加载评论
    @RequestMapping("/admin/loadComment")
    public PaginationResultVO loadComment(Integer pageNo, String videoNameFuzzy) {
        VideoCommentQuery videoCommentQuery = new VideoCommentQuery();
        videoCommentQuery.setOrderBy("comment_id desc");
        videoCommentQuery.setPageNo(pageNo);
        videoCommentQuery.setQueryVideoInfo(true);
        videoCommentQuery.setVideoNameFuzzy(videoNameFuzzy);
        PaginationResultVO resultVO = videoCommentService.findListByPage(videoCommentQuery);
        return resultVO;
    }

    //删除评论
    @RequestMapping("/admin/delComment")
    public void delComment(@NotNull Integer commentId) {
        videoCommentService.deleteComment(commentId, null);
    }

    //删除评论
    @RequestMapping("/delCommentByVideoId")
    public void delCommentByVideoId(@NotNull String videoId) {
        VideoCommentQuery videoCommentQuery = new VideoCommentQuery();
        videoCommentQuery.setVideoId(videoId);
        videoCommentService.deleteByParam(videoCommentQuery);
    }
}
