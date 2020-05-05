package com.example.demo.api.jwt;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.club.club.entity.TClub;
import com.example.demo.club.club.entity.TClubUser;
import com.example.demo.club.club.mapper.TClubUserMapper;
import com.example.demo.config.common.BaseContant;
import com.example.demo.config.common.BaseController;
import com.example.demo.config.common.WebResult;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(BaseController.JWT_URL+"/clubUser")
@Api(description = "社团成员管理 ",tags="社团成员管理  ")
public class JwtClubUserController extends BaseController{

	@Autowired
	private TClubUserMapper tClubUserMapper;
	
	@ApiOperation(value = "查询社团成员", notes = "")
	@GetMapping("/clubUser")
	public WebResult getClubUsers(@RequestHeader(BaseContant.HEAD_PARAMS_CLUB_ID)String clubId,Page<TClubUser> page,
			String status,String name) {
		QueryWrapper<TClubUser> query=new QueryWrapper<TClubUser>();
		if(StrUtil.isNotBlank(status)) {//根据状态和名称进行搜索
			query.eq("cu.status", status);
		}
		if(StrUtil.isNotBlank(name)) {
			query.like("cu.alias_user_name", name);
		}
		query.eq(CLUB_ID, clubId);
		//自定义Sql，可查询出用户在user表中的名称和性别等
		Page<TClubUser> club=tClubUserMapper.selectClubUser(page,query);
		if(club.getRecords()!=null&& !club.getRecords().isEmpty()) {
			return selectSuccess(club);
		}
		return selectNotFound();
	}
	
	@ApiOperation(value = "修改社团成员别称", notes = "")
	@PutMapping("/clubUser")
	public WebResult EditClub(@RequestBody TClubUser club) {
		 int i=tClubUserMapper.updateById(club);
		 if(i>0) {
			 return updateSuccess(club);
		 }
		 return updateNotFound();
	}
	
	@ApiOperation(value = "同意/拒绝加入社团", notes = "0未同意 2为拒绝")
	@PostMapping("/clubUser")
	public WebResult agreeJoin(@RequestHeader(BaseContant.HEAD_PARAMS_CLUB_ID)String clubId,String id,String status) {
		TClubUser user=tClubUserMapper.selectById(id);
		if(user!=null) {
			TClubUser updateEntity =new TClubUser();
			updateEntity.setId(id);
			updateEntity.setStatus(status);
			updateEntity.setJoinDate(LocalDateTime.now());
			int i=tClubUserMapper.updateById(updateEntity);
			if(i>0) {
				return updateSuccess(i);
			}
		}
		return updateNotFound();
	}
}
