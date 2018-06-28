package c41.docgit.config;

public enum MavenRepository {

	central("http://central.maven.org/maven2"),
	clojars("http://clojars.org/repo"),
	jspresso("http://repository.jspresso.org/maven2/"),
	jahia("http://maven.jahia.org/maven2/"),
	;
	
	private String url;
	
	public String getUrl() {
		return url;
	}
	
	private MavenRepository(String url) {
		this.url = url;
	}
	
}
