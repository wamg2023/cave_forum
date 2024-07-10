package cn.fbi.common;

import lombok.Data;

/**
 *
 * @parm post_id ,评论的帖子id
 * @parm sortingMethod ,前端获取数据的方式，0表示按时间顺序，1表示按点赞数排序，2表示只看作者
 * */
@Data
public class CommentParm {
    private int post_id;//评论的帖子id
    private int sortingMethod;//数据的排序方式
}
