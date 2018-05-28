package c41.docgit.generator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import c41.docgit.generator.template.HtmlConfig;
import c41.docgit.generator.template.TemplateFile;
import c41.docgit.generator.template.TemplateGenerator;
import c41.docgit.generator.vo.MajorGroup;
import c41.docgit.generator.vo.Project;
import c41.docgit.generator.vo.Version;
import freemarker.template.TemplateException;

public class CategoryGenerator {

	private final String categoryName;
	private final File categoryInputFolder;
	private final File categoryOutputFolder;
	
	private final ArrayList<Project> projects = new ArrayList<>();
	
	public CategoryGenerator(String name, File inputFolder, File outputFolder) {
		this.categoryName = name;
		this.categoryInputFolder = inputFolder;
		this.categoryOutputFolder = outputFolder;
	}
	
	public void Run() throws IOException, TemplateException, DocumentException {
		for(File projectFolder : categoryInputFolder.listFiles(f -> f.isDirectory())) {
			String projectName = projectFolder.getName();
			System.out.println("generate project: " + this.categoryName + "/" + projectName);
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

		List<MajorGroup> majors = new ArrayList<>();
		
		Element majorsElement = rootElement.element("majors");
		for(Object majorObject : majorsElement.elements("major")) {
			MajorGroup group = new MajorGroup();
			Element majorElement = (Element) majorObject;
			String name = majorElement.attributeValue("name");
			group.setName(name);
			String majorDocument = majorElement.attributeValue("document");
			if(majorDocument != null) {
				group.setUrl(majorDocument);
			}
			
			for(Object minorObject : majorElement.elements("version")) {
				Element minorElement = (Element) minorObject;
				Version version = new Version();
				version.setName(minorElement.attributeValue("name"));
				version.setUrl(minorElement.attributeValue("document"));
				if(minorElement.element("cache-document") != null) {
					version.setUrl("/DocGit/doc/" + categoryName + "/" + projectName + "/" + version.getName());
				}
				
				group.addVersion(version);
			}
			
			majors.add(group);
		}
		
		Element latestElement = majorsElement.element("latest");
		if(latestElement != null) {
			project.setLatest(latestElement.attributeValue("document"));
		}
		
		HtmlConfig config = new HtmlConfig();
		config.arguments.put("groups", majors);
		config.arguments.put("latest", project.getLatest());
		config.arguments.put("home", project.getHome());
		config.arguments.put("category", categoryName);
		config.arguments.put("project", projectName);
		config.arguments.put("description", project.getDescription());
		config.arguments.put("tags", project.getTags());
		config.title = projectName;
		
		File indexFile = new File(outputFolder, "index.html");
		TemplateGenerator.getInstance().generateHtml(TemplateFile.PROJECT_INDEX_HTML, indexFile, config);
 	}

	private void generateIndexHtml() throws IOException, TemplateException {
		File outputIndexHtml = new File(categoryOutputFolder, "index.html");
		
		HtmlConfig htmlConfig = new HtmlConfig();
		htmlConfig.title = "docgit for " + categoryName;
		htmlConfig.arguments.put("category", categoryName);
		htmlConfig.arguments.put("projects", projects);
		
		TemplateGenerator.getInstance().generateHtml(TemplateFile.CATEGORY_INDEX_HTML, outputIndexHtml, htmlConfig);
	}

}
