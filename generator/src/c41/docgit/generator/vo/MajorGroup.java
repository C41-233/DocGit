package c41.docgit.generator.vo;

import java.util.ArrayList;
import java.util.List;

public class MajorGroup {

	private String name;
	private List<Version> versions = new ArrayList<>();
	private String url;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addVersion(Version version) {
		this.versions.add(version);
		versions.sort((v1, v2)->v1.getName().compareTo(v2.getName()));
	}
	
	public Iterable<Version> getVersions(){
		return versions;
	}
	
	public int getVersionCount() {
		return versions.size();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
