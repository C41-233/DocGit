package c41.docgit.generator.method;

import java.util.List;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class Path implements TemplateMethodModelEx{

	@SuppressWarnings("rawtypes")
	@Override
	public Object exec(List args) throws TemplateModelException {
		StringBuilder sb = new StringBuilder();
		sb.append("/DocGit");
		for(Object arg : args) {
			sb.append(arg);
		}
		return sb.toString();
	}

}
