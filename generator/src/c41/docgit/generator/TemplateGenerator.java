package c41.docgit.generator;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

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
	
	public String generateHtml(File input, Object arguments) throws IOException, TemplateException {
		String body = generate(input, arguments);
		HashMap<String, Object> maps = new HashMap<>();
		maps.put("body", body);
		
		return generate(TemplateFile.MAIN_TEMPLATE_HTML, maps);
	}
	
}
