package fr.epita.doa;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import fr.epita.classes.FullQuestion;
import fr.epita.classes.FullQuiz;
import fr.epita.classes.Quiz;
import fr.epita.classes.Student;
import fr.epita.services.filereader.Reader;

public class StudentQuizDAO {
	Reader r = new Reader();
	String database = r.getDatabase();
	String username = r.getUsername();
	String password = r.getPassword();
	public boolean isPresent(Student student,FullQuiz fq)throws IOException{
		boolean exists = false;
		StudentDAO sdao =  new StudentDAO();
		QuizDAO qdao = new QuizDAO();
		int studentId = sdao.getStudentId(student) ;
		int quizId = qdao.getQuizId(fq.getQuiz());
		
		if(studentId== 0){
			throw new IOException("Student doesnt exist");
		}		
		else if(quizId== 0){
			throw new IOException("Quiz doesnt exist");
		}
		else {
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			try {
				connection = DriverManager.getConnection(database, username, password);
				String inserting = "select grade from public.\"Student_Quizes\" where id_quiz = ? and id_student = ?;";
				preparedStatement = connection.prepareStatement(inserting);
				preparedStatement.setInt(1, quizId);
				preparedStatement.setInt(2, studentId);
				ResultSet rs = preparedStatement.executeQuery();
				while(rs.next()) {
					exists = true;
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
			
		}
		return exists;
	}
	public void updateGrade(Student student,FullQuiz fq) throws IOException{
		StudentDAO sdao =  new StudentDAO();
		QuizDAO qdao = new QuizDAO();

		int studentId = sdao.getStudentId(student) ;
		int quizId = qdao.getQuizId(fq.getQuiz());
		
		if(studentId== 0){
			throw new IOException("Student doesnt exist");
		}		
		else if(quizId== 0){
			throw new IOException("Quiz doesnt exist");
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
			Connection connection = null;
			PreparedStatement insertingStatement = null;
			try {
				connection = DriverManager.getConnection(database, username, password);
				String inserting = "update public.\"Student_Quizes\" set grade= ? where id_student = ? and id_quiz = ?;";
				insertingStatement = connection.prepareStatement(inserting);
				insertingStatement.setInt(1, studentGrade);
				insertingStatement.setInt(2, studentId);
				insertingStatement.setInt(3, quizId);
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
	public void setGrade(Student student,FullQuiz fq,int grade) throws IOException{
		StudentDAO sdao =  new StudentDAO();
		QuizDAO qdao = new QuizDAO();

		int studentId = sdao.getStudentId(student) ;
		int quizId = qdao.getQuizId(fq.getQuiz());
		
		if(studentId== 0){
			throw new IOException("Student doesnt exist");
		}		
		else if(quizId== 0){
			throw new IOException("Quiz doesnt exist");
		}else {
			Connection connection = null;
			PreparedStatement insertingStatement = null;
			try {
				connection = DriverManager.getConnection(database, username, password);
				String inserting = "Insert into public.\"Student_Quizes\" (grade,id_student,id_quiz) values (?,?,?);";
				insertingStatement = connection.prepareStatement(inserting);
				insertingStatement.setInt(1, grade);
				insertingStatement.setInt(2, studentId);
				insertingStatement.setInt(3, quizId);
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

	public int getGrade(Student student,FullQuiz fq) throws IOException{
		StudentDAO sdao =  new StudentDAO();
		QuizDAO qdao = new QuizDAO();
		int grade = 0;

		int studentId = sdao.getStudentId(student) ;
		int quizId = qdao.getQuizId(fq.getQuiz());
		
		if(studentId== 0){
			throw new IOException("Student doesnt exist");
		}		
		else if(quizId== 0){
			throw new IOException("Quiz doesnt exist");
		}
		else {
			PreparedStatement preparedStatement = null;
			Connection connection = null;	
					
			try {
				connection = DriverManager.getConnection(database, username, password);
				String inserting = "select grade from public.\"Student_Quizes\" where id_quiz = ? and id_student = ?;";
				preparedStatement = connection.prepareStatement(inserting);
				preparedStatement.setInt(1, quizId);
				preparedStatement.setInt(2, studentId);
				ResultSet rs = preparedStatement.executeQuery();
				while(rs.next()) {
					grade = rs.getInt("grade");
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
			
			
			
		}
		return grade;
		
	}
	public Hashtable<Student,Integer> getQuizGrades(FullQuiz fq) throws IOException{
		StudentDAO sdao =  new StudentDAO();
		QuizDAO qdao = new QuizDAO();
		
		Hashtable<Student,Integer> out = new Hashtable<>();
		int quizId = qdao.getQuizId(fq.getQuiz());
		if(quizId== 0){
			throw new IOException("Quiz doesnt exist");
		}
		else {
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			try {
				connection = DriverManager.getConnection(database, username, password);
				String inserting = "select * from public.\"Student_Quizes\" where id_quiz = ?";
				preparedStatement = connection.prepareStatement(inserting);
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
	
	
	public ArrayList<FullQuiz> getStudentQuizesByGrade(Student student,int grade) throws IOException{
		ArrayList<FullQuiz> out = new ArrayList<>();
		
		StudentDAO sdao =  new StudentDAO();
		RelationQuizQuestionDAO rltnqqdao = new RelationQuizQuestionDAO();
		QuizDAO quizdao = new QuizDAO();

		int studentId = sdao.getStudentId(student) ;
		int quizId = 0;
		if(studentId== 0){
			throw new IOException("Student doesnt exist");
		}		
		else {
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			try {
				connection = DriverManager.getConnection(database, username, password);
				String inserting = "select id_quiz from public.\"Student_Quizes\" where grade = ? and id_student = ?;";
				preparedStatement = connection.prepareStatement(inserting);
				preparedStatement.setInt(1, grade);
				preparedStatement.setInt(2, studentId);
				ResultSet rs = preparedStatement.executeQuery();
				while(rs.next()) {
					quizId = rs.getInt("id_quiz");
					Quiz quiz = new Quiz(quizId,quizdao.getTitleById(quizId));
					out.add(rltnqqdao.getQuiz(quiz));
					
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
			
			
			
		}
		
		return out;
	}
	// get all quizes and grades of specific student
	public Hashtable<FullQuiz, Integer> getAllStudentQuizes(Student student) throws IOException{
		Hashtable<FullQuiz, Integer> out = new Hashtable<>();
		
		StudentDAO sdao =  new StudentDAO();
		RelationQuizQuestionDAO rltnqqdao = new RelationQuizQuestionDAO();
		QuizDAO quizdao = new QuizDAO();

		int studentId = sdao.getStudentId(student) ;
		int quizId = 0;
		int grade = 0;
		if(studentId== 0){
			throw new IOException("Student doesnt exist");
		}		
		else {
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			try {
				connection = DriverManager.getConnection(database, username, password);
				String inserting = "select * from public.\"Student_Quizes\" where id_student = ?;";
				preparedStatement = connection.prepareStatement(inserting);
				preparedStatement.setInt(1, studentId);
				ResultSet rs = preparedStatement.executeQuery();
				while(rs.next()) {
					quizId = rs.getInt("id_quiz");
					grade = rs.getInt("grade");
					Quiz quiz = new Quiz(quizId,quizdao.getTitleById(quizId));
					out.put(rltnqqdao.getQuiz(quiz),grade);
					
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
			
		}
		
		return out;
	}
	
}
