package com.example.demo.club.club.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 社团成员表
 * </p>
 *
 * @author youkehai
 * @since 2020-02-03
 */
@TableName("t_club_user")
public class TClubUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /***
     * 成员名称
     */
    @TableField(exist = false)
    private String userName;
    @TableField(exist = false)
    private String clubReason;
    
    public String getClubReason() {
		return clubReason;
	}

	public void setClubReason(String clubReason) {
		this.clubReason = clubReason;
	}

	/***
     * 社团状态
     */
    @TableField(exist = false)
    private String clubStatus;
    /***
     * 成员头像
     */
    @TableField(exist = false)
    private String userAvatar;
    /***
     * 社团名称
     */
    @TableField(exist = false)
    private String clubName;
    /***
     * 社团说明信息
     */
    @TableField(exist = false)
    private String clubTips;
    
    public String getClubTips() {
		return clubTips;
	}

	public void setClubTips(String clubTips) {
		this.clubTips = clubTips;
	}

	/***
     * 社团头像
     */
    @TableField(exist = false)
    private String avatar;
    
    public String getUserName() {
		return userName;
	}

	public String getClubStatus() {
		return clubStatus;
	}

	public void setClubStatus(String clubStatus) {
		this.clubStatus = clubStatus;
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

	public String getClubName() {
		return clubName;
	}

	public void setClubName(String clubName) {
		this.clubName = clubName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	/**
     * id
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
     * 备注名称
     */
    @TableField("alias_user_name")
    private String aliasUserName;

    /**
     * 说明信息
     */
    @TableField("tips")
    private String tips;

    /**
     * 社团id
     */
    @TableField("club_id")
    private String clubId;

    /**
     * 申请时间
     */
    @TableField("create_date")
    private LocalDateTime createDate;

    /**
     * 加入时间
     */
    @TableField("join_date")
    private LocalDateTime joinDate;

    /**
     * 状态(0正常，1审核中，2被拒绝)
     */
    @TableField("status")
    private String status;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private String userId;

    /**
     * 用户类型（0为正常用户，1为社团管理员）
     */
    @TableField("user_type")
    private String userType;

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
    public String getAliasUserName() {
        return aliasUserName;
    }

    public void setAliasUserName(String aliasUserName) {
        this.aliasUserName = aliasUserName;
    }
    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }
    public String getClubId() {
        return clubId;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDateTime joinDate) {
        this.joinDate = joinDate;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    @Override
    public String toString() {
        return "TClubUser{" +
            "id=" + id +
            ", aliasUserName=" + aliasUserName +
            ", tips=" + tips +
            ", clubId=" + clubId +
            ", createDate=" + createDate +
            ", joinDate=" + joinDate +
            ", status=" + status +
            ", userId=" + userId +
            ", userType=" + userType +
            ", createId=" + createId +
        "}";
    }
}
