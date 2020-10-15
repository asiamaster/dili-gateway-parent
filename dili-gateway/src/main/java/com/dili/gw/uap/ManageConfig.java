package com.dili.gw.uap;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 权限配置信息<BR>
 * Created by asiamaster on 2017/7/4 0004.
 */
@Component
@ConfigurationProperties(prefix = "manage")
@PropertySource({"classpath:conf/manage.properties"})
public class ManageConfig {

	//排除列表
	private List<String> excludes;

	/**
	 * 验证是否排除
	 * @return
	 */
	public boolean isExclude(String uri) {
		return urlFilter(excludes, uri);
	}

	/**
	 * 获取排除列表
	 * @return
	 */
	public List<String> getExcludes() {
		return excludes;
	}

	/**
	 * 设置排除列表
	 * @param excludes
	 */
	public void setExcludes(List<String> excludes) {
		this.excludes = excludes;
	}

	/**
	 * 验证uri是否匹配patternStrs
	 * @param patternStrs
	 * @param uri
	 * @return
	 */
	private Boolean urlFilter(List<String> patternStrs, String uri){
		for(String str : patternStrs){
			if(Pattern.compile(str).matcher(uri).find()){
				return true;
			}
		}
		return false;
	}

}