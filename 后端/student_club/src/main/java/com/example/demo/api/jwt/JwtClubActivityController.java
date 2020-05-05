package com.example.demo.api.jwt;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.club.admin.entity.TActivityTpe;
import com.example.demo.club.admin.mapper.TActivityTpeMapper;
import com.example.demo.club.club.entity.TClubActivity;
import com.example.demo.club.club.entity.TClubActivityAddress;
import com.example.demo.club.club.entity.TClubActivityUser;
import com.example.demo.club.club.mapper.TClubActivityAddressMapper;
import com.example.demo.club.club.mapper.TClubActivityMapper;
import com.example.demo.club.club.mapper.TClubActivityUserMapper;
import com.example.demo.club.club.service.impl.TClubActivityServiceImpl;
import com.example.demo.club.club.service.impl.TClubActivityUserServiceImpl;
import com.example.demo.club.user.entity.TUser;
import com.example.demo.club.user.mapper.TUserMapper;
import com.example.demo.config.common.BaseContant;
import com.example.demo.config.common.BaseController;
import com.example.demo.config.common.WebResult;
import com.example.demo.config.utils.JWTUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(BaseController.JWT_URL+"/clubActivity")
@Api(description = "社团活动接口 ",tags="社团活动的增删改查  ")
public class JwtClubActivityController extends BaseController{

	@Autowired
	private TClubActivityMapper tClubActivityMapper;
	@Autowired
	private TActivityTpeMapper tActivityTpeMapper;
	@Autowired
	private TUserMapper tUserMapper;
	@Autowired
	private TClubActivityServiceImpl tClubActivityServiceImpl;
	@Autowired
	private TClubActivityAddressMapper tClubActivityAddressMapper;
	@Autowired
	private TClubActivityUserServiceImpl tClubActivityUserServiceImpl;
	@Autowired
	private TClubActivityUserMapper tClubActivityUserMapper;
	
	@ApiOperation(value = "发起社团活动", notes = "")
	@PostMapping("/activity")
	public WebResult createActivity(@RequestHeader(Authorization)String token,@RequestHeader(BaseContant.HEAD_PARAMS_CLUB_ID)String clubId,
			@RequestBody @Validated TClubActivity tClubActivity) throws UnsupportedEncodingException {
		 Claims userMap=JWTUtils.verifyToken(token);
		 String userId=userMap.get("uid",String.class);
		 TUser user=tUserMapper.selectById(userId);
		 boolean i=tClubActivityServiceImpl.insertActivity(user,clubId,tClubActivity);
		 if(i) {
			 return insertSuccess(tClubActivity);
		 }
		 return insertDataRepeat();
	}
	
	@ApiOperation(value = "删除社团活动", notes = "")
	@DeleteMapping("/activity")
	public WebResult createActivity(@RequestHeader(Authorization)String token,@RequestHeader(BaseContant.HEAD_PARAMS_CLUB_ID)String clubId,
			String id) throws UnsupportedEncodingException {
		//传入活动ID到service层进行删除
		 boolean i=tClubActivityServiceImpl.deleteActivity(id);
		 if(i) {
			 return deleteSuccess(i);
		 }
		 return insertDataRepeat();
	}
	
	@ApiOperation(value = "报名/取消社团活动", notes = "status 0报名1取消")
	@PostMapping("/enroll")
	public WebResult enrollActivity(@RequestHeader(Authorization)String token,@RequestHeader(BaseContant.HEAD_PARAMS_CLUB_ID)String clubId,
			String activityId,String status) throws UnsupportedEncodingException {
		 Claims userMap=JWTUtils.verifyToken(token);
		 String userId=userMap.get("uid",String.class);
		 boolean i=tClubActivityUserServiceImpl.insertNewInfo(userId,activityId,status);
		if(i) {
			return insertSuccess(userId);
		}else {
			return insertDataRepeat();
		}
	}
	
	@ApiOperation(value = "查看活动报名人员", notes = "status 0报名1取消")
	@GetMapping("/enroll")
	public WebResult enrollActivity(@RequestHeader(Authorization)String token,@RequestHeader(BaseContant.HEAD_PARAMS_CLUB_ID)String clubId,
			String activityId) throws UnsupportedEncodingException {
		QueryWrapper<TClubActivityUser> query=new QueryWrapper<TClubActivityUser>();
		query.eq("activity_id",activityId);
		//通过活动ID查找到所有报名了此活动的用户ID
		List<TClubActivityUser> list=tClubActivityUserMapper.selectList(query);
		if(list!=null && !list.isEmpty()) {
			List<String> userIdList=Lists.newArrayList();//拿到所有用户ID
			Map<String, LocalDateTime> userTimeMap=Maps.newHashMap();
			for (TClubActivityUser tClubActivityUser : list) {
				userIdList.add(tClubActivityUser.getUserId());
				//将报名时间和用户ID组装成map
				userTimeMap.put(tClubActivityUser.getUserId(),tClubActivityUser.getCreateDate());
			}
			List<TUser> userList=tUserMapper.selectBatchIds(userIdList);
			for (TUser tUser : userList) {
				//将用户表中的创建时间替换成用户活动表中的创建时间，用于前端展示报名时间
				tUser.setCreateDate(userTimeMap.get(tUser.getId()));
				tUser.setPassword("");//将密码设置成空，不返回给前端
			}
			return selectSuccess(userList);
		}
		return selectNotFound();
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "查询社团活动", notes = "")
	@GetMapping("/activity")
	public WebResult allActivity(@RequestHeader(Authorization)String token,@RequestHeader(BaseContant.HEAD_PARAMS_CLUB_ID)String clubId,
			Page<TClubActivity> page,String userId) throws UnsupportedEncodingException {
		@SuppressWarnings("rawtypes")
		QueryWrapper query=new QueryWrapper();
		query.eq("club_id",clubId);
		//查询所有场地信息
		List<TClubActivityAddress> addressList=tClubActivityAddressMapper.selectList(query);
		Map<String, List<TClubActivityAddress>> addressMap=addressList.stream()
				.collect(Collectors.groupingBy(TClubActivityAddress :: getActivityId));//通过stream分组
		Page<TClubActivity> activityList=tClubActivityMapper.selectPage(page, query);
		//查询到所有活动类型的数据
		List<TActivityTpe> typeList=tActivityTpeMapper.selectList(null);
		//组装成ID-name的活动map 例如1-社交活动
		Map<String, String> typeMap=typeList.stream()
				.collect(Collectors.toMap(TActivityTpe::getId, TActivityTpe::getName));
		if(activityList.getRecords()!=null && !activityList.getRecords().isEmpty()) {
			List<String> activityIdList=Lists.newArrayList();
			for(TClubActivity activity:activityList.getRecords()) {//将场地信息放到对应的里面
				//根据上面的活动类型map的key去取到类型的name放入typename字段
				activity.setTypeName(typeMap.get(activity.getTypeId()));
				activity.settClubActivityAddress(addressMap.get(activity.getId()).get(0));
				activityIdList.add(activity.getId());
			}
			if(StrUtil.isNotBlank(userId)) {//根据传递的userID查询是否报过名
				//根据UserId和所有活动ID查询出对应的报名信息
				QueryWrapper<TClubActivityUser> query1=new QueryWrapper<TClubActivityUser>();
				query1.eq("user_id", userId).in("activity_id",activityIdList);
				List<TClubActivityUser> userActivityList=tClubActivityUserMapper.selectList(query1);
				if(userActivityList!=null && !userActivityList.isEmpty()) {
					Map<String, TClubActivityUser> activityUserMap=userActivityList.stream()
							.collect(Collectors.toMap(TClubActivityUser::getActivityId, TClubActivityUser->TClubActivityUser));
					for(TClubActivity activity:activityList.getRecords()) {
						//如果我报名了就讲inscroll字段设为1，用于前端展示
						if(activityUserMap.get(activity.getId())!=null) {
							activity.setInscroll("1");
						}
					}
				}
			}
			return selectSuccess(activityList);
		}
		return selectNotFound();
	}
	
	@ApiOperation(value = "查询社团活动类型", notes = "")
	@GetMapping("/activityType")
	public WebResult activityType() {
		List<TActivityTpe> typeList=tActivityTpeMapper.selectList(null);
		return selectSuccess(typeList);
	}
}
