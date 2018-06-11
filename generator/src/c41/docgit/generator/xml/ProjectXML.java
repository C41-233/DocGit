package c41.docgit.generator.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "project")
public class ProjectXML {

	public String home;
	public String description;
	
	@XmlElementWrapper(name = "tags")
	@XmlElement(name = "tag")
	public String[] tags;

	public MajorsXML majors;
	
	public BodyXML body;
	
	public static class MajorsXML{
		
		public LatestXML latest;
		
		@XmlElement(name = "major")
		public List<MajorXML> majorList;

		public static class LatestXML{
			
			@XmlAttribute
			public String document;
			
		}

		public static class MajorXML {
			
			@XmlAttribute
			public String name;
			
			@XmlElement(name = "version")
			public List<VersionXML> versions;

			@XmlAttribute
			public String document;
			
			public MavenXML maven;
			
			public static class VersionXML{

				@XmlAttribute
				public String name;
				
				@XmlAttribute
				public String document;
				
				@XmlElement(name = "cache-document")
				public CacheDocument cacheDocument;

				@XmlAttribute
				public String download;
				
				public static class CacheDocument{
					
				}
				
			}

			public static class MavenXML{
				
				public String groupId;
				public String artifactId;
				public String repository;
				
			}
			
		}

	}

	public static class BodyXML{
		
		@XmlElement(name = "import")
		public List<ImportXML> imports;
		
		public static class ImportXML{
			
			@XmlAttribute
			public String src;
		
		}
		
	}
	
}
