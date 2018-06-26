package c41.docgit.generator.vo;

import java.util.ArrayList;
import java.util.List;

import c41.docgit.generator.vo.Document.DocumentType;

public class Version {

	private String name;
	private List<Document> documents = new ArrayList<>();

	private List<Artifact> artifacts = new ArrayList<>();
	
	private boolean download = true;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Document> getDocuments() {
		return documents;
	}

	private boolean noDefaultDocument;
	
	public void addDocument(String url, DocumentType type) {
		if(noDefaultDocument && type == DocumentType.Default) {
			return;
		}
		if(type == DocumentType.Force || type == DocumentType.Cache) {
			noDefaultDocument = true;
		}
		this.documents.add(new Document(url, type));
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
	
	public boolean isDownload() {
		return download;
	}

	public void setDownload(boolean download) {
		this.download = download;
	}

	public void setNoDefaultDocument(boolean v) {
		noDefaultDocument = true;
	}

}
