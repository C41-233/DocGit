package c41.docgit.generator.vo;

import java.util.ArrayList;
import java.util.List;

public class Majors {

	private List<Major> majors = new ArrayList<>();

	private String latest;
	
	private String name;
	
	public void addMajor(Major group) {
		majors.add(group);
	}
	
	public List<Major> getMajors(){
		return majors;
	}

	public String getLatest() {
		return latest;
	}

	public void setLatest(String latest) {
		this.latest = latest;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
