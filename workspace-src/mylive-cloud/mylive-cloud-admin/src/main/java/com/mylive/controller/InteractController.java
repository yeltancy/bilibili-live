package com.mylive.controller;

import com.mylive.api.consumer.InteractClient;
import com.mylive.entity.vo.PaginationResultVO;
import com.mylive.entity.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/interact")
@Validated
@Slf4j
public class InteractController extends ABaseController {

    @Resource
    private InteractClient interactClient;

    //加载评论
    @RequestMapping("/loadComment")
    public ResponseVO loadComment(Integer pageNo, String videoNameFuzzy) {
        return getSuccessResponseVO(interactClient.loadComment(pageNo, videoNameFuzzy));
    }

    //删除评论
    @RequestMapping("/delComment")
    public ResponseVO delComment(@NotNull Integer commentId) {
        interactClient.delComment(commentId);
        return getSuccessResponseVO(null);
    }

    //加载弹幕
    @RequestMapping("/loadDanmu")
    public ResponseVO loadDanmu(Integer pageNo, String videoNameFuzzy) {
        return getSuccessResponseVO(interactClient.loadDanmu(pageNo, videoNameFuzzy));
    }

    //删除弹幕
    @RequestMapping("/delDanmu")
    public ResponseVO delDanmu(@NotNull Integer danmuId) {
        interactClient.delDanmu(danmuId);
        return getSuccessResponseVO(null);
    }
}
