package cn.fbi.entity;

import lombok.Data;

@Data
public class PostWithUsername {
    private Post post;
    private String username;
    private boolean likePostFlag;
    private boolean bookmarkPostFlag;
    private boolean followUserFlag;

    public PostWithUsername(Post post, String username, boolean likePostFlag, boolean bookmarkPostFlag, boolean followUserFlag) {
        this.post = post;
        this.username = username;
        this.likePostFlag = likePostFlag;
        this.bookmarkPostFlag = bookmarkPostFlag;
        this.followUserFlag = followUserFlag;
    }

}
