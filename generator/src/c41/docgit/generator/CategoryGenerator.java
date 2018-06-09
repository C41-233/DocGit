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
import c41.docgit.generator.template.XMLHelper;
import c41.docgit.generator.vo.MajorGroup;
import c41.docgit.generator.vo.Project;
import c41.docgit.generator.vo.Tag;
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
		
		boolean hasMaven = false;
		
		File manifest = new File(inputFoler, "manifest.xml");
		SAXReader reader = new SAXReader();
		Document document = reader.read(manifest);
		Element rootElement = document.getRootElement();
		
		Element homeElement = rootElement.element("home");
		project.setHome(homeElement.getTextTrim());
		
		Element tagsElement = rootElement.element("tags");
		for(Object tagObject : tagsElement.elements("tag")) {
			Element tagElement = (Element) tagObject;
			String tag = tagElement.getTextTrim();
			project.addTag(tag);
		}
		
		Element descriptionElement = rootElement.element("description");
		project.setDescription(descriptionElement.getTextTrim());
		
		projects.add(project);

		List<MajorGroup> majors = new ArrayList<>();
		
		Element majorsElement = rootElement.element("majors");
		if(majorsElement != null) {
			for(Element majorElement : XMLHelper.elements(majorsElement, "major")) {
				MajorGroup majorGroup = new MajorGroup();
				String name = majorElement.attributeValue("name");
				majorGroup.setName(name);
				String majorDocument = majorElement.attributeValue("document");
				if(majorDocument != null) {
					majorGroup.setUrl(majorDocument);
				}
				
				for(Element minorElement : XMLHelper.elements(majorElement, "version")) {
					Version version = new Version();
					version.setName(minorElement.attributeValue("name"));
					version.setUrl(minorElement.attributeValue("document"));
					if(minorElement.element("cache-document") != null) {
						version.setUrl("/DocGit/doc/" + categoryName + "/" + projectName + "/" + version.getName());
					}
					
					Element mavenElement = minorElement.element("maven");
					if(mavenElement != null) {
						String cdata = mavenElement.getStringValue().trim().replace("\t", "    ");
						version.setMaven(cdata);
						hasMaven = true;
					}
					
					majorGroup.addVersion(version);
				}
				
				majors.add(majorGroup);
			}
			Element latestElement = majorsElement.element("latest");
			if(latestElement != null) {
				project.setLatest(latestElement.attributeValue("document"));
			}
		}
		
		HtmlConfig config = new HtmlConfig();
		config.title = projectName;
		config.importCss.add("project.css");

		config.arguments.put("groups", majors);
		config.arguments.put("latest", project.getLatest());
		config.arguments.put("hasBody", project.getLatest() != null || !majors.isEmpty());
		config.arguments.put("hasMaven", hasMaven);
		config.arguments.put("home", project.getHome());
		config.arguments.put("category", categoryName);
		config.arguments.put("project", projectName);
		config.arguments.put("description", project.getDescription());
		config.arguments.put("tags", project.getTags());
		
		List<String> bodies = new ArrayList<>();
		config.arguments.put("bodies", bodies);
		Element bodyElement = rootElement.element("body");
		if(bodyElement != null) {
			for(Object importObject : bodyElement.elements("import")) {
				Element importElement = (Element)importObject;
				String src = importElement.attributeValue("src");
				File srcFile = new File(inputFoler, src);
				bodies.add(FileUtils.readFileToString(srcFile, "utf-8"));
			}
		}
		
		File indexFile = new File(outputFolder, "index.html");
		TemplateGenerator.getInstance().generateHtml(TemplateFile.PROJECT_INDEX_HTML, indexFile, config);
 	}

	private void generateIndexHtml() throws IOException, TemplateException {
		File outputIndexHtml = new File(categoryOutputFolder, "index.html");
		
		HtmlConfig htmlConfig = new HtmlConfig();
		htmlConfig.title = "docgit for " + categoryName;
		htmlConfig.importCss.add("category.css");
		htmlConfig.importJs.add("category.js");
		htmlConfig.arguments.put("category", categoryName);
		htmlConfig.arguments.put("projects", projects);
		
		HashMap<String, Tag> tagsSet = new HashMap<>();
		for(Project project : projects) {
			for (String name : project.getTags()) {
				Tag tag;
				if(tagsSet.containsKey(name)) {
					tag = tagsSet.get(name);
				}
				else {
					tag = new Tag();
					tag.setName(name);
					tagsSet.put(name, tag);
				}
				tag.addCount();
			}
		}
		List<Tag> tagsList = new ArrayList<>();
		for (Tag tag : tagsSet.values()) {
			tagsList.add(tag);
		}
		tagsList.sort((t1, t2) -> t1.getName().compareTo(t2.getName()));
		htmlConfig.arguments.put("tags", tagsList);
		
		TemplateGenerator.getInstance().generateHtml(TemplateFile.CATEGORY_INDEX_HTML, outputIndexHtml, htmlConfig);
	}

}
