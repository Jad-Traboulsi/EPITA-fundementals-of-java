package fr.epita.doa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Date;

import fr.epita.classes.Student;

//SELECT * from public."Students" where DATE_PART('month', dob) = 3;
//
//Date d1 = Date.from(t1.atStartOfDay(ZoneId.systemDefault()).toInstant());
public class StudentDAO {
	public int getStudentId(Student student) throws Exception {
		Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java",
				"postgres", "");
		String str = "SELECT id FROM public.\"Students\" where lower(first_name) = lower(?) and  lower(last_name) = lower(?) and  lower(gender) = lower(?) and  dob = ? ;";

		PreparedStatement preparedStatement = connection.prepareStatement(str);

		preparedStatement.setString(1, student.getFirst_name());
		preparedStatement.setString(2, student.getLast_name());
		preparedStatement.setString(3, student.getGender());
		preparedStatement.setDate(4,Date.valueOf(student.getDob()));

		ResultSet rs = preparedStatement.executeQuery();
		int id = 0;
		while (rs.next()) {
			id = rs.getInt("id");
		}
		connection.close();

		return id;
	}
	
	public Student getStudentByName(Student student) throws Exception {

		Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java",
				"postgres", "");
		String toBeUpdatedQuestions = "SELECT * FROM public.\"Students\" where lower(first_name) = lower(?) and  lower(last_name) = lower(?);";

		PreparedStatement preparedStatement = connection.prepareStatement(toBeUpdatedQuestions);

		preparedStatement.setString(1, student.getFirst_name());
		preparedStatement.setString(2, student.getLast_name());

		ResultSet rs = preparedStatement.executeQuery();
		int id = 0;
		String first_name = "";
		String last_name= "";
		Date dob = new Date(2000, 1, 1);
		String gender= "";
		while (rs.next()) {
			id = rs.getInt("id");
			first_name = rs.getString("first_name");
			last_name = rs.getString("last_name");
			gender = rs.getString("gender");
			dob = rs.getDate("dob");
		}
		connection.close();
		
		return new Student(id,first_name,last_name,dob.toLocalDate(),gender);
	}
	public ArrayList<Student> getStudentsByDate(String method,int methodValue) throws Exception {
		ArrayList<Student> out = new ArrayList<>();
		Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java",
				"postgres", "");
		String toBeUpdatedQuestions = "SELECT * FROM public.\"Students\" where DATE_PART(?, dob) = ?;";

		PreparedStatement preparedStatement = connection.prepareStatement(toBeUpdatedQuestions);

		preparedStatement.setString(1, method);
		preparedStatement.setInt(2, methodValue);

		ResultSet rs = preparedStatement.executeQuery();
		int id = 0;
		String first_name = "";
		String last_name= "";
		Date dob = new Date(2000, 1, 1);
		String gender= "";
		while (rs.next()) {
			id = rs.getInt("id");
			first_name = rs.getString("first_name");
			last_name = rs.getString("last_name");
			gender = rs.getString("gender");
			dob = rs.getDate("dob");
			out.add(new Student(id,first_name,last_name,dob.toLocalDate(),gender));
		}
		connection.close();
		
		return out;
	}
	public Student getStudentById(int idSearch) throws Exception {
		Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java",
				"postgres", "");
		String toBeUpdatedQuestions = "SELECT * FROM public.\"Students\" where id = ?;";

		PreparedStatement preparedStatement = connection.prepareStatement(toBeUpdatedQuestions);

		preparedStatement.setInt(1, idSearch);

		ResultSet rs = preparedStatement.executeQuery();
		int id = 0;
		String first_name = "";
		String last_name= "";
		Date dob = new Date(2000, 1, 1);
		String gender= "";
		while (rs.next()) {
			id = rs.getInt("id");
			first_name = rs.getString("first_name");
			last_name = rs.getString("last_name");
			gender = rs.getString("gender");
			dob = rs.getDate("dob");
		}
		connection.close();
		
		return new Student(id,first_name,last_name,dob.toLocalDate(),gender);
	}
	public ArrayList<Student> getAllStudents() throws Exception {
		ArrayList<Student> out = new ArrayList<>();
		Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java",
				"postgres", "");
		String toBeUpdatedQuestions = "SELECT * FROM public.\"Students\";";

		PreparedStatement preparedStatement = connection.prepareStatement(toBeUpdatedQuestions);


		ResultSet rs = preparedStatement.executeQuery();
		int id = 0;
		String first_name = "";
		String last_name= "";
		Date dob = new Date(2000, 1, 1);
		String gender= "";
		while (rs.next()) {
			id = rs.getInt("id");
			first_name = rs.getString("first_name");
			last_name = rs.getString("last_name");
			gender = rs.getString("gender");
			dob = rs.getDate("dob");
			out.add(new Student(id,first_name,last_name,dob.toLocalDate(),gender));
		}
		connection.close();
		
		return out;
	}
	
	public void createStudent(Student student) throws Exception {
		if(student.getFirst_name() == "" || student.getFirst_name() == " ") {
			throw new Exception("First name cannot be empty");
		}
		else if(student.getLast_name() == "" || student.getLast_name() == " ") {
			throw new Exception("Last name cannot be empty");
		}
		else if(!student.getGender().equalsIgnoreCase("M") && !student.getGender().equalsIgnoreCase("F") ) {
			throw new Exception("Invalid Gender. Gender should be one character");
		}
		else if(getStudentId(student) != 0){
			System.out.println("Student already exists");
		}
		else {
			
			Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java", "postgres", "");
			
			String inserting = "INSERT INTO public.\"Students\"(first_name, last_name, dob, gender)VALUES (?,?,?, ?);";		
			PreparedStatement insertingStatement = connection.prepareStatement(inserting);
			insertingStatement.setString(1, student.getFirst_name());
			insertingStatement.setString(2, student.getLast_name());
			insertingStatement.setDate(3,Date.valueOf(student.getDob()));
			insertingStatement.setString(4, student.getGender().toUpperCase());
			insertingStatement.execute();
			System.out.println("Student Created");
			connection.close();
		}
	}
	public void deleteStudent(Student student) throws Exception {
		int id = getStudentId(student);
		
		// check if student exists
		if (id == 0) {

			throw new Exception("Student not found");
		}
		// delete student
		else {
			Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java",
					"postgres", "");

			String str = "DELETE FROM public.\"Students\" WHERE id = " + id + ";";
			PreparedStatement preparedStatement = connection.prepareStatement(str);
			preparedStatement.executeUpdate();
			connection.close();
			System.out.println("Student Deleted");
		}
	}
	public void updateStudent(Student oldStudent,Student newStudent) throws Exception{
		int id= getStudentId(oldStudent);
		if(id == 0) {
			throw new Exception("Student doesnt exist");
		}
		else if(newStudent.getFirst_name() == "" || newStudent.getFirst_name() == " ") {
			throw new Exception("First name cannot be empty");
		}
		else if(newStudent.getLast_name() == "" || newStudent.getLast_name() == " ") {
			throw new Exception("Last name cannot be empty");
		}
		else if(!newStudent.getGender().equalsIgnoreCase("M") && !newStudent.getGender().equalsIgnoreCase("F") ) {
			throw new Exception("Invalid Gender. Gender should be one character");
		}
		else if(getStudentId(newStudent) != 0){
			System.out.println("Student already exists");
		}
		else {
			Connection connection = DriverManager
					.getConnection("jdbc:postgresql://localhost:5432/fundementals-of-java", "postgres", "");
			String str = "UPDATE public.\"Students\" set first_name=?, last_name=?, dob=?, gender=? where id =?";
			PreparedStatement insertingStatement = connection.prepareStatement(str);
			insertingStatement.setString(1, newStudent.getFirst_name());
			insertingStatement.setString(2, newStudent.getLast_name());
			insertingStatement.setDate(3,Date.valueOf(newStudent.getDob()));
			insertingStatement.setString(4, newStudent.getGender().toUpperCase());
			insertingStatement.setInt(5, id);
			insertingStatement.execute();
			connection.close();
			
		}
	}
	
}
