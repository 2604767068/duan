package com.example.demo.club.club.service.impl;

import com.example.demo.club.club.entity.TClub;
import com.example.demo.club.club.entity.TClubUser;
import com.example.demo.club.club.mapper.TClubMapper;
import com.example.demo.club.club.mapper.TClubUserMapper;
import com.example.demo.club.club.service.ITClubService;
import com.example.demo.club.user.entity.TUser;
import com.example.demo.config.common.BaseContant;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 社团表 服务实现类
 * </p>
 *
 * @author youkehai
 * @since 2020-02-03
 */
@Service
public class TClubServiceImpl extends ServiceImpl<TClubMapper, TClub> implements ITClubService {

	@Autowired
	private TClubUserMapper tClubUserMapper;
	
	/***
	 * 增加社团信息和社团管理员信息
	 * @param user
	 * @param club
	 * @return
	 */
	@Transactional
	public boolean insertClubAndManager(TUser user, TClub club) {
		club.setCreateDate(LocalDateTime.now());
		club.setCreateId(user.getId());
		club.setStatus(BaseContant.CLUB_STATUS_APPLYING);
		boolean a= save(club);
		TClubUser clubUser=new TClubUser();
		clubUser.setAliasUserName(user.getName());
		clubUser.setClubId(club.getId());
		clubUser.setCreateDate(LocalDateTime.now());
		clubUser.setCreateId(user.getId());
		clubUser.setJoinDate(LocalDateTime.now());
		clubUser.setStatus(BaseContant.CLUB_USER_STATUS_NORMAL);
		clubUser.setUserId(user.getId());
		clubUser.setUserType(BaseContant.CLUB_MANAGE);
		tClubUserMapper.insert(clubUser);//新增1社团管理员
		return a;
	}

}
