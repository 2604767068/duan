package com.example.demo.api.jwt;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.club.admin.entity.TAdmin;
import com.example.demo.club.admin.mapper.TAdminMapper;
import com.example.demo.club.club.entity.TClubUser;
import com.example.demo.club.club.mapper.TClubUserMapper;
import com.example.demo.club.user.entity.TUser;
import com.example.demo.club.user.mapper.TUserMapper;
import com.example.demo.config.common.BaseController;
import com.example.demo.config.common.WebResult;
import com.example.demo.config.utils.JWTUtils;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(BaseController.JWT_URL+"/user")
@Api(description = "个人信息接口 ",tags="个人信息接口  ")
public class JwtUserInfoController extends BaseController{

	@Autowired
	private TUserMapper tUserMapper;
	@Autowired
	private TAdminMapper tAdminMapper;
	@Autowired
	private TClubUserMapper tClubUserMapper;
	
	@ApiOperation(value = "获取个人信息", notes = "")
	@GetMapping("/userInfo")
	public WebResult getUserInfo(@RequestHeader(Authorization)String token) throws UnsupportedEncodingException {
		//解析令牌拿到UseId，进行查询个人信息
		Claims userMap=JWTUtils.verifyToken(token);
		 String userId=userMap.get("uid",String.class);
		 TUser user=tUserMapper.selectById(userId);
		 if(user!=null) {
			 return selectSuccess(user);
		 }
		 return selectNotFound();
	}
	
	@ApiOperation(value = "获取个人所在的所有的社团列表", notes = "")
	@GetMapping("/clubs")
	public WebResult getMyClubs(@RequestHeader(Authorization)String token,Page<TClubUser> page,String status) throws UnsupportedEncodingException {
		//条件构造器
		QueryWrapper<TClubUser> query=new QueryWrapper<TClubUser>();
		 Claims userMap=JWTUtils.verifyToken(token);
		 String userId=userMap.get("uid",String.class);
		 //构造cu.use_id=userId
		query.eq("cu.user_id", userId);
		if(StrUtil.isNotBlank(status)) {
			query.eq("cu.status", status);
		}
		//自定义sql，可查询出社团名称说明等信息
		Page<TClubUser> clubList=tClubUserMapper.selectmyClubInfo(page,query);
		if(clubList.getRecords()!=null && !clubList.getRecords().isEmpty()) {
			return selectSuccess(clubList);
		}
		return selectNotFound();
	}
	
	@ApiOperation(value = "获取管理员个人信息", notes = "")
	@GetMapping("/adminInfo")
	public WebResult getAdminInfo(@RequestHeader(Authorization)String token) throws UnsupportedEncodingException {
		//解析令牌拿到UseId，进行查询个人信息
		Claims userMap=JWTUtils.verifyToken(token);
		 String userId=userMap.get("uid",String.class);
		 TAdmin admin=tAdminMapper.selectById(userId);
		 if(admin!=null) {
			 return selectSuccess(admin);
		 }
		 return selectNotFound();
	}
}
