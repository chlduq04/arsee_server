package core;

import java.util.regex.Pattern;

public class CheckValidation {
	public boolean checkValidate(String value, int min_length, int max_length, String ...regex){
		if(value.length() > min_length && value.length() < max_length){
			for(int i=0 ; i<regex.length ; i++){
				if(!Pattern.matches(regex[i], value)){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	public boolean checkValidateLength(String value, int min_length, int max_length){

		if(min_length != 0 && value.length() < min_length){
			return false;
		}
		if(max_length !=0 && value.length() > max_length){
			return false;
		}
		return true;
	}

	
	public boolean checkValidateEmail( String value, int min_length, int max_length ){
		return checkValidate(value, min_length, max_length, "^[a-z0-9A-Z_-]*@[a-z0-9A-Z]*.[a-zA-Z.]*$");
	}

	public boolean checkValidatePhone( String value, int min_length, int max_length ){
		return checkValidate(value, min_length, max_length, "^0\\d{2}");
	}

	public boolean checkValidateResidentNumber( String value, int min_length, int max_length ){
		return checkValidate(value, min_length, max_length, "^\\d{6}(1|2|3|4)");
	}

	public boolean checkValidateIp( String value, int min_length, int max_length ){
		return checkValidate(value, min_length, max_length, "^\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}$");
	}

	public boolean checkValidateFileImage( String value, int min_length, int max_length ){
		return checkValidate(value, min_length, max_length, "^\\S+.(?i)(png|jpg|bmp|gif)$");
	}

	public boolean checkValidatePassword( String value, int min_length, int max_length ){
		return checkValidate(value, min_length, max_length, "^[a-zA-Z0-9~!@#$%^&*()]{8,16}");
	}

	public boolean checkValidateKorean( String value, int min_length, int max_length ){
		return checkValidate(value, min_length, max_length, "^[가-힣]*$");
	}

	public boolean checkValidateOnlyNumber( String value, int min_length, int max_length ){
		return checkValidate(value, min_length, max_length, "\\d");
	}

	public boolean checkValidateOnlyChar( String value, int min_length, int max_length ){
		return checkValidate(value, min_length, max_length, "\\D");
	}

}
