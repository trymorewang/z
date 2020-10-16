package com.miaosha.mapper;


import com.miaosha.common.entity.IpLog;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IpLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(IpLog record);

    IpLog selectByPrimaryKey(Long id);

    List<IpLog> selectAll();

    int updateByPrimaryKey(IpLog record);

}