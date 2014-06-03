package core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;

public class Login extends Database{
	public boolean signUp( String email, String pass, String key, String name, String phone ) throws SQLException{
		initializeDB();
		int val = makePstmtUpdate("INSERT INTO arsee_manager_datas (`email` ,`passwd` ,`name` ,`phone`) VALUES ( ?,  ?,  ?,  ? )", email, pass, name, phone);
		if(val == 0){
			return false;
		}
		return true;
	}
	
	public boolean loginEmail(String email) throws SQLException{
		initializeDB();
		ResultSet rs = makePstmtExecute("SELECT id FROM arsee_manager_datas WHERE email = ?", email);
		rs.last();
		if(rs.getRow()>0){
			return false;
		}
		return true;
	}

	public boolean loginManager( String email, String pass ) throws SQLException{
		initializeDB();
		ResultSet rs = makePstmtExecute("SELECT id FROM arsee_manager_datas WHERE email = ? AND passwd = ?", email, pass);
		rs.last();
		if(rs.getRow()>0){
			return true;
		}
		return false;
	}
	
	public void logoutManager(HttpServletRequest request, String ...name){
		for(int i = 0 ; i < name.length ; i++){
			request.removeAttribute(name[i]);			
		}
	}
	
	public void setSession(HttpServletRequest request, String name, String values){
		HttpSession session = request.getSession(false);
		session.setAttribute(name, values);
	}
	
	public boolean islogin(HttpServletRequest request){
		HttpSession session = request.getSession(false);
		if (session == null) {
			return false;
		} else {
			return true;
		}
	}
}
