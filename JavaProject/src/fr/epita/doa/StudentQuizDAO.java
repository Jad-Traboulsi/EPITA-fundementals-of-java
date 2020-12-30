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
import fr.epita.classes.Quiz;
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
	public void calculateGrade(Student student,FullQuiz fq) throws Exception{
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
	public void setGrade(Student student,FullQuiz fq,int grade) throws Exception{
		StudentDAO sdao =  new StudentDAO();
		QuizDAO qdao = new QuizDAO();

		int studentId = sdao.getStudentId(student) ;
		int quizId = qdao.getQuizId(fq.getQuiz());
		
		if(studentId== 0){
			throw new Exception("Student doesnt exist");
		}		
		else if(quizId== 0){
			throw new Exception("Quiz doesnt exist");
		}else {
			

			Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java", "postgres", "");
			
			String inserting = "Insert public.\"Student_Quizes\" (grade,id_student,id_quiz) values (?,?,?);";
			PreparedStatement insertingStatement = connection.prepareStatement(inserting);
			insertingStatement.setInt(1, grade);
			insertingStatement.setInt(2, studentId);
			insertingStatement.setInt(3, quizId);
			insertingStatement.execute();
			connection.close();
			
			
		}
		
	}

	public int getGrade(Student student,FullQuiz fq) throws Exception{
		StudentDAO sdao =  new StudentDAO();
		QuizDAO qdao = new QuizDAO();
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
	public Hashtable<Student,Integer> getQuizGrades(FullQuiz fq) throws Exception{
		StudentDAO sdao =  new StudentDAO();
		QuizDAO qdao = new QuizDAO();
		
		Hashtable<Student,Integer> out = new Hashtable<>();
		int quizId = qdao.getQuizId(fq.getQuiz());
		if(quizId== 0){
			throw new Exception("Quiz doesnt exist");
		}
		else {

			Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java", "postgres", "");
			
			String inserting = "select * from public.\"Student_Quizes\" where id_quiz = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(inserting);
			preparedStatement.setInt(1, quizId);
			ResultSet rs = preparedStatement.executeQuery();
			int grade = 0;
			int studentId = 0;
			while(rs.next()) {
				grade = rs.getInt("grade");
				studentId = rs.getInt("id_student");
				Student s = sdao.getStudentById(studentId);
				out.put(s,grade);
				
			}
			connection.close();
			
			
		}
		return out;
		
	}
	
	
	public ArrayList<FullQuiz> getStudentQuizesByGrade(Student student,int grade) throws Exception{
		ArrayList<FullQuiz> out = new ArrayList<>();
		
		StudentDAO sdao =  new StudentDAO();
		RelationQuizQuestionDAO rltnqqdao = new RelationQuizQuestionDAO();
		QuizDAO quizdao = new QuizDAO();

		int studentId = sdao.getStudentId(student) ;
		int quizId = 0;
		if(studentId== 0){
			throw new Exception("Student doesnt exist");
		}		
		else {

			Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java", "postgres", "");
			
			String inserting = "select id_quiz from public.\"Student_Quizes\" where grade = ? and id_student = ?;";
			PreparedStatement preparedStatement = connection.prepareStatement(inserting);
			preparedStatement.setInt(1, grade);
			preparedStatement.setInt(2, studentId);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()) {
				quizId = rs.getInt("id_quiz");
				Quiz quiz = new Quiz(quizId,quizdao.getTitleById(quizId));
				out.add(rltnqqdao.getQuiz(quiz));
				
			}
			connection.close();
			
			
		}
		
		return out;
	}
	// get all quizes and grades of specific student
	public Hashtable<FullQuiz, Integer> getAllStudentQuizes(Student student) throws Exception{
		Hashtable<FullQuiz, Integer> out = new Hashtable<>();
		
		StudentDAO sdao =  new StudentDAO();
		RelationQuizQuestionDAO rltnqqdao = new RelationQuizQuestionDAO();
		QuizDAO quizdao = new QuizDAO();

		int studentId = sdao.getStudentId(student) ;
		int quizId = 0;
		int grade = 0;
		if(studentId== 0){
			throw new Exception("Student doesnt exist");
		}		
		else {

			Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java", "postgres", "");
			
			String inserting = "select * from public.\"Student_Quizes\" where and id_student = ?;";
			PreparedStatement preparedStatement = connection.prepareStatement(inserting);
			preparedStatement.setInt(2, studentId);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()) {
				quizId = rs.getInt("id_quiz");
				grade = rs.getInt("grade");
				Quiz quiz = new Quiz(quizId,quizdao.getTitleById(quizId));
				out.put(rltnqqdao.getQuiz(quiz),grade);
				
			}
			connection.close();
			
			
		}
		
		return out;
	}
	
}
