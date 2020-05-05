package com.example.demo.club.admin.service.impl;

import com.example.demo.club.admin.entity.TMessage;
import com.example.demo.club.admin.mapper.TMessageMapper;
import com.example.demo.club.admin.service.ITMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 平台留言表 服务实现类
 * </p>
 *
 * @author youkehai
 * @since 2020-02-03
 */
@Service
public class TMessageServiceImpl extends ServiceImpl<TMessageMapper, TMessage> implements ITMessageService {

}
