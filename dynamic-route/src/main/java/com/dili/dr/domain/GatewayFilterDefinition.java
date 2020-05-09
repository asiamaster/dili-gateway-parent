package com.dili.dr.domain;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 过滤器模型
 * @author wangmi
 */

public class GatewayFilterDefinition {

    //Filter Name
    private String name;
    //对应的路由规则
    private Map<String, String> args = new LinkedHashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getArgs() {
        return args;
    }

    public void setArgs(Map<String, String> args) {
        this.args = args;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            GatewayFilterDefinition that = (GatewayFilterDefinition)o;
            return Objects.equals(this.name, that.name) && Objects.equals(this.args, that.args);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(new Object[]{this.name, this.args});
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("FilterDefinition{");
        sb.append("name='").append(this.name).append('\'');
        sb.append(", args=").append(this.args);
        sb.append('}');
        return sb.toString();
    }
}
