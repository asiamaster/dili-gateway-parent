package com.dili.dr.boot;

import com.dili.dr.domain.GatewayRoutes;
import com.dili.dr.service.GatewayRoutesService;
import com.dili.ss.domain.BaseOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 路由控制器
 */
@RestController
@RequestMapping("/api/route")
public class RouteApi {

    @Autowired
    private GatewayRoutesService gatewayRoutesService;

    /**
     * 路由管理页
     * http://localhost:8286/api/route/init
     * @return
     */
    @GetMapping("/load")
    public BaseOutput index() {
        gatewayRoutesService.init();
        return BaseOutput.success("初始化完成");
    }

    /**
     * 查询所有路由
     * @return
     */
    @GetMapping("/list")
    public BaseOutput<List<GatewayRoutes>> list(){
        return BaseOutput.successData(gatewayRoutesService.list(null));
    }

}
