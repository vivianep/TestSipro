package controllers;
import play.data.validation.Validation;


public class ValidationTest {
	
	private boolean hasErrors;
	ValidationTest(){
		hasErrors=false;
	}
	
	public void required(Object object){
			if(object==null)
				hasErrors=true;
			else if (object instanceof String){
				if(((String)object).equals(""))
					hasErrors=true;
			}
	
		
	}
	
	public boolean email(String string){
		CharSequence com= ".com";
		CharSequence at="@";
		if (!string.contains(com)||!string.contains(at))
			return false;
		return true;
	}
	
	public boolean hasErrors(){
		return hasErrors;
	}
	
	public boolean equals (String object1,String object2){
		return object1.equals(object2);
	}
	
	public boolean range ( int param,int min, int max){
		if(param>=min&&param<=max)
			return true;
		return false;
	}
	/*
	public boolean required(String tipo, Object object){
		switch(tipo){
		case "crm":
			String crm=(String)object;
			for (int i = 0; i < crm.length(); i++) {
				if(crm.codePointAt(i)<48||crm.codePointAt(i)>=58)
					return false;
			}
			return true;
		case "nome":
		case "prontuario":
		case "sexo":
		case "telefone":
		case "dia":
		case "mes":
		case "ano":
				
			
		}
	}
	*/
}
