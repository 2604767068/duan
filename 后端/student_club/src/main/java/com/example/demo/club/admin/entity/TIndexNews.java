package com.example.demo.club.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 首页新闻表
 * </p>
 *
 * @author youkehai
 * @since 2020-02-03
 */
@TableName("t_index_news")
public class TIndexNews implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
     * 新闻内容
     */
    @TableField("content")
    private String content;

    /**
     * 创建时间
     */
    @TableField("create_date")
    private LocalDateTime createDate;

    /**
     * 状态(0正常，1停用)
     */
    @TableField("status")
    private String status;

    /**
     * 新闻图片
     */
    @TableField("images")
    private String images;

    /**
     * 新闻标题
     */
    @TableField("title")
    private String title;

    /**
     * 创建者ID
     */
    @TableField("create_id")
    private String createId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    @Override
    public String toString() {
        return "TIndexNews{" +
            "id=" + id +
            ", content=" + content +
            ", createDate=" + createDate +
            ", status=" + status +
            ", images=" + images +
            ", title=" + title +
            ", createId=" + createId +
        "}";
    }
}
