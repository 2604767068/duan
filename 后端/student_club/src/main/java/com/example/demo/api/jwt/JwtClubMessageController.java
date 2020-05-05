package com.example.demo.api.jwt;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.club.admin.entity.TMessage;
import com.example.demo.club.admin.mapper.TMessageMapper;
import com.example.demo.club.club.entity.TClubMessage;
import com.example.demo.club.club.entity.TClubNews;
import com.example.demo.club.club.mapper.TClubMessageMapper;
import com.example.demo.club.user.entity.TUser;
import com.example.demo.club.user.mapper.TUserMapper;
import com.example.demo.config.common.BaseContant;
import com.example.demo.config.common.BaseController;
import com.example.demo.config.common.WebResult;
import com.example.demo.config.utils.JWTUtils;

import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(BaseController.JWT_URL+"/clubMessage")
@Api(description = "社团留言管理 ",tags="社团留言  ")
public class JwtClubMessageController extends BaseController{

	@Autowired
	private TMessageMapper tMessageMapper;
	@Autowired
	private TClubMessageMapper tClubMessageMapper;
	@Autowired
	private TUserMapper tUserMapper;
	
	@ApiOperation(value = "发布网站留言", notes = "发布网站留言")
	@PostMapping("/message")
	public WebResult webMessage(@RequestHeader(Authorization)String token,@RequestBody TMessage message) throws UnsupportedEncodingException {
		//解析令牌，拿到UserId
		Claims userMap=JWTUtils.verifyToken(token);
		 String userId=userMap.get("uid",String.class);
		 TUser user=tUserMapper.selectById(userId);
		 message.setCreateDate(LocalDateTime.now());
		 message.setCreateId(userId);
		 message.setCreateName(user.getName());
		 //新增，basemapper统一方法
		 int i=tMessageMapper.insert(message);
			if(i>0) {
				return insertSuccess(message);
			}
			return insertDataRepeat();
	}
	
	@ApiOperation(value = "发布社团留言", notes = "发布网站留言")
	@PostMapping("/clubMessage")
	public WebResult clubMessage(@RequestHeader(Authorization)String token,@RequestHeader(BaseContant.HEAD_PARAMS_CLUB_ID)String clubId,
			@RequestBody TClubMessage message) throws UnsupportedEncodingException {
		//解析令牌，拿到UserId
		Claims userMap=JWTUtils.verifyToken(token);
		 String userId=userMap.get("uid",String.class);
		 TUser user=tUserMapper.selectById(userId);
		message.setClubId(clubId);
		message.setCreateDate(LocalDateTime.now());
		message.setCreateId(userId);
		message.setCreateName(user.getName());
		int i=tClubMessageMapper.insert(message);
		if(i>0) {
			return insertSuccess(message);
		}
		return insertDataRepeat();
	}
	
	@ApiOperation(value = "查询所有社团留言", notes = "查询所有社团留言")
	@GetMapping("/clubMessage")
	public WebResult clubMessage(@RequestHeader(Authorization)String token,@RequestHeader(BaseContant.HEAD_PARAMS_CLUB_ID)String clubId,
			Page<TClubMessage> page) throws UnsupportedEncodingException {
		QueryWrapper<TClubMessage> query=new QueryWrapper<TClubMessage>();
		query.eq(CLUB_ID, clubId);
		Page<TClubMessage> pageList=tClubMessageMapper.selectPage(page, query);
		if(pageList.getRecords()!=null && !pageList.getRecords().isEmpty()) {
			return selectSuccess(pageList);
		}
		return selectNotFound();
	}
}
