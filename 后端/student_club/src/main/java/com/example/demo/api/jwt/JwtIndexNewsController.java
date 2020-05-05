package com.example.demo.api.jwt;

import java.time.LocalDateTime;
import java.util.List;

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
import com.example.demo.api.jwt.entity.IndexReport;
import com.example.demo.club.admin.entity.TIndexNews;
import com.example.demo.club.admin.mapper.TIndexNewsMapper;
import com.example.demo.club.club.entity.TClub;
import com.example.demo.club.club.entity.TClubActivity;
import com.example.demo.club.club.entity.TClubNews;
import com.example.demo.club.club.entity.TClubUser;
import com.example.demo.club.club.mapper.TClubActivityMapper;
import com.example.demo.club.club.mapper.TClubMapper;
import com.example.demo.club.club.mapper.TClubNewsMapper;
import com.example.demo.club.user.entity.TUser;
import com.example.demo.club.user.mapper.TUserMapper;
import com.example.demo.config.common.BaseContant;
import com.example.demo.config.common.BaseController;
import com.example.demo.config.common.WebResult;
import com.example.demo.config.utils.DateUtils;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(BaseController.JWT_URL+"/indexNews")
@Api(description = "首页新闻相关接口 ",tags="设置学生端首页内容  ")
public class JwtIndexNewsController extends BaseController{

	@Autowired
	private TIndexNewsMapper tIndexNewsMapper;
	@Autowired
	private TClubNewsMapper tClubNewsMapper;
	@Autowired
	private TUserMapper tUserMapper;
	@Autowired
	private TClubMapper tClubMapper;
	@Autowired
	private TClubActivityMapper tClubActivityMapper;
	
	@ApiOperation(value = "新增首页新闻", notes = "")
	@PostMapping("/indexNews")
	public WebResult getClubUsers(@RequestBody TIndexNews indexNews) {
		indexNews.setCreateDate(LocalDateTime.now());
		indexNews.setStatus(BaseContant.STATUS_STOP);//默认停用
		int i=tIndexNewsMapper.insert(indexNews);
		if(i>0) {
			return insertSuccess(indexNews);
		}
		return insertDataRepeat();
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "查询首页报表", notes = "")
	@GetMapping("/indexReport")
	public WebResult getIndexReport() {
		//查询总人数
		List<TUser> userList=tUserMapper.selectList(null);
		//查询所有社团
		List<TClub> clubList=tClubMapper.selectList(null);
		//查询所有活动
		List<TClubActivity> activityList=tClubActivityMapper.selectList(null);
		@SuppressWarnings("rawtypes")
		QueryWrapper query=new QueryWrapper();
		String today=DateUtils.getDate();
		query.like("create_date", today);
		//查询今日注册的所有人
		List<TUser> TodayUserList=tUserMapper.selectList(query);
		//查询今日新增的所有社团
		List<TClub> TodayClubList=tClubMapper.selectList(query);
		//查询今日新增的活动
		List<TClubActivity> TodayActivityList=tClubActivityMapper.selectList(query);
		//自定义首页报表实体类
		IndexReport report=new IndexReport();
		//将每个list的size放入对应的属性
		report.setUserNum(userList.size());
		report.setClubNum(clubList.size());
		report.setActivityNum(activityList.size());
		report.setTodayActivityNum(TodayActivityList.size());
		report.setTodayClubNum(TodayClubList.size());
		report.setTodayUserNum(TodayUserList.size());
		return selectSuccess(report);
	}
	
	@ApiOperation(value = "查询首页所有新闻", notes = "")
	@GetMapping("/indexNews")
	public WebResult getClubUsers(Page<TIndexNews> page,String status) {
		QueryWrapper<TIndexNews> query=new QueryWrapper<TIndexNews>();
		if(StrUtil.isNotBlank(status)) {
			query.eq("status", status);
		}
		Page<TIndexNews> pageList=tIndexNewsMapper.selectPage(page, query);
		if(pageList.getRecords()!=null && !pageList.getRecords().isEmpty()) {
			return selectSuccess(pageList);
		}
		return selectNotFound();
	}
	
	@ApiOperation(value = "停用启用新闻", notes = "")
	@PutMapping("/indexNews")
	public WebResult getClubUsers(String id,String status) {
		TIndexNews news=tIndexNewsMapper.selectById(id);
		if(news!=null ) {
			TIndexNews updateEntity=new TIndexNews();
			updateEntity.setId(id);
			updateEntity.setStatus(status);
			int i=tIndexNewsMapper.updateById(updateEntity);
			if(i>0) {
				return updateSuccess(i);
			}
		}
			return updateNotFound();
	}
	
	@ApiOperation(value = "删除新闻", notes = "")
	@DeleteMapping("/indexNews")
	public WebResult deleteIndexNews(String id) {
		int i=tIndexNewsMapper.deleteById(id);
		if(i>0 ) {
				return deleteSuccess(i);
		}
		return deleteNotFound();
	}
	
	@ApiOperation(value = "社团新增新闻告示", notes = "")
	@PostMapping("/clubNews")
	public WebResult insertClubNews(@RequestHeader(BaseContant.HEAD_PARAMS_CLUB_ID)String clubId,@RequestBody TClubNews tClubNews) {
		tClubNews.setClubId(clubId);
		tClubNews.setCreateDate(LocalDateTime.now());
		tClubNews.setStatus(BaseContant.STATUS_STOP);//默认停用
		int i=tClubNewsMapper.insert(tClubNews);
		if(i>0) {
			return insertSuccess(tClubNews);
		}
		return insertDataRepeat();
	}
	
	@ApiOperation(value = "查询首页所有社团新闻", notes = "")
	@GetMapping("/clubNews")
	public WebResult getClubNews(Page<TClubNews> page,String status) {
		QueryWrapper<TClubNews> query=new QueryWrapper<TClubNews>();
		if(StrUtil.isNotBlank(status)) {
			query.eq("status", status);
		}
		Page<TClubNews> pageList=tClubNewsMapper.selectPage(page, query);
		if(pageList.getRecords()!=null && !pageList.getRecords().isEmpty()) {
			return selectSuccess(pageList);
		}
		return selectNotFound();
	}
	
	@ApiOperation(value = "停用启用社团新闻", notes = "")
	@PutMapping("/clubNews")
	public WebResult stopClubNews(String id,String status) {
		TClubNews news=tClubNewsMapper.selectById(id);
		if(news!=null ) {
			TClubNews updateEntity=new TClubNews();
			updateEntity.setId(id);
			updateEntity.setStatus(status);
			int i=tClubNewsMapper.updateById(updateEntity);
			if(i>0) {
				return updateSuccess(i);
			}
		}
			return updateNotFound();
	}
}
