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
     * http://localhost:8081/route/index.html
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
    public BaseOutput<String> add(@RequestBody GatewayRoutes gatewayRoutes) {
        this.gatewayRoutesService.insertSelective(gatewayRoutes);
        return BaseOutput.success("新增成功");
    }

    /**
     * 删除路由
     * @param routeId
     * @return
     */
    @PostMapping("/del.action")
    public BaseOutput<String> delete(@RequestParam("routeId") String routeId) {
        gatewayRoutesService.delete(routeId);
        return BaseOutput.success("删除成功");
    }

    /**
     * 更新路由
     * @param gatewayRoutes
     * @return
     */
    @PostMapping("/update.action")
    public BaseOutput<String> update(@RequestBody GatewayRoutes gatewayRoutes) {
        this.gatewayRoutesService.updateSelective(gatewayRoutes);
        return BaseOutput.success("修改成功");
    }

    /**
     * 更新路由
     * @param id
     * @return
     */
    @PostMapping("/doEnable.action")
    public BaseOutput<String> doEnable(Long id, Boolean enable) {
        this.gatewayRoutesService.updateEnable(id, enable);
        return BaseOutput.success("修改成功");
    }


}
