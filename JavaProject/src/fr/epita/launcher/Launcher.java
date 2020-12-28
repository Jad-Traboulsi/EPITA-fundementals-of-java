package fr.epita.launcher;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

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

//		RelationQuestionDAO relquestiondao = new RelationQuestionDAO();
//		RelationQuizQuestionDAO relqqdao = new RelationQuizQuestionDAO();
//		StudentDAO sdao = new StudentDAO();
//		StudentAnswersDAO sanswersdao =  new  StudentAnswersDAO();
//		StudentQuizDAO squizdao =  new  StudentQuizDAO();

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
//		FullQuiz fq = relqqdao.getQuiz(new Quiz("Programming"));
//		relqqdao.outOnText(fq);
//		Student s1 = new Student("Jad","Traboulsi", LocalDate.of(1996, 4, 16),"M");
//		sdao.createStudent(s1);
//		ArrayList<String> answers = new ArrayList<>();
//		answers.add("Introduction to Java");
//		answers.add("Java");
//		sanswersdao.createAnswers(s1, fq, answers);
//		System.out.println(squizdao.getGrade(s1, fq));
//		System.out.println(squizdao.totalMax(s1, fq));
		menuUsingConsole();
	}

	public static void menuUsingConsole() throws Exception {
		int menu1 = 0;
		Scanner input = new Scanner(System.in);
		while (menu1 == 0) {
			System.out.println("Welcome to Jad's Quiz Program");
			System.out.println("What would you like to do?");
			System.out.println("1- If you're a Teacher");
			System.out.println("2- If you're a Student");
			System.out.println("3- If you want to quit");
			menu1 = input.nextInt();
			while (menu1 != 1 && menu1 != 2 && menu1 != 3) {
				System.out.println("Invalid input.");
				System.out.println("What would you like to do?");
				System.out.println("1- If you're a Teacher");
				System.out.println("2- If you're a Student");
				System.out.println("3- If you want to quit");
				menu1 = input.nextInt();
			}
			// teache
			while (menu1 == 1) {
				int menu2 = 0;
				System.out.println("What would you like to Access?");
				System.out.println("1- Access Questions");
				System.out.println("2- Access Students");
				System.out.println("3- Access Quizes");
				System.out.println("4- Exit");
				menu2 = input.nextInt();
				while (menu2 != 1 && menu2 != 2 && menu2 != 3 && menu2 != 4) {
					System.out.println("Invalid input.");
					System.out.println("What would you like to Access?");
					System.out.println("1- Access Questions");
					System.out.println("2- Access Students");
					System.out.println("3- Access Quizes");
					System.out.println("4- Exit");
					menu2 = input.nextInt();
				}
				if (menu2 == 4) {
					menu1 = 0;
					menu2 = 0;
					System.out.println("Thank you! Come again.");
					break;

				}

			}
			// student
			while (menu1 == 2) {
				int menu2 = 0;
				String firstName = "";
				String lastName = "";
				System.out.println("Input your first name");
				firstName = input.next();
				System.out.println("Input your last name");
				lastName = input.next();
				if (firstName.equals("-1") && lastName.equals("-1")) {
					System.out.println("Thank you, come again.");
					menu1 = 0;
					menu2 = 0;
					break;
				}
				StudentDAO sdao = new StudentDAO();

				Student student = sdao
						.getStudentByName(new Student(firstName, lastName, LocalDate.of(1996, 4, 16), "M"));
				while (student.getId() == 0) {
					System.out.println("Couldnt find you in the Database");
					System.out.println("Input your first name");
					firstName = input.next();
					System.out.println("Input your last name");
					lastName = input.next();

					if (firstName.equals("-1") && lastName.equals("-1")) {
						System.out.println("Thank you, come again.");
						menu1 = 0;
						menu2 = 0;
						break;
					}

					student = sdao.getStudentByName(new Student(firstName, lastName, LocalDate.of(1996, 4, 16), "M"));

				}
				System.err.println("Hello " + student.getFirst_name());
				while (menu2 != 3) {
					System.out.println("What would you like to do?");
					System.out.println("1- Answer quiz");
					System.out.println("2- Get grade of quiz");
					System.out.println("3- Exit");
					menu2 = input.nextInt();

					while (menu2 != 1 && menu2 != 2 && menu2 != 3) {
						System.out.println("Invalid input.");
						System.out.println("What would you like to do?");
						System.out.println("1- Answer quiz");
						System.out.println("2- Get grade of quiz");
						System.out.println("3- Exit");
						menu2 = input.nextInt();
					}
					if (menu2 == 1) {

						QuizDAO qdao = new QuizDAO();
						System.out.println("Enter Quiz ID");
						int quizId = input.nextInt();
						quizId = qdao.getQuizId(new Quiz(qdao.getTitleById(quizId)));
						if (quizId == 0) {
							System.out.println("Quiz not found");
						} else {
							StudentQuizDAO squizdao = new StudentQuizDAO();
							RelationQuizQuestionDAO rltnqqdao = new RelationQuizQuestionDAO();
							Quiz quiz = new Quiz(quizId, qdao.getTitleById(quizId));
							FullQuiz fullQuiz = rltnqqdao.getQuiz(quiz);
							int grade = squizdao.getGrade(student, fullQuiz);
							if (grade != -1) {
								System.out.println("You already did that quiz");
							} else {
								ArrayList<String> answers = new ArrayList<>();
								for(int i=0;i<fullQuiz.getFullQuestion().size();i++) {
									System.out.println(i+1+". "+fullQuiz.getFullQuestion().get(i).getQuestion().getQuestion());
									int randNumber = 0;
									Random rand= new Random();
									ArrayList<Integer> numbersChosen = new ArrayList<>();
									for(int j=0;j<fullQuiz.getFullQuestion().get(i).getChoices().length;j++) {

										System.out.print("\t");
										randNumber = rand.nextInt(fullQuiz.getFullQuestion().get(i).getChoices().length);
										while(numbersChosen.contains(randNumber)) {
											randNumber = rand.nextInt(fullQuiz.getFullQuestion().get(i).getChoices().length);
										}
										numbersChosen.add(randNumber);
										System.out.println(j+1+". "+fullQuiz.getFullQuestion().get(i).getChoices()[randNumber].getChoice());
									}
									System.out.println("Your answer is:");
									int picked = input.nextInt();
									String answer = fullQuiz.getFullQuestion().get(i).getChoices()[numbersChosen.get(picked-1)].getChoice();
									answers.add(answer);
								}
								StudentAnswersDAO sanswerdao = new StudentAnswersDAO();
								sanswerdao.createAnswers(student, fullQuiz, answers);
								squizdao.updateGrade(student, fullQuiz);
							}
						}

					}
					if (menu2 == 2) {
						QuizDAO qdao = new QuizDAO();
						System.out.println("Enter Quiz ID");
						int quizId = input.nextInt();
						quizId = qdao.getQuizId(new Quiz(qdao.getTitleById(quizId)));
						if (quizId == 0) {
							System.out.println("Quiz not found");
						} else {
							StudentQuizDAO squizdao = new StudentQuizDAO();
							RelationQuizQuestionDAO rltnqqdao = new RelationQuizQuestionDAO();
							Quiz quiz = new Quiz(quizId, qdao.getTitleById(quizId));
							FullQuiz fullQuiz = rltnqqdao.getQuiz(quiz);
							int grade = squizdao.getGrade(student, fullQuiz);
							if (grade == -1) {
								System.out.println("You havent done that exam yet");
							} else {
								int max = rltnqqdao.totalGradeOfQuiz(fullQuiz);
								System.out.println(student.getFirst_name() + ": " + quiz.getTitle() + " Grade = "
										+ grade + " / " + max);
							}
						}
					}
					if (menu2 == 3) {
						System.out.println("Thank you for using Jad's program!");
						menu1 = 0;
					}

				}
			}
			if (menu1 == 3) {
				System.out.println("Thanks for Jad's program!");
			}
			
		}
	}

}
