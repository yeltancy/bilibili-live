package com.mylive.api.consumer;

import com.mylive.entity.constants.Constants;
import com.mylive.entity.vo.PaginationResultVO;
import com.mylive.entity.vo.ResponseVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;

@FeignClient(name = Constants.SERVER_NAME_INTERACT)
public interface InteractClient {
    //加载评论
    @RequestMapping(Constants.INNER_API_PREFIX + "/comment/admin/loadComment")
    ResponseVO loadComment(@RequestParam Integer pageNo, @RequestParam String videoNameFuzzy);

    //删除评论
    @RequestMapping(Constants.INNER_API_PREFIX + "/comment/admin/delComment")
    ResponseVO delComment(@RequestParam Integer commentId);

    //加载弹幕
    @RequestMapping(Constants.INNER_API_PREFIX + "/danmu/admin/loadDanmu")
    ResponseVO loadDanmu(@RequestParam Integer pageNo, @RequestParam String videoNameFuzzy);

    //删除弹幕
    @RequestMapping(Constants.INNER_API_PREFIX + "/danmu/admin/delDanmu")
    ResponseVO delDanmu(@RequestParam Integer danmuId);
}
