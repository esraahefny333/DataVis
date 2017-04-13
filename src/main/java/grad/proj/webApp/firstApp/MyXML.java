package grad.proj.webApp.firstApp;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MyXML {

	String name;
	int id;
	
	public MyXML() {
		
	}
	public MyXML(String name, int id) {
		super();
		this.name = name;
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}
