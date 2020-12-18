package com.dili.gw.boot;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryCustomizer;
import org.springframework.boot.web.reactive.server.ConfigurableReactiveWebServerFactory;

/**
 * @description:
 * @author: WM
 * @time: 2020/9/24 10:03
 */
//@Component
public class ServerCustomizationBean extends ReactiveWebServerFactoryCustomizer {

    public ServerCustomizationBean(ServerProperties serverProperties) {
        super(serverProperties);
    }

    @Override
    public void customize(ConfigurableReactiveWebServerFactory factory) {
        super.customize(factory);
        factory.setPort(8285);
    }
}
