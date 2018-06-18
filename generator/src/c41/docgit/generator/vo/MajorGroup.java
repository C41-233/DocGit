package c41.docgit.generator.vo;

import java.util.ArrayList;
import java.util.List;

public class MajorGroup {

	private String name;
	private List<Version> versions = new ArrayList<>();
	private List<Maven> mavens = new ArrayList<>();
	private String document;
	
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

	public List<Maven> getMavens() {
		return mavens;
	}

	public void addMaven(Maven maven) {
		this.mavens.add(maven);
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}
	
}
