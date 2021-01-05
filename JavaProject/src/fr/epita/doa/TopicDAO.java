package fr.epita.doa;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import fr.epita.classes.Topic;
import fr.epita.services.filereader.Reader;


public class TopicDAO {
	Reader r = new Reader();
	String database = r.getDatabase();
	String username = r.getUsername();
	String password = r.getPassword();
	
	public int getID(String topic){
		int id = 0;
		PreparedStatement prepareStatement = null;
		Connection connection = null;
		try {

			connection = DriverManager.getConnection(database, username, password);
			// Searching in topics if the topic already exists
		
			String query = "SELECT id FROM public.\"Topics\" where  lower(topic) = lower(?); ";
			prepareStatement = connection.prepareStatement(query);	
			prepareStatement.setString(1,topic);
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
	public String getTopic(int id){

		String topic2 = "";
		Connection connection = null;
		PreparedStatement prepareStatement = null;
		try {
			connection = DriverManager.getConnection(database, username, password);
			// Searching in topics if the topic already exists
			
			String query = "SELECT topic FROM public.\"Topics\" where  id = ?; ";
			prepareStatement = connection.prepareStatement(query);	
			prepareStatement.setInt(1,id);
			ResultSet rs = prepareStatement.executeQuery();
			while (rs.next())
			{
				topic2 = rs.getString("topic");
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
		

		return topic2;
	}
	
	public void createTopic(Topic topic) throws IOException{

		int id = getID(topic.getTopicString());
		// if not create topic
		// get topic id add to list
		if(id==0)
		{
			PreparedStatement insertingStatement = null;
			Connection connection = null;
			try {
				connection = DriverManager.getConnection(database, username, password);
				String inserting = "INSERT INTO public.\"Topics\"(topic) VALUES (?);";		
				insertingStatement = connection.prepareStatement(inserting);
				insertingStatement.setString(1,topic.getTopicString());
				insertingStatement.execute();
				System.out.println("Topic Created");
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
		else if(topic.getTopicString().equals("")) {
			throw new IOException("Topic cannot be empty");
		}
		else
		{
			System.out.println("Topic already exists");
		}
		
	
		
	}	
	public void updateTopic(Topic oldTopic,Topic newTopic)
	{

		
		int newId = getID(newTopic.getTopicString());
		int id = getID(oldTopic.getTopicString());
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
		else if(getTopic(getID(oldTopic.getTopicString())).equalsIgnoreCase(newTopic.getTopicString())|| newTopic.getTopicString().equals(""))
		{
			System.out.println("No Topics Changed");
		}
		// update topic
		else
		{
			PreparedStatement prepareTopicStatement = null;
			Connection connection = null;
			try {
				connection = DriverManager.getConnection(database, username, password);
				String toBeUpdatedTopic = "UPDATE public.\"Topics\" SET topic = ? where id ="+id+";";
				prepareTopicStatement = connection.prepareStatement(toBeUpdatedTopic);

				prepareTopicStatement.setString(1,newTopic.getTopicString());
				prepareTopicStatement.executeUpdate();
				System.out.println("Topic Updated");

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
	public void deleteTopic(Topic topic) throws IOException{

		
		int id = getID(topic.getTopicString());
		// no id found
		if (id==0)
		{
			throw new IOException("Topic not found");
		}
		// delete topic
		else
		{
			Connection connection = null;
			PreparedStatement prepareTopicStatement = null;
			try {
				connection = DriverManager.getConnection(database, username, password);
				String toBeUpdatedTopic = "DELETE FROM public.\"Topics\" WHERE id = "+id+";";
				prepareTopicStatement = connection.prepareStatement(toBeUpdatedTopic);
				prepareTopicStatement.executeUpdate();
				System.out.println("Topic Deleted");
	
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
	public Topic searchTopic(Topic topic) throws IOException{
		Topic out = new Topic();
		int id = getID(topic.getTopicString());
		if(id == 0)
		{
			throw new IOException("Topic not found");
		}
		else {

			out.setId(id);
			out.setTopicString(getTopic(id));
		}
		return out;
	}
	public ArrayList<Topic> getAllTopics(){
		ArrayList<Topic> out = new ArrayList<>();
		
		Connection connection = null;
		PreparedStatement prepareStatement = null;
		try {
			connection = DriverManager.getConnection(database, username, password);
			// Searching in topics if the topic already exists
		
			String query = "SELECT * FROM public.\"Topics\" ";
			prepareStatement = connection.prepareStatement(query);	
			ResultSet rs = prepareStatement.executeQuery();
			int id = 0;
			String topic = "";
			
			while (rs.next())
			{
				id = rs.getInt("id");
				topic = rs.getString("topic");
				out.add(new Topic(topic,id));
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
