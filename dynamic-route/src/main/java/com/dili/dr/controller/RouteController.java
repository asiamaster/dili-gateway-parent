package com.dili.dr.controller;

import com.dili.dr.domain.GatewayRoutes;
import com.dili.dr.service.GatewayRoutesService;
import com.dili.ss.domain.BaseOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 路由控制器
 */
@RestController
@RequestMapping("/route")
public class RouteController {

    @Autowired
    private GatewayRoutesService gatewayRoutesService;

    /**
     * 路由管理页
     * @return
     */
    @GetMapping("/index.html")
    public String index() {
        return "route/index";
    }
    /**
     * 查询路由
     * @return
     */
    @GetMapping("/listPage")
    public List<GatewayRoutes> listPage(GatewayRoutes gatewayRoutes) {
//        dynamicRouteService.listAll();
        return gatewayRoutesService.listByExample(gatewayRoutes);
    }

    /**
     * 增加路由
     * @param gatewayRoutes
     * @return
     */
    @PostMapping("/add")
    public BaseOutput<String> add(@RequestBody GatewayRoutes gatewayRoutes) {
        this.gatewayRoutesService.insertSelective(gatewayRoutes);
        return BaseOutput.success("新增成功");
    }

    /**
     * 删除路由
     * @param routeId
     * @return
     */
    @DeleteMapping("/del/{routeId}")
    public BaseOutput<String> delete(@PathVariable String routeId) {
        gatewayRoutesService.delete(routeId);
        return BaseOutput.success("删除成功");
    }

    /**
     * 更新路由
     * @param gatewayRoutes
     * @return
     */
    @PostMapping("/update")
    public BaseOutput<String> update(@RequestBody GatewayRoutes gatewayRoutes) {
        this.gatewayRoutesService.updateSelective(gatewayRoutes);
        return BaseOutput.success("修改成功");
    }

}
