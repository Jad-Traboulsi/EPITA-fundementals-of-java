package fr.epita.doa;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import fr.epita.classes.Choice;
import fr.epita.services.filereader.Reader;


public class ChoiceDAO{
	Reader r = new Reader();
	String database = r.getDatabase();
	String username = r.getUsername();
	String password = r.getPassword();
	public int getID(String choice){
		int id = 0;
		PreparedStatement prepareStatement = null;
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(database, username, password);
		
			// Searching in choices if the choice already exists
		
			String query = "SELECT id FROM public.\"Choices\" where  lower(choice) = lower(?); ";
			prepareStatement = connection.prepareStatement(query);	
			prepareStatement.setString(1,choice);
			ResultSet rs = prepareStatement.executeQuery();
			
			while (rs.next())
			{
				id = rs.getInt("id");
			}
				
		} catch (Exception e) {

			e.printStackTrace();
		}finally {
			if(prepareStatement!=null) {
				try {
					prepareStatement.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			if(connection!=null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		

		return id;
	}
	public String getChoice(int id){

		String choice2 = "";
		PreparedStatement prepareStatement = null;
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(database, username, password);
			
			// Searching in choices if the choice already exists
			
			String query = "SELECT choice FROM public.\"Choices\" where  id = ?; ";
			prepareStatement = connection.prepareStatement(query);	
			prepareStatement.setInt(1,id);
			ResultSet rs = prepareStatement.executeQuery();
			while (rs.next())
			{
				choice2 = rs.getString("choice");
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(prepareStatement!=null) {
				try {
					prepareStatement.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			if(connection!=null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		

		return choice2;
	}
	
	public void createChoice(Choice choice) throws IOException{

		int id = getID(choice.getChoiceString());
		// if not create choice
		// get choice id add to list
		if(id==0)
		{
			Connection connection = null;
			PreparedStatement insertingStatement = null;
			try {
				connection = DriverManager.getConnection(database, username, password);
			
				String inserting = "INSERT INTO public.\"Choices\"(choice) VALUES (?);";		
				insertingStatement = connection.prepareStatement(inserting);
				insertingStatement.setString(1,choice.getChoiceString());
				insertingStatement.execute();
				System.out.println("Choice Created");
				
			} catch (Exception e) {

				e.printStackTrace();
			}finally {
				if(insertingStatement!=null) {
					try {
						insertingStatement.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				if(connection!=null) {
					try {
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			
		}
		else if(choice.getChoiceString().equals("")) {
			throw new IOException("Choice cannot be empty");
		}
		else
		{
			System.out.println("Choice already exists");
		}
		
	
		
	}	
	public void updateChoice(Choice oldChoice,Choice newChoice)
	{

		int oldId = getID(oldChoice.getChoiceString());
		int newId = getID(newChoice.getChoiceString());
		// no id found
		if (oldId==0)
		{
			System.out.println("Choice not found");
		}
		// checking if choice already exists
		else if (newId != 0)
		{
			System.out.println("New Choice already Exists");
		}
		// check if choice update needed 
		else if(getChoice(getID(oldChoice.getChoiceString())).equalsIgnoreCase(newChoice.getChoiceString().toLowerCase())|| newChoice.getChoiceString().equals(""))
		{
			System.out.println("No Choices Changed");
		}
		// update choice
		else
		{
			Connection connection = null;
			PreparedStatement prepareTopicStatement= null;
			
			try {
				connection = DriverManager.getConnection(database, username, password);

				String toBeUpdatedTopic = "UPDATE public.\"Choices\" SET choice = ? where id ="+oldId+";";
				prepareTopicStatement = connection.prepareStatement(toBeUpdatedTopic);
	
				prepareTopicStatement.setString(1,newChoice.getChoiceString());
				prepareTopicStatement.executeUpdate();
				System.out.println("Choice Updated");
	
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				if(prepareTopicStatement!=null) {
					try {
						prepareTopicStatement.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				if(connection!=null) {
					try {
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			
		}
	
	

	
	}
	public void deleteChoice(Choice choice) throws IOException{

		
		int id = getID(choice.getChoiceString());
		// no id found
		if (id==0)
		{
			throw new IOException("Choice not found");
		}
		// delete choice
		else
		{	
			Connection connection = null;
			PreparedStatement query = null;
			try {
				connection = DriverManager.getConnection(database, username, password);

				String str = "DELETE FROM public.\"Choices\" WHERE id = "+id+";";
				query = connection.prepareStatement(str);
				query.executeUpdate();
				System.out.println("Choice Deleted");
			} catch (Exception e) {

				e.printStackTrace();
			}finally {
				if(query!=null) {
					try {
						query.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				if(connection!=null) {
					try {
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			
		}
	
	}
	public Choice searchChoice(Choice choice){
		Choice out = new Choice();
		int id = getID(choice.getChoiceString());
		out.setId(id);
		out.setChoiceString(getChoice(choice.getId()));
		if(id == 0)
		{
			System.out.println("Choice not found");
		}
		else
		{
			out.setId(id);
			out.setChoiceString(getChoice(id));
		}
		return out;
	}
	public ArrayList<Choice> getAllChoices(){
		ArrayList<Choice> out = new ArrayList<>();
		Connection connection = null;
		PreparedStatement prepareStatement = null;
		try {
			connection = DriverManager.getConnection(database, username, password);
			
			// Searching in choices if the choice already exists
		
			String query = "SELECT * FROM public.\"Choices\" ";
			prepareStatement = connection.prepareStatement(query);	
			ResultSet rs = prepareStatement.executeQuery();
			int id = 0;
			String choice = "";
			
			while (rs.next())
			{
				id = rs.getInt("id");
				choice = rs.getString("choice");
				out.add(new Choice(choice,id));
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(prepareStatement!=null) {
				try {
					prepareStatement.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			if(connection!=null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		

		
		return out;
		
	}

	
}
