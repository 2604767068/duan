package com.example.demo.club.club.service.impl;

import com.example.demo.club.club.entity.TClubUser;
import com.example.demo.club.club.mapper.TClubUserMapper;
import com.example.demo.club.club.service.ITClubUserService;
import com.example.demo.config.common.BaseContant;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 社团成员表 服务实现类
 * </p>
 *
 * @author youkehai
 * @since 2020-02-03
 */
@Service
public class TClubUserServiceImpl extends ServiceImpl<TClubUserMapper, TClubUser> implements ITClubUserService {

	/***
	 * 修改社团管理员
	 * @param clubId
	 * @param userId
	 * @return
	 */
	@Transactional
	public boolean updateAdmin(String clubId, String userId) {
		//先查出本来的管理员，将userType改为0
		QueryWrapper<TClubUser> query=new QueryWrapper<TClubUser>();
		query.eq("club_id", clubId).eq("user_type", BaseContant.CLUB_MANAGE);
		TClubUser manage=getOne(query);
		manage.setUserType(BaseContant.CLUB_USER);
		updateById(manage);
		//再查询出新管理员将userType改为1
		QueryWrapper<TClubUser> query1=new QueryWrapper<TClubUser>();
		query1.eq("club_id", clubId).eq("user_id", userId);
		TClubUser newManage=getOne(query1);
		newManage.setUserType(BaseContant.CLUB_MANAGE);
		return updateById(newManage);
	}

}
