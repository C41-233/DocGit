package c41.docgit.generator.vo;

import java.util.ArrayList;
import java.util.List;

public class Project {

	private String name;
	private String home;
	private String description;
	private String latest;
	private List<String> tags = new ArrayList<>();
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setHome(String home) {
		this.home = home;
	}

	public String getHome() {
		return home;
	}
	
	public void addTag(String tag) {
		this.tags.add(tag);
	}
	
	public Iterable<String> getTags(){
		return this.tags;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLatest() {
		return latest;
	}

	public void setLatest(String latest) {
		this.latest = latest;
	}
	
}
