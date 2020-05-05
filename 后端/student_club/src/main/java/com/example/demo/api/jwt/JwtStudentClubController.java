package com.example.demo.api.jwt;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.club.admin.entity.TSetting;
import com.example.demo.club.admin.mapper.TSettingMapper;
import com.example.demo.club.club.entity.TClub;
import com.example.demo.club.club.entity.TClubUser;
import com.example.demo.club.club.mapper.TClubMapper;
import com.example.demo.club.club.mapper.TClubUserMapper;
import com.example.demo.club.club.service.impl.TClubServiceImpl;
import com.example.demo.club.user.entity.TUser;
import com.example.demo.club.user.mapper.TUserMapper;
import com.example.demo.config.common.BaseContant;
import com.example.demo.config.common.BaseController;
import com.example.demo.config.common.WebResult;
import com.example.demo.config.enums.BaseErrorEnum;
import com.example.demo.config.utils.JWTUtils;
import com.google.common.collect.Lists;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(BaseController.JWT_URL+"/studentClub")
@Api(description = "学生操作社团接口 ",tags="学生操作社团接口  ")
public class JwtStudentClubController extends BaseController{

	@Autowired
	private TClubMapper tClubMapper;
	@Autowired
	private TUserMapper tUserMapper;
	@Autowired
	private TClubServiceImpl tClubServiceImpl;
	@Autowired
	private TSettingMapper tSettingMapper;
	@Autowired
	private TClubUserMapper tClubUserMapper;
	
	@ApiOperation(value = "查询所有社团列表", notes = "")
	@GetMapping("/club")
	public WebResult registClub(Page<TClub> page,String status,String name) {
		QueryWrapper<TClub> query=new QueryWrapper<TClub>();
		if(StrUtil.isNotBlank(status)) {//根据状态和名称进行搜索
			query.eq("c.status", status);
		}
		if(StrUtil.isNotBlank(name)) {
			query.like("c.name", name);
		}
		Page<TClub> club=tClubMapper.selectPageAndClubManage(page, query);
		if(club.getRecords()!=null && !club.getRecords().isEmpty()) {
			return selectSuccess(club);
		}
		return selectNotFound();
	}
	
	@ApiOperation(value = "修改社团信息", notes = "")
	@PutMapping("/club")
	public WebResult EditClub(@RequestBody TClub club) {
		 int i=tClubMapper.updateById(club);
		 if(i>0) {
			 return updateSuccess(club);
		 }
		 return updateNotFound();
	}
	
	
	
	@ApiOperation(value = "退出社团", notes = "")
	@DeleteMapping("/club/{id}")
	public WebResult exitClub(@PathVariable String id) {
	//删除掉用户社团数据
		int i=tClubUserMapper.deleteById(id);
		if(i>0) {
			return deleteSuccess(i);
		}
		return deleteNotFound();
	}
	
	@ApiOperation(value = "根据社团ID查询社团信息", notes = "")
	@GetMapping("/club/{id}")
	public WebResult EditClub(@PathVariable String id) {
		//basemapper统一方法 根据ID查询
		 TClub i=tClubMapper.selectById(id);
		 if(i!=null) {
			 return selectSuccess(i);
		 }
		 return selectNotFound();
	}
	
	@ApiOperation(value = "重新申请加入社团", notes = "")
	@PutMapping("/clubUser")
	public WebResult clubUser(String id) {
		TClubUser update =new TClubUser();
		update.setId(id);
		//重新修改状态为申请中
		update.setStatus(BaseContant.CLUB_STATUS_APPLYING);
		 int i=tClubUserMapper.updateById(update);
		 if(i>0) {
			 return selectSuccess(i);
		 }
		 return selectNotFound();
	}
	
	@ApiOperation(value = "重新申请创建社团", notes = "")
	@PutMapping("/clubApply")
	public WebResult clubApply(String clubId) {
		TClub update =new TClub();
		update.setId(clubId);
		//重新修改状态位申请中
		update.setStatus(BaseContant.CLUB_STATUS_APPLYING);
		 int i=tClubMapper.updateById(update);
		 if(i>0) {
			 return selectSuccess(i);
		 }
		 return selectNotFound();
	}
	
	@ApiOperation(value = "申请加入社团", notes = "")
	@PostMapping("/join")
	public WebResult joinClub(@RequestHeader(Authorization)String token,@RequestBody TClubUser clubUser) throws UnsupportedEncodingException {
		Claims userMap=JWTUtils.verifyToken(token);
		 String userId=userMap.get("uid",String.class);
		 QueryWrapper<TClubUser> query=new QueryWrapper<TClubUser>();
		 query.eq("club_id",clubUser.getClubId()).eq("user_id",userId);
		 TClubUser exist=tClubUserMapper.selectOne(query);
		 if(exist!=null) {
			 //如果是被拒绝的状态，进入此if
			 if(BaseContant.CLUB_STATUS_REFUSE.equals(exist.getStatus())) {
				 //如果是被拒绝的状态，重新改为申请中
				 TClubUser updateEntity=new TClubUser();
				 updateEntity.setId(exist.getId());
				 updateEntity.setTips(clubUser.getTips());
				 updateEntity.setStatus(BaseContant.CLUB_STATUS_APPLYING);
				 int i=tClubUserMapper.updateById(updateEntity);
				 if(i>0) {
					 return insertSuccess(clubUser);
				 }
				 return insertDataRepeat();
			 }
			 return insertDataRepeat(exist);
		 }else {
			 //如果找不到，则新增
			 TUser user=tUserMapper.selectById(userId);
			 clubUser.setCreateDate(LocalDateTime.now());
			 clubUser.setUserType(BaseContant.CLUB_USER);
			 clubUser.setStatus(BaseContant.CLUB_STATUS_APPLYING);
			 clubUser.setAliasUserName(user.getName());
			 clubUser.setCreateId(userId);
			 clubUser.setUserId(userId);
			 int i=tClubUserMapper.insert(clubUser);
			 if(i>0) {
				 return insertSuccess(clubUser);
			 }
			 return insertDataRepeat();
		 }
	}
	
	@ApiOperation(value = "注册社团", notes = "")
	@PostMapping("/club")
	public WebResult registClub(@RequestHeader(Authorization)String token,@RequestBody @Validated TClub club) throws UnsupportedEncodingException {
		 Claims userMap=JWTUtils.verifyToken(token);
		 String userId=userMap.get("uid",String.class);
		 //查询是否有重复名称的社团
		 QueryWrapper<TClub> query=new QueryWrapper<TClub>();
		 query.eq("name",club.getName()).eq("status", BaseContant.CLUB_STATUS_NORMAL);
		 TClub repeat=tClubMapper.selectOne(query);
		 if(repeat!=null) {
			 return insertDataRepeat();
		 }
		 //查询我创建的和申请的社团有没有超过最大限定数
		 QueryWrapper<TClub> query1=new QueryWrapper<TClub>();
		 List<String> status=Lists.newArrayList();
		 status.add(BaseContant.CLUB_STATUS_NORMAL);
		 status.add(BaseContant.CLUB_STATUS_APPLYING);
		 query1.eq("create_id", userId).in("status", status);
		 List<TClub> createList=tClubMapper.selectList(query1);
		 //查询最大限制数
		 QueryWrapper<TSetting> query2=new QueryWrapper<TSetting>();
		 query2.eq("name", BaseContant.MAX_CLUB_NUM);
		 TSetting setting=tSettingMapper.selectOne(query2);
		 if(createList.size()>=setting.getNum()) {//如果我创建的社团已经超过最大数量，抛出异常
			 return error(BaseErrorEnum.ERR_MAX_CLUB_NUM);
		 }
		 TUser user=tUserMapper.selectById(userId);
		 boolean i=tClubServiceImpl.insertClubAndManager(user,club);
		 if(i) {
			 return insertSuccess(club);
		 }
		 return insertDataRepeat();
	}
}
