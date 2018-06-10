package c41.docgit.generator.vo;

import java.util.ArrayList;
import java.util.List;

public class MajorGroup {

	private String name;
	private List<Version> versions = new ArrayList<>();
	private Maven maven;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addVersion(Version version) {
		this.versions.add(version);
	}
	
	public Iterable<Version> getVersions(){
		return versions;
	}
	
	public int getVersionCount() {
		return versions.size();
	}

	public Maven getMaven() {
		return maven;
	}

	public void setMaven(Maven maven) {
		this.maven = maven;
	}
	
}
