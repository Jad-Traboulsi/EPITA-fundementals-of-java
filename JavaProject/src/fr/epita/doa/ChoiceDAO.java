package fr.epita.doa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import fr.epita.classes.Choice;


public class ChoiceDAO {

	public int getID(String choice) throws SQLException{


		Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java", "postgres", "");
		
		// Searching in choices if the choice already exists
	
		String query = "SELECT id FROM public.\"Choices\" where  lower(choice) = lower(?); ";
		PreparedStatement prepareStatement = connection.prepareStatement(query);	
		prepareStatement.setString(1,choice);
		ResultSet rs = prepareStatement.executeQuery();
		int id = 0;
		while (rs.next())
		{
			id = rs.getInt("id");
		}
			
		connection.close();

		return id;
	}
	public String getChoice(int id) throws SQLException{


		Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java", "postgres", "");
		
		// Searching in choices if the choice already exists
		
		String query = "SELECT choice FROM public.\"Choices\" where  id = ?; ";
		PreparedStatement prepareStatement = connection.prepareStatement(query);	
		prepareStatement.setInt(1,id);
		ResultSet rs = prepareStatement.executeQuery();
		String choice2 = "";
		while (rs.next())
		{
			choice2 = rs.getString("choice");
		}
			
		connection.close();

		return choice2;
	}
	
	public void createChoice(Choice choice) throws Exception{

		int id = getID(choice.getChoice());
		// if not create choice
		// get choice id add to list
		if(id==0)
		{

			Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java", "postgres", "");
			
			String inserting = "INSERT INTO public.\"Choices\"(choice) VALUES (?);";		
			PreparedStatement insertingStatement = connection.prepareStatement(inserting);
			insertingStatement.setString(1,choice.getChoice());
			insertingStatement.execute();
			System.out.println("Choice Created");
			connection.close();
		}
		else if(choice.getChoice() == "") {
			throw new Exception("Choice cannot be empty");
		}
		else
		{
			System.out.println("Choice already exists");
		}
		
	
		
	}	
	public void updateChoice(Choice oldChoice,Choice newChoice) throws SQLException
	{

		int oldId = getID(oldChoice.getChoice());
		int newId = getID(newChoice.getChoice());
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
		else if(getChoice(getID(oldChoice.getChoice())).toLowerCase().equals(newChoice.getChoice().toLowerCase())|| newChoice.getChoice().equals(""))
		{
			System.out.println("No Choices Changed");
		}
		// update choice
		else
		{
			Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java", "postgres", "");

			String toBeUpdatedTopic = "UPDATE public.\"Choices\" SET choice = ? where id ="+oldId+";";
			PreparedStatement prepareTopicStatement = connection.prepareStatement(toBeUpdatedTopic);

			prepareTopicStatement.setString(1,newChoice.getChoice());
			prepareTopicStatement.executeUpdate();
			System.out.println("Choice Updated");

			connection.close();
		}
	
	

	
	}
	public void deleteChoice(Choice choice) throws Exception{

		
		int id = getID(choice.getChoice());
		// no id found
		if (id==0)
		{
			throw new Exception("Choice not found");
		}
		// delete choice
		else
		{
			Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java", "postgres", "");

			String toBeUpdatedTopic = "DELETE FROM public.\"Choices\" WHERE id = "+id+";";
			PreparedStatement prepareTopicStatement = connection.prepareStatement(toBeUpdatedTopic);
			prepareTopicStatement.executeUpdate();
			connection.close();
			System.out.println("Choice Deleted");
		}
	
	}
	public Choice searchChoice(Choice choice) throws SQLException{
		Choice out = new Choice();
		int id = getID(choice.getChoice());
		out.setId(id);
		out.setChoice(getChoice(choice.getId()));
		if(id == 0)
		{
			System.out.println("Choice not found");
		}
		else
		{
			out.setId(id);
			out.setChoice(getChoice(id));
		}
		return out;
	}
	public ArrayList<Choice> getAllChoices() throws SQLException{
		ArrayList<Choice> out = new ArrayList<>();
		


		Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java", "postgres", "");
		
		// Searching in choices if the choice already exists
	
		String query = "SELECT * FROM public.\"Choices\" ";
		PreparedStatement prepareStatement = connection.prepareStatement(query);	
		ResultSet rs = prepareStatement.executeQuery();
		int id = 0;
		String choice = "";
		
		while (rs.next())
		{
			id = rs.getInt("id");
			choice = rs.getString("choice");
			out.add(new Choice(choice,id));
		}
			
		connection.close();

		
		return out;
		
	}

	
}
