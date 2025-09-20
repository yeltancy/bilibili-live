package com.mylive.component;

import com.mylive.api.consumer.VideoClient;
import com.mylive.entity.config.AppConfig;
import com.mylive.entity.constants.Constants;
import com.mylive.entity.dto.UploadingFileDto;
import com.mylive.entity.enums.VideoFileTransferResultEnum;
import com.mylive.entity.enums.VideoStatusEnum;
import com.mylive.entity.po.VideoInfoFilePost;
import com.mylive.entity.po.VideoInfoPost;
import com.mylive.entity.query.VideoInfoFilePostQuery;
import com.mylive.exception.BusinessException;
import com.mylive.utils.FFmpegUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.RandomAccessFile;

@Component
@Slf4j
public class TransferFileComponent {
    @Resource
    private RedisComponent redisComponent;

    @Resource
    private AppConfig appConfig;

    @Resource
    private FFmpegUtils fFmpegUtils;

    @Resource
    private VideoClient videoClient;

    /**
     * 转码视频文件
     */
    public void transferVideoFile(VideoInfoFilePost videoInfoFilePost) {
        VideoInfoFilePost updateFilePost = new VideoInfoFilePost();
        try {
            UploadingFileDto fileDto = redisComponent.getUploadVideoFileInfo(videoInfoFilePost.getUserId(), videoInfoFilePost.getUploadId());
            String tempFilePath = appConfig.getProjectFolder() + Constants.FILE_FOLDER + Constants.FILE_FOLDER_TEMP + fileDto.getFilePath();
            File tempFile = new File(tempFilePath);
            String targetFilePath = appConfig.getProjectFolder() + Constants.FILE_FOLDER + Constants.FILE_VIDEO + fileDto.getFilePath();
            File targetFile = new File(targetFilePath);
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }
            FileUtils.copyDirectory(tempFile, targetFile);
            //删除临时目录
            FileUtils.forceDelete(tempFile);
            redisComponent.delVideoFileInfo(videoInfoFilePost.getUserId(), videoInfoFilePost.getUploadId());
            String completeVideo = targetFilePath + Constants.TEMP_VIDEO_NAME;
            //合并文件
            this.union(targetFilePath, completeVideo, true);
            //获取播放时长
            Integer duration = fFmpegUtils.getVideoInfoDuration(completeVideo);
            updateFilePost.setDuration(duration);
            updateFilePost.setFileSize(new File(completeVideo).length());
            updateFilePost.setFilePath(Constants.FILE_VIDEO + fileDto.getFilePath());
            updateFilePost.setTransferResult(VideoFileTransferResultEnum.SUCCESS.getStatus());
            //转成ts文件
            this.convertVideo2Ts(completeVideo);
        } catch (Exception e) {
            log.error("转码视频文件失败", e);
            updateFilePost.setTransferResult(VideoFileTransferResultEnum.FAIL.getStatus());
        } finally {
            videoClient.transferVideoFile4Db(videoInfoFilePost.getUserId(), videoInfoFilePost.getUploadId(), videoInfoFilePost.getVideoId(), updateFilePost);
        }
    }

    protected void union(String dirPath, String toFilePath, Boolean delSource) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            throw new BusinessException("目录不存在");
        }
        File[] fileList = dir.listFiles();
        File targetFile = new File(toFilePath);
        try (RandomAccessFile writeFile = new RandomAccessFile(targetFile, "rw")) {
            byte[] b = new byte[1024 * 10];
            if (fileList != null) {
                for (int i = 0; i < fileList.length; i++) {
                    int len = -1;
                    // 创建读块文件的对象
                    File chunkFile = new File(dirPath + File.separator + i);
                    try (RandomAccessFile readFile = new RandomAccessFile(chunkFile, "r")) {
                        while ((len = readFile.read(b)) != -1) {
                            writeFile.write(b, 0, len);
                        }
                    } catch (Exception e) {
                        log.error("合并分片失败", e);
                        throw new BusinessException("合并文件失败");
                    }
                }
            }
        } catch (Exception e) {
            throw new BusinessException("合并文件" + dirPath + "出错了");
        } finally {
            if (delSource) {
                if (fileList != null) {
                    for (File file : fileList) {
                        file.delete();
                    }
                }
            }
        }
    }

    protected void convertVideo2Ts(String completeVideo) {
        File videoFile = new File(completeVideo);
        File tsFolder = videoFile.getParentFile();
        String codec = fFmpegUtils.getVideoCodec(completeVideo);
        if (Constants.VIDEO_CODE_HEVC.equals(codec)) {
            String tempFileName = completeVideo + Constants.VIDEO_CODE_TEMP_FILE_SUFFIX;
            new File(completeVideo).renameTo(new File(tempFileName));
            fFmpegUtils.convertHevc2Mp4(tempFileName, completeVideo);
            new File(tempFileName).delete();
        }
        fFmpegUtils.convertVideo2Ts(tsFolder, completeVideo);
        videoFile.delete();
    }
}
