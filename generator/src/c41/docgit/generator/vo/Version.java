package c41.docgit.generator.vo;

public class Version {

	private String name;
	private String document;
	
	private String artifactName;
	private String artifactUrl;
	
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

	public boolean HasArtifact() {
		return artifactName != null;
	}
	
	public String getArtifactName() {
		return artifactName;
	}

	public void setArtifactName(String artifactName) {
		this.artifactName = artifactName;
	}

	public String getArtifactUrl() {
		return artifactUrl;
	}

	public void setArtifactUrl(String artifactUrl) {
		this.artifactUrl = artifactUrl;
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
