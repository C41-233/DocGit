package c41.docgit.generator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import c41.docgit.generator.template.HtmlConfig;
import c41.docgit.generator.template.TemplateFile;
import c41.docgit.generator.template.TemplateGenerator;
import c41.docgit.generator.vo.Project;
import freemarker.template.TemplateException;

public class CategoryGenerator {

	private final String name;
	private final File categoryInputFolder;
	private final File categoryOutputFolder;
	
	private final ArrayList<Project> projects = new ArrayList<>();
	
	public CategoryGenerator(String name, File inputFolder, File outputFolder) {
		this.name = name;
		this.categoryInputFolder = inputFolder;
		this.categoryOutputFolder = outputFolder;
	}
	
	public void Run() throws IOException, TemplateException, DocumentException {
		for(File projectFolder : categoryInputFolder.listFiles(f -> f.isDirectory())) {
			String projectName = projectFolder.getName();
			System.out.println("generate project: " + this.name + "/" + projectName);
			File outputFolder = new File(categoryOutputFolder, projectName);
			outputFolder.mkdir();
			generateProject(projectName, projectFolder, outputFolder);
		}
		
		generateIndexHtml();
	}

	private void generateProject(String projectName, File inputFoler, File outputFolder) throws DocumentException {
		Project project = new Project();
		project.setName(projectName);
		
		File manifest = new File(inputFoler, "manifest.xml");
		SAXReader reader = new SAXReader();
		Document document = reader.read(manifest);
		Element rootElement = document.getRootElement();
		
		Element homeElement = rootElement.element("home");
		project.setHome(homeElement.getTextTrim());
		
		Element tagsElement = rootElement.element("tags");
		for(Object tagObject : tagsElement.elements("tag")) {
			Element tagElement = (Element) tagObject;
			project.addTag(tagElement.getTextTrim());
		}
		
		Element descriptionElement = rootElement.element("description");
		project.setDescription(descriptionElement.getTextTrim());
		
		projects.add(project);
 	}
	
	private void generateIndexHtml() throws IOException, TemplateException {
		File outputIndexHtml = new File(categoryOutputFolder, "index.html");
		
		HtmlConfig htmlConfig = new HtmlConfig();
		htmlConfig.arguments.put("projects", projects);
		htmlConfig.cssFile = TemplateFile.CATEGORY_INLINE_CSS;
		htmlConfig.jsFile = TemplateFile.CATEGORY_INLINE_JS;
		
		String output = TemplateGenerator.getInstance().generateHtml(TemplateFile.CATEGORY_INDEX_HTML, htmlConfig);
		FileUtils.write(outputIndexHtml, output, "utf-8");
	}
	
}
