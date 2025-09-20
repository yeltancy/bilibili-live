package com.mylive.controller;

import com.mylive.annotation.GlobalInterceptor;
import com.mylive.api.consumer.VideoClient;
import com.mylive.entity.dto.TokenUserInfoDto;
import com.mylive.entity.query.VideoCommentQuery;
import com.mylive.entity.query.VideoDanmuQuery;
import com.mylive.entity.vo.PaginationResultVO;
import com.mylive.entity.vo.ResponseVO;
import com.mylive.service.VideoCommentService;
import com.mylive.service.VideoDanmuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@Api(tags = "创作中心交互(评论、弹幕)controller")
@RestController
@RequestMapping("/ucenter")
@Validated
@Slf4j
public class UcenterController extends ABaseController {

    @Resource
    private VideoDanmuService videoDanmuService;

    @Resource
    private VideoCommentService videoCommentService;

    @Resource
    private VideoClient videoClient;

    @ApiOperation("获取所有评论")
    @RequestMapping("/loadComment")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO loadComment(Integer pageNo,String videoId) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        VideoCommentQuery videoCommentQuery = new VideoCommentQuery();
        videoCommentQuery.setVideoId(videoId);
        videoCommentQuery.setVideoUserId(tokenUserInfoDto.getUserId());
        videoCommentQuery.setOrderBy("comment_id desc");
        videoCommentQuery.setPageSize(pageNo);
        videoCommentQuery.setQueryVideoInfo(true);
        PaginationResultVO resultVO = this.videoCommentService.findListByPage(videoCommentQuery);
        return getSuccessResponseVO(resultVO);
    }

    @ApiOperation("删除评论")
    @RequestMapping("/delComment")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO delComment(@NotNull Integer commentId) {
        videoCommentService.deleteComment(commentId,getTokenUserInfoDto().getUserId());
        return getSuccessResponseVO(null);
    }

    @ApiOperation("获取所有弹幕")
    @RequestMapping("/loadDanmu")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO loadDanmu(Integer pageNo,String videoId) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        VideoDanmuQuery videoDanmuQuery = new VideoDanmuQuery();
        videoDanmuQuery.setVideoId(videoId);
        videoDanmuQuery.setVideoUserId(tokenUserInfoDto.getUserId());
        videoDanmuQuery.setOrderBy("danmu_id desc");
        videoDanmuQuery.setPageSize(pageNo);
        videoDanmuQuery.setQueryVideoInfo(true);
        PaginationResultVO resultVO = this.videoDanmuService.findListByPage(videoDanmuQuery);
        return getSuccessResponseVO(resultVO);
    }

    @ApiOperation("删除弹幕")
    @RequestMapping("/delDanmu")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO delDanmu(@NotNull Integer danmuId) {
        videoDanmuService.deleteDanmu(getTokenUserInfoDto().getUserId(),danmuId);
        return getSuccessResponseVO(null);
    }
}
