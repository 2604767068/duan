package com.example.demo.club.club.mapper;

import com.example.demo.club.club.entity.TClubActivity;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 社团活动表 Mapper 接口
 * </p>
 *
 * @author youkehai
 * @since 2020-02-03
 */
public interface TClubActivityMapper extends BaseMapper<TClubActivity> {

	List<TClubActivity> selectGroupByay();
}
