package com.echo.common.security.entity;

/**
 * 用户和岗位关联 sys_user_post
 *
 * @author echo
 */
public class SysUserPost
{
    /** 用户ID */
    private Long userId;

    /** 岗位ID */
    private Long postId;

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getPostId()
    {
        return postId;
    }

    public void setPostId(Long postId)
    {
        this.postId = postId;
    }

}
