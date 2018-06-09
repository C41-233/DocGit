package c41.docgit.generator.template;

import java.util.List;

import org.dom4j.Element;

public final class XMLHelper {

	@SuppressWarnings("unchecked")
	public static List<Element> elements(Element root, String name){
		return root.elements(name);
	}
	
}
