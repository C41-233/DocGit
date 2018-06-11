package c41.docgit.generator.xml;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.dom4j.Element;

public final class XMLHelper {

	@SuppressWarnings("unchecked")
	public static List<Element> elements(Element root, String name){
		return root.elements(name);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T read(File file, Class<T> clazz) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(ProjectXML.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		return (T) unmarshaller.unmarshal(file);
	}
	
}
