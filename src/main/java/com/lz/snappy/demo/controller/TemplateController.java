package com.lz.snappy.demo.controller;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Controller
@RequestMapping("/template")
public class TemplateController {
	
	@Autowired
	private FreeMarkerConfigurer cfg;
	
	//注意在spring4.1.6中没有GetMapping这个注解，改为5.0.4版本之后问题解决
	//如果想要返回的内容中包含标签则需要，在mapping中设置如下属性值
	@RequestMapping(value="/parser.do",produces={"application/xml; charset=UTF-8"})
	@ResponseBody//不加此注解默认返回为'返回值+.jsp'所以会导致
	public String parser(HttpServletResponse resp) {
		Map<String,Object> data = new HashMap<>();
		data.put("entryId", "1000652");
		data.put("dDate", LocalDateTime.now());
		data.put("channel", "G");
		data.put("iEFlag", "ieflag");
		data.put("declProt", "declProt");
		data.put("iEPort", "iEPort");
		data.put("iEDate", LocalDateTime.now());
		data.put("trafMode", "trafMode");
		data.put("entryStatus", 1000);
		char[] chars;
		CharArrayWriter out = null;
		try {
			Template template = cfg.getConfiguration().getTemplate("basCustomsResult.ftl");
			out = new CharArrayWriter();
			template.process(data, out);
			chars = out.toCharArray();
		}catch (IOException | TemplateException e) {
			return "解析文件出现异常";
		}finally {
			out.close();
		}
		
		String result = new String(chars);
		System.out.println(result);
		return result;
	}

}
