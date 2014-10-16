package ControllerTests;
import play.data.validation.Validation;


public class ValidationTest {

	private boolean hasErrors;
	public ValidationTest(){
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

	public boolean range ( Object param,int min, int max){
		if(param!=null){
			if((Integer)param>=min&&(Integer)param<=max){
				return true;
			}
		}
		hasErrors=true;
		return false;
	}

	public boolean required(String tipo, Object object){
		if(tipo =="crm"){
			return this.required(object);
		}
		else if(tipo =="nome"){
			return this.required(object);
		}
		else if(tipo == "prontuario"){
			return this.required(object);
		}
		else if(tipo =="sexo"){
			String nome = (String) object;
			if (nome =="m" || nome=="f")
				return true;
			hasErrors= true;
			return false;
		}
		else if(tipo =="telefone"){
			return this.required(object);
		}
		else if(tipo =="dia"){
			if(this.required(object))
				return range((Integer) object,1,31);
			return false;
		}
		else if(tipo =="mes"){
			if(this.required(object))
				return range((Integer) object,1,12);
			return false;
		}
		else if(tipo =="ano"){
			if(this.required(object))
				return range((Integer) object,1900,2100);
			return false;     
		}
		return true;
	}

}
