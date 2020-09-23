package com.dili.dr.controller;

import com.dili.dr.domain.GatewayRoutes;
import com.dili.dr.service.GatewayRoutesService;
import com.dili.ss.domain.BaseOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 路由控制器
 */
@Controller
@RequestMapping("/route")
public class RouteController {

    @Autowired
    private GatewayRoutesService gatewayRoutesService;

    /**
     * 路由管理页
     * http://localhost:8286/route/index.html
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
    @PostMapping("/listPage.action")
    @ResponseBody
    public String listPage(GatewayRoutes gatewayRoutes) throws Exception {
//        dynamicRouteService.listAll();
        return gatewayRoutesService.listEasyuiPageByExample(gatewayRoutes, true).toString();
    }

    /**
     * 增加路由
     * @param gatewayRoutes
     * @return
     */
    @PostMapping("/add.action")
    @ResponseBody
    public BaseOutput<String> add(@ModelAttribute GatewayRoutes gatewayRoutes) {
        return this.gatewayRoutesService.insertSelectiveAndRefreshRoute(gatewayRoutes);
    }

    /**
     * 删除路由
     * @param routeId
     * @return
     */
    @PostMapping("/del.action")
    @ResponseBody
    public BaseOutput<String> delete(@RequestParam("routeId") String routeId) {
        return gatewayRoutesService.deleteAndRefreshRoute(routeId);
    }

    /**
     * 更新路由
     * @param gatewayRoutes
     * @return
     */
    @PostMapping("/update.action")
    @ResponseBody
    public BaseOutput<String> update(GatewayRoutes gatewayRoutes) {
        return this.gatewayRoutesService.updateSelectiveAndRefreshRoute(gatewayRoutes);
    }

    /**
     * 更新路由
     * @param id
     * @return
     */
    @PostMapping("/doEnable.action")
    @ResponseBody
    public BaseOutput<String> doEnable(Long id, Boolean enable) {
        return this.gatewayRoutesService.updateEnableAndRefreshRoute(id, enable);
    }

    /**
     * 重加载路由
     * @return
     */
    @GetMapping("/reload.action")
    @ResponseBody
    public BaseOutput<String> reload() {
        return this.gatewayRoutesService.reload();
    }

}
