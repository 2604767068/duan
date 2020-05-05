package com.example.demo.club.club.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 社团留言表
 * </p>
 *
 * @author youkehai
 * @since 2020-02-03
 */
@TableName("t_club_message")
public class TClubMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
     * 社团id
     */
    @TableField("club_id")
    private String clubId;

    /**
     * 留言内容
     */
    @TableField("message")
    private String message;

    /**
     * 留言人ID
     */
    @TableField("create_id")
    private String createId;

    /**
     * 留言人
     */
    @TableField("create_name")
    private String createName;

    /**
     * 创建时间
     */
    @TableField("create_date")
    private LocalDateTime createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getClubId() {
        return clubId;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }
    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "TClubMessage{" +
            "id=" + id +
            ", clubId=" + clubId +
            ", message=" + message +
            ", createId=" + createId +
            ", createName=" + createName +
            ", createDate=" + createDate +
        "}";
    }
}
