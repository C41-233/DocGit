package c41.docgit.generator.vo;

import java.util.ArrayList;
import java.util.List;

public class MajorGroup {

	private String name;
	private List<String> versions = new ArrayList<>();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addVersion(String version) {
		this.versions.add(version);
	}
	
	public Iterable<String> getVersions(){
		return versions;
	}
	
	public int getVersionCount() {
		return versions.size();
	}
	
}
