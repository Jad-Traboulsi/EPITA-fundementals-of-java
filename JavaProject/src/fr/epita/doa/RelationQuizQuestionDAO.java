package fr.epita.doa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import fr.epita.classes.FullQuestion;
import fr.epita.classes.FullQuiz;
import fr.epita.classes.Question;
import fr.epita.classes.Quiz;
import fr.epita.classes.Topic;

public class RelationQuizQuestionDAO {
	public void createFullQuiz(FullQuiz fullQuiz) throws Exception {
		RelationQuestionDAO rltnqdao = new RelationQuestionDAO();
		QuizDAO qdao = new QuizDAO();
		
		qdao.createQuiz(fullQuiz.getQuiz());
		for(FullQuestion i : fullQuiz.getFullQuestion()) {
			rltnqdao.createFullQuestion(i);
			relateQuestionToQuiz(i, fullQuiz.getQuiz());
		}
		
		
		
		
	}
	public void relateQuestionToQuiz(FullQuestion fullQuestion, Quiz quiz) throws Exception {

		QuestionsDOA questiondao = new QuestionsDOA();
		QuizDAO quizdao = new QuizDAO();
		
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
		// check if relation already exists
		
		else {
		
			Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java",
					"postgres", "");
	
			String inserting = "INSERT INTO public.\"Relation_Quizes_Questions\"(id_question,id_quiz) VALUES (?,?);";
			PreparedStatement insertingStatement = connection.prepareStatement(inserting);
			insertingStatement.setInt(1, questionId);
			insertingStatement.setInt(2, quizId);
			insertingStatement.execute();
			connection.close();
		}

	}

	// get all related to quiz
	
	public ArrayList<FullQuiz> getAllQuizes() throws Exception{
		ArrayList<FullQuiz> out = new ArrayList<>();
		
		Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java",
				"postgres", "");
		String str = "SELECT * FROM public.\"Relation_Quizes_Questions\";";
	
		PreparedStatement preparedStatement = connection.prepareStatement(str);

		
		ResultSet rs = preparedStatement.executeQuery();
		int questionId = 0;
		int quizId = 0;
		ArrayList<FullQuestion> questionsFound = new ArrayList<>();
		RelationQuestionDAO rltnQuestion = new RelationQuestionDAO();
		QuestionsDOA qdoa = new QuestionsDOA();
		QuizDAO quizdao = new QuizDAO();
		int temp = 0;
		while (rs.next()) {
			temp = quizId;
			quizId = rs.getInt("id_quiz");
			if(quizId == temp) {
				questionId = rs.getInt("id_question");
				questionsFound.add(rltnQuestion.getAllRelatedToQuestion(qdoa.getQuestion(questionId)));
			}
			else {
				temp =quizId;
				String quizTitle = quizdao.getTitleById(quizId);
				Quiz q = new Quiz(quizId,quizTitle);
				out.add(new FullQuiz(q,questionsFound));
				questionsFound.clear();
			}
			
		}
		connection.close();
	
		return out;
	}
}
