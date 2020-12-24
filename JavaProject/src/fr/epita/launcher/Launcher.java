package fr.epita.launcher;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import fr.epita.classes.Question;
import fr.epita.classes.Quiz;
import fr.epita.classes.Student;
import fr.epita.classes.Topic;
import fr.epita.classes.Choice;
import fr.epita.classes.FullQuestion;
import fr.epita.doa.ChoiceDAO;
import fr.epita.doa.QuestionsDOA;
import fr.epita.doa.QuizDAO;
import fr.epita.doa.RelationQuestionDAO;
import fr.epita.doa.StudentDAO;
import fr.epita.doa.TopicDAO;

public class Launcher {
	public static void main(String[] args) throws Exception {
		

//		QuestionsDOA qdoa = new QuestionsDOA();
//		TopicDAO tdoa = new TopicDAO();
//		ChoiceDAO cdoa = new ChoiceDAO();
//		
//
//		Topic[]topics = new Topic[4];
//		topics[0] = new Topic("Programming Langauge");
//		topics[1] = new Topic("Programming");
//		topics[2] = new Topic("Java");
//		topics[3] = new Topic("Python");
//		
//		tdoa.createTopic(topics[3]);
//		ArrayList<Topic> all = tdoa.getAllTopics();
//		for(Topic i: all)
//		{
//			System.out.println(i);
//		}
//		//tdoa.deleteTopic(topics[1]);
//		
//
//		Choice[]choices = new Choice[3];
//		choices[0] = new Choice("Programming Langauge");
//		choices[1] = new Choice("All of the above");
//		choices[2] = new Choice("Java");
//		ArrayList<Choice> allChoices = cdoa.getAllChoices();
//		for(Choice i: allChoices)
//		{
//			System.out.println(i);
//		}
//		cdoa.createChoice(new Choice("ProgrammingLangauge"));
//		for(Choice i: choices)
//		{
//			cdoa.createChoice(i);
//		}
//		cdoa.deleteChoice(choices[0]);
//
//		cdoa.updateChoice(choices[2], choices[1]);
//		cdoa.updateChoice(choices[2], choices[1]);
//		cdoa.updateChoice(choices[2], choices[2]);
//		cdoa.updateChoice(new Choice(""), choices[2]);
//		cdoa.updateChoice(choices[2], new Choice(""));
//		cdoa.updateChoice(choices[2], new Choice("None"));
//		Question q = new Question("How Are You?!??","ProgrammingLangauge",1);
//		Question q2 = new Question("","Meh",-1);
//		for(Question i: qdoa.getAllQuestions())
//			System.out.println(i);
//		qdoa.updateQuestion(q.getQuestion(), q2);
//		
//		
//		FullQuestion full = new FullQuestion(q, topics, choices);
//		
//		RelationQuestionDAO reldao = new RelationQuestionDAO();
//		
//		reldao.createFullQuestion(full);
//		reldao.unRelateQuestionFromAll(full.getQuestion());
//		System.out.println(reldao.getAllRelatedToTopic(topics[3]));
//		reldao.unRelateChoiceFromAll(choices[0]);
//		System.out.println(reldao.getAllRelatedToTopic(topics[3]));

//		Student s1 = new Student("Nour","TR",LocalDate.of(1996, 4, 16), "m");
//		Student s2 = new Student("Jad","Traboulsi",LocalDate.of(1996, 4, 16), "m");
//		StudentDAO studentdao = new StudentDAO();

//		studentdao.updateStudent(s2, s1);
//		studentdao.createStudent(s1);
//		System.out.println(studentdao.getStudentId(s1));
//		
//		System.out.println(studentdao.getStudent(s1));
//		String method = "year";
//		int methodValue = 1996;		
	
//		Quiz q1 = new Quiz("Java Programming 1");
//		Quiz q2 = new Quiz("Advanced Java 1");
//		
//		QuizDAO qdao = new QuizDAO();
//		
//		
//		qdao.createQuiz(q1);
//		qdao.createQuiz(q2);
//		
//		for(Quiz i : qdao.getAllQuizes()) {
//			System.out.println(i);
//		}
//		
		
	}
}
