package com.shangqiu.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import com.alibaba.fastjson.JSONObject;

public class AnnotationHandlerMethodExceptionResolver extends ExceptionHandlerExceptionResolver {

	private String defaultErrorView;

	public String getDefaultErrorView() {
		return defaultErrorView;
	}

	public void setDefaultErrorView(String defaultErrorView) {
		this.defaultErrorView = defaultErrorView;
	}

	@Override
	protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception exception) {
		//TODO 此处应该将异常日志记录，以便于分析

		//判断是否为json请求-begin
		HttpInputMessage inputMessage = new ServletServerHttpRequest(request);
		List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();

		if (acceptedMediaTypes.isEmpty()) {
			acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
		}

		MediaType.sortByQualityValue(acceptedMediaTypes);

		boolean isJsonRequest = false;
		for (MediaType acceptedMediaType : acceptedMediaTypes) {
			if (MediaType.APPLICATION_JSON_VALUE.equals(acceptedMediaType.getType() + "/" + acceptedMediaType.getSubtype())) {
				isJsonRequest = true;
			}
		}
		if (isJsonRequest) {
			response.setHeader("Content-Type", "application/json;charset=utf-8");
			response.setStatus(500);
			PrintWriter writer = null;
			try {
				writer = response.getWriter();
				JSONObject root = new JSONObject();
				root.put("msg", "内部异常，请联系管理员");
				root.put("code", 500);

				//如果是datatables发起的ajax请求，设置http status 为403，这样datatables就走error函数了
				if (StringUtils.isNotBlank(request.getParameter("draw"))) {
					root.put("data", new ArrayList<String>());
				}

				writer.write(root.toJSONString());
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return new ModelAndView();
		}
		//
		else {
			ModelAndView defaultView = new ModelAndView(defaultErrorView);
			defaultView.addObject("message", ExceptionUtils.getMessage(exception));
			defaultView.addObject("stackTrace", ExceptionUtils.getStackTrace(exception));
			return defaultView;
		}
	}

}
