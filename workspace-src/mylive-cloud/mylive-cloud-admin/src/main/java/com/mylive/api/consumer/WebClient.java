package com.mylive.api.consumer;

import com.mylive.annotation.RecordUserMessage;
import com.mylive.entity.constants.Constants;
import com.mylive.entity.enums.MessageTypeEnum;
import com.mylive.entity.po.StatisticsInfo;
import com.mylive.entity.po.VideoInfoFilePost;
import com.mylive.entity.query.UserInfoQuery;
import com.mylive.entity.query.VideoInfoFilePostQuery;
import com.mylive.entity.query.VideoInfoPostQuery;
import com.mylive.entity.query.VideoInfoQuery;
import com.mylive.entity.vo.PaginationResultVO;
import com.mylive.entity.vo.ResponseVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@FeignClient(name = Constants.SERVER_NAME_WEB)
public interface WebClient {

    @RequestMapping(Constants.INNER_API_PREFIX + "/statistics/admin/getActualTimeStatisticsInfo")
    Map getActualTimeStatisticsInfo();

    @RequestMapping(Constants.INNER_API_PREFIX + "/statistics/admin/getWeekStatisticsInfo")
    List<StatisticsInfo> getWeekStatisticsInfo(@RequestParam Integer dataType);

    //获取所有稿件视频
    @RequestMapping(Constants.INNER_API_PREFIX + "/video/admin/loadVideoList")
    PaginationResultVO loadVideoList(@RequestBody VideoInfoPostQuery videoInfoPostQuery);

    //审核视频
    @RequestMapping(Constants.INNER_API_PREFIX + "/video/admin/auditVideo")
    @RecordUserMessage(messageType = MessageTypeEnum.SYS)
    void auditVideo(@RequestParam String videoId, @RequestParam Integer status, @RequestParam String reason);

    //推荐视频
    @RequestMapping(Constants.INNER_API_PREFIX + "/video/admin/recommendVideo")
    void recommendVideo(@RequestParam String videoId);

    //删除视频
    @RequestMapping(Constants.INNER_API_PREFIX + "/video/admin/deleteVideo")
    void deleteVideo(@RequestParam String videoId);

    //加载分p信息
    @RequestMapping(Constants.INNER_API_PREFIX + "/video/admin/loadVideoPList")
    List<VideoInfoFilePost> loadVideoPList(@RequestParam String videoId);

    //加载用户
    @RequestMapping(Constants.INNER_API_PREFIX + "/user/loadUser")
    PaginationResultVO loadUser(@RequestBody UserInfoQuery userInfoQuery);

    //更改用户状态
    @RequestMapping(Constants.INNER_API_PREFIX + "/user/changeStatus")
    void changeStatus(@RequestParam String userId, @RequestParam Integer status);

    //获取视频数量
    @RequestMapping(Constants.INNER_API_PREFIX + "/video/getVideoCount")
    Integer getVideoCount(@RequestBody VideoInfoQuery videoInfoQuery);
}
