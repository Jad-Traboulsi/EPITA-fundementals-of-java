package fr.epita.doa;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;

import fr.epita.classes.Student;
import fr.epita.services.filereader.Reader;


public class StudentDAO {
	Reader r = new Reader();
	String database = r.getDatabase();
	String username = r.getUsername();
	String password = r.getPassword();
	public int getStudentId(Student student){
		int id = 0;
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(database, username, password);
			String str = "SELECT id FROM public.\"Students\" where lower(first_name) = lower(?) and  lower(last_name) = lower(?) and  lower(gender) = lower(?) and  dob = ? ;";

			preparedStatement = connection.prepareStatement(str);

			preparedStatement.setString(1, student.getFirstName());
			preparedStatement.setString(2, student.getLastName());
			preparedStatement.setString(3, student.getGender());
			preparedStatement.setDate(4,Date.valueOf(student.getDob()));

			ResultSet rs = preparedStatement.executeQuery();
			
			while (rs.next()) {
				id = rs.getInt("id");
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
		

		return id;
	}
	
	public Student getStudentByName(Student student){
		int id = 0;
		String firstName = "";
		String lastName= "";
		Date dob = new Date(System.currentTimeMillis());
		String gender= "";

		PreparedStatement preparedStatement = null;
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(database, username, password);
			String toBeUpdatedQuestions = "SELECT * FROM public.\"Students\" where lower(first_name) = lower(?) and  lower(last_name) = lower(?);";

			preparedStatement = connection.prepareStatement(toBeUpdatedQuestions);

			preparedStatement.setString(1, student.getFirstName());
			preparedStatement.setString(2, student.getLastName());

			ResultSet rs = preparedStatement.executeQuery();
			
			while (rs.next()) {
				id = rs.getInt("id");
				firstName = rs.getString("first_name");
				lastName = rs.getString("last_name");
				gender = rs.getString("gender");
				dob = rs.getDate("dob");
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
		
		
		return new Student(id,firstName,lastName,dob.toLocalDate(),gender);
	}
	public ArrayList<Student> getStudentsByDate(String method,int methodValue){
		ArrayList<Student> out = new ArrayList<>();

		PreparedStatement preparedStatement = null;
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(database, username, password);
			String toBeUpdatedQuestions = "SELECT * FROM public.\"Students\" where DATE_PART(?, dob) = ?;";

			preparedStatement = connection.prepareStatement(toBeUpdatedQuestions);

			preparedStatement.setString(1, method);
			preparedStatement.setInt(2, methodValue);

			ResultSet rs = preparedStatement.executeQuery();
			int id = 0;
			String firstName = "";
			String lastName= "";
			Date dob;
			String gender= "";
			while (rs.next()) {
				id = rs.getInt("id");
				firstName = rs.getString("first_name");
				lastName = rs.getString("last_name");
				gender = rs.getString("gender");
				dob = rs.getDate("dob");
				out.add(new Student(id,firstName,lastName,dob.toLocalDate(),gender));
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
	public Student getStudentById(int idSearch){
		int id = 0;
		String firstName = "";
		String lastName= "";
		Date dob = new Date(System.currentTimeMillis());
		String gender= "";
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(database, username, password);
			String toBeUpdatedQuestions = "SELECT * FROM public.\"Students\" where id = ?;";

			preparedStatement = connection.prepareStatement(toBeUpdatedQuestions);

			preparedStatement.setInt(1, idSearch);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				id = rs.getInt("id");
				firstName = rs.getString("first_name");
				lastName = rs.getString("last_name");
				gender = rs.getString("gender");
				dob = rs.getDate("dob");
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
		
		return new Student(id,firstName,lastName,dob.toLocalDate(),gender);
	}
	public ArrayList<Student> getAllStudents(){
		ArrayList<Student> out = new ArrayList<>();
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(database, username, password);
			String toBeUpdatedQuestions = "SELECT * FROM public.\"Students\";";

			preparedStatement = connection.prepareStatement(toBeUpdatedQuestions);


			ResultSet rs = preparedStatement.executeQuery();
			int id = 0;
			String firstName = "";
			String lastName= "";
			Date dob;
			String gender= "";
			while (rs.next()) {
				id = rs.getInt("id");
				firstName = rs.getString("first_name");
				lastName = rs.getString("last_name");
				gender = rs.getString("gender");
				dob = rs.getDate("dob");
				out.add(new Student(id,firstName,lastName,dob.toLocalDate(),gender));
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
	
	public void createStudent(Student student) throws IOException {
		if(student.getFirstName().equals("") || student.getFirstName().equals(" ")) {
			throw new IOException("First name cannot be empty");
		}
		else if(student.getLastName().equals("") || student.getLastName().equals(" ")) {
			throw new IOException("Last name cannot be empty");
		}
		else if(!student.getGender().equalsIgnoreCase("M") && !student.getGender().equalsIgnoreCase("F") ) {
			throw new IOException("Invalid Gender. Gender should be one character");
		}
		else if(getStudentId(student) != 0){
			System.out.println("Student already exists");
		}
		else {
			PreparedStatement preparedStatement = null;
			Connection connection = null;
			try {
				connection = DriverManager.getConnection(database, username, password);
				String inserting = "INSERT INTO public.\"Students\"(first_name, last_name, dob, gender)VALUES (?,?,?, ?);";		
				preparedStatement = connection.prepareStatement(inserting);
				preparedStatement.setString(1, student.getFirstName());
				preparedStatement.setString(2, student.getLastName());
				preparedStatement.setDate(3,Date.valueOf(student.getDob()));
				preparedStatement.setString(4, student.getGender().toUpperCase());
				preparedStatement.execute();
				System.out.println("Student Created");
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
	public void deleteStudent(Student student) throws IOException {
		int id = getStudentId(student);
		
		// check if student exists
		if (id == 0) {

			throw new IOException("Student not found");
		}
		// delete student
		else {
			PreparedStatement preparedStatement = null;
			Connection connection = null;
			try {
				connection = DriverManager.getConnection(database, username, password);

				String str = "DELETE FROM public.\"Students\" WHERE id = " + id + ";";
				preparedStatement = connection.prepareStatement(str);
				preparedStatement.executeUpdate();
				System.out.println("Student Deleted");
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
	public void updateStudent(Student oldStudent,Student newStudent) throws IOException{
		int id= getStudentId(oldStudent);
		if(id == 0) {
			throw new IOException("Student doesnt exist");
		}
		else if(newStudent.getFirstName().equals("") || newStudent.getFirstName().equals(" ")) {
			throw new IOException("First name cannot be empty");
		}
		else if(newStudent.getLastName().equals("")|| newStudent.getLastName().equals(" ")) {
			throw new IOException("Last name cannot be empty");
		}
		else if(!newStudent.getGender().equalsIgnoreCase("M") && !newStudent.getGender().equalsIgnoreCase("F") ) {
			throw new IOException("Invalid Gender. Gender should be one character");
		}
		else if(getStudentId(newStudent) != 0){
			System.out.println("Student already exists");
		}
		else {
			PreparedStatement preparedStatement = null;
			Connection connection = null;
			try {
				connection = DriverManager.getConnection(database, username, password);
				String str = "UPDATE public.\"Students\" set first_name=?, last_name=?, dob=?, gender=? where id =?";
				preparedStatement = connection.prepareStatement(str);
				preparedStatement.setString(1, newStudent.getFirstName());
				preparedStatement.setString(2, newStudent.getLastName());
				preparedStatement.setDate(3,Date.valueOf(newStudent.getDob()));
				preparedStatement.setString(4, newStudent.getGender().toUpperCase());
				preparedStatement.setInt(5, id);
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
