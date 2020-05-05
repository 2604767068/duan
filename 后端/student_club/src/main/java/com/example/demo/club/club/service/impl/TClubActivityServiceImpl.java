package com.example.demo.club.club.service.impl;

import com.example.demo.club.club.entity.TClubActivity;
import com.example.demo.club.club.entity.TClubActivityAddress;
import com.example.demo.club.club.mapper.TClubActivityAddressMapper;
import com.example.demo.club.club.mapper.TClubActivityMapper;
import com.example.demo.club.club.mapper.TClubActivityUserMapper;
import com.example.demo.club.club.service.ITClubActivityService;
import com.example.demo.club.user.entity.TUser;
import com.example.demo.config.common.BaseContant;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 社团活动表 服务实现类
 * </p>
 *
 * @author youkehai
 * @since 2020-02-03
 */
@Service
public class TClubActivityServiceImpl extends ServiceImpl<TClubActivityMapper, TClubActivity> implements ITClubActivityService {

	@Autowired
	private TClubActivityAddressMapper tClubActivityAddressMapper;
	@Autowired
	private TClubActivityUserMapper tClubActivityUserMapper;
	/**
	 * 添加社团活动表内容和社团器材场地等内容
	 * @param user
	 * @param clubId 
	 * @param tClubActivity
	 * @return
	 */
	@Transactional
	public boolean insertActivity(TUser user, String clubId, TClubActivity tClubActivity) {
		tClubActivity.setCreateDate(LocalDateTime.now());
		tClubActivity.setCreateId(user.getId());
		tClubActivity.setClubId(clubId);
		tClubActivity.setStatus(BaseContant.CLUB_STATUS_APPLYING);
		tClubActivity.setUserNum(BaseContant.INIT_NUM);
		boolean i=save(tClubActivity);
		TClubActivityAddress address=tClubActivity.gettClubActivityAddress();
		if(address!=null) {
			address.setClubId(clubId);
			address.setActivityId(tClubActivity.getId());
			tClubActivityAddressMapper.insert(address);
		}
		return i;
	}
	/***
	 * 删除活动表和活动场地表和活动报名表
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public boolean deleteActivity(String id) {
		@SuppressWarnings("rawtypes")
		QueryWrapper query=new QueryWrapper<>();
		query.eq("activity_id", id);
		tClubActivityAddressMapper.delete(query);
		tClubActivityUserMapper.delete(query);
		return removeById(id);
	}

}
