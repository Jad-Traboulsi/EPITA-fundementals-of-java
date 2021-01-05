package fr.epita.doa;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import fr.epita.classes.FullQuestion;
import fr.epita.classes.FullQuiz;
import fr.epita.classes.Student;
import fr.epita.services.filereader.Reader;

public class StudentAnswersDAO {
	Reader r = new Reader();
	String database = r.getDatabase();
	String username = r.getUsername();
	String password = r.getPassword();
	
	
	public Hashtable<FullQuestion,String> getStudentAnswers(Student student,FullQuiz fq) throws IOException{
		
		Hashtable<FullQuestion,String> answers = new Hashtable<>();
		StudentDAO sdao = new StudentDAO();
		QuizDAO qdao = new QuizDAO();
		RelationQuestionDAO relquestdao = new RelationQuestionDAO();
		QuestionsDOA questdao = new QuestionsDOA();
		
		int studentId = sdao.getStudentId(student);
		int quizId = qdao.getQuizId(fq.getQuiz());
		
		if(studentId==0) {
			throw new IOException("Student doesnt exist");
		}
		else if(quizId== 0) {
			throw new IOException("Quiz doesnt exist");
		}
		else {
			PreparedStatement preparedStatement = null;
			Connection connection = null;
			try {
				connection = DriverManager.getConnection(database, username, password);
				String str = "SELECT * FROM public.\"Student_Answers\" where id_student = ? and id_quiz = ? ;";

				preparedStatement = connection.prepareStatement(str);

				preparedStatement.setInt(1, studentId);
				preparedStatement.setInt(2, quizId);
				ResultSet rs = preparedStatement.executeQuery();
				String answer = "";
				int questionId= 0;
				while(rs.next()) {
					answer = rs.getString("answer");
					questionId = rs.getInt("id_question");
					answers.put(relquestdao.getAllRelatedToQuestion(questdao.getQuestion(questionId)), answer);
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
		
		
		return answers;
	}
	
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
				String inserting = "select * from public.\"Student_Answers\" where id_quiz = ? and id_student = ?;";
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
	public void createAnswers(Student student,FullQuiz fq, ArrayList<String> answers) throws IOException{
		StudentDAO sdao =  new StudentDAO();
		QuizDAO qdao = new QuizDAO();
		QuestionsDOA questdao = new QuestionsDOA();
		int studentId = sdao.getStudentId(student) ;
		int quizId = qdao.getQuizId(fq.getQuiz());
		
		if(studentId== 0){
			throw new IOException("Student doesnt exist");
		}		
		else if(quizId== 0){
			throw new IOException("Quiz doesnt exist");
		}
		else if(answers.size()!=fq.getFullQuestion().size()) {
			throw new IOException("Answers arent equal to questions");
		}
		// check if already placed
		
		if(!isPresent(student, fq)) {
			for(int i = 0;i< answers.size();i++) {
				Connection connection = null;
				PreparedStatement preparedStatement = null;
				try {
					connection = DriverManager.getConnection(database, username, password);	
					

					String inserting = "INSERT INTO public.\"Student_Answers\"(id_student, id_quiz, id_question, answer)VALUES (?,?,?, ?);";	
					preparedStatement = connection.prepareStatement(inserting);
					preparedStatement.setInt(1, studentId);
					preparedStatement.setInt(2, quizId);
					preparedStatement.setInt(3, questdao.getID(fq.getFullQuestion().get(i).getQuestion().getQuestionString()));
					preparedStatement.setString(4, answers.get(i));
					preparedStatement.execute();
					
					StudentQuizDAO studentQuizdao = new StudentQuizDAO();
					studentQuizdao.updateGrade(student, fq);
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
	
	public void updateAnswer(Student student,FullQuiz quiz,FullQuestion question,String newAnswer) throws IOException{
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
			throw new IOException("Student doesnt exist");
		}		
		else if(quizId== 0){
			throw new IOException("Quiz doesnt exist");
		}
		else {
			for(FullQuestion i :questionsFound) {
				if(i.equals(questionChosen)) {
					exists = true;
					questionId = questdao.getID(i.getQuestion().getQuestionString());
					break;
				}
			}
		}
		if(exists) {
			PreparedStatement preparedStatement = null;
			Connection connection = null;
			try {
				connection = DriverManager.getConnection(database, username, password);
				String inserting = "update public.\"Student_Answers\" set answer = ? where id_student=? and id_quiz= ? and id_question = ?;";		

				preparedStatement = connection.prepareStatement(inserting);
				preparedStatement.setString(1, newAnswer);
				preparedStatement.setInt(2, studentId);
				preparedStatement.setInt(3, quizId);
				preparedStatement.setInt(4, questionId);
				preparedStatement.execute();
				
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
