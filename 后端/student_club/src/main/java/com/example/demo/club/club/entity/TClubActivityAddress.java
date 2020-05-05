package com.example.demo.club.club.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 社团活动-场地器材表
 * </p>
 *
 * @author youkehai
 * @since 2020-02-03
 */
@TableName("t_club_activity_address")
public class TClubActivityAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;

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

    /**
     * 活动Id
     */
    @TableField("activity_id")
    private String activityId;

    /**
     * 所需器材
     */
    @TableField("material")
    private String material;

    /**
     * 场地
     */
    @TableField("address")
    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }
    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "TClubActivityAddress{" +
            "id=" + id +
            ", activityId=" + activityId +
            ", material=" + material +
            ", address=" + address +
        "}";
    }
}
