package c41.docgit.generator.vo;

public class Document {

	public static enum DocumentType{
		Force,
		Cache,
		Default,
	}
	
	private String url;
	private DocumentType type;
	
	public Document(String url, DocumentType type) {
		this.url = url;
		this.type = type;
	}
	
	@Override
	public String toString() {
		return url;
	}
	
	public DocumentType getType() {
		return this.type;
	}
	
}
