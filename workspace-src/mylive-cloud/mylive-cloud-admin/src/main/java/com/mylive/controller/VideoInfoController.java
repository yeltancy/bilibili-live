package com.mylive.controller;

import com.mylive.annotation.RecordUserMessage;
import com.mylive.api.consumer.WebClient;
import com.mylive.entity.enums.MessageTypeEnum;
import com.mylive.entity.po.VideoInfoFilePost;
import com.mylive.entity.query.VideoInfoFilePostQuery;
import com.mylive.entity.query.VideoInfoPostQuery;
import com.mylive.entity.vo.PaginationResultVO;
import com.mylive.entity.vo.ResponseVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/videoInfo")
@Validated
public class VideoInfoController extends ABaseController {

    @Resource
    private WebClient webClient;

    //获取所有稿件视频
    @RequestMapping("/loadVideoList")
    public ResponseVO loadVideoList(VideoInfoPostQuery videoInfoPostQuery) {
        return getSuccessResponseVO(webClient.loadVideoList(videoInfoPostQuery));
    }

    //审核视频
    @RequestMapping("/auditVideo")
    @RecordUserMessage(messageType = MessageTypeEnum.SYS)
    public ResponseVO auditVideo(@NotEmpty String videoId, @NotNull Integer status, String reason) {
        webClient.auditVideo(videoId, status, reason);
        return getSuccessResponseVO(null);
    }

    //推荐视频
    @RequestMapping("/recommendVideo")
    public ResponseVO recommendVideo(@NotEmpty String videoId) {
        webClient.recommendVideo(videoId);
        return getSuccessResponseVO(null);
    }

    //删除视频
    @RequestMapping("/deleteVideo")
    public ResponseVO deleteVideo(@NotEmpty String videoId) {
        webClient.deleteVideo(videoId);
        return getSuccessResponseVO(null);
    }

    //加载分p信息
    @RequestMapping("/loadVideoPList")
    public ResponseVO loadVideoPList(@NotEmpty String videoId) {
        return getSuccessResponseVO(webClient.loadVideoPList(videoId));
    }
}
