package com.example.demo.club.club.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 社团表
 * </p>
 *
 * @author youkehai
 * @since 2020-02-03
 */
@TableName("t_club")
public class TClub implements Serializable {

    private static final long serialVersionUID = 1L;

    /***
     * 管理员名称
     */
    @TableField(exist = false)
    private String userName;
    /***
     * 管理员头像
     */
    @TableField(exist = false)
    private String userAvatar;
    
    public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserAvatar() {
		return userAvatar;
	}

	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}

	/**
     * id
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
     * 名称
     */
    @NotNull
    @TableField("name")
    private String name;
    /**
     *驳回理由
     */
    @TableField("reason")
    private String reason;

    /**
     * 说明信息
     */
    @TableField("tips")
    private String tips;

    /**
     * 社团logo
     */
    @NotNull
    @TableField("avatar")
    private String avatar;

    /**
     * 注册时间
     */
    @TableField("create_date")
    private LocalDateTime createDate;
    
    /**
     * 同意拒绝时间
     */
    @TableField("update_date")
    private LocalDateTime updateDate;

    /**
     * 状态(0正常，1审核中，2被拒绝)
     */
    @TableField("status")
    private String status;

    /**
     * 创建者ID
     */
    @TableField("create_id")
    private String createId;

    public String getId() {
        return id;
    }

    public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getTips() {
        return tips;
    }

    public LocalDateTime getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}

	public void setTips(String tips) {
        this.tips = tips;
    }
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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
    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    @Override
    public String toString() {
        return "TClub{" +
            "id=" + id +
            ", name=" + name +
            ", tips=" + tips +
            ", avatar=" + avatar +
            ", createDate=" + createDate +
            ", status=" + status +
            ", createId=" + createId +
        "}";
    }
}
