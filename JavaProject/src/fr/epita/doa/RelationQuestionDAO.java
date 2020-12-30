package fr.epita.doa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import fr.epita.classes.Choice;
import fr.epita.classes.FullQuestion;
import fr.epita.classes.Question;
import fr.epita.classes.Topic;

public class RelationQuestionDAO {

	// check if answer in choices
	
	public boolean answerInChoices(FullQuestion full) throws Exception{
		boolean exists = false;
		for (Choice i: full.getChoices()){
			if(i.getChoice().equals(full.getQuestion().getAnswer())) {
				exists = true;
				break;
				
			}
		}
		return exists;
	}
	
	// for creating
	
	public void createFullQuestion(FullQuestion full) throws Exception {
		if(answerInChoices(full)) {
			Question question = full.getQuestion();
			QuestionsDOA qdoa = new QuestionsDOA();
			TopicDAO tdao = new TopicDAO();
			ChoiceDAO cdao = new ChoiceDAO();
			int questionId = qdoa.getID(question.getQuestion());
			if (questionId == 0) {
				System.out.println("Question not found");
				System.out.println("Creating Question");
				qdoa.createQuestion(question);
				questionId = qdoa.getID(question.getQuestion());
			}
			else {
				throw new Exception("Question Already Exists");
			}
			for (Topic i : full.getTopics()) {
				int topicId = tdao.getID(i.getTopic());
				if(topicId == 0) {
					tdao.createTopic(i);
					topicId = tdao.getID(i.getTopic());
				}
				System.out.println("Creating topic relation");
	
				Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java",
						"postgres", "");
	
				String inserting = "INSERT INTO public.\"Relation_Questions_Topics\"(id_question,id_topic) VALUES (?,?);";
				PreparedStatement insertingStatement = connection.prepareStatement(inserting);
				insertingStatement.setInt(1, questionId);
				insertingStatement.setInt(2, topicId);
				insertingStatement.execute();
				connection.close();
				System.out.println("Topic Relation Created");
			}
			for (Choice i : full.getChoices()) {
				int choiceId = cdao.getID(i.getChoice());
				if(choiceId == 0) {
					cdao.createChoice(i);
					choiceId = cdao.getID(i.getChoice());
				}
	
				System.out.println("Creating choice relation");
	
				Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java",
						"postgres", "");
	
				String inserting = "INSERT INTO public.\"Relation_Questions_Choices\"(id_question,id_choice) VALUES (?,?);";
				PreparedStatement insertingStatement = connection.prepareStatement(inserting);
				insertingStatement.setInt(1, questionId);
				insertingStatement.setInt(2, choiceId);
				insertingStatement.execute();
				connection.close();
				System.out.println("Choice Relation Created");
			}
		}
		else {
			throw new Exception("Answer not in choices");
		}
	
	}

	// for removing relation

	public void unRelateQuestionFromAll(Question question) throws Exception {

		QuestionsDOA qdoa = new QuestionsDOA();

		int questionId = qdoa.getID(question.getQuestion());

		ArrayList<Integer> topicIds = getRelationTopic(questionId);
		ArrayList<Integer> choiceIds = getRelationChoice(questionId);
		if (questionId == 0) {
			throw new Exception("Question not found");
		} else {
			if (topicIds.size() > 0) {

				Connection connection = DriverManager
						.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java", "postgres", "");
				for (int i : topicIds) {
					String deleting = "delete from public.\"Relation_Questions_Topics\" where id_topic = ? ;";
					PreparedStatement deletingStatement = connection.prepareStatement(deleting);
					deletingStatement.setInt(1, i);
					deletingStatement.execute();
					System.out.println("Topic Relation Deleted");
				}
				connection.close();
			}
			if (choiceIds.size() > 0) {

				Connection connection = DriverManager
						.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java", "postgres", "");
				for (int i : choiceIds) {
					String deleting = "delete from public.\"Relation_Questions_Choices\" where id_choice = ? ;";
					PreparedStatement deletingStatement = connection.prepareStatement(deleting);
					deletingStatement.setInt(1, i);
					deletingStatement.execute();
					System.out.println("Choice Relation Deleted");
				}
				connection.close();
			}
		}

	}

	public void unRelateTopicFromAll(Topic topic) throws Exception {

		TopicDAO tdoa = new TopicDAO();

		int topicId = tdoa.getID(topic.getTopic());

		ArrayList<Integer> questionIds = getRelationTopicQuestion(topicId);
		if (topicId == 0) {
			throw new Exception("Topic not found");
		} else if (questionIds.size() == 0) {
			System.out.println("Topic not linked to any question");
		} else {

			Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java",
					"postgres", "");
			String deleting = "delete from public.\"Relation_Questions_Topics\" where id_topic = ? ;";
			PreparedStatement deletingStatement = connection.prepareStatement(deleting);
			deletingStatement.setInt(1, topicId);
			deletingStatement.execute();

			connection.close();
			System.out.println("Topic Relation Deleted");
		}

	}

	public void unRelateChoiceFromAll(Choice choice) throws Exception {

		ChoiceDAO cdoa = new ChoiceDAO();

		int choiceId = cdoa.getID(choice.getChoice());

		ArrayList<Integer> questionIds = getRelationChoiceQuestion(choiceId);
		if (choiceId == 0) {
			throw new Exception("Choice not found");
		} else if (questionIds.size() == 0) {
			System.out.println("Choice Not linked to any question");
		} else {

			Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java",
					"postgres", "");
			String deleting = "delete from public.\"Relation_Questions_Choices\" where id_choice = ? ;";
			PreparedStatement deletingStatement = connection.prepareStatement(deleting);

			deletingStatement.setInt(1, choiceId);
			deletingStatement.execute();

			connection.close();
			System.out.println("Choice Relation Deleted");
		}

	}

	public void unRelateTopicFromQuestion(Topic topic,Question question) throws Exception {

		TopicDAO tdoa = new TopicDAO();
		QuestionsDOA qdao = new QuestionsDOA();
		int topicId = tdoa.getID(topic.getTopic());
		int questionId = qdao.getID(question.getQuestion());
		ArrayList<Integer> questionIds = getRelationTopicQuestion(topicId);
		if (topicId == 0) {
			throw new Exception("Topic not found");
		} else if (questionId == 0) {
			System.out.println("Question not found");
		}else if (!questionIds.contains(questionId)) {
			System.out.println("Topic not linked to question");
		} else {

			Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java",
					"postgres", "");
			String deleting = "delete from public.\"Relation_Questions_Topics\" where id_topic = ? and id_question = ?;";
			PreparedStatement deletingStatement = connection.prepareStatement(deleting);
			deletingStatement.setInt(1, topicId);
			deletingStatement.setInt(2, questionId);
			deletingStatement.execute();

			connection.close();
			System.out.println("Topic Relation Deleted");
			
			
		}

	}

	public void unRelateChoiceFromQuestion(Choice choice,Question question) throws Exception {

		ChoiceDAO cdoa = new ChoiceDAO();

		QuestionsDOA qdao = new QuestionsDOA();
		int choiceId = cdoa.getID(choice.getChoice());

		int questionId = qdao.getID(question.getQuestion());
		ArrayList<Integer> questionIds = getRelationChoiceQuestion(choiceId);
		if (choiceId == 0) {
			throw new Exception("Choice not found");
		} else if (questionId == 0) {
			System.out.println("Question not found");
		}else if (!questionIds.contains(questionId)) {
			System.out.println("Choice not linked to question");
		} else {

			Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java",
					"postgres", "");
			String deleting = "delete from public.\"Relation_Questions_Choices\" where id_choice = ? and id_question = ? ;";
			PreparedStatement deletingStatement = connection.prepareStatement(deleting);

			deletingStatement.setInt(1, choiceId);
			deletingStatement.setInt(2, questionId);
			deletingStatement.execute();

			connection.close();
			System.out.println("Choice Relation Deleted");
		}

	}

	
	// for adding relation
	
	public void relateTopicToQuestion(Question question, Topic topic) throws Exception {

		FullQuestion full = getAllRelatedToQuestion(question);
		Topic[] topics = full.getTopics();
		QuestionsDOA qdoa = new QuestionsDOA();
		TopicDAO tdao = new TopicDAO();

		boolean exists = false;
		int questionId = qdoa.getID(question.getQuestion());
		int topicId = tdao.getID(topic.getTopic());

		if (questionId == 0) {
			throw new Exception("Question not found in database");
		} else if (topicId == 0) {
			tdao.createTopic(topic);
			topicId = tdao.getID(topic.getTopic());
		} else {
			for (Topic i : topics) {
				int topicIdInArray = tdao.getID(i.getTopic());
				if (topicIdInArray == topicId) {
					exists = true;
					System.out.println("Topic Relation already exists");
					break;
				}
			}
		}

		if (!exists) {
			System.out.println("Creating topic relation");

			Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java",
					"postgres", "");

			String inserting = "INSERT INTO public.\"Relation_Questions_Topics\"(id_question,id_topic) VALUES (?,?);";
			PreparedStatement insertingStatement = connection.prepareStatement(inserting);
			insertingStatement.setInt(1, questionId);
			insertingStatement.setInt(2, topicId);
			insertingStatement.execute();
			connection.close();
			System.out.println("Topic Relation Created");
		}

	}

	public void relateChoiceToQuestion(Question question, Choice choice) throws Exception {

		FullQuestion full = getAllRelatedToQuestion(question);
		Choice[] choices = full.getChoices();
		QuestionsDOA qdoa = new QuestionsDOA();
		ChoiceDAO cdao = new ChoiceDAO();
		int questionId = qdoa.getID(question.getQuestion());
		int choiceId = cdao.getID(choice.getChoice());
		int choiceAnswerIdFound = cdao.getID(question.getAnswer());

		boolean existsAnswer = false;
		boolean exists = false;
		if (questionId == 0) {
			throw new Exception("Question not found in database");
		} else if (choiceId == 0) {
			cdao.createChoice(choice);
			choiceId = cdao.getID(choice.getChoice());
		} else if(choiceAnswerIdFound == 0) {
			
				throw new Exception(question.getAnswer()+" Answer not found in database");				
			
			
		}else {
			for (Choice i : choices) {
				int topicIdInArray = cdao.getID(i.getChoice());
				if (topicIdInArray == choiceId) {
					exists = true;
					System.out.println("Choice Relation already exists");
				}
				if(i.getChoice() == question.getAnswer())
					existsAnswer = true;
			}
		}


		if(!existsAnswer) {
			throw new Exception("Answer not found in the choices");
		}
		if (!exists) {
			
			
			System.out.println("Creating choice relation");

			Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java",
					"postgres", "");

			String inserting = "INSERT INTO public.\"Relation_Questions_Choices\"(id_question,id_choice) VALUES (?,?);";
			PreparedStatement insertingStatement = connection.prepareStatement(inserting);
			insertingStatement.setInt(1, questionId);
			insertingStatement.setInt(2, choiceId);
			insertingStatement.execute();
			connection.close();
			System.out.println("Choice Relation Created");
		}

	}

	// get all topics and choices related to question
	public FullQuestion getAllRelatedToQuestion(Question quest) throws Exception {
		QuestionsDOA qdao = new QuestionsDOA();
		int question_id = qdao.getID(quest.getQuestion());

		FullQuestion out = new FullQuestion();
		if (question_id == 0) {
			throw new Exception("Question not found");
		} else {
			TopicDAO tdao = new TopicDAO();
			ChoiceDAO cdao = new ChoiceDAO();

			ArrayList<Integer> topicIds = getRelationTopic(question_id);
			ArrayList<Topic> topics = new ArrayList<>();

			for (int i : topicIds) {
				topics.add(new Topic(tdao.getTopic(i), i));
			}
			ArrayList<Integer> choiceIds = getRelationChoice(question_id);
			ArrayList<Choice> choices = new ArrayList<>();
			for (int i : choiceIds) {
				choices.add(new Choice(cdao.getChoice(i),i));
			}

			out = new FullQuestion(quest, topics.toArray(new Topic[topics.size()]),
					choices.toArray(new Choice[choices.size()]));

		}

		return out;
	}

	// get all by topic
	public ArrayList<FullQuestion> getAllRelatedToTopic(Topic topic) throws Exception {
		TopicDAO tdao = new TopicDAO();
		int topic_id = tdao.getID(topic.getTopic());

		ArrayList<Question> questions = new ArrayList<>();
		ArrayList<FullQuestion> out = new ArrayList<>();

		if (topic_id == 0) {
			throw new Exception("Topic not found");
		} else {
			QuestionsDOA qdao = new QuestionsDOA();

			ArrayList<Integer> questionIds = getRelationTopicQuestion(topic_id);

			for (int i : questionIds) {
				Question question = qdao.getQuestion(i);
				if (question.getId() != 0) {
					questions.add(question);
				}

			}

			if (questions.size() == 0) {
				System.out.println("No questions linked to this topic");
			} else {
				for (Question i : questions) {
					out.add(getAllRelatedToQuestion(i));
				}
			}

		}

		return out;
	}
	
	public ArrayList<FullQuestion> getAll() throws Exception{
		ArrayList<FullQuestion> out = new ArrayList<>();
		QuestionsDOA qdao = new QuestionsDOA();
		Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java",
				"postgres", "");
		String query = "SELECT id_question FROM public.\"Relation_Questions_Topics\";";
		PreparedStatement prepareQuestionStatement = connection.prepareStatement(query);
		ResultSet rs = prepareQuestionStatement.executeQuery();
		int id = 0;
		int temp = 0;
		while (rs.next()) {
			id = rs.getInt("id_question");
			temp = id;
			out.add(getAllRelatedToQuestion(qdao.getQuestion(id)));
			break;
		}
		rs = prepareQuestionStatement.executeQuery();
		while (rs.next()) {
			id = rs.getInt("id_question");
			if(temp != id) {
				out.add(getAllRelatedToQuestion(qdao.getQuestion(id)));
				temp = id;
			}

		}

		
		
		
		return out;
	}
	
	// get topic
	public ArrayList<Integer> getRelationTopicQuestion(int topicId) throws SQLException {
		ArrayList<Integer> out = new ArrayList<>();

		Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java",
				"postgres", "");
		String query = "SELECT * FROM public.\"Relation_Questions_Topics\" where id_topic = ?;";
		PreparedStatement prepareQuestionStatement = connection.prepareStatement(query);
		prepareQuestionStatement.setInt(1, topicId);
		ResultSet rs = prepareQuestionStatement.executeQuery();
		int id = 0;
		while (rs.next()) {
			id = rs.getInt("id_question");
			out.add(id);
		}

		return out;
	}

	public ArrayList<Integer> getRelationChoiceQuestion(int choiceId) throws SQLException {
		ArrayList<Integer> out = new ArrayList<>();

		Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java",
				"postgres", "");
		String query = "SELECT * FROM public.\"Relation_Questions_Choices\" where id_choice = ?;";
		PreparedStatement prepareQuestionStatement = connection.prepareStatement(query);
		prepareQuestionStatement.setInt(1, choiceId);
		ResultSet rs = prepareQuestionStatement.executeQuery();
		int id = 0;
		while (rs.next()) {
			id = rs.getInt("id_question");
			out.add(id);
		}

		return out;
	}

	public ArrayList<Integer> getRelationQuestionChoice(int questionId) throws SQLException {
		ArrayList<Integer> out = new ArrayList<>();

		Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java",
				"postgres", "");
		String query = "SELECT * FROM public.\"Relation_Questions_Choices\" where id_question = ?;";
		PreparedStatement prepareQuestionStatement = connection.prepareStatement(query);
		prepareQuestionStatement.setInt(1, questionId);
		ResultSet rs = prepareQuestionStatement.executeQuery();
		int id = 0;
		while (rs.next()) {
			id = rs.getInt("id_choice");
			out.add(id);
		}

		return out;
	}

	public ArrayList<Integer> getRelationTopic(int questionId) throws SQLException {
		ArrayList<Integer> out = new ArrayList<>();

		Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java",
				"postgres", "");
		String query = "SELECT * FROM public.\"Relation_Questions_Topics\" where id_question = ?;";
		PreparedStatement prepareQuestionStatement = connection.prepareStatement(query);
		prepareQuestionStatement.setInt(1, questionId);
		ResultSet rs = prepareQuestionStatement.executeQuery();
		int id = 0;
		while (rs.next()) {
			id = rs.getInt("id_topic");
			out.add(id);
		}

		return out;
	}

	public ArrayList<Integer> getRelationChoice(int questionId) throws SQLException {
		ArrayList<Integer> out = new ArrayList<>();

		Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java",
				"postgres", "");
		String query = "SELECT * FROM public.\"Relation_Questions_Choices\" where id_question = ?;";
		PreparedStatement prepareQuestionStatement = connection.prepareStatement(query);
		prepareQuestionStatement.setInt(1, questionId);
		ResultSet rs = prepareQuestionStatement.executeQuery();
		int id = 0;
		while (rs.next()) {
			id = rs.getInt("id_choice");
			out.add(id);
		}

		return out;
	}

}
