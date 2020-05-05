package com.example.demo.club.club.mapper;

import com.example.demo.club.club.entity.TClubUser;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 社团成员表 Mapper 接口
 * </p>
 *
 * @author youkehai
 * @since 2020-02-03
 */
public interface TClubUserMapper extends BaseMapper<TClubUser> {
	Page<TClubUser> selectmyClubInfo(Page<TClubUser> page,@Param(Constants.WRAPPER) QueryWrapper<TClubUser> queryWrapper);
	Page<TClubUser> selectClubUser(Page<TClubUser> page,@Param(Constants.WRAPPER) QueryWrapper<TClubUser> queryWrapper);
}
