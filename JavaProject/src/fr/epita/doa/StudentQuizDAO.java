package fr.epita.doa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.Hashtable;

import fr.epita.classes.FullQuestion;
import fr.epita.classes.FullQuiz;
import fr.epita.classes.Student;

public class StudentQuizDAO {
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
			
			String inserting = "select grade from public.\"Student_Quizes\" where id_quiz = ? and id_student = ?;";
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
	public void setGrade(Student student,FullQuiz fq) throws Exception{
		StudentDAO sdao =  new StudentDAO();
		QuizDAO qdao = new QuizDAO();
		QuestionsDOA questdao = new QuestionsDOA();
		
		int studentId = sdao.getStudentId(student) ;
		int quizId = qdao.getQuizId(fq.getQuiz());
		
		if(studentId== 0){
			throw new Exception("Student doesnt exist");
		}		
		else if(quizId== 0){
			throw new Exception("Quiz doesnt exist");
		}
		// check if already placed
		if(!isPresent(student,fq)) {
			StudentAnswersDAO studentAnswerdao = new StudentAnswersDAO();
			Hashtable<FullQuestion,String> answers =  studentAnswerdao.getStudentAnswers(student,fq);
			int studentGrade = 0;
			Enumeration<FullQuestion> keys = answers.keys();
			while(keys.hasMoreElements()) {
				FullQuestion key = keys.nextElement();
				
				String questionAnswer = key.getQuestion().getAnswer();
				String studentAnswer =answers.get(key);
				if(questionAnswer.equals(studentAnswer)) {
					studentGrade+=key.getQuestion().getDifficulty();
				}
			}

			Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java", "postgres", "");
			
			String inserting = "INSERT INTO public.\"Student_Quizes\"(id_student, id_quiz, grade)VALUES (?,?,?);";
			PreparedStatement insertingStatement = connection.prepareStatement(inserting);
			insertingStatement.setInt(1, studentId);
			insertingStatement.setInt(2, quizId);
			insertingStatement.setInt(3, studentGrade);
			insertingStatement.execute();
			connection.close();
			
			
		}
		
	}
	public void updateGrade(Student student,FullQuiz fq) throws Exception{
		StudentDAO sdao =  new StudentDAO();
		QuizDAO qdao = new QuizDAO();
		QuestionsDOA questdao = new QuestionsDOA();

		int studentId = sdao.getStudentId(student) ;
		int quizId = qdao.getQuizId(fq.getQuiz());
		
		if(studentId== 0){
			throw new Exception("Student doesnt exist");
		}		
		else if(quizId== 0){
			throw new Exception("Quiz doesnt exist");
		}else {
			StudentAnswersDAO studentAnswerdao = new StudentAnswersDAO();
			Hashtable<FullQuestion,String> answers =  studentAnswerdao.getStudentAnswers(student,fq);
			int studentGrade = 0;
			Enumeration<FullQuestion> keys = answers.keys();
			while(keys.hasMoreElements()) {
				FullQuestion key = keys.nextElement();
				
				String questionAnswer = key.getQuestion().getAnswer();
				String studentAnswer =answers.get(key);
				
				if(questionAnswer.equals(studentAnswer)) {
					studentGrade+=key.getQuestion().getDifficulty();
				}
			}

			Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java", "postgres", "");
			
			String inserting = "update public.\"Student_Quizes\" set grade= ? where id_student = ? and id_quiz = ?;";
			PreparedStatement insertingStatement = connection.prepareStatement(inserting);
			insertingStatement.setInt(1, studentGrade);
			insertingStatement.setInt(2, studentId);
			insertingStatement.setInt(3, quizId);
			insertingStatement.execute();
			connection.close();
			
			
		}
		
	}

	public int getGrade(Student student,FullQuiz fq) throws Exception{
		StudentDAO sdao =  new StudentDAO();
		QuizDAO qdao = new QuizDAO();
		QuestionsDOA questdao = new QuestionsDOA();
		int grade = 0;

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
			
			String inserting = "select grade from public.\"Student_Quizes\" where id_quiz = ? and id_student = ?;";
			PreparedStatement preparedStatement = connection.prepareStatement(inserting);
			preparedStatement.setInt(1, quizId);
			preparedStatement.setInt(2, studentId);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()) {
				grade = rs.getInt("grade");
			}
			connection.close();
			
			
		}
		return grade;
		
	}

}
