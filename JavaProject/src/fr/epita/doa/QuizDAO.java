package fr.epita.doa;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import fr.epita.classes.Quiz;
import fr.epita.services.filereader.Reader;

public class QuizDAO {
	Reader r = new Reader();
	String database = r.getDatabase();
	String username = r.getUsername();
	String password = r.getPassword();
	public int getQuizId(Quiz quiz){
		int id = 0;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = DriverManager.getConnection(database, username, password);
			String str = "SELECT id FROM public.\"Quizes\" where title = ?;";
		
			preparedStatement = connection.prepareStatement(str);
		
			preparedStatement.setString(1, quiz.getTitle());
			
			ResultSet rs = preparedStatement.executeQuery();
			
			while (rs.next()) {
				id = rs.getInt("id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(preparedStatement!=null) {
				try {
					preparedStatement.close();
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
	public String getTitleById(int id){

		String title = "";
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(database, username, password);
			String str = "SELECT title FROM public.\"Quizes\" where id = ?;";
		
			preparedStatement = connection.prepareStatement(str);
		
			preparedStatement.setInt(1, id);
			
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				title = rs.getString("title");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(preparedStatement!=null) {
				try {
					preparedStatement.close();
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
		
	
		return title;
	}

	
	
	public ArrayList<Quiz> getAllQuizes(){
		ArrayList<Quiz> out = new ArrayList<>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = DriverManager.getConnection(database, username, password);
			String str = "SELECT * FROM public.\"Quizes\";";
		
			preparedStatement = connection.prepareStatement(str);

			
			ResultSet rs = preparedStatement.executeQuery();
			String title = "";
			int id = 0;
			while (rs.next()) {
				title = rs.getString("title");
				id = rs.getInt("id");
				out.add(new Quiz(id,title));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(preparedStatement!=null) {
				try {
					preparedStatement.close();
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
	
	public void createQuiz(Quiz quiz) throws IOException{
		if(quiz.getTitle().equalsIgnoreCase("") || quiz.getTitle().equalsIgnoreCase(" ")) {
			throw new IOException("Title cannot be empty");
		}
		else if(getQuizId(quiz) != 0){
			throw new IOException("Quiz already exists");
		}
		else {
			PreparedStatement insertingStatement = null;
			Connection connection = null;
			try {
				connection = DriverManager.getConnection(database, username, password);
				
				String inserting = "INSERT INTO public.\"Quizes\"(title)VALUES (?);";		
				insertingStatement = connection.prepareStatement(inserting);
				insertingStatement.setString(1, quiz.getTitle());
				insertingStatement.execute();
				System.out.println("Quiz Created");
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
	}
	public void deleteQuiz(Quiz quiz) throws IOException{
		int id = getQuizId(quiz);
		
		// check if quiz exists
		if (id == 0) {

			throw new IOException("Quiz not found");
		}
		// delete Quiz
		else {
			PreparedStatement preparedStatement = null;
			Connection connection = null;
			try {
				connection = DriverManager.getConnection(database, username, password);

				String str = "DELETE FROM public.\"Quizes\" WHERE id = " + id + ";";
				preparedStatement = connection.prepareStatement(str);
				preparedStatement.executeUpdate();
				System.out.println("Quiz Deleted");
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				if(preparedStatement!=null) {
					try {
						preparedStatement.close();
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
	public void updateQuiz(Quiz oldQuiz,Quiz newQuiz) throws IOException{
		int id= getQuizId(oldQuiz);
		if(id == 0) {
			throw new IOException("Quiz doesnt exist");
		}
		else if(newQuiz.getTitle().equalsIgnoreCase("") || newQuiz.getTitle().equalsIgnoreCase(" ")) {
			throw new IOException("Title cannot be empty");
		}
		else if(getQuizId(newQuiz) != 0){
			System.out.println("Quiz already exists");
		}
		else {
			PreparedStatement preparedStatement = null;
			Connection connection = null;
			try {
				connection = DriverManager.getConnection(database, username, password);
				String str = "UPDATE public.\"Quizes\" set title = ? where id =?";
				preparedStatement = connection.prepareStatement(str);
				preparedStatement.setString(1, newQuiz.getTitle());
				preparedStatement.setInt(2, id);
				preparedStatement.execute();
				System.out.println("Quiz updated");
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				if(preparedStatement!=null) {
					try {
						preparedStatement.close();
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
}
