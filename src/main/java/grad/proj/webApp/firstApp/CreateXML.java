package grad.proj.webApp.firstApp;

import java.util.ArrayList;

public class CreateXML {

	
	public CreateXML() {

		
	}
	
	public ArrayList<MyXML> makeList(ArrayList <MyXML> lista)
	{
		for(int i=0;i<5;i++)
		{
			MyXML xml=new  MyXML("esraa",i);
			lista.add(xml);
		}
		return lista;
	}


	
	
	
}
