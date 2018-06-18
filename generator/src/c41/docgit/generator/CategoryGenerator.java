package c41.docgit.generator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.dom4j.DocumentException;

import c41.docgit.config.MavenRepository;
import c41.docgit.generator.template.HtmlConfig;
import c41.docgit.generator.template.TemplateFile;
import c41.docgit.generator.template.TemplateGenerator;
import c41.docgit.generator.vo.MajorGroup;
import c41.docgit.generator.vo.Maven;
import c41.docgit.generator.vo.Project;
import c41.docgit.generator.vo.Tag;
import c41.docgit.generator.vo.Version;
import c41.docgit.generator.xml.ProjectXML;
import c41.docgit.generator.xml.ProjectXML.BodyXML.ImportXML;
import c41.docgit.generator.xml.ProjectXML.MajorsXML.MajorXML;
import c41.docgit.generator.xml.ProjectXML.MajorsXML.MajorXML.MavenXML;
import c41.docgit.generator.xml.ProjectXML.MajorsXML.MajorXML.VersionXML;
import c41.docgit.generator.xml.XMLHelper;
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
	
	public void Run() throws IOException, TemplateException, DocumentException, JAXBException {
		for(File projectFolder : categoryInputFolder.listFiles(f -> f.isDirectory())) {
			String projectName = projectFolder.getName();
			System.out.println("generate project: " + this.categoryName + "/" + projectName);
			File outputFolder = new File(categoryOutputFolder, projectName);
			outputFolder.mkdir();
			generateProject(projectName, projectFolder, outputFolder);
		}
		
		generateIndexHtml();
	}

	private void generateProject(String projectName, File inputFoler, File outputFolder) throws DocumentException, IOException, TemplateException, JAXBException {
		Project project = new Project();
		projects.add(project);

		List<MajorGroup> majors = new ArrayList<>();
		
		File manifest = new File(inputFoler, "manifest.xml");
		ProjectXML projectXML = XMLHelper.read(manifest, ProjectXML.class);
		
		project.setName(projectName);
		project.setHome(projectXML.home);	
		project.setDescription(projectXML.description);
		
		for(String tag : projectXML.tags) {
			project.addTag(tag);
		}
		
		String latest = null;
		
		if(projectXML.majors != null) {
			if(projectXML.majors.majorList != null) {
				for(MajorXML majorElement : projectXML.majors.majorList) {
					MajorGroup majorGroup = new MajorGroup();
					majors.add(majorGroup);
					
					majorGroup.setName(majorElement.name);
					majorGroup.setDocument(majorElement.document);
					
					if(majorElement.versions != null) {
						for(VersionXML versionElement : majorElement.versions) {
							Version version = new Version();
							majorGroup.addVersion(version);
							
							version.setName(versionElement.name);
							version.setDocument(versionElement.document);
							
							if(versionElement.cacheDocument != null) {
								version.setCacheDocument(true);
							}
							
							if(versionElement.download != null) {
								if(versionElement.download.equals("false")) {
									version.setDownload(false);
								}
							}
						}
					}
					
					if(majorElement.mavens != null) {
						for(MavenXML mavenElement : majorElement.mavens) {
							Maven maven = new Maven();
							majorGroup.addMaven(maven);
							
							maven.setGroupId(mavenElement.groupId);
							maven.setArtifactId(mavenElement.artifactId);
							
							String repository = MavenRepository.valueOf(mavenElement.repository).getUrl();
							
							for(Version version : majorGroup.getVersions()) {
								if(version.isDownload()) {
									String name = maven.getArtifactId() + "-" + version.getName() + ".jar";
									String url = repository + "/" 
											+ maven.getGroupId().replace('.', '/') + "/" 
											+ maven.getArtifactId() + "/" 
											+ version.getName() + "/" 
											+ name;
									version.addArtifact(name, url);
								}
							}
						}
					}
				}
			}
			if(projectXML.majors.latest != null) {
				latest = projectXML.majors.latest.document;
			}
		}
		
		HtmlConfig config = new HtmlConfig();
		config.title = projectName;
		config.importCss.add("project.css");

		config.arguments.put("project", project);
		config.arguments.put("category", categoryName);
		config.arguments.put("majors", majors);
		config.arguments.put("latest", latest);
		
		config.arguments.put("hasMajors", latest != null || !majors.isEmpty());
		
		List<String> bodies = new ArrayList<>();
		config.arguments.put("bodies", bodies);
		if(projectXML.body != null) {
			for(ImportXML importXML : projectXML.body.imports) {
				File srcFile = new File(inputFoler, importXML.src);
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
