package com.mylive.api.consumer;

import com.mylive.entity.constants.Constants;
import com.mylive.entity.enums.SearchOrderTypeEnum;
import com.mylive.entity.po.UserInfo;
import com.mylive.entity.po.VideoInfo;
import com.mylive.entity.po.VideoInfoFile;
import com.mylive.entity.po.VideoInfoPost;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = Constants.SERVER_NAME_WEB)
public interface VideoClient {
    @RequestMapping(Constants.INNER_API_PREFIX + "/user/updateCoinCountInfo")
    Integer updateCoinCountInfo(@RequestParam String userId, @RequestParam Integer count);

    @RequestMapping(Constants.INNER_API_PREFIX + "/user/getUserInfoByUserId")
    UserInfo getUserInfoByUserId(@RequestParam String userId);

    @RequestMapping(Constants.INNER_API_PREFIX + "/video/getVideoInfoByVideoId")
    VideoInfo getVideoInfoByVideoId(@RequestParam String videoId);

    @RequestMapping(Constants.INNER_API_PREFIX + "/video/updateCountInfo")
    void updateCountInfo(@RequestParam String videoId, @RequestParam String fileId, @RequestParam Integer changeCount);

    @RequestMapping(Constants.INNER_API_PREFIX + "/video/getVideoInfoPostByVideoId")
    VideoInfoPost getVideoInfoPostByVideoId(@RequestParam String videoId);

    @RequestMapping(Constants.INNER_API_PREFIX + "/video/updateDocCount")
    void updateDocCount(@RequestParam String videoId, @RequestParam SearchOrderTypeEnum searchOrderTypeEnum, @RequestParam Integer changeCount);
}
