package com.mylive.controller;

import com.mylive.annotation.GlobalInterceptor;
import com.mylive.api.consumer.VideoClient;
import com.mylive.entity.constants.Constants;
import com.mylive.entity.po.VideoDanmu;
import com.mylive.entity.po.VideoInfo;
import com.mylive.entity.query.VideoDanmuQuery;
import com.mylive.entity.vo.ResponseVO;
import com.mylive.service.VideoDanmuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;

/**
 * 视频弹幕 Controller
 */
@Api(tags = "视频弹幕controller")
@RestController("videoDanmuController")
@RequestMapping("/danmu")
public class VideoDanmuController extends ABaseController {

    @Resource
    private VideoDanmuService videoDanmuService;

    @Resource
    private VideoClient videoClient;

    @ApiOperation("发布弹幕")
    @RequestMapping("/postDanmu")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO postDanmu(@NotEmpty String videoId,
                                @NotEmpty String fileId,
                                @NotEmpty @Size(max = 200) String text, @NotNull Integer mode,
                                @NotEmpty String color, @NotNull Integer time) {
        VideoDanmu videoDanmu = new VideoDanmu();
        videoDanmu.setVideoId(videoId);
        videoDanmu.setFileId(fileId);
        videoDanmu.setText(text);
        videoDanmu.setMode(mode);
        videoDanmu.setColor(color);
        videoDanmu.setTime(time);
        videoDanmu.setUserId(getTokenUserInfoDto().getUserId());
        videoDanmu.setPostTime(new Date());

        videoDanmuService.saveVideoDanmu(videoDanmu);
        return getSuccessResponseVO(null);
    }

    @ApiOperation("获取弹幕列表")
    @RequestMapping("/loadDanmu")
    public ResponseVO loadDanmu(@NotEmpty String fileId,
                                @NotEmpty String videoId) {
        VideoInfo videoInfo = videoClient.getVideoInfoByVideoId(videoId);
        if (videoInfo.getInteraction() != null && videoInfo.getInteraction().contains(Constants.ONE.toString())) {
            return getSuccessResponseVO(new ArrayList<>());
        }
        VideoDanmuQuery videoDanmuQuery = new VideoDanmuQuery();
        videoDanmuQuery.setFileId(fileId);
        videoDanmuQuery.setOrderBy("danmu_id asc");
        return getSuccessResponseVO(videoDanmuService.findListByParam(videoDanmuQuery));
    }
}