package c41.docgit.generator;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.dom4j.DocumentException;

import c41.docgit.generator.template.TemplateFile;
import freemarker.template.TemplateException;

public class Main {

	public static void main(String[] args) throws IOException, TemplateException, DocumentException, JAXBException {
		if(args.length < 3) {
			System.err.println("usage: <jar> <template-folder> <output-folder> <data-folder>");
			return;
		}
		String templateFolderPath = args[0];
		String outputFolderPath = args[1];
		String dataFolderPath = args[2];
		
		File templateFolder = new File(templateFolderPath);
		File outputFolder = new File(outputFolderPath);
		File dataFolder = new File(dataFolderPath);
		System.out.println("generate template config by:");
		System.out.println("template: " + templateFolder.getAbsolutePath());
		System.out.println("output: " + outputFolder.getAbsolutePath());
		System.out.println("config: " + dataFolder.getAbsolutePath());
		System.out.println();
		
		//clean directory
		FileUtils.cleanDirectory(outputFolder);
		
		TemplateFile.TEMPLATE_FOLDER = templateFolder;
		buildConfig();
		
		generate(templateFolder, outputFolder, dataFolder);
	}
	
	private static void buildConfig() {
		TemplateFile.MAIN_TEMPLATE_HTML = new File(TemplateFile.TEMPLATE_FOLDER, "main.template.html");
		TemplateFile.CATEGORY_INDEX_HTML = new File(TemplateFile.TEMPLATE_FOLDER, "category.html");
		TemplateFile.PROJECT_INDEX_HTML = new File(TemplateFile.TEMPLATE_FOLDER, "project.html");
	}
	
	private static void generate(File templateFolder, File outputFolder, File dataFolder) throws IOException, TemplateException, DocumentException, JAXBException {
		for(File categoryFolder : dataFolder.listFiles(File::isDirectory)) {
			String category = categoryFolder.getName();
			System.out.println("generate category: " + category);
			File outputCategoryFolder = new File(outputFolder, category);
			outputCategoryFolder.mkdir();
			CategoryGenerator generator = new CategoryGenerator(category, categoryFolder, outputCategoryFolder);
			generator.Run();
		}
	}
	
}
