package fr.epita.launcher;

import java.time.LocalDate;
import java.util.ArrayList;


import fr.epita.classes.Question;
import fr.epita.classes.Quiz;
import fr.epita.classes.Student;
import fr.epita.classes.Topic;
import fr.epita.classes.Choice;
import fr.epita.classes.FullQuestion;
import fr.epita.classes.FullQuiz;
import fr.epita.doa.ChoiceDAO;
import fr.epita.doa.QuestionsDOA;
import fr.epita.doa.QuizDAO;
import fr.epita.doa.RelationQuestionDAO;
import fr.epita.doa.RelationQuizQuestionDAO;
import fr.epita.doa.StudentAnswersDAO;
import fr.epita.doa.StudentDAO;
import fr.epita.doa.StudentQuizDAO;
import fr.epita.doa.TopicDAO;

public class Launcher {
	public static void main(String[] args) throws Exception {
		

		RelationQuestionDAO relquestiondao = new RelationQuestionDAO();
		RelationQuizQuestionDAO relqqdao = new RelationQuizQuestionDAO();
		StudentDAO sdao = new StudentDAO();
		StudentAnswersDAO sanswersdao =  new  StudentAnswersDAO();
		StudentQuizDAO squizdao =  new  StudentQuizDAO();
		
//		Question question1 = new Question("What is this course's name?","Introduction to Java",1);
//		Choice choices1[] =new Choice[3];
//		choices1[0] = new Choice("Introduction to python");
//		choices1[1] = new Choice("Introduction to Java");
//		choices1[2] = new Choice("Introduction to C++");
//		Topic topics1[] = new Topic[2];
//		topics1[0] = new Topic("Introduction");
//		topics1[1] = new Topic("Basic");
//
//		
//		relquestiondao.createFullQuestion(new FullQuestion(question1,topics1,choices1));
//		
//		
//		System.out.println("Done 1");
//		Question question2 = new Question("What is your favorite programming language","Java",1);
//		Choice choices2[] =new Choice[3];
//		choices2[0] = new Choice("Python");
//		choices2[1] = new Choice("Java");
//		choices2[2] = new Choice("C++");
//		Topic topics2[] = new Topic[2];
//		topics2[0] = new Topic("Introduction");
//		topics2[1] = new Topic("Preference");
//		
//		relquestiondao.createFullQuestion(new FullQuestion(question2,topics2,choices2));
//		
//		System.out.println("Done 2");
//
//		ArrayList<FullQuestion> fqByTopic = relquestiondao.getAllRelatedToTopic(new Topic("Introduction"));
//		System.out.println(fqByTopic.size());
//		for(FullQuestion i :fqByTopic) {
//			System.out.println(i);
//		}
//		
//
//		FullQuiz out = relqqdao.getQuizFromTopic("Programming", new Topic("Introduction"),5);
//		FullQuiz out2 = relqqdao.getQuizFromTopic("Programming 2", new Topic("Preference"),5);
//		FullQuiz out3 = relqqdao.getQuizFromTopic("Programming 3", new Topic("Basic"),5);
//		System.out.println(out);
//		System.out.println(out2);
//		System.out.println(out3);
//		
//		for(FullQuiz i :relqqdao.getAllQuizes()) {
//			System.out.println(i);
//		}
		FullQuiz fq = relqqdao.getQuiz(new Quiz("Programming"));
//		relqqdao.outOnText(fq);
		Student s1 = new Student("Jad","Traboulsi", LocalDate.of(1996, 4, 16),"M");
//		sdao.createStudent(s1);
//		ArrayList<String> answers = new ArrayList<>();
//		answers.add("Introduction to Java");
//		answers.add("Java");
//		sanswersdao.createAnswers(s1, fq, answers);
		System.out.println(squizdao.getGrade(s1, fq));
		System.out.println(squizdao.totalMax(s1, fq));
	}
}
