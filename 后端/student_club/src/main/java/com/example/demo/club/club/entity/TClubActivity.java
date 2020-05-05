package com.example.demo.club.club.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 社团活动表
 * </p>
 *
 * @author youkehai
 * @since 2020-02-03
 */
@TableName("t_club_activity")
public class TClubActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /***
     * 社团活动器材场地信息
     */
    @TableField(exist = false)
    private TClubActivityAddress tClubActivityAddress;
    /***
     * 按日统计
     */
    @TableField(exist = false)
    private String date;
    /**是否报名*/
    @TableField(exist = false)
    private String inscroll;
    /**活动类型名称*/
    @TableField(exist = false)
    private String typeName;
    
    
    
    public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getInscroll() {
		return inscroll;
	}

	public void setInscroll(String inscroll) {
		this.inscroll = inscroll;
	}

	public TClubActivityAddress gettClubActivityAddress() {
		return tClubActivityAddress;
	}

	public void settClubActivityAddress(TClubActivityAddress tClubActivityAddress) {
		this.tClubActivityAddress = tClubActivityAddress;
	}

	/**
     * 活动名称
     */
    @NotNull
    @TableField("name")
    private String name;
    /**报名人数*/
    @TableField("user_num")
    private String userNum;
    
    /**
     * 创建时间
     */
    @TableField("create_date")
    private LocalDateTime createDate;

    /**
     * 活动开始时间
     */
    @NotNull
    @TableField("activity_start_date")
    private LocalDateTime activityStartDate;
    
    /***
     * 社团ID
     */
    @TableField("club_id")
    private String clubId;
    
    public String getClubId() {
		return clubId;
	}

	public void setClubId(String clubId) {
		this.clubId = clubId;
	}
	

	public String getUserNum() {
		return userNum;
	}

	public void setUserNum(String userNum) {
		this.userNum = userNum;
	}

	/**
     * 活动结束时间
     */
    @NotNull
    @TableField("activity_end_date")
    private LocalDateTime activityEndDate;

    /**
     * 状态(0正常，1申请中,2被驳回)
     */
    @TableField("status")
    private String status;

    /**
     * 活动所需经费
     */
    @NotNull
    @TableField("funds")
    private BigDecimal funds;

    /**
     * 活动内容
     */
    @TableField("tips")
    private String tips;

    /**
     * 驳回理由
     */
    @TableField("reason")
    private String reason;

    /**
     * 活动类型ID
     */
    @TableField("type_id")
    private String typeId;

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
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
    public LocalDateTime getActivityStartDate() {
        return activityStartDate;
    }

    public void setActivityStartDate(LocalDateTime activityStartDate) {
        this.activityStartDate = activityStartDate;
    }
    public LocalDateTime getActivityEndDate() {
        return activityEndDate;
    }

    public void setActivityEndDate(LocalDateTime activityEndDate) {
        this.activityEndDate = activityEndDate;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public BigDecimal getFunds() {
        return funds;
    }

    public void setFunds(BigDecimal funds) {
        this.funds = funds;
    }
    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    @Override
    public String toString() {
        return "TClubActivity{" +
            "id=" + id +
            ", name=" + name +
            ", createDate=" + createDate +
            ", activityStartDate=" + activityStartDate +
            ", activityEndDate=" + activityEndDate +
            ", status=" + status +
            ", funds=" + funds +
            ", tips=" + tips +
            ", reason=" + reason +
            ", typeId=" + typeId +
            ", createId=" + createId +
        "}";
    }
}
