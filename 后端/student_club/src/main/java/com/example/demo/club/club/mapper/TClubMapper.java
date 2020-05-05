package com.example.demo.club.club.mapper;

import com.example.demo.club.club.entity.TClub;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 社团表 Mapper 接口
 * </p>
 *
 * @author youkehai
 * @since 2020-02-03
 */
public interface TClubMapper extends BaseMapper<TClub> {

	Page<TClub> selectPageAndClubManage(Page<TClub> page, @Param(Constants.WRAPPER)QueryWrapper<TClub> query);

}
