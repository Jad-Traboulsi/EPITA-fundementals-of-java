package fr.epita.doa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import fr.epita.classes.FullQuestion;
import fr.epita.classes.FullQuiz;
import fr.epita.classes.Student;

public class StudentAnswersDAO {
	public Hashtable<FullQuestion,String> getStudentAnswers(Student student,FullQuiz fq) throws Exception{
		
		Hashtable<FullQuestion,String> answers = new Hashtable<>();
		StudentDAO sdao = new StudentDAO();
		QuizDAO qdao = new QuizDAO();
		RelationQuestionDAO relquestdao = new RelationQuestionDAO();
		QuestionsDOA questdao = new QuestionsDOA();
		
		int studentId = sdao.getStudentId(student);
		int quizId = qdao.getQuizId(fq.getQuiz());
		
		if(studentId==0) {
			throw new Exception("Student doesnt exist");
		}
		else if(quizId== 0) {
			throw new Exception("Quiz doesnt exist");
		}
		else {
			Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java",
					"postgres", "");
			String str = "SELECT * FROM public.\"Student_Answers\" where id_student = ? and id_quiz = ? ;";

			PreparedStatement preparedStatement = connection.prepareStatement(str);

			preparedStatement.setInt(1, studentId);
			preparedStatement.setInt(2, quizId);
			ResultSet rs = preparedStatement.executeQuery();
			String answer = " ";
			int questionId= 0;
			while(rs.next()) {
				answer = rs.getString("answer");
				questionId = rs.getInt("id_question");
				answers.put(relquestdao.getAllRelatedToQuestion(questdao.getQuestion(questionId)), answer);
			}
			connection.close();
		}
		
		
		return answers;
	}
	public String printHashtable(Hashtable<FullQuestion,String> table) {
		String out = "";
		Enumeration<FullQuestion> keys = table.keys();
		while(keys.hasMoreElements()) {
			FullQuestion key = keys.nextElement();
         String str = key.toString();
         out+= str + ": " + table.get(key);
         out+="\n";
      }  
		return out;
	}
	public boolean isPresent(Student student,FullQuiz fq)throws Exception{
		boolean exists = false;
		StudentDAO sdao =  new StudentDAO();
		QuizDAO qdao = new QuizDAO();
		int studentId = sdao.getStudentId(student) ;
		int quizId = qdao.getQuizId(fq.getQuiz());
		
		if(studentId== 0){
			throw new Exception("Student doesnt exist");
		}		
		else if(quizId== 0){
			throw new Exception("Quiz doesnt exist");
		}
		else {
			Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java", "postgres", "");
			
			String inserting = "select * from public.\"Student_Answers\" where id_quiz = ? and id_student = ?;";
			PreparedStatement preparedStatement = connection.prepareStatement(inserting);
			preparedStatement.setInt(1, quizId);
			preparedStatement.setInt(2, studentId);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()) {
				exists = true;
			}
			connection.close();
		}
		return exists;
	}
	public void createAnswers(Student student,FullQuiz fq, ArrayList<String> answers) throws Exception{
		StudentDAO sdao =  new StudentDAO();
		QuizDAO qdao = new QuizDAO();
		QuestionsDOA questdao = new QuestionsDOA();
		boolean exists = false;
		int studentId = sdao.getStudentId(student) ;
		int quizId = qdao.getQuizId(fq.getQuiz());
		
		if(studentId== 0){
			throw new Exception("Student doesnt exist");
		}		
		else if(quizId== 0){
			throw new Exception("Quiz doesnt exist");
		}
		else if(answers.size()!=fq.getFullQuestion().size()) {
			throw new Exception("Answers arent equal to questions");
		}
		// check if already placed
		
		if(!isPresent(student, fq)) {

			Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java", "postgres", "");
				
			for(int i = 0;i< answers.size();i++) {

				String inserting = "INSERT INTO public.\"Student_Answers\"(id_student, id_quiz, id_question, answer)VALUES (?,?,?, ?);";	
				PreparedStatement insertingStatement = connection.prepareStatement(inserting);
				insertingStatement.setInt(1, studentId);
				insertingStatement.setInt(2, quizId);
				insertingStatement.setInt(3, questdao.getID(fq.getFullQuestion().get(i).getQuestion().getQuestion()));
				insertingStatement.setString(4, answers.get(i));
				insertingStatement.execute();
			}
			connection.close();
			StudentQuizDAO studentQuizdao = new StudentQuizDAO();
			studentQuizdao.setGrade(student, fq);
		}
	}
	
	public void updateAnswer(Student student,FullQuiz quiz,FullQuestion question,String newAnswer) throws Exception{
		StudentDAO sdao =  new StudentDAO();
		QuizDAO qdao = new QuizDAO();
		QuestionsDOA questdao = new QuestionsDOA();
		RelationQuizQuestionDAO rlqqdao = new RelationQuizQuestionDAO();
		RelationQuestionDAO rlqdao = new RelationQuestionDAO();
		
		FullQuestion questionChosen = rlqdao.getAllRelatedToQuestion(question.getQuestion());
		int studentId = sdao.getStudentId(student) ;
		int quizId = qdao.getQuizId(quiz.getQuiz());
		int questionId = 0;
		ArrayList<FullQuestion>questionsFound =  rlqqdao.getAllRelatedToQuiz(quiz.getQuiz());
		boolean exists = false;
		if(studentId== 0){
			throw new Exception("Student doesnt exist");
		}		
		else if(quizId== 0){
			throw new Exception("Quiz doesnt exist");
		}
		else {
			for(FullQuestion i :questionsFound) {
				if(i.equals(questionChosen)) {
					exists = true;
					questionId = questdao.getID(i.getQuestion().getQuestion());
					break;
				}
			}
		}
		if(exists) {

			Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java", "postgres", "");
			
			String inserting = "update public.\"Student_Answers\" answer = ? where id_student=? and id_quiz= ? and id_question = ?;";		

			PreparedStatement insertingStatement = connection.prepareStatement(inserting);
			insertingStatement.setString(1, newAnswer);
			insertingStatement.setInt(2, studentId);
			insertingStatement.setInt(3, quizId);
			insertingStatement.setInt(4, questionId);
			insertingStatement.execute();
			
			connection.close();
		}
	}
}
