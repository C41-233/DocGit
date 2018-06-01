package c41.docgit.generator.template;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HtmlConfig {

	public File cssFile;
	public File jsFile;
	public final HashMap<String, Object> arguments = new HashMap<>();
	public String title;
	public List<String> importCss = new ArrayList<>(); 
	public List<String> importJs = new ArrayList<>();
	
}
