package com.example.demo.club.club.service.impl;

import com.example.demo.club.club.entity.TClubActivity;
import com.example.demo.club.club.entity.TClubActivityUser;
import com.example.demo.club.club.mapper.TClubActivityMapper;
import com.example.demo.club.club.mapper.TClubActivityUserMapper;
import com.example.demo.club.club.service.ITClubActivityUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 社团活动-报名表 服务实现类
 * </p>
 *
 * @author youkehai
 * @since 2020-02-20
 */
@Service
public class TClubActivityUserServiceImpl extends ServiceImpl<TClubActivityUserMapper, TClubActivityUser> implements ITClubActivityUserService {

	@Autowired
	private TClubActivityMapper tClubActivityMapper;
	/***
	 * 新增报名表数据和修改活动表人数
	 * @param userId
	 * @param activityId
	 * @param status
	 * @return
	 */
	@Transactional
	public boolean insertNewInfo(String userId, String activityId, String status) {
		boolean i=false; 
		TClubActivity activity=tClubActivityMapper.selectById(activityId);
		if("1".equals(status)) {//如果是1就删除
				QueryWrapper<TClubActivityUser> query=new QueryWrapper<TClubActivityUser>();
				query.eq("user_id", userId).eq("activity_id", activityId);
				i=remove(query);
				//再修改报名人数
				if(activity!=null) {
					TClubActivity updateEntity=new TClubActivity();
					updateEntity.setId(activityId);
					Integer num=Integer.parseInt(activity.getUserNum())-1;
					updateEntity.setUserNum(num.toString());
					tClubActivityMapper.updateById(updateEntity);
				}
		}else {//增加报名记录
				 TClubActivityUser user=new TClubActivityUser();
				 user.setUserId(userId);
				 user.setActivityId(activityId);
				 user.setCreateDate(LocalDateTime.now());
				 i=save(user);
				//再修改报名人数
				if(activity!=null) {
					TClubActivity updateEntity=new TClubActivity();
					updateEntity.setId(activityId);
					Integer num=Integer.parseInt(activity.getUserNum())+1;
					updateEntity.setUserNum(num.toString());
					tClubActivityMapper.updateById(updateEntity);
				}
			 }
		return i;
	}

}
