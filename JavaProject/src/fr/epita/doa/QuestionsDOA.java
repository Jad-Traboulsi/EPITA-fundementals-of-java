package fr.epita.doa;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import fr.epita.classes.Question;
import fr.epita.services.filereader.Reader;

public class QuestionsDOA {
	Reader r = new Reader();
	String database = r.getDatabase();
	String username = r.getUsername();
	String password = r.getPassword();
	public int getID(String questionTitle){
		int id = 0;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = DriverManager.getConnection(database, username, password);
			String str = "SELECT id FROM public.\"Questions\" where lower(question) = lower(?)";

			preparedStatement = connection.prepareStatement(str);
			preparedStatement.setString(1, questionTitle);

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
	public Question getQuestion(int id){

		String quest = "";
		String answer = "";
		int dif = 0;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = DriverManager.getConnection(database, username, password);
			String query = "SELECT * FROM public.\"Questions\" where id = ?;";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, id);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				dif = rs.getInt("difficulty");
				quest = rs.getString("question");
				answer = rs.getString("answer");
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
		

		return new Question(quest, answer, dif, id);
	}

	public ArrayList<Question> selectQuestions(Question question){

		ArrayList<Question> out = new ArrayList<>();
		if (!(question.getAnswer().equals("") && question.getDifficulty() == -1 && question.getQuestionString().equals(""))) {
			Connection connection = null;
			PreparedStatement prepareQuestionStatement = null;
			try {
				connection = DriverManager.getConnection(database, username, password);
				String query = "SELECT * FROM public.\"Questions\" where ";

				// check what values are to be updated
				boolean[] areHere = new boolean[3];
				for (int i = 0; i < 3; i++) {
					areHere[i] = false;
				}
				// check for nulls and adding to query
				if (!question.getAnswer().equals("")) {
					areHere[0] = true;
					query += "lower(answer) = lower(?) and ";
				}
				if (!question.getQuestionString().equals("")) {
					areHere[1] = true;
					query += "lower(question) = lower(?) and ";
				}
				if (question.getDifficulty() != -1) {
					areHere[2] = true;
					query += "difficulty = ? and ";
				}
				query = query.substring(0, query.length() - 4);
				query += ";";
				prepareQuestionStatement = connection.prepareStatement(query);
				int count = 1;
				if (areHere[0]) {
					prepareQuestionStatement.setString(count, question.getAnswer());
					count++;
				}
				if (areHere[1]) {
					prepareQuestionStatement.setString(count, question.getQuestionString());
					count++;
				}
				if (areHere[2]) {
					prepareQuestionStatement.setInt(count, question.getDifficulty());
					
				}

				ResultSet rs = prepareQuestionStatement.executeQuery();

				int id = 0;
				String quest = "";
				String answer = "";
				int dif = 0;
				while (rs.next()) {
					id = rs.getInt("id");
					dif = rs.getInt("difficulty");
					quest = rs.getString("question");
					answer = rs.getString("answer");
					out.add(new Question(quest, answer, dif, id));
				}
			} catch (Exception e) {

				e.printStackTrace();
			}finally {
				if(prepareQuestionStatement!=null) {
					try {
						prepareQuestionStatement.close();
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
		return out;
	}
	public ArrayList<Question> getAllQuestions(){

		ArrayList<Question> out = new ArrayList<>();
		PreparedStatement prepareQuestionStatement = null;
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(database, username, password);
			String query = "SELECT * FROM public.\"Questions\" ";

			prepareQuestionStatement = connection.prepareStatement(query);
			ResultSet rs = prepareQuestionStatement.executeQuery();
			int id = 0;
			String quest = "";
			String answer = "";
			int dif = 0;
			while (rs.next()) {
				id = rs.getInt("id");
				dif = rs.getInt("difficulty");
				quest = rs.getString("question");
				answer = rs.getString("answer");
				out.add(new Question(quest, answer, dif, id));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(prepareQuestionStatement!=null) {
				try {
					prepareQuestionStatement.close();
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

	public void createQuestion(Question questionWanted) throws IOException {

		int id = getID(questionWanted.getQuestionString());

		if (id != 0) {

			System.out.println("Question already exists");
		} else if (questionWanted.getQuestionString().equals("")) {
			throw new IOException("Question cannot be empty");

		} else if (questionWanted.getAnswer().equals("")) {
			throw new IOException("Answer cannot be empty");

		} else if (questionWanted.getDifficulty() == -1) {
			throw new IOException("Difficulty cannot be -1");
		}else {

				// inserting in table question
			Connection connection = null;
			PreparedStatement prepareStatement1 = null;
			try {
				connection = DriverManager.getConnection(database, username, password);
				String query1 = "INSERT INTO public.\"Questions\"(question, answer, difficulty)VALUES (?, ?, ?); ";
				prepareStatement1 = connection.prepareStatement(query1);
				prepareStatement1.setString(1, questionWanted.getQuestionString());
				prepareStatement1.setString(2, questionWanted.getAnswer());
				prepareStatement1.setInt(3, questionWanted.getDifficulty());
				prepareStatement1.execute();
				System.out.println("Question Created");
			}catch (Exception e) {
				e.printStackTrace();
			}finally {
				if(prepareStatement1!=null) {
					try {
						prepareStatement1.close();
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

	public void deleteQuestion(Question questionWanted) throws IOException {

		int id = getID(questionWanted.getQuestionString());

		// check if question exists
		if (id == 0) {

			throw new IOException("Question not found");
		}
		// delete question
		else {
			PreparedStatement preparedStatement = null;
			Connection connection = null;
			try {
				connection = DriverManager.getConnection(database, username, password);

				String str = "DELETE FROM public.\"Questions\" WHERE id = " + id + ";";
				preparedStatement = connection.prepareStatement(str);
				preparedStatement.executeUpdate();
				System.out.println("Question Deleted");
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

	public void updateQuestion(Question question, Question newQuestion){
		if (!question.getQuestionString().equals("")) {
			
			int oldId = getID(question.getQuestionString());
			int newId = getID(newQuestion.getQuestionString());
			// no id found
			if (oldId == 0) {
				System.out.println("Question not found");
			}
			// checking if question already exists
			else if (newId != 0) {
				System.out.println("New Question already Exists");
			}
			// update question
			else {
				PreparedStatement prepareQuestionStatement = null;
				Connection connection = null;
				try {
					connection = DriverManager.getConnection(database, username, password);
					String toBeUpdatedQuestions = "UPDATE public.\"Questions\" set ";

					// check what values are to be updated
					boolean[] areHere = new boolean[3];
					for (int i = 0; i < 3; i++) {
						areHere[i] = false;
					}
					// check for nulls and adding to query
					if (!newQuestion.getAnswer().equals("")) {
						areHere[0] = true;
						toBeUpdatedQuestions += "answer = ?, ";
					}
					if (!newQuestion.getQuestionString().equals("")) {
						areHere[1] = true;
						toBeUpdatedQuestions += "question = ?, ";
					}
					if (newQuestion.getDifficulty() != -1) {
						areHere[2] = true;
						toBeUpdatedQuestions += "difficulty = ?, ";
					}
					toBeUpdatedQuestions = toBeUpdatedQuestions.substring(0, toBeUpdatedQuestions.length() - 2);
					toBeUpdatedQuestions = toBeUpdatedQuestions + " where id = " + oldId + ";";
					prepareQuestionStatement = connection.prepareStatement(toBeUpdatedQuestions);
					int count = 1;
					if (areHere[0]) {
						prepareQuestionStatement.setString(count, newQuestion.getAnswer());
						count++;
					}
					if (areHere[1]) {
						prepareQuestionStatement.setString(count, newQuestion.getQuestionString());
						count++;
					}
					if (areHere[2]) {
						prepareQuestionStatement.setInt(count, newQuestion.getDifficulty());
					}

					prepareQuestionStatement.execute();

					System.out.println("Question Updated");
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
					if(prepareQuestionStatement!=null) {
						try {
							prepareQuestionStatement.close();
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

		} else {
			System.out.println("Value to be looked upon should have a question");
		}
	}

}
