package com.mylive.api.provider;

import com.mylive.entity.constants.Constants;
import com.mylive.entity.query.VideoCommentQuery;
import com.mylive.entity.query.VideoDanmuQuery;
import com.mylive.entity.vo.PaginationResultVO;
import com.mylive.entity.vo.ResponseVO;
import com.mylive.service.VideoCommentService;
import com.mylive.service.VideoDanmuService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(Constants.INNER_API_PREFIX + "/danmu")
public class VideoDanmuApi {
    @Resource
    private VideoDanmuService videoDanmuService;

    //加载弹幕
    @RequestMapping("/admin/loadDanmu")
    public PaginationResultVO loadDanmu(Integer pageNo, String videoNameFuzzy) {
        VideoDanmuQuery videoDanmuQuery = new VideoDanmuQuery();
        videoDanmuQuery.setOrderBy("danmu_id desc");
        videoDanmuQuery.setPageNo(pageNo);
        videoDanmuQuery.setQueryVideoInfo(true);
        videoDanmuQuery.setVideoNameFuzzy(videoNameFuzzy);
        PaginationResultVO resultVO = videoDanmuService.findListByPage(videoDanmuQuery);
        return resultVO;
    }

    //删除弹幕
    @RequestMapping("/admin/delDanmu")
    public void delDanmu(@NotNull Integer danmuId) {
        videoDanmuService.deleteDanmu(null, danmuId);
    }

    //删除弹幕
    @RequestMapping("/delDanmuByVideoId")
    public void delDanmuByVideoId(@NotNull String videoId) {
        VideoDanmuQuery videoDanmuQuery = new VideoDanmuQuery();
        videoDanmuQuery.setVideoId(videoId);
        videoDanmuService.deleteByParam(videoDanmuQuery);
    }
}
