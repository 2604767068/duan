package com.example.demo.api.jwt;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.club.admin.entity.TActivityTpe;
import com.example.demo.club.admin.entity.TIndexNews;
import com.example.demo.club.admin.entity.TMessage;
import com.example.demo.club.admin.entity.TSetting;
import com.example.demo.club.admin.mapper.TActivityTpeMapper;
import com.example.demo.club.admin.mapper.TIndexNewsMapper;
import com.example.demo.club.admin.mapper.TMessageMapper;
import com.example.demo.club.admin.mapper.TSettingMapper;
import com.example.demo.club.club.entity.TClub;
import com.example.demo.club.club.entity.TClubActivity;
import com.example.demo.club.club.entity.TClubActivityAddress;
import com.example.demo.club.club.entity.TClubActivityUser;
import com.example.demo.club.club.entity.TClubUser;
import com.example.demo.club.club.mapper.TClubActivityAddressMapper;
import com.example.demo.club.club.mapper.TClubActivityMapper;
import com.example.demo.club.club.mapper.TClubMapper;
import com.example.demo.club.club.mapper.TClubUserMapper;
import com.example.demo.club.club.service.impl.TClubUserServiceImpl;
import com.example.demo.club.user.entity.TUser;
import com.example.demo.club.user.mapper.TUserMapper;
import com.example.demo.config.common.BaseContant;
import com.example.demo.config.common.BaseController;
import com.example.demo.config.common.WebResult;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(BaseController.JWT_URL+"/clubManager")
@Api(description = "管理员管理社团接口 ",tags="管理员管理社团接口  ")
public class JwtClubManagerController extends BaseController{

	@Autowired
	private TClubMapper tClubMapper;
	@Autowired
	private TActivityTpeMapper tActivityTpeMapper;
	@Autowired
	private TClubActivityMapper tClubActivityMapper;
	@Autowired
	private TMessageMapper tMessageMapper;
	@Autowired
	private TUserMapper tUserMapper;
	@Autowired
	private TIndexNewsMapper tIndexNewsMapper;
	@Autowired
	private TClubUserMapper tClubUserMapper;
	@Autowired
	private TClubUserServiceImpl tClubUserServiceImpl;
	@Autowired
	private TClubActivityAddressMapper tClubActivityAddressMapper;
	@Autowired
	private TSettingMapper tSettingMapper;
	
	@ApiOperation(value = "同意/拒绝成立社团", notes = "0未同意 2为拒绝")
	@PostMapping("/club")
	public WebResult agreeCreate(String id,String status,String reason){
		TClub club=tClubMapper.selectById(id);
		if(club!=null) {
			TClub updateEntity=new TClub();
			updateEntity.setId(id);
			updateEntity.setStatus(status);
			updateEntity.setReason(reason);
			//通过ID修改
			int i=tClubMapper.updateById(updateEntity);
			if(i>0) {
				return updateSuccess(i);
			}
		}
		return updateNotFound();
	}
	
	@ApiOperation(value = "查询配置", notes = "")
	@GetMapping("/setting")
	public WebResult getSetting(){
		//查询每个人最多建立社团个数的配置
		TSetting club=tSettingMapper.selectOne(null);
		return selectSuccess(club);
	}
	
	@ApiOperation(value = "修改配置", notes = "")
	@PutMapping("/setting")
	public WebResult updateSetting(String id,long num){
		TSetting update =new TSetting();
		update.setId(id);
		update.setNum(num);
		int i=tSettingMapper.updateById(update);
		return updateSuccess(update);
	}
	
	@ApiOperation(value = "将某人设为管理员", notes = "userId,clubId")
	@PostMapping("/clubAdmin")
	public WebResult changeAdmin(String clubId,String userId){
		//管理员更换社团负责人
		boolean update=tClubUserServiceImpl.updateAdmin(clubId,userId);
		if(update) {
			return updateSuccess(userId);
		}
		return updateNotFound();
	}
	
	@ApiOperation(value = "根据社团ID查询所有会员", notes = "")
	@GetMapping("/clubUsers")
	public WebResult allUserByClub(Page<TClubUser> page,String clubId){
		QueryWrapper<TClubUser> query=new QueryWrapper<TClubUser>();
		query.eq(CLUB_ID, clubId);
		//根据社团ID查询所有社团成员
		Page<TClubUser> userList=tClubUserMapper.selectPage(page, query);
		if(userList.getRecords()!=null && !userList.getRecords().isEmpty()) {
			List<String> userIdList=Lists.newArrayList();
			Map<String,String> userTypeMap=Maps.newHashMap();
			for (TClubUser tClubUser : userList.getRecords()) {
				//拿到所有用户ID
				userIdList.add(tClubUser.getUserId());
				//组装一个map，用来存放用户在社团中的角色，是否是社团管理员
				userTypeMap.put(tClubUser.getUserId(),tClubUser.getUserType());
			}
			List<TUser> userListData=tUserMapper.selectBatchIds(userIdList);
			for (TUser tUser : userListData) {
				//循环用户list，将用户在社团中的角色放进去
				tUser.setUserType(userTypeMap.get(tUser.getId()));
			}
			//重新new一个page对象存放数据
			Page<TUser> pageList=new Page<TUser>();
			pageList.setCurrent(userList.getCurrent());
			pageList.setRecords(userListData);
			pageList.setPages(userList.getPages());
			pageList.setTotal(userList.getTotal());
			pageList.setSize(userList.getSize());
			if(pageList.getRecords()!=null && !pageList.getRecords().isEmpty()) {
				return selectSuccess(pageList);
			}
		}
		return selectNotFound();
	}
	
	@ApiOperation(value = "查询所有会员", notes = "")
	@GetMapping("/users")
	public WebResult allUser(Page<TUser> page,String name){
		QueryWrapper<TUser> query=new QueryWrapper<TUser>();
		//可根据名称搜索
		if(StrUtil.isNotBlank(name)) {
			query.like("name", name);
		}
		Page<TUser> pageList=tUserMapper.selectPage(page, query);
		if(pageList.getRecords()!=null && !pageList.getRecords().isEmpty()) {
			return selectSuccess(pageList);
		}
		return selectNotFound();
	}
	
	@ApiOperation(value = "查询某个人详细资料", notes = "")
	@GetMapping("/users/{id}")
	public WebResult allUser(@PathVariable String id){
		TUser user=tUserMapper.selectById(id);//查询个人信息
		if(user!=null) {
			//查询此人所在的所有社团
			QueryWrapper<TClubUser> query=new QueryWrapper<TClubUser>();
			query.eq("user_id", id);
			List<TClubUser> clubUserList=tClubUserMapper.selectList(query);
			if(clubUserList!=null && !clubUserList.isEmpty()) {
				List<String> clubIds=Lists.newArrayList();
				for (TClubUser tClubUser : clubUserList) {
					clubIds.add(tClubUser.getClubId());
				}
				//查到所有社团
				List<TClub> clubList=tClubMapper.selectBatchIds(clubIds);
				StringBuilder sb=new StringBuilder();
				for (TClub tClub : clubList) {
					//拼接社团名字
					sb.append(tClub.getName()).append(",");
				}
				user.setClubName(sb.toString());
			}
			return selectSuccess(user);
		}
		return selectNotFound();
	}
	
	@ApiOperation(value = "查询所有首页新闻", notes = "")
	@GetMapping("/indexNews")
	public WebResult indexNews(Page<TIndexNews> page){
		//查询所有首页新闻
		QueryWrapper<TIndexNews> query=new QueryWrapper<TIndexNews>();
		Page<TIndexNews> pageList=tIndexNewsMapper.selectPage(page, query);
		if(pageList.getRecords()!=null && !pageList.getRecords().isEmpty()) {
			return selectSuccess(pageList);
		}
		return selectNotFound();
	}
	
	@ApiOperation(value = "查询所有留言", notes = "")
	@GetMapping("/message")
	public WebResult messages(Page<TMessage> page){
		QueryWrapper<TMessage> query=new QueryWrapper<TMessage>();
		Page<TMessage> pageList=tMessageMapper.selectPage(page, query);
		if(pageList.getRecords()!=null && !pageList.getRecords().isEmpty()) {
			return selectSuccess(pageList);
		}
		return selectNotFound();
	}
	
	@ApiOperation(value = "查询经费日期报表", notes = "")
	@GetMapping("/fundsReport")
	public WebResult fundsReport(){
		//自定义sql语句查询报表
		List<TClubActivity> reportList=tClubActivityMapper.selectGroupByay();
		if(reportList!=null && !reportList.isEmpty()) {
			return selectSuccess(reportList);
		}
		return selectNotFound();
	}
	
	@ApiOperation(value = "查询所有社团活动", notes = "")
	@GetMapping("/activity")
	public WebResult allActivity(@RequestHeader(Authorization)String token,
			Page<TClubActivity> page,String status) throws UnsupportedEncodingException {
		//查询所有场地信息
		QueryWrapper<TClubActivity> query=new QueryWrapper<TClubActivity>();
		if(StrUtil.isNotBlank(status)) {
			query.eq("status",status);
		}
		List<TClubActivityAddress> addressList=tClubActivityAddressMapper.selectList(null);
		//通过stream分组成活动ID-活动场地地址信息
		Map<String, List<TClubActivityAddress>> addressMap=addressList.stream()
				.collect(Collectors.groupingBy(TClubActivityAddress :: getActivityId));
		Page<TClubActivity> activityList=tClubActivityMapper.selectPage(page, query);
		List<TActivityTpe> typeList=tActivityTpeMapper.selectList(null);
		Map<String, String> typeMap=typeList.stream()
				.collect(Collectors.toMap(TActivityTpe::getId, TActivityTpe::getName));
		if(activityList.getRecords()!=null && !activityList.getRecords().isEmpty()) {
			List<String> activityIdList=Lists.newArrayList();
			for(TClubActivity activity:activityList.getRecords()) {//将场地信息放到对应的里面
				activity.setTypeName(typeMap.get(activity.getTypeId()));
				activity.settClubActivityAddress(addressMap.get(activity.getId()).get(0));
				activityIdList.add(activity.getId());
			}
			return selectSuccess(activityList);
		}
		return selectNotFound();
	}
	
	@ApiOperation(value = "同意/拒绝社团活动", notes = "0未同意 2为拒绝")
	@PostMapping("/activity")
	public WebResult agreeCreateActivity(String id,String status,String reason){
		TClubActivity activity=tClubActivityMapper.selectById(id);
		if(activity!=null) {
			TClubActivity updateEntity=new TClubActivity();
			updateEntity.setId(id);
			updateEntity.setStatus(status);
			updateEntity.setReason(reason);
			//根据ID修改,basemapper中的统一方法
			int i=tClubActivityMapper.updateById(updateEntity);
			if(i>0) {
				return updateSuccess(i);
			}
		}
		return updateNotFound();
	}
}
