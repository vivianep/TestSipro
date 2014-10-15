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
			return this.required(object);
		}
		else if(tipo =="nome"){
			String nome = (String) object;
			if (object == null || object.equals(""))
				return false;
			return true;
		}
		else if(tipo == "prontuario"){
			String prontuario=(String)object;
			for (int i = 0; i < prontuario.length(); i++) {
				if(prontuario.codePointAt(i)<48||prontuario.codePointAt(i)>=58)
					return false;
			}
			return true;
		}
		else if(tipo =="sexo"){
			String nome = (String) object;
			if (object == null || object.equals(""))
				return false;
			return true;
		}
		else if(tipo =="telefone"){
			String tel=(String)object;
			for (int i = 0; i < tel.length(); i++) {
				if(tel.codePointAt(i)<48||tel.codePointAt(i)>=58)
					return false;
			}
			return true;
		}
		else if(tipo =="dia"){
			int d = (Integer) object;
			return range(d,1,31);
		}
		else if(tipo =="mes"){
			int m = (Integer) object;
			return range(m,1,12);
		}
		else if(tipo =="ano"){
			int a = (Integer) object;
			return range(a,1900,2100);     
		}
		return true;
	}

}
