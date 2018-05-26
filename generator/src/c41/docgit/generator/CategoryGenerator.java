package c41.docgit.generator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;

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
	
	public void Run() throws IOException, TemplateException {
		for(File projectFolder : categoryInputFolder.listFiles(f -> f.isDirectory())) {
			String projectName = projectFolder.getName();
			System.out.println("generate project: " + this.name + "/" + projectName);
			File outputFolder = new File(categoryOutputFolder, projectName);
			outputFolder.mkdir();
			generateProject(projectName, projectFolder, outputFolder);
		}
		
		generateIndexHtml();
	}

	private void generateProject(String projectName, File inputFoler, File outputFolder) {
		Project project = new Project();
		project.setName(projectName);
		
		File manifest = new File(inputFoler, "manifest.xml");
		
		
		projects.add(project);
 	}
	
	private void generateIndexHtml() throws IOException, TemplateException {
		File outputIndexHtml = new File(categoryOutputFolder, "index.html");
		
		HashMap<String, Object> maps = new HashMap<>();
		maps.put("projects", projects);
		
		String output = TemplateGenerator.getInstance().generateHtml(TemplateFile.CATEGORY_INDEX_HTML, maps);
		FileUtils.write(outputIndexHtml, output, "utf-8");
	}
	
}
