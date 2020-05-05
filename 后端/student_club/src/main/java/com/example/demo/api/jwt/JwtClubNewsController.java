package com.example.demo.api.jwt;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.club.club.entity.TClubNews;
import com.example.demo.club.club.mapper.TClubNewsMapper;
import com.example.demo.club.user.mapper.TUserMapper;
import com.example.demo.config.common.BaseContant;
import com.example.demo.config.common.BaseController;
import com.example.demo.config.common.WebResult;
import com.example.demo.config.utils.JWTUtils;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
@RestController
@RequestMapping(BaseController.JWT_URL+"/clubNews")
@Api(description = "社团留言管理 ",tags="社团留言  ")
public class JwtClubNewsController extends BaseController{
	@Autowired
	private TUserMapper tUserMapper;
	@Autowired
	private TClubNewsMapper tClubNewsMapper;
	
	@ApiOperation(value = "发布社团新闻", notes = "发布社团新闻")
	@PostMapping("/clubNews")
	public WebResult clubNews(@RequestHeader(Authorization)String token,@RequestHeader(BaseContant.HEAD_PARAMS_CLUB_ID)String clubId,@RequestBody TClubNews tClubNews) throws UnsupportedEncodingException {
		Claims userMap=JWTUtils.verifyToken(token);
		 String userId=userMap.get("uid",String.class);
		 tClubNews.setCreateDate(LocalDateTime.now());
		 tClubNews.setCreateId(userId);
		 tClubNews.setStatus(BaseContant.STATUS_STOP);//默认停用
		 tClubNews.setClubId(clubId);
		 int i=tClubNewsMapper.insert(tClubNews);
			if(i>0) {
				return insertSuccess(tClubNews);
			}
			return insertDataRepeat();
	}
	
	@ApiOperation(value = "停用启用社团新闻", notes = "发布社团新闻")
	@PutMapping("/clubNews")
	public WebResult clubNews(@RequestHeader(Authorization)String token,@RequestHeader(BaseContant.HEAD_PARAMS_CLUB_ID)String clubId,String id,String status) throws UnsupportedEncodingException {
		 TClubNews club=new TClubNews();
		 club.setId(id);
		 club.setStatus(status);
		 int i=tClubNewsMapper.updateById(club);
			if(i>0) {
				return insertSuccess(club);
			}
			return insertDataRepeat();
	}
	
	@ApiOperation(value = "删除社团新闻", notes = "删除社团新闻")
	@DeleteMapping("/clubNews")
	public WebResult deleteclubNews(@RequestHeader(Authorization)String token,@RequestHeader(BaseContant.HEAD_PARAMS_CLUB_ID)String clubId,String id) throws UnsupportedEncodingException {
		//根据ID删除，basemapper统一方法
		int i=tClubNewsMapper.deleteById(id);
			if(i>0) {
				return insertSuccess(i);
			}
			return insertDataRepeat();
	}
	
	@ApiOperation(value = "查询所有社团新闻", notes = "查询所有社团新闻")
	@GetMapping("/clubNews")
	public WebResult getClubNews(@RequestHeader(Authorization)String token,@RequestHeader(BaseContant.HEAD_PARAMS_CLUB_ID)String clubId,Page<TClubNews> page,String status) throws UnsupportedEncodingException {
		 QueryWrapper<TClubNews> query=new QueryWrapper<TClubNews>();
		 query.eq(CLUB_ID, clubId);
		 if(StrUtil.isNotBlank(status)) {
			 query.eq("status", clubId);
		 }
		 Page<TClubNews> pageList=tClubNewsMapper.selectPage(page, query);
			if(pageList.getRecords()!=null && !pageList.getRecords().isEmpty()) {
				return selectSuccess(pageList);
			}
			return selectNotFound();
	}
}
