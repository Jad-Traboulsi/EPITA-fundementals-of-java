package fr.epita.doa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import fr.epita.classes.Question;

public class QuestionsDOA {

	public int getID(String questionTitle) throws Exception {
		Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java",
				"postgres", "");
		String toBeUpdatedQuestions = "SELECT id FROM public.\"Questions\" where lower(question) = lower(?)";

		PreparedStatement prepareQuestionStatement = connection.prepareStatement(toBeUpdatedQuestions);
		prepareQuestionStatement.setString(1, questionTitle);

		ResultSet rs = prepareQuestionStatement.executeQuery();
		int id = 0;
		while (rs.next()) {
			id = rs.getInt("id");
		}
		connection.close();

		return id;
	}
	public Question getQuestion(int id) throws Exception {
		Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java",
				"postgres", "");
		String query = "SELECT * FROM public.\"Questions\" where id = ?;";
		PreparedStatement prepareQuestionStatement = connection.prepareStatement(query);
		prepareQuestionStatement.setInt(1, id);
		ResultSet rs = prepareQuestionStatement.executeQuery();

		String quest = "";
		String answer = "";
		int dif = 0;
		while (rs.next()) {
			dif = rs.getInt("difficulty");
			quest = rs.getString("question");
			answer = rs.getString("answer");
		}

		return new Question(quest, answer, dif, id);
	}

	public ArrayList<Question> selectQuestions(Question question) throws SQLException {

		ArrayList<Question> out = new ArrayList<>();
		if (!(question.getAnswer() == "" && question.getDifficulty() == -1 && question.getQuestion() == "")) {
			Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java",
					"postgres", "");
			String toBeUpdatedQuestions = "SELECT * FROM public.\"Questions\" where ";

			// check what values are to be updated
			boolean[] areHere = new boolean[3];
			for (int i = 0; i < 3; i++) {
				areHere[i] = false;
			}
			// check for nulls and adding to query
			if (question.getAnswer() != "") {
				areHere[0] = true;
				toBeUpdatedQuestions += "lower(answer) = lower(?) and ";
			}
			if (question.getQuestion() != "") {
				areHere[1] = true;
				toBeUpdatedQuestions += "lower(question) = lower(?) and ";
			}
			if (question.getDifficulty() != -1) {
				areHere[2] = true;
				toBeUpdatedQuestions += "difficulty = ? and ";
			}
			toBeUpdatedQuestions = toBeUpdatedQuestions.substring(0, toBeUpdatedQuestions.length() - 4);
			toBeUpdatedQuestions += ";";
			PreparedStatement prepareQuestionStatement = connection.prepareStatement(toBeUpdatedQuestions);
			int count = 1;
			if (areHere[0]) {
				prepareQuestionStatement.setString(count, question.getAnswer());
				count++;
			}
			if (areHere[1]) {
				prepareQuestionStatement.setString(count, question.getQuestion());
				count++;
			}
			if (areHere[2]) {
				prepareQuestionStatement.setInt(count, question.getDifficulty());
				count++;
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
			connection.close();
		}
		return out;
	}
	public ArrayList<Question> getAllQuestions() throws SQLException {

		ArrayList<Question> out = new ArrayList<>();

		Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java",
				"postgres", "");
		String toBeUpdatedQuestions = "SELECT * FROM public.\"Questions\" ";

		PreparedStatement prepareQuestionStatement = connection.prepareStatement(toBeUpdatedQuestions);
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
		connection.close();

		return out;
	}

	public void createQuestion(Question questionWanted) throws Exception {

		int id = getID(questionWanted.getQuestion());
		RelationQuestionDAO qdao = new RelationQuestionDAO();

		ChoiceDAO cdao = new ChoiceDAO();
		if (id != 0) {

			System.out.println("Question already exists");
		} else if (questionWanted.getQuestion() == "") {
			throw new Exception("Question cannot be empty");

		} else if (questionWanted.getAnswer() == "") {
			throw new Exception("Answer cannot be empty");

		} else if (questionWanted.getDifficulty() == -1) {
			throw new Exception("Difficulty cannot be -1");
		}else {

				// inserting in table question
				Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java",
						"postgres", "");
				String query1 = "INSERT INTO public.\"Questions\"(question, answer, difficulty)VALUES (?, ?, ?); ";
				PreparedStatement prepareStatement1 = connection.prepareStatement(query1);
				prepareStatement1.setString(1, questionWanted.getQuestion());
				prepareStatement1.setString(2, questionWanted.getAnswer());
				prepareStatement1.setInt(3, questionWanted.getDifficulty());
				prepareStatement1.execute();
				connection.close();
	
				System.out.println("Question Created");
		}
		

	}

	public void deleteQuestion(Question questionWanted) throws Exception {

		int id = getID(questionWanted.getQuestion());

		// check if question exists
		if (id == 0) {

			throw new Exception("Question not found");
		}
		// delete question
		else {
			Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java",
					"postgres", "");

			String toBeUpdatedTopic = "DELETE FROM public.\"Questions\" WHERE id = " + id + ";";
			PreparedStatement prepareTopicStatement = connection.prepareStatement(toBeUpdatedTopic);
			prepareTopicStatement.executeUpdate();
			connection.close();
			System.out.println("Question Deleted");
		}
	}

	public void updateQuestion(String question, Question newQuestion) throws Exception {
		if (question != "") {
			int oldId = getID(question);
			int newId = getID(newQuestion.getQuestion());
			Question oldQuestion = new Question();
			if (oldId != 0) {
				oldQuestion = getQuestion(oldId);
			}
			// no id found
			if (oldId == 0) {
				System.out.println("Question not found");
			}
			// checking if question already exists
			else if (newId != 0) {
				System.out.println("New Question already Exists");
			}
			// check if choice update needed
			else if ((oldQuestion.getAnswer().toLowerCase().equals(newQuestion.getAnswer().toLowerCase())
					|| newQuestion.getQuestion().equals(""))
					&& (oldQuestion.getQuestion().toLowerCase().equals(newQuestion.getQuestion().toLowerCase())
							|| newQuestion.getQuestion().equals(""))
					&& (oldQuestion.getDifficulty() == newQuestion.getDifficulty()
							|| newQuestion.getDifficulty() == -1)) {
				System.out.println("No Question Changed");
			}
			// update choice
			else {

				Connection connection = DriverManager
						.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java", "postgres", "");
				String toBeUpdatedQuestions = "UPDATE FROM public.\"Questions\" set ";

				// check what values are to be updated
				boolean[] areHere = new boolean[3];
				for (int i = 0; i < 3; i++) {
					areHere[i] = false;
				}
				// check for nulls and adding to query
				if (newQuestion.getAnswer() != "") {
					areHere[0] = true;
					toBeUpdatedQuestions += "answer = ?, ";
				}
				if (newQuestion.getQuestion() != "") {
					areHere[1] = true;
					toBeUpdatedQuestions += "question = ?, ";
				}
				if (newQuestion.getDifficulty() != -1) {
					areHere[2] = true;
					toBeUpdatedQuestions += "difficulty = ?, ";
				}
				toBeUpdatedQuestions = toBeUpdatedQuestions.substring(0, toBeUpdatedQuestions.length() - 2);
				toBeUpdatedQuestions = toBeUpdatedQuestions + " where id = " + oldId + ";";
				PreparedStatement prepareQuestionStatement = connection.prepareStatement(toBeUpdatedQuestions);
				int count = 1;
				if (areHere[0]) {
					prepareQuestionStatement.setString(count, newQuestion.getAnswer());
					count++;
				}
				if (areHere[1]) {
					prepareQuestionStatement.setString(count, newQuestion.getQuestion());
					count++;
				}
				if (areHere[2]) {
					prepareQuestionStatement.setInt(count, newQuestion.getDifficulty());
					count++;
				}

				System.out.println(prepareQuestionStatement);
				// prepareQuestionStatement.execute();
				connection.close();

				System.out.println("Question Updated");
			}

		} else {
			System.out.println("Value to be looked upon should have a question");
		}
	}

}
