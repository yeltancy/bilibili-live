package com.mylive.entity.constants;

public class Constants {
    //零
    public static final Integer ZERO = 0;
    //一
    public static final Integer ONE = 1;
    //4
    public static final Integer LENGTH_4 = 4;
    //10
    public static final Integer LENGTH_10 = 10;
    //15
    public static final Integer LENGTH_15 = 15;
    //20
    public static final Integer LENGTH_20 = 20;
    //30
    public static final Integer LENGTH_30 = 30;
    //24小时
    public static final Integer HOUR_24 = 24;
    //MB
    public static final Long MB_SIZE = 1024 * 1024L;
    //临时文件存放路径
    public static final String FILE_FOLDER_TEMP = "temp/";
    //文件存放路径
    public static final String FILE_FOLDER = "file/";
    //封面存放路径
    public static final String FILE_COVER = "cover/";
    //视频存放路径
    public static final String FILE_VIDEO = "video/";
    //密码正则
    // 至少包含一个数字,至少包含一个字母（大小写均可）,可以包含数字、字母以及特殊字符（~!@#$%^&*_）。
    public static final String REGEX_PASSWORD = "^(?=.*\\d)(?=.*[a-zA-Z])[\\da-zA-Z~!@#$%^&*_]{8,18}";
    //过期时间为1秒钟
    public static final Integer REDIS_KEY_EXPIRES_ONE_SECONDS = 1000;
    //过期时间为1分钟
    public static final Integer REDIS_KEY_EXPIRES_ONE_MINUTE = 60000;
    //过期时间为1天
    public static final Integer REDIS_KEY_EXPIRES_ONE_DAY = REDIS_KEY_EXPIRES_ONE_MINUTE * 60 * 24;
    //秒级时间的1天
    public static final Integer TIMES_SECONDS_DAY = REDIS_KEY_EXPIRES_ONE_DAY / 1000;
    //多个项目用同一个redis时，key前缀加上项目名
    public static final String REDIS_KEY_PREFIX = "mylive:";
    //验证码key
    public static final String REDIS_KEY_CHECK_CODE = REDIS_KEY_PREFIX + "checkcode:";
    //web端token key
    public static final String REDIS_KEY_TOKEN_WEB = REDIS_KEY_PREFIX + "token:web:";
    //admin端token key
    public static final String REDIS_KEY_TOKEN_ADMIN = REDIS_KEY_PREFIX + "token:admin:";
    //token
    public static final String TOKEN_WEB = "token";
    //token
    public static final String TOKEN_ADMIN = "adminToken";
    //分类列表
    public static final String REDIS_KEY_CATEGORY_LIST = REDIS_KEY_PREFIX + "category:list:";
    //缩略图后缀
    public static final String IMAGE_THUMBNAIL_SUFFIX = "_thumbnail.jpg";
    //上传视频存放路径
    public static final String REDIS_KEY_UPLOADING_FILE = REDIS_KEY_PREFIX + "uploading:";
    //系统设置
    public static final String REDIS_KEY_SYS_SETTING = REDIS_KEY_PREFIX + "sysSetting:";
    //删除文件的结合
    public static final String REDIS_KEY_FILE_DEL = REDIS_KEY_PREFIX + "file:list:del:";
    //转码队列
    public static final String REDIS_KEY_QUEUE_TRANSFER = REDIS_KEY_PREFIX + "queue:transfer:";
    //播放量队列
    public static final String REDIS_KEY_QUEUE_VIDEO_PLAY = REDIS_KEY_PREFIX + "queue:video:play:";
    //临时文件名
    public static final String TEMP_VIDEO_NAME = "/temp.mp4";
    //hevc文件格式
    public static final String VIDEO_CODE_HEVC = "hevc";
    //_temp文件格式后缀
    public static final String VIDEO_CODE_TEMP_FILE_SUFFIX = "_temp";
    //ts文件名
    public static final String TS_NAME = "index.ts";
    //m3u8文件名
    public static final String M3U8_NAME = "index.m3u8";

    // 视频在线
    public static final String REDIS_KEY_VIDEO_PLAY_COUNT_ONLINE_PREFIX = REDIS_KEY_PREFIX + "video:play:online:";
    public static final String REDIS_KEY_VIDEO_PLAY_COUNT_ONLINE = REDIS_KEY_VIDEO_PLAY_COUNT_ONLINE_PREFIX + "count:%s";
    public static final String REDIS_KEY_VIDEO_PLAY_COUNT_USER_PREFIX = "user:";
    public static final String REDIS_KEY_VIDEO_PLAY_COUNT_USER = REDIS_KEY_VIDEO_PLAY_COUNT_ONLINE_PREFIX + REDIS_KEY_VIDEO_PLAY_COUNT_USER_PREFIX + "%s:%s";

    //修改昵称需要花费硬币数
    public static final Integer UPDATE_NICK_NAME_COIN = 5;
    //搜索热词
    public static final String REDIS_KEY_VIDEO_SEARCH_COUNT = REDIS_KEY_PREFIX + "video:search:";
    //按天记录播放数量
    public static final String REDIS_KEY_VIDEO_PLAY_COUNT = REDIS_KEY_PREFIX + "video:playcount:";

    //内部api调用前缀
    public static final String INNER_API_PREFIX = "/innerApi";
    //admin服务端名
    public static final String SERVER_NAME_ADMIN = "mylive-cloud-admin";
    //web服务端名
    public static final String SERVER_NAME_WEB = "mylive-cloud-web";
    //resource服务端名
    public static final String SERVER_NAME_RESOURCE = "mylive-cloud-resource";
    //interact服务端名
    public static final String SERVER_NAME_INTERACT = "mylive-cloud-interact";
}
