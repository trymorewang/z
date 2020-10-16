package com.websocket.controller;

import cn.hutool.core.lang.Dict;
import com.websocket.model.Server;
import com.websocket.payload.ServerVO;
import com.websocket.util.ServerUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *     服务器监控控制器
 * </p>
 *
 * @author Zhi.Wang
 * @version 1.0
 * @date 2020-10-16
 */
@RestController
@RequestMapping("/server")
public class ServerController {

    @GetMapping
    public Dict serverInfo() throws Exception {
        Server server = new Server();
        server.copyTo();
        ServerVO serverVO = ServerUtil.wrapServerVO(server);
        return ServerUtil.wrapServerDict(serverVO);
    }

}
