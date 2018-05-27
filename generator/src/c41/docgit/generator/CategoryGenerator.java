package c41.docgit.generator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import c41.docgit.generator.template.HtmlConfig;
import c41.docgit.generator.template.TemplateFile;
import c41.docgit.generator.template.TemplateGenerator;
import c41.docgit.generator.vo.Project;
import c41.docgit.generator.vo.MajorGroup;
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

	private void generateProject(String projectName, File inputFoler, File outputFolder) throws DocumentException, IOException, TemplateException {
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

		HashMap<String, MajorGroup> majors = new HashMap<>();
		
		Element majorsElement = rootElement.element("majors");
		for(Object majorObject : majorsElement.elements("major")) {
			MajorGroup group = new MajorGroup();
			Element majorElment = (Element) majorObject;
			String name = majorElment.getTextTrim();
			group.setName(name);
			majors.put(name, group);
		}
		
		HtmlConfig config = new HtmlConfig();
		config.arguments.put("groups", majors.values().toArray());
		
		File indexFile = new File(outputFolder, "index.html");
		TemplateGenerator.getInstance().generateHtml(TemplateFile.PROJECT_INDEX_HTML, indexFile, config);
 	}

	private void generateIndexHtml() throws IOException, TemplateException {
		File outputIndexHtml = new File(categoryOutputFolder, "index.html");
		
		HtmlConfig htmlConfig = new HtmlConfig();
		htmlConfig.arguments.put("projects", projects);
		htmlConfig.cssFile = TemplateFile.CATEGORY_INLINE_CSS;
		htmlConfig.jsFile = TemplateFile.CATEGORY_INLINE_JS;
		
		TemplateGenerator.getInstance().generateHtml(TemplateFile.CATEGORY_INDEX_HTML, outputIndexHtml, htmlConfig);
	}

}
