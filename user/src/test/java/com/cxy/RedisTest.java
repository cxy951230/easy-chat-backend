package com.cxy;

import com.alibaba.fastjson.JSONObject;
import com.cxy.http.Session;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApplication.class)
public class RedisTest {

	@Autowired
	public JedisUtil jedisUtil;

	@Test
	public void test1() {
		Object o = jedisUtil.get("82524865-0401-4800-a42f-a24498aa3707");
		System.out.println(1);
	}
	@Test
	public void test2(){
		String str = "{\"new\":false,\"@clazz\":\"com.cxy.http.Session\",\"creationTime\":0,\"last\":0,\"valueNames\":[\"key12\"],\"servletContext\":{\"initParameterNames\":[],\"virtualServerName\":\"Tomcat/localhost\",\"filterRegistrations\":{\"webMvcMetricsFilter\":{\"initParameters\":{},\"name\":\"webMvcMetricsFilter\",\"className\":\"org.springframework.boot.actuate.metrics.web.servlet.WebMvcMetricsFilter\",\"servletNameMappings\":[],\"urlPatternMappings\":[\"/*\"]},\"requestContextFilter\":{\"initParameters\":{},\"name\":\"requestContextFilter\",\"className\":\"org.springframework.boot.web.servlet.filter.OrderedRequestContextFilter\",\"servletNameMappings\":[],\"urlPatternMappings\":[\"/*\"]},\"Tomcat WebSocket (JSR356) Filter\":{\"initParameters\":{},\"name\":\"Tomcat WebSocket (JSR356) Filter\",\"className\":\"org.apache.tomcat.websocket.server.WsFilter\",\"servletNameMappings\":[],\"urlPatternMappings\":[\"/*\"]},\"sessionFilter\":{\"initParameters\":{},\"name\":\"sessionFilter\",\"className\":\"com.cxy.http.filter.SessionFilter\",\"servletNameMappings\":[],\"urlPatternMappings\":[\"/*\"]},\"characterEncodingFilter\":{\"initParameters\":{},\"name\":\"characterEncodingFilter\",\"className\":\"org.springframework.boot.web.servlet.filter.OrderedCharacterEncodingFilter\",\"servletNameMappings\":[],\"urlPatternMappings\":[\"/*\"]},\"springSecurityFilterChain\":{\"initParameters\":{},\"name\":\"springSecurityFilterChain\",\"className\":\"org.springframework.boot.web.servlet.DelegatingFilterProxyRegistrationBean$1\",\"servletNameMappings\":[],\"urlPatternMappings\":[\"/*\"]},\"formContentFilter\":{\"initParameters\":{},\"name\":\"formContentFilter\",\"className\":\"org.springframework.boot.web.servlet.filter.OrderedFormContentFilter\",\"servletNameMappings\":[],\"urlPatternMappings\":[\"/*\"]}},\"servletNames\":[],\"contextPath\":\"\",\"defaultSessionTrackingModes\":[\"COOKIE\",\"URL\"],\"majorVersion\":4,\"effectiveMinorVersion\":0,\"sessionCookieConfig\":{\"maxAge\":-1,\"httpOnly\":false,\"secure\":false},\"effectiveSessionTrackingModes\":[\"COOKIE\",\"URL\"],\"attributeNames\":[\"javax.servlet.context.tempdir\",\"org.apache.catalina.resources\",\"org.springframework.web.context.WebApplicationContext.ROOT\",\"org.springframework.web.context.support.ServletContextScope\",\"org.apache.tomcat.InstanceManager\",\"org.apache.catalina.jsp_classpath\",\"javax.websocket.server.ServerContainer\",\"org.apache.tomcat.JarScanner\",\"org.springframework.web.servlet.FrameworkServlet.CONTEXT.dispatcherServlet\"],\"serverInfo\":\"Apache Tomcat/9.0.29\",\"servletRegistrations\":{\"default\":{\"initParameters\":{\"listings\":\"false\",\"debug\":\"0\"},\"mappings\":[],\"name\":\"default\",\"className\":\"org.apache.catalina.servlets.DefaultServlet\"},\"dispatcherServlet\":{\"initParameters\":{},\"mappings\":[\"/\"],\"name\":\"dispatcherServlet\",\"className\":\"org.springframework.web.servlet.DispatcherServlet\"}},\"servlets\":[],\"sessionTimeout\":30,\"minorVersion\":0,\"servletContextName\":\"application\",\"effectiveMajorVersion\":3},\"isNew\":false,\"attrs\":{\"key12\":\"f111\"},\"lastAccessedTime\":0,\"attributeNames\":[\"key12\"],\"maxInactiveInterval\":0,\"id\":\"82524865-0401-4800-a42f-a24498aa3707\",\"invalidated\":false}";
		JSONObject jsonObject = JSONObject.parseObject(str);
		JSONObject.parseObject(str, Session.class)
		System.out.println(11);
	}
}
