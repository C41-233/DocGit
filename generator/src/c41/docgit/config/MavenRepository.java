package c41.docgit.config;

public enum MavenRepository {

	central("http://central.maven.org/maven2"),
	clojars("http://clojars.org/repo"),
	;
	
	private String url;
	
	public String getUrl() {
		return url;
	}
	
	private MavenRepository(String url) {
		this.url = url;
	}
	
}
