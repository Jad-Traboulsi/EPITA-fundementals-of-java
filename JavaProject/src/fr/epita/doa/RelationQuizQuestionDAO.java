package fr.epita.doa;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import fr.epita.classes.Choice;
import fr.epita.classes.FullQuestion;
import fr.epita.classes.FullQuiz;
import fr.epita.classes.Quiz;
import fr.epita.classes.Topic;
import fr.epita.services.filereader.Reader;

public class RelationQuizQuestionDAO {
	Reader r = new Reader();
	String database = r.getDatabase();
	String username = r.getUsername();
	String password = r.getPassword();
	private Random rand= new Random();
	public void createFullQuiz(FullQuiz fullQuiz) throws IOException{
		RelationQuestionDAO rltnqdao = new RelationQuestionDAO();
		QuizDAO qdao = new QuizDAO();
		
		qdao.createQuiz(fullQuiz.getQuiz());
		for(FullQuestion i : fullQuiz.getFullQuestion()) {
			rltnqdao.createFullQuestion(i);
			relateQuestionToQuiz(i, fullQuiz.getQuiz());
		}
		
		
		
		
	}
	public FullQuiz getQuizFromTopic(String title,Topic topic,int numberOfQuestions) throws IOException {
		TopicDAO tdao = new TopicDAO();
		if(tdao.getID(topic.getTopicString()) == 0) {
			throw new IOException("Topic not found in database");
		}
		FullQuiz out = new FullQuiz();
		QuizDAO quizdao = new QuizDAO();
		RelationQuestionDAO rltnquestiondao = new RelationQuestionDAO();
		quizdao.createQuiz(new Quiz(title));
		ArrayList<FullQuestion> questionsFound = rltnquestiondao.getAllRelatedToTopic(topic);
		ArrayList<FullQuestion> questionsUsed = new ArrayList<>();
		if(numberOfQuestions >= questionsFound.size()) {
			out.setFullQuestion(rltnquestiondao.getAllRelatedToTopic(topic));
		}
		else {
			ArrayList<Integer> picked = new ArrayList<>();
			int chooser = 0;
			for(int i=0;i<numberOfQuestions;i++) {
				chooser = rand.nextInt(questionsFound.size());
				while (picked.contains(chooser)){
					chooser = rand.nextInt(questionsFound.size());
				}
				
				questionsUsed.add(questionsFound.get(chooser));
			}

			out.setFullQuestion(questionsUsed);
		}
		out.setQuiz(new Quiz(quizdao.getQuizId(new Quiz(title)),title));
		
		for(FullQuestion fq: out.getFullQuestion()) {
			relateQuestionToQuiz(fq, new Quiz(title));
		}
		
		return out;
	}
	public void relateQuestionToQuiz(FullQuestion fullQuestion, Quiz quiz) throws IOException {

		QuestionsDOA questiondao = new QuestionsDOA();
		QuizDAO quizdao = new QuizDAO();
		boolean exists = false;
		
		//get question id
		int questionId = questiondao.getID(fullQuestion.getQuestion().getQuestionString());
		//get quiz id
		int quizId = quizdao.getQuizId(quiz);
		if(questionId == 0) {
			throw new IOException("Question "+fullQuestion.getQuestion().getQuestionString() + " not found in database");
		}
		else if(quizId == 0) {
			throw new IOException("Quiz "+quiz.getTitle() + " not found in database");
		}
		else {
			for(FullQuestion i : getAllRelatedToQuiz(quiz)) {

				if (i.equals(fullQuestion)){
					exists = true;
					System.out.println("Relation already exists");
					break;
				}
			}
		}
		if(!exists){
			PreparedStatement insertingStatement = null;
			Connection connection = null;
			try {
				connection = DriverManager.getConnection(database, username, password);
				
				String inserting = "INSERT INTO public.\"Relation_Quizes_Questions\"(id_question,id_quiz) VALUES (?,?);";
				insertingStatement = connection.prepareStatement(inserting);
				insertingStatement.setInt(1, questionId);
				insertingStatement.setInt(2, quizId);
				insertingStatement.execute();
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
	public void unrelateQuestionFromQuiz(FullQuestion fullQuestion, Quiz quiz) throws IOException{

		QuestionsDOA questiondao = new QuestionsDOA();
		QuizDAO quizdao = new QuizDAO();
		boolean exists = false;
		
		//get question id
		int questionId = questiondao.getID(fullQuestion.getQuestion().getQuestionString());
		//get quiz id
		int quizId = quizdao.getQuizId(quiz);
		if(questionId == 0) {
			throw new IOException("Question "+fullQuestion.getQuestion().getQuestionString() + " not found in database");
		}
		else if(quizId == 0) {
			throw new IOException("Quiz "+quiz.getTitle() + " not found in database");
		}
		else {
			int count = 0;
			ArrayList<FullQuestion> all = getAllRelatedToQuiz(quiz);
			for(FullQuestion i : all) {
				if (i.equals(fullQuestion)){
					exists = true;
					break;
				}
				count+=1;
			}
			if(all.size() == count && !exists) {
				System.out.println("Relation doesnt exist");
			}
		}
		if(exists){
			PreparedStatement insertingStatement = null;
			Connection connection = null;
			try {

				connection = DriverManager.getConnection(database, username, password);
		
				String inserting = "delete from public.\"Relation_Quizes_Questions\" where id = ?;";
				insertingStatement = connection.prepareStatement(inserting);
				insertingStatement.setInt(1, quizId);
				insertingStatement.execute();
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
	
	// get all related to quiz
	public ArrayList<FullQuestion> getAllRelatedToQuiz(Quiz quiz){
		ArrayList<FullQuestion> out = new ArrayList<>();
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(database, username, password);
			String str = "SELECT id_question FROM public.\"Relation_Quizes_Questions\" where id_quiz = ?;";

			RelationQuestionDAO rltnQuestion = new RelationQuestionDAO();
			QuestionsDOA qdoa = new QuestionsDOA();
			QuizDAO quizdao = new QuizDAO();
			preparedStatement = connection.prepareStatement(str);
			preparedStatement.setInt(1, quizdao.getQuizId(quiz));

			ResultSet rs = preparedStatement.executeQuery();
			int questionId = 0;
			
			while (rs.next()) {
				questionId = rs.getInt("id_question");
				out.add(rltnQuestion.getAllRelatedToQuestion(qdoa.getQuestion(questionId)));
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
	public ArrayList<FullQuiz> getAllQuizes(){
		ArrayList<FullQuiz> out = new ArrayList<>();
		
		QuizDAO quizdao = new QuizDAO();
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(database, username, password);
			String str = "SELECT * FROM public.\"Relation_Quizes_Questions\";";
		
			preparedStatement = connection.prepareStatement(str);

			
			ResultSet rs = preparedStatement.executeQuery();
			int quizId = 0;
			int temp = 0;
			while (rs.next()) {
				temp = quizId;
				quizId = rs.getInt("id_quiz");
				if(temp != quizId) {
					String quizTitle = quizdao.getTitleById(quizId);
					Quiz q = new Quiz(quizId,quizTitle);
					out.add(new FullQuiz(q,getAllRelatedToQuiz(q)));
				}
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
	public FullQuiz getQuiz(Quiz quiz) throws IOException{
		FullQuiz out = new FullQuiz();
		QuizDAO quizdao = new QuizDAO();
		//get quiz id
		int quizId = quizdao.getQuizId(quiz);
		if(quizId == 0) {
			throw new IOException("Quiz "+quiz.getTitle() + " not found in database");
		}
		else {
			PreparedStatement preparedStatement = null;
			Connection connection = null;
			try {
				connection = DriverManager.getConnection(database, username, password);
				String str = "SELECT id_question FROM public.\"Relation_Quizes_Questions\" where id_quiz = ?;";
				
				preparedStatement = connection.prepareStatement(str);
				preparedStatement.setInt(1, quizId);
				
				ResultSet rs = preparedStatement.executeQuery();
				int questionId = 0;
				ArrayList<FullQuestion> questionsFound = new ArrayList<>();
				RelationQuestionDAO rltnQuestion = new RelationQuestionDAO();
				QuestionsDOA qdoa = new QuestionsDOA();
				while (rs.next()) {
					questionId = rs.getInt("id_question");
					questionsFound.add(rltnQuestion.getAllRelatedToQuestion(qdoa.getQuestion(questionId)));
				}

				String quizTitle = quizdao.getTitleById(quizId);
				Quiz q = new Quiz(quizId,quizTitle);
				out = new FullQuiz(q,questionsFound);
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
		return out;
	}
	
	// print on text
	public void outOnText(FullQuiz fq){
		QuizDAO quizDAO = new QuizDAO();
		File file = new File("./"+fq.getQuiz().getTitle()+".txt");
		boolean fileExists = file.exists();
		if(!fileExists) {
			try {
				if(file.createNewFile()) {
					System.out.println("File Created");
				}
				else {
					System.out.println("Cannot create file");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try(FileWriter fr = new FileWriter(file,false)){
			
		
			String lineSep = System.getProperty("line.separator");
			fr.append("Name:________"+lineSep);
			fr.append("ID:________"+lineSep);
			fr.append(lineSep);
			fr.append(fq.getQuiz().getTitle()+" Quiz");
			fr.append(lineSep);
			fr.append("Quiz ID: "+quizDAO.getQuizId(fq.getQuiz()));
			fr.append(lineSep);
			fr.append(lineSep);
			fr.append(lineSep);
			ArrayList<FullQuestion> allQuestions = fq.getFullQuestion();
			
			
			for(int i = 0;i<allQuestions.size();i++) {
				FullQuestion question= allQuestions.get(i);
				fr.append(i+1+". "+question.getQuestion().getQuestionString()+"\t("+question.getQuestion().getDifficulty()+" pt(s))");
				fr.append(lineSep);
				ArrayList<Integer> numbersChosen = new ArrayList<>();
				Choice[] choices = question.getChoices();
				int randNumber = 0;
				for(int j=0;j<choices.length;j++) {
	
					fr.append("\t");
					randNumber = rand.nextInt(choices.length);
					while(numbersChosen.contains(randNumber)) {
						randNumber = rand.nextInt(choices.length);
					}
					numbersChosen.add(randNumber);
					fr.append(j+1+". "+choices[randNumber].getChoiceString());
					fr.append(lineSep);
				}
				fr.append(lineSep);
				
			}
			fr.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	

	}
	public int totalGradeOfQuiz(FullQuiz fq){
		int total = 0;
		
		for(FullQuestion i : fq.getFullQuestion()) {
			total+=i.getQuestion().getDifficulty();
		}
		
		return total;
	}
}
