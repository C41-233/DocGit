package c41.docgit.generator.vo;

import java.util.ArrayList;
import java.util.List;

public class Version {

	private String name;
	private String document;
	private boolean needDocument = true;

	private List<Artifact> artifacts = new ArrayList<>();
	
	private boolean download = true;
	
	private boolean cacheDocument;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String url) {
		this.document = url;
	}

	public void setDocument(boolean b) {
		this.needDocument = b;
	}

	public boolean needDocument() {
		return needDocument;
	}
	
	public boolean hasDocument() {
		return document != null || cacheDocument;
	}

	public boolean hasArtifact() {
		return artifacts.size() > 0;
	}

	public void addArtifact(String name, String url) {
		Artifact artifact = new Artifact();
		artifact.setName(name);
		artifact.setUrl(url);
		this.artifacts.add(artifact);
	}

	public List<Artifact> getArtifacts(){
		return this.artifacts;
	}
	
	public boolean isCacheDocument() {
		return cacheDocument;
	}

	public void setCacheDocument(boolean cacheDocument) {
		this.cacheDocument = cacheDocument;
	}

	public boolean isDownload() {
		return download;
	}

	public void setDownload(boolean download) {
		this.download = download;
	}

}
