package c41.docgit.generator.template;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

public class TemplateGenerator {

	private static TemplateGenerator inst;
	
	public static TemplateGenerator getInstance() {
		if(inst == null) {
			inst = new TemplateGenerator();
		}
		return inst;
	}
	
	private final Configuration configuration;
	
	private TemplateGenerator() {
		configuration = new Configuration(new Version("2.3.27"));
		configuration.setDefaultEncoding("utf8");
		configuration.setLogTemplateExceptions(false);
		configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
	}
	
	public String generate(File input, Object arguments) throws IOException, TemplateException {
		configuration.setDirectoryForTemplateLoading(input.getParentFile());
		
		Template template = configuration.getTemplate(input.getName());
		
		StringWriter writer = new StringWriter();
		template.process(arguments, writer);
		return writer.toString();
	}
	
	public void generateHtml(File input, File output, HtmlConfig config) throws IOException, TemplateException {
		String body = generate(input, config.arguments);
		
		HashMap<String, Object> maps = new HashMap<>();
		maps.put("title", config.title);
		maps.put("body", body);
		
		if(config.cssFile != null) {
			String text = FileUtils.readFileToString(config.cssFile, "utf-8");
			maps.put("css", text);
		}

		if(config.jsFile != null) {
			String text = FileUtils.readFileToString(config.jsFile, "utf-8");
			maps.put("js", text);
		}
		
		String result = generate(TemplateFile.MAIN_TEMPLATE_HTML, maps);
		FileUtils.write(output, result, "utf-8");
	}
	
}
