package fr.epita.doa;

import java.io.IOException;
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
import fr.epita.services.filereader.Reader;

public class RelationQuestionDAO {
	Reader r = new Reader();
	String database = r.getDatabase();
	String username = r.getUsername();
	String password = r.getPassword();
	// check if answer in choices
	
	public boolean answerInChoices(FullQuestion full){
		boolean exists = false;
		for (Choice i: full.getChoices()){
			if(i.getChoiceString().equals(full.getQuestion().getAnswer())) {
				exists = true;
				break;
				
			}
		}
		return exists;
	}
	
	// for creating
	
	public void createFullQuestion(FullQuestion full) throws IOException {
		if(answerInChoices(full)) {
			Question question = full.getQuestion();
			QuestionsDOA qdoa = new QuestionsDOA();
			TopicDAO tdao = new TopicDAO();
			ChoiceDAO cdao = new ChoiceDAO();
			int questionId = qdoa.getID(question.getQuestionString());
			if (questionId == 0) {
				System.out.println("Question not found");
				System.out.println("Creating Question");
				qdoa.createQuestion(question);
				questionId = qdoa.getID(question.getQuestionString());
			}
			else {
				throw new IOException("Question Already Exists");
			}
			for (Topic i : full.getTopics()) {
				int topicId = tdao.getID(i.getTopicString());
				if(topicId == 0) {
					tdao.createTopic(i);
					topicId = tdao.getID(i.getTopicString());
				}
				
				PreparedStatement preparedStatement = null;
				Connection connection = null;
				try {
					System.out.println("Creating topic relation");
					connection = DriverManager.getConnection(database, username, password);
					
					String inserting = "INSERT INTO public.\"Relation_Questions_Topics\"(id_question,id_topic) VALUES (?,?);";
					preparedStatement = connection.prepareStatement(inserting);
					preparedStatement.setInt(1, questionId);
					preparedStatement.setInt(2, topicId);
					preparedStatement.execute();
					System.out.println("Topic Relation Created");
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
			for (Choice i : full.getChoices()) {
				int choiceId = cdao.getID(i.getChoiceString());
				if(choiceId == 0) {
					cdao.createChoice(i);
					choiceId = cdao.getID(i.getChoiceString());
				}
	
				PreparedStatement preparedStatement = null;
				Connection connection = null;
				try {
					System.out.println("Creating choice relation");
					connection = DriverManager.getConnection(database, username, password);
					
					String inserting = "INSERT INTO public.\"Relation_Questions_Choices\"(id_question,id_choice) VALUES (?,?);";
					preparedStatement = connection.prepareStatement(inserting);
					preparedStatement.setInt(1, questionId);
					preparedStatement.setInt(2, choiceId);
					preparedStatement.execute();
					System.out.println("Choice Relation Created");
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
		else {
			throw new IOException("Answer not in choices");
		}
	
	}

	// for removing relation

	public void unRelateQuestionFromAll(Question question) throws IOException {

		QuestionsDOA qdoa = new QuestionsDOA();

		int questionId = qdoa.getID(question.getQuestionString());

		ArrayList<Integer> topicIds = getRelationTopic(questionId);
		ArrayList<Integer> choiceIds = getRelationQuestionChoice(questionId);
		if (questionId == 0) {
			throw new IOException("Question not found");
		} else {
			if (topicIds.isEmpty()) {
				PreparedStatement preparedStatement = null;
				Connection connection = null;
				for (int i : topicIds) {
					try {
						connection = DriverManager.getConnection(database, username, password);
						String deleting = "delete from public.\"Relation_Questions_Topics\" where id_topic = ? ;";
						preparedStatement = connection.prepareStatement(deleting);
						preparedStatement.setInt(1, i);
						preparedStatement.execute();
						System.out.println("Topic Relation Deleted");
						
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
			if (choiceIds.isEmpty()) {
				PreparedStatement preparedStatement = null;
				Connection connection = null;
				for (int i : choiceIds) {
					try {
						connection = DriverManager.getConnection(database, username, password);
				
						String deleting = "delete from public.\"Relation_Questions_Choices\" where id_choice = ? ;";
						preparedStatement = connection.prepareStatement(deleting);
						preparedStatement.setInt(1, i);
						preparedStatement.execute();
						System.out.println("Choice Relation Deleted");
						
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

	}

	public void unRelateTopicFromAll(Topic topic) throws IOException {

		TopicDAO tdoa = new TopicDAO();

		int topicId = tdoa.getID(topic.getTopicString());

		ArrayList<Integer> questionIds = getRelationTopicQuestion(topicId);
		if (topicId == 0) {
			throw new IOException("Topic not found");
		} else if (questionIds.isEmpty()) {
			System.out.println("Topic not linked to any question");
		} else {
			PreparedStatement preparedStatement = null;
			Connection connection = null;
			try {
				connection = DriverManager.getConnection(database, username, password);
				String deleting = "delete from public.\"Relation_Questions_Topics\" where id_topic = ? ;";
				preparedStatement = connection.prepareStatement(deleting);
				preparedStatement.setInt(1, topicId);
				preparedStatement.execute();

				System.out.println("Topic Relation Deleted");
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

	public void unRelateChoiceFromAll(Choice choice) throws IOException {

		ChoiceDAO cdoa = new ChoiceDAO();

		int choiceId = cdoa.getID(choice.getChoiceString());

		ArrayList<Integer> questionIds = getRelationChoiceQuestion(choiceId);
		if (choiceId == 0) {
			throw new IOException("Choice not found");
		} else if (questionIds.isEmpty()) {
			System.out.println("Choice Not linked to any question");
		} else {
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			try {
				connection = DriverManager.getConnection(database, username, password);
				String deleting = "delete from public.\"Relation_Questions_Choices\" where id_choice = ? ;";
				preparedStatement = connection.prepareStatement(deleting);

				preparedStatement.setInt(1, choiceId);
				preparedStatement.execute();

				System.out.println("Choice Relation Deleted");
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

	public void unRelateTopicFromQuestion(Topic topic,Question question) throws IOException {

		TopicDAO tdoa = new TopicDAO();
		QuestionsDOA qdao = new QuestionsDOA();
		int topicId = tdoa.getID(topic.getTopicString());
		int questionId = qdao.getID(question.getQuestionString());
		ArrayList<Integer> questionIds = getRelationTopicQuestion(topicId);
		if (topicId == 0) {
			throw new IOException("Topic not found");
		} else if (questionId == 0) {
			System.out.println("Question not found");
		}else if (!questionIds.contains(questionId)) {
			System.out.println("Topic not linked to question");
		} else {
			PreparedStatement preparedStatement = null;
			Connection connection = null;
			try {
				connection = DriverManager.getConnection(database, username, password);
				String deleting = "delete from public.\"Relation_Questions_Topics\" where id_topic = ? and id_question = ?;";
				preparedStatement = connection.prepareStatement(deleting);
				preparedStatement.setInt(1, topicId);
				preparedStatement.setInt(2, questionId);
				preparedStatement.execute();

				System.out.println("Topic Relation Deleted");
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

	public void unRelateChoiceFromQuestion(Choice choice,Question question) throws IOException {

		ChoiceDAO cdoa = new ChoiceDAO();

		QuestionsDOA qdao = new QuestionsDOA();
		int choiceId = cdoa.getID(choice.getChoiceString());

		int questionId = qdao.getID(question.getQuestionString());
		ArrayList<Integer> questionIds = getRelationChoiceQuestion(choiceId);
		if (choiceId == 0) {
			throw new IOException("Choice not found");
		} else if (questionId == 0) {
			System.out.println("Question not found");
		}else if (!questionIds.contains(questionId)) {
			System.out.println("Choice not linked to question");
		} else {
			PreparedStatement preparedStatement = null;
			Connection connection = null;
			try {
				connection = DriverManager.getConnection(database, username, password);
				String deleting = "delete from public.\"Relation_Questions_Choices\" where id_choice = ? and id_question = ? ;";
				preparedStatement = connection.prepareStatement(deleting);

				preparedStatement.setInt(1, choiceId);
				preparedStatement.setInt(2, questionId);
				preparedStatement.execute();

				System.out.println("Choice Relation Deleted");
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

	
	// for adding relation
	
	public void relateTopicToQuestion(Question question, Topic topic) throws IOException {

		FullQuestion full = getAllRelatedToQuestion(question);
		Topic[] topics = full.getTopics();
		QuestionsDOA qdoa = new QuestionsDOA();
		TopicDAO tdao = new TopicDAO();

		boolean exists = false;
		int questionId = qdoa.getID(question.getQuestionString());
		int topicId = tdao.getID(topic.getTopicString());

		if (questionId == 0) {
			throw new IOException("Question not found in database");
		} else if (topicId == 0) {
			tdao.createTopic(topic);
			topicId = tdao.getID(topic.getTopicString());
		} else {
			for (Topic i : topics) {
				int topicIdInArray = tdao.getID(i.getTopicString());
				if (topicIdInArray == topicId) {
					exists = true;
					System.out.println("Topic Relation already exists");
					break;
				}
			}
		}

		if (!exists) {
			System.out.println("Creating topic relation");
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			try {
				connection = DriverManager.getConnection(database, username, password);
				
				String inserting = "INSERT INTO public.\"Relation_Questions_Topics\"(id_question,id_topic) VALUES (?,?);";
				preparedStatement = connection.prepareStatement(inserting);
				preparedStatement.setInt(1, questionId);
				preparedStatement.setInt(2, topicId);
				preparedStatement.execute();
				
				System.out.println("Topic Relation Created");
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

	public void relateChoiceToQuestion(Question question, Choice choice) throws IOException {

		FullQuestion full = getAllRelatedToQuestion(question);
		Choice[] choices = full.getChoices();
		QuestionsDOA qdoa = new QuestionsDOA();
		ChoiceDAO cdao = new ChoiceDAO();
		int questionId = qdoa.getID(question.getQuestionString());
		int choiceId = cdao.getID(choice.getChoiceString());
		int choiceAnswerIdFound = cdao.getID(question.getAnswer());

		boolean existsAnswer = false;
		boolean exists = false;
		if (questionId == 0) {
			throw new IOException("Question not found in database");
		} else if (choiceId == 0) {
			cdao.createChoice(choice);
			choiceId = cdao.getID(choice.getChoiceString());
		} else if(choiceAnswerIdFound == 0) {
			
				throw new IOException(question.getAnswer()+" Answer not found in database");				
			
			
		}else {
			for (Choice i : choices) {
				int topicIdInArray = cdao.getID(i.getChoiceString());
				if (topicIdInArray == choiceId) {
					exists = true;
					System.out.println("Choice Relation already exists");
				}
				if(i.getChoiceString().equals(question.getAnswer()))
					existsAnswer = true;
			}
		}


		if(!existsAnswer) {
			throw new IOException("Answer not found in the choices");
		}
		if (!exists) {
			PreparedStatement preparedStatement = null;
			Connection connection = null;
			try {
				System.out.println("Creating choice relation");
				
				connection = DriverManager.getConnection(database, username, password);
				String inserting = "INSERT INTO public.\"Relation_Questions_Choices\"(id_question,id_choice) VALUES (?,?);";
				preparedStatement = connection.prepareStatement(inserting);
				preparedStatement.setInt(1, questionId);
				preparedStatement.setInt(2, choiceId);
				preparedStatement.execute();
				System.out.println("Choice Relation Created");
			
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

	// get all topics and choices related to question
	public FullQuestion getAllRelatedToQuestion(Question quest) throws IOException{
		QuestionsDOA qdao = new QuestionsDOA();
		int questionId = qdao.getID(quest.getQuestionString());

		ArrayList<Topic> topics = new ArrayList<>();
		ArrayList<Choice> choices = new ArrayList<>();
		
		if (questionId == 0) {
			throw new IOException("Question not found");
		} else {
			TopicDAO tdao = new TopicDAO();
			ChoiceDAO cdao = new ChoiceDAO();

			ArrayList<Integer> topicIds = getRelationTopic(questionId);

			
			for (int i : topicIds) {
				topics.add(new Topic(tdao.getTopic(i), i));
			}
			ArrayList<Integer> choiceIds = getRelationQuestionChoice(questionId);
			for (int i : choiceIds) {
				choices.add(new Choice(cdao.getChoice(i),i));
			}

		}

		return new FullQuestion(quest, topics.toArray(new Topic[topics.size()]),
				choices.toArray(new Choice[choices.size()]));
	}

	// get all by topic
	public ArrayList<FullQuestion> getAllRelatedToTopic(Topic topic) throws IOException {
		TopicDAO tdao = new TopicDAO();
		int topicId = tdao.getID(topic.getTopicString());

		ArrayList<Question> questions = new ArrayList<>();
		ArrayList<FullQuestion> out = new ArrayList<>();

		if (topicId == 0) {
			throw new IOException("Topic not found");
		} else {
			QuestionsDOA qdao = new QuestionsDOA();

			ArrayList<Integer> questionIds = getRelationTopicQuestion(topicId);

			for (int i : questionIds) {
				Question question = qdao.getQuestion(i);
				if (question.getId() != 0) {
					questions.add(question);
				}

			}

			if (questions.isEmpty()) {
				System.out.println("No questions linked to this topic");
			} else {
				for (Question i : questions) {
					out.add(getAllRelatedToQuestion(i));
				}
			}

		}

		return out;
	}
	
	public ArrayList<FullQuestion> getAll(){
		ArrayList<FullQuestion> out = new ArrayList<>();
		QuestionsDOA qdao = new QuestionsDOA();
		PreparedStatement prepareQuestionStatement = null;
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(database, username, password);
			String query = "SELECT id_question FROM public.\"Relation_Questions_Topics\";";
			prepareQuestionStatement = connection.prepareStatement(query);
			ResultSet rs = prepareQuestionStatement.executeQuery();
			int id = 0;
			int temp = 0;
			boolean first = true;
			while(rs.next()) {
				if (first) {
					id = rs.getInt("id_question");
					temp = id;
					out.add(getAllRelatedToQuestion(qdao.getQuestion(id)));
					first = false;
				}
			}
			rs = prepareQuestionStatement.executeQuery();
			while (rs.next()) {
				id = rs.getInt("id_question");
				if(temp != id) {
					out.add(getAllRelatedToQuestion(qdao.getQuestion(id)));
					temp = id;
				}

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
	
	// get topic
	public ArrayList<Integer> getRelationTopicQuestion(int topicId){
		ArrayList<Integer> out = new ArrayList<>();
		Connection connection = null;
		PreparedStatement prepareQuestionStatement = null;
		try {
			connection = DriverManager.getConnection(database, username, password);
			String query = "SELECT * FROM public.\"Relation_Questions_Topics\" where id_topic = ?;";
			prepareQuestionStatement = connection.prepareStatement(query);
			prepareQuestionStatement.setInt(1, topicId);
			ResultSet rs = prepareQuestionStatement.executeQuery();
			int id = 0;
			while (rs.next()) {
				id = rs.getInt("id_question");
				out.add(id);
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

	public ArrayList<Integer> getRelationChoiceQuestion(int choiceId){
		ArrayList<Integer> out = new ArrayList<>();
		Connection connection = null;
		PreparedStatement prepareQuestionStatement = null;
		try {
			connection = DriverManager.getConnection(database, username, password);
			String query = "SELECT * FROM public.\"Relation_Questions_Choices\" where id_choice = ?;";
			prepareQuestionStatement = connection.prepareStatement(query);
			prepareQuestionStatement.setInt(1, choiceId);
			ResultSet rs = prepareQuestionStatement.executeQuery();
			int id = 0;
			while (rs.next()) {
				id = rs.getInt("id_question");
				out.add(id);
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

	public ArrayList<Integer> getRelationQuestionChoice(int questionId) {
		ArrayList<Integer> out = new ArrayList<>();
		Connection connection = null;
		PreparedStatement prepareQuestionStatement = null;
		try {
			connection = DriverManager.getConnection(database, username, password);
			String query = "SELECT * FROM public.\"Relation_Questions_Choices\" where id_question = ?;";
			prepareQuestionStatement = connection.prepareStatement(query);
			prepareQuestionStatement.setInt(1, questionId);
			ResultSet rs = prepareQuestionStatement.executeQuery();
			int id = 0;
			while (rs.next()) {
				id = rs.getInt("id_choice");
				out.add(id);
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

	public ArrayList<Integer> getRelationTopic(int questionId){
		ArrayList<Integer> out = new ArrayList<>();
		Connection connection = null;
		PreparedStatement prepareQuestionStatement = null;
		try {
			connection = DriverManager.getConnection(database, username, password);
			String query = "SELECT * FROM public.\"Relation_Questions_Topics\" where id_question = ?;";
			prepareQuestionStatement = connection.prepareStatement(query);
			prepareQuestionStatement.setInt(1, questionId);
			ResultSet rs = prepareQuestionStatement.executeQuery();
			int id = 0;
			while (rs.next()) {
				id = rs.getInt("id_topic");
				out.add(id);
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

}
