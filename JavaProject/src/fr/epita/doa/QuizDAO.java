package fr.epita.doa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import fr.epita.classes.Quiz;

public class QuizDAO {
	String database = "jdbc:postgresql://localhost:5432/fundementals-of-java";
	String username = "postgres";
	String password = "";
	public int getQuizId(Quiz quiz) throws Exception{
		Connection connection = DriverManager.getConnection(database, username, password);
		String str = "SELECT id FROM public.\"Quizes\" where title = ?;";
	
		PreparedStatement preparedStatement = connection.prepareStatement(str);
	
		preparedStatement.setString(1, quiz.getTitle());
		
		ResultSet rs = preparedStatement.executeQuery();
		int id = 0;
		while (rs.next()) {
			id = rs.getInt("id");
		}
		connection.close();
	
		return id;
	}
	public String getTitleById(int id) throws Exception{
		Connection connection = DriverManager.getConnection(database, username, password);
		String str = "SELECT title FROM public.\"Quizes\" where id = ?;";
	
		PreparedStatement preparedStatement = connection.prepareStatement(str);
	
		preparedStatement.setInt(1, id);
		
		ResultSet rs = preparedStatement.executeQuery();
		String title = "";
		while (rs.next()) {
			title = rs.getString("title");
		}
		connection.close();
	
		return title;
	}

	
	
	public ArrayList<Quiz> getAllQuizes() throws Exception{
		ArrayList<Quiz> out = new ArrayList<>();
		Connection connection = DriverManager.getConnection(database, username, password);
		String str = "SELECT * FROM public.\"Quizes\";";
	
		PreparedStatement preparedStatement = connection.prepareStatement(str);

		
		ResultSet rs = preparedStatement.executeQuery();
		String title = "";
		int id = 0;
		while (rs.next()) {
			title = rs.getString("title");
			id = rs.getInt("id");
			out.add(new Quiz(id,title));
		}
		connection.close();
	
		return out;
	}
	
	public void createQuiz(Quiz quiz) throws Exception{
		if(quiz.getTitle().equalsIgnoreCase("") || quiz.getTitle().equalsIgnoreCase(" ")) {
			throw new Exception("Title cannot be empty");
		}
		else if(getQuizId(quiz) != 0){
			throw new Exception("Quiz already exists");
		}
		else {
			
			Connection connection = DriverManager.getConnection(database, username, password);
			
			String inserting = "INSERT INTO public.\"Quizes\"(title)VALUES (?);";		
			PreparedStatement insertingStatement = connection.prepareStatement(inserting);
			insertingStatement.setString(1, quiz.getTitle());
			insertingStatement.execute();
			System.out.println("Quiz Created");
			connection.close();
		}
	}
	public void deleteQuiz(Quiz quiz) throws Exception{
		int id = getQuizId(quiz);
		
		// check if quiz exists
		if (id == 0) {

			throw new Exception("Quiz not found");
		}
		// delete Quiz
		else {
			Connection connection = DriverManager.getConnection(database, username, password);

			String str = "DELETE FROM public.\"Quizes\" WHERE id = " + id + ";";
			PreparedStatement preparedStatement = connection.prepareStatement(str);
			preparedStatement.executeUpdate();
			connection.close();
			System.out.println("Quiz Deleted");
		}
	}
	public void updateQuiz(Quiz oldQuiz,Quiz newQuiz) throws Exception{
		int id= getQuizId(oldQuiz);
		if(id == 0) {
			throw new Exception("Quiz doesnt exist");
		}
		else if(newQuiz.getTitle().equalsIgnoreCase("") || newQuiz.getTitle().equalsIgnoreCase(" ")) {
			throw new Exception("Title cannot be empty");
		}
		else if(getQuizId(newQuiz) != 0){
			System.out.println("Quiz already exists");
		}
		else {
			Connection connection = DriverManager.getConnection(database, username, password);
			String str = "UPDATE public.\"Quizes\" set title = ? where id =?";
			PreparedStatement insertingStatement = connection.prepareStatement(str);
			insertingStatement.setString(1, newQuiz.getTitle());
			insertingStatement.setInt(2, id);
			insertingStatement.execute();
			connection.close();
			System.out.println("Quiz updated");
			
		}
	}
}
