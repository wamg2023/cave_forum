package cn.fbi.entity;

import lombok.Data;

@Data
public class PostWithUsername {
    private Post post;
    private String username;
    public PostWithUsername(Post post, String username) {
        this.post = post;
        this.username = username;
    }

}
