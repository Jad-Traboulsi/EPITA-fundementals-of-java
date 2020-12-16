package fr.epita.doa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import fr.epita.classes.Topic;


public class TopicDAO {
	public int getID(String topic) throws SQLException{


		Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java", "postgres", "");
		
		// Searching in topics if the topic already exists
	
		String query = "SELECT id FROM public.\"Topics\" where  lower(topic) = lower(?); ";
		PreparedStatement prepareStatement = connection.prepareStatement(query);	
		prepareStatement.setString(1,topic);
		ResultSet rs = prepareStatement.executeQuery();
		int id = 0;
		while (rs.next())
		{
			id = rs.getInt("id");
		}
			
		connection.close();

		return id;
	}
	public String getTopic(int id) throws SQLException{


		Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java", "postgres", "");
		
		// Searching in topics if the topic already exists
		
		String query = "SELECT topic FROM public.\"Topics\" where  id = ?; ";
		PreparedStatement prepareStatement = connection.prepareStatement(query);	
		prepareStatement.setInt(1,id);
		ResultSet rs = prepareStatement.executeQuery();
		String topic2 = "";
		while (rs.next())
		{
			topic2 = rs.getString("topic");
		}
			
		connection.close();

		return topic2;
	}
	
	public void createTopic(Topic topic) throws Exception{

		int id = getID(topic.getTopic());
		// if not create topic
		// get topic id add to list
		if(id==0)
		{

			Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java", "postgres", "");
			
			String inserting = "INSERT INTO public.\"Topics\"(topic) VALUES (?);";		
			PreparedStatement insertingStatement = connection.prepareStatement(inserting);
			insertingStatement.setString(1,topic.getTopic());
			insertingStatement.execute();
			System.out.println("Topic Created");
			connection.close();
		}
		else if(topic.getTopic() == "") {
			throw new Exception("Topic cannot be empty");
		}
		else
		{
			System.out.println("Topic already exists");
		}
		
	
		
	}	
	public void updateTopic(Topic oldTopic,Topic newTopic) throws SQLException
	{

		
		int newId = getID(newTopic.getTopic());
		int id = getID(oldTopic.getTopic());
		// no id found
		if (id==0)
		{
			System.out.println("Topic not found");
		}
		// if new topic already exists
		else if (newId != 0)
		{
			System.out.println("New Topic Already Exists");
		}
		// check if topic update needed 
		else if(getTopic(getID(oldTopic.getTopic())).toLowerCase().equals(newTopic.getTopic().toLowerCase())|| newTopic.getTopic().equals(""))
		{
			System.out.println("No Topics Changed");
		}
		// update topic
		else
		{
			Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java", "postgres", "");

			String toBeUpdatedTopic = "UPDATE public.\"Topics\" SET topic = ? where id ="+id+";";
			PreparedStatement prepareTopicStatement = connection.prepareStatement(toBeUpdatedTopic);

			prepareTopicStatement.setString(1,newTopic.getTopic());
			prepareTopicStatement.executeUpdate();
			System.out.println("Topic Updated");

			connection.close();
		}
	
	

	
	}
	public void deleteTopic(Topic topic) throws Exception{

		
		int id = getID(topic.getTopic());
		// no id found
		if (id==0)
		{
			throw new Exception("Topic not found");
		}
		// delete topic
		else
		{
			Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java", "postgres", "");

			String toBeUpdatedTopic = "DELETE FROM public.\"Topics\" WHERE id = "+id+";";
			PreparedStatement prepareTopicStatement = connection.prepareStatement(toBeUpdatedTopic);
			prepareTopicStatement.executeUpdate();
			System.out.println("Topic Deleted");

			connection.close();
		}
	
	}
	public Topic searchTopic(Topic topic) throws Exception{
		Topic out = new Topic();
		int id = getID(topic.getTopic());
		if(id == 0)
		{
			throw new Exception("Topic not found");
		}
		else {

			out.setId(id);
			out.setTopic(getTopic(id));
		}
		return out;
	}
	public ArrayList<Topic> getAllTopics() throws SQLException{
		ArrayList<Topic> out = new ArrayList<>();
		


		Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java", "postgres", "");
		
		// Searching in topics if the topic already exists
	
		String query = "SELECT * FROM public.\"Topics\" ";
		PreparedStatement prepareStatement = connection.prepareStatement(query);	
		ResultSet rs = prepareStatement.executeQuery();
		int id = 0;
		String topic = "";
		
		while (rs.next())
		{
			id = rs.getInt("id");
			topic = rs.getString("topic");
			out.add(new Topic(topic,id));
		}
			
		connection.close();

		
		return out;
		
	}
}
