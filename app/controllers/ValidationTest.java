package controllers;
import play.data.validation.Validation;


public class ValidationTest {
	
	private boolean hasErrors;
	ValidationTest(){
		hasErrors=false;
	}
	
	public boolean required(Object object){
			if(object==null){
				hasErrors=true;
				return false;
			}	
			else if (object instanceof String){
				if(((String)object).equals("")){
					hasErrors=true;
					return false;
				}
			}
			
			return true;
	
		
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
		if(object1.equals(object2)){
			return true;
		}
		hasErrors=true;
		return false;
	}
	
	public boolean range ( int param,int min, int max){
		if(param>=min&&param<=max){
			return true;
		}
		hasErrors=true;
	return false;
	}
	
	public boolean required(String tipo, Object object){
		
		if(tipo =="crm"){
			String crm=(String)object;
			for (int i = 0; i < crm.length(); i++) {
				if(crm.codePointAt(i)<48||crm.codePointAt(i)>=58)
					return false;
			}
			return true;
		}
		else if(tipo =="nome"){
			return true;
		}
		else if(tipo == "prontuario"){
			return true;
		}
		else if(tipo =="sexo"){
			return true;
		}
		else if(tipo =="telefone"){
			return true;
		}
		else if(tipo =="dia"){
			return true;
		}
		if(tipo =="mes"){
			return true;
		}
		if(tipo =="ano"){
			return true;	
		}
		return true;
	}
	
}
