package com.websocket.payload.server;

import com.google.common.collect.Lists;
import com.websocket.model.server.Sys;
import com.websocket.payload.KV;
import lombok.Data;

import java.util.List;

/**
 * <p>
 *     系统相关信息实体VO
 * </p>
 *
 * @author Zhi.Wang
 * @version 1.0
 * @date 2020-10-16
 */
@Data
public class SysVO {
    List<KV> data = Lists.newArrayList();

    public static SysVO create(Sys sys) {
        SysVO vo = new SysVO();
        vo.data.add(new KV("服务器名称", sys.getComputerName()));
        vo.data.add(new KV("服务器Ip", sys.getComputerIp()));
        vo.data.add(new KV("项目路径", sys.getUserDir()));
        vo.data.add(new KV("操作系统", sys.getOsName()));
        vo.data.add(new KV("系统架构", sys.getOsArch()));
        return vo;
    }
}