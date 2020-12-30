package fr.epita.doa;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Random;

import fr.epita.classes.Choice;
import fr.epita.classes.FullQuestion;
import fr.epita.classes.FullQuiz;
import fr.epita.classes.Quiz;
import fr.epita.classes.Topic;

public class RelationQuizQuestionDAO {
	String database = "jdbc:postgresql://localhost:5432/fundementals-of-java";
	String username = "postgres";
	String password = "";
	
	public void createFullQuiz(FullQuiz fullQuiz) throws Exception {
		RelationQuestionDAO rltnqdao = new RelationQuestionDAO();
		QuizDAO qdao = new QuizDAO();
		
		qdao.createQuiz(fullQuiz.getQuiz());
		for(FullQuestion i : fullQuiz.getFullQuestion()) {
			rltnqdao.createFullQuestion(i);
			relateQuestionToQuiz(i, fullQuiz.getQuiz());
		}
		
		
		
		
	}
	public FullQuiz getQuizFromTopic(String title,Topic topic,int numberOfQuestions) throws Exception {
		TopicDAO tdao = new TopicDAO();
		if(tdao.getID(topic.getTopic()) == 0) {
			throw new Exception("Topic not found in database");
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
			Random rand= new Random();
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
	public void relateQuestionToQuiz(FullQuestion fullQuestion, Quiz quiz) throws Exception {

		QuestionsDOA questiondao = new QuestionsDOA();
		QuizDAO quizdao = new QuizDAO();
		boolean exists = false;
		
		//get question id
		int questionId = questiondao.getID(fullQuestion.getQuestion().getQuestion());
		//get quiz id
		int quizId = quizdao.getQuizId(quiz);
		if(questionId == 0) {
			throw new Exception("Question "+fullQuestion.getQuestion().getQuestion() + " not found in database");
		}
		else if(quizId == 0) {
			throw new Exception("Quiz "+quiz.getTitle() + " not found in database");
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
		
			Connection connection = DriverManager.getConnection(database, username, password);
	
			String inserting = "INSERT INTO public.\"Relation_Quizes_Questions\"(id_question,id_quiz) VALUES (?,?);";
			PreparedStatement insertingStatement = connection.prepareStatement(inserting);
			insertingStatement.setInt(1, questionId);
			insertingStatement.setInt(2, quizId);
			insertingStatement.execute();
			connection.close();
		}

	}
	public void unrelateQuestionFromQuiz(FullQuestion fullQuestion, Quiz quiz) throws Exception{

		QuestionsDOA questiondao = new QuestionsDOA();
		QuizDAO quizdao = new QuizDAO();
		boolean exists = false;
		
		//get question id
		int questionId = questiondao.getID(fullQuestion.getQuestion().getQuestion());
		//get quiz id
		int quizId = quizdao.getQuizId(quiz);
		if(questionId == 0) {
			throw new Exception("Question "+fullQuestion.getQuestion().getQuestion() + " not found in database");
		}
		else if(quizId == 0) {
			throw new Exception("Quiz "+quiz.getTitle() + " not found in database");
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
		
			Connection connection = DriverManager.getConnection(database, username, password);
	
			String inserting = "delete from public.\"Relation_Quizes_Questions\" where id = ?;";
			PreparedStatement insertingStatement = connection.prepareStatement(inserting);
			insertingStatement.setInt(1, quizId);
			insertingStatement.execute();
			connection.close();
		}
	}
	
	// get all related to quiz
	public ArrayList<FullQuestion> getAllRelatedToQuiz(Quiz quiz)throws Exception{
		ArrayList<FullQuestion> out = new ArrayList<>();
		
		Connection connection = DriverManager.getConnection(database, username, password);
		String str = "SELECT id_question FROM public.\"Relation_Quizes_Questions\" where id_quiz = ?;";

		RelationQuestionDAO rltnQuestion = new RelationQuestionDAO();
		QuestionsDOA qdoa = new QuestionsDOA();
		QuizDAO quizdao = new QuizDAO();
		PreparedStatement preparedStatement = connection.prepareStatement(str);
		preparedStatement.setInt(1, quizdao.getQuizId(quiz));

		ResultSet rs = preparedStatement.executeQuery();
		int questionId = 0;
		
		while (rs.next()) {
			questionId = rs.getInt("id_question");
			out.add(rltnQuestion.getAllRelatedToQuestion(qdoa.getQuestion(questionId)));
		}
		
		return out;
	}
	public ArrayList<FullQuiz> getAllQuizes() throws Exception{
		ArrayList<FullQuiz> out = new ArrayList<>();
		
		QuizDAO quizdao = new QuizDAO();
		
		Connection connection = DriverManager.getConnection(database, username, password);
		String str = "SELECT * FROM public.\"Relation_Quizes_Questions\";";
	
		PreparedStatement preparedStatement = connection.prepareStatement(str);

		
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

		return out;
	}
	public FullQuiz getQuiz(Quiz quiz) throws Exception{
		FullQuiz out = new FullQuiz();
		QuizDAO quizdao = new QuizDAO();
		//get quiz id
		int quizId = quizdao.getQuizId(quiz);
		if(quizId == 0) {
			throw new Exception("Quiz "+quiz.getTitle() + " not found in database");
		}
		else {
			Connection connection = DriverManager.getConnection(database, username, password);
			String str = "SELECT id_question FROM public.\"Relation_Quizes_Questions\" where id_quiz = ?;";
			
			PreparedStatement preparedStatement = connection.prepareStatement(str);
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
			connection.close();
		}
		return out;
	}
	
	// print on text
	public void outOnText(FullQuiz fq) throws Exception{
		QuizDAO quizDAO = new QuizDAO();
		File file = new File("./"+fq.getQuiz().getTitle()+".txt");
		boolean fileExists = file.exists();
		if(!fileExists)
			file.createNewFile();

		FileWriter fr = new FileWriter(file,false);
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
			fr.append(i+1+". "+question.getQuestion().getQuestion()+"\t("+question.getQuestion().getDifficulty()+" pt(s))");
			fr.append(lineSep);
			Random rand= new Random();
			ArrayList<Integer> numbersChosen = new ArrayList<>();
			Choice choices[] = question.getChoices();
			int randNumber = 0;
			for(int j=0;j<choices.length;j++) {

				fr.append("\t");
				randNumber = rand.nextInt(choices.length);
				while(numbersChosen.contains(randNumber)) {
					randNumber = rand.nextInt(choices.length);
				}
				numbersChosen.add(randNumber);
				fr.append(j+1+". "+choices[randNumber].getChoice());
				fr.append(lineSep);
			}
			fr.append(lineSep);
			
		}
			
		
		fr.close();
	

	}
	public int totalGradeOfQuiz(FullQuiz fq) throws Exception{
		int total = 0;
		
		for(FullQuestion i : fq.getFullQuestion()) {
			total+=i.getQuestion().getDifficulty();
		}
		
		return total;
	}
}
