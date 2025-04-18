package com.cqu.filmsystem.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;



@JsonIgnoreProperties(value = { "handler" })
public class Comment {
    private Integer id;

    private String content;

    private Date createTime;

    @JsonIgnore
    private Comment parentComment;

    private List<Comment> replyList;

    private UserInfo userInfo;

    private Movie movie;

    private String  parentCommentUserInfoNickname;

    private Long  parentCommentUserInfoId;


    public Long getParentCommentUserInfoId() {
        return parentCommentUserInfoId;
    }

    public void setParentCommentUserInfoId(Long parentCommentUserInfoId) {
        this.parentCommentUserInfoId = parentCommentUserInfoId;
    }

    public String getParentCommentUserInfoNickname() {
        return parentCommentUserInfoNickname;
    }

    public void setParentCommentUserInfoNickname(String parentCommentUserInfoNickname) {
        this.parentCommentUserInfoNickname = parentCommentUserInfoNickname;
    }

    public List<Comment> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<Comment> replyList) {
        this.replyList = replyList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }



    public Comment getParentComment() {
        return parentComment;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
//                ", parentComment=" + parentComment +
                ", replyList=" + replyList +
                ", userInfo=" + userInfo +
                ", movie=" + movie +
                ", parentCommentUserInfoNickname='" + parentCommentUserInfoNickname + '\'' +
                ", parentCommentUserInfoId=" + parentCommentUserInfoId +
                '}';
    }
}
