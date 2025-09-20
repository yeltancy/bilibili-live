package com.mylive.task;

import com.mylive.component.EsSearchComponent;
import com.mylive.component.RedisComponent;
import com.mylive.component.TransferFileComponent;
import com.mylive.entity.constants.Constants;
import com.mylive.entity.dto.VideoPlayInfoDto;
import com.mylive.entity.enums.SearchOrderTypeEnum;
import com.mylive.entity.po.VideoInfoFilePost;
import com.mylive.service.VideoInfoPostService;
import com.mylive.service.VideoInfoService;
import com.mylive.service.VideoPlayHistoryService;
import com.mylive.utils.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class ExecuteQueueTask {
    private ExecutorService executorService = Executors.newFixedThreadPool(Constants.LENGTH_4);

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private TransferFileComponent transferFileComponent;

    @PostConstruct
    public void consumeTransferFileQueue() {
        executorService.execute(() -> {
            while (true) {
                try {
                    VideoInfoFilePost videoInfoFile = redisComponent.getFileFromTransferQueue();
                    if (videoInfoFile == null) {
                        Thread.sleep(1500);
                        continue;
                    }
                    transferFileComponent.transferVideoFile(videoInfoFile);
                } catch (InterruptedException e) {
                    log.error("获取转码文件队列信息失败", e);
                }
            }
        });
    }

//    @PostConstruct
//    public void consumeTransferFileQueue() {
//        executorService.execute(() -> {
//            while (true) {
//                try {
//                    VideoInfoFilePost videoInfoFile = redisComponent.getFileFromTransferQueue();
//                    if (videoInfoFile == null) {
//                        Thread.sleep(1500);
//                        continue;
//                    }
//                    videoInfoPostService.transferVideoFile(videoInfoFile);
//                } catch (InterruptedException e) {
//                    log.error("获取转码文件队列信息失败", e);
//                }
//            }
//        });
//    }
}
