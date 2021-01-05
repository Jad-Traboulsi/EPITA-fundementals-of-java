package fr.epita.services.filereader;

import java.io.File;
import java.nio.file.Files;
import java.util.List;


public class Reader {
	private String database;
	private String username;
	private String password;
	
	public Reader(){
		database = assign(0);
		username = assign(1);
		password = assign(2);
					
	}
	
	private String assign(int i){
		String out = "";
		try {
			List<String> listOfLines = Files.readAllLines(new File("DB Configuration.txt").toPath());

			String line = listOfLines.get(i).replace("\"", "");
			String[] l = line.split(" = ");
			
			if(l.length != 1) {
				out = l[1];
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return out;
		
		
	}
	
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
