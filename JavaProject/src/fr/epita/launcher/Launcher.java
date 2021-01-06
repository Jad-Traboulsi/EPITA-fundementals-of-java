package fr.epita.launcher;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
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
	private static Random rand = new Random();

	public static void main(String[] args) throws IOException {
		menuUsingConsole();

	}

	public static void menuUsingConsole() throws IOException {
		int menu1 = 0;
		Scanner input = new Scanner(System.in);
		while (menu1 == 0) {
			System.out.println("Welcome to Jad's Quiz Program");
			System.out.println("What would you like to do?");
			System.out.println("1- If you're a Teacher");
			System.out.println("2- If you're a Student");
			System.out.println("3- If you want to quit");
			menu1 = Integer.parseInt(input.nextLine());

			while (menu1 != 1 && menu1 != 2 && menu1 != 3) {
				System.out.println("Invalid input.");
				System.out.println("What would you like to do?");
				System.out.println("1- If you're a Teacher");
				System.out.println("2- If you're a Student");
				System.out.println("3- If you want to quit");
				menu1 = Integer.parseInt(input.nextLine());

			}
			// teacher
			while (menu1 == 1) {
				int menu2 = 0;
				System.out.println("What would you like to Access?");
				System.out.println("1- Access Questions");
				System.out.println("2- Access Students");
				System.out.println("3- Access Quizes");
				System.out.println("4- Exit");
				menu2 = Integer.parseInt(input.nextLine());

				while (menu2 != 1 && menu2 != 2 && menu2 != 3 && menu2 != 4) {
					System.out.println("Invalid input.");
					System.out.println("What would you like to Access?");
					System.out.println("1- Access Questions");
					System.out.println("2- Access Students");
					System.out.println("3- Access Quizes");
					System.out.println("4- Exit");
					menu2 = Integer.parseInt(input.nextLine());

				}
				// Questions
				while (menu2 == 1) {
					int menu3 = 0;
					System.out.println("What would you like to do Questions?");
					System.out.println("1- Get All Full Questions");
					System.out.println("2- Modify Full Question");
					System.out.println("3- Add Full Question");
					System.out.println("4- Exit");
					menu3 = Integer.parseInt(input.nextLine());

					while (menu3 != 1 && menu3 != 2 && menu3 != 3 && menu3 != 4) {
						System.out.println("Invalid input.");
						System.out.println("What would you like to do Questions?");
						System.out.println("1- Get All Full Questions");
						System.out.println("2- Modify Full Questions");
						System.out.println("3- Add Full Question");
						System.out.println("4- Exit");
						menu3 = Integer.parseInt(input.nextLine());

					}
					if (menu3 == 1) {
						RelationQuestionDAO rltndao = new RelationQuestionDAO();
						int counter = 1;
						for (FullQuestion i : rltndao.getAll()) {
							System.out.println(counter + ". " + i.toString());
							counter += 1;
						}
						System.out.println("Total Full Questions found = " + (counter - 1));
					}
					while (menu3 == 2) {
						int menu4 = 0;

						RelationQuestionDAO rltndao = new RelationQuestionDAO();
						QuestionsDOA questiondao = new QuestionsDOA();
						System.out.println("  you want to modify?");
						System.out.println("Enter question ID");
						int id = Integer.parseInt(input.nextLine());

						Question quest = questiondao.getQuestion(id);
						if (quest.getQuestionString().equals("")) {
							System.out.println("Question not found");
							menu3 = 0;
						} else {
							while (menu4 != 4) {
								FullQuestion fullQuestion = rltndao.getAllRelatedToQuestion(quest);
								System.out.println(fullQuestion.toString());
								System.out.println("What do you want to modify?");
								System.out.println("1- Question");
								System.out.println("2- Topic");
								System.out.println("3- Choice");
								System.out.println("4- Exit");
								menu4 = Integer.parseInt(input.nextLine());

								while (menu4 != 1 && menu4 != 2 && menu4 != 3 && menu4 != 4) {
									System.out.println("Invalid Input");
									System.out.println("What do you want to modify?");
									System.out.println("1- Question");
									System.out.println("2- Topic");
									System.out.println("3- Choice");
									System.out.println("4- Exit");
									menu4 = Integer.parseInt(input.nextLine());

								}

								while (menu4 == 1) {

									int menu5 = 0;
									System.out.println("What do you want to modify?");
									System.out.println("1- Question");
									System.out.println("2- Answer");
									System.out.println("3- Difficulty");
									System.out.println("4- Exit");
									menu5 = Integer.parseInt(input.nextLine());

									while (menu5 != 1 && menu5 != 2 && menu5 != 3 && menu5 != 4) {
										System.out.println("Invalid Input");
										System.out.println("What do you want to modify?");
										System.out.println("1- Question");
										System.out.println("2- Answer");
										System.out.println("3- Difficulty");
										System.out.println("4- Exit");
										menu5 = Integer.parseInt(input.nextLine());

									}
									if (menu5 == 1) {
										System.out.println("Enter new Question");
										String newQuest = input.nextLine();
										questiondao.updateQuestion(quest, new Question(newQuest, "", -1));
										quest.setQuestionString(newQuest);
										System.out.println("Done");
									} else if (menu5 == 2) {
										System.out.println("Enter new Answer");
										String newAnswer = input.nextLine();
										questiondao.updateQuestion(quest, new Question("", newAnswer, -1));
										quest.setAnswer(newAnswer);
										System.out.println("Done");
									} else if (menu5 == 3) {
										System.out.println("Enter new Difficulty");
										int newDifficulty = Integer.parseInt(input.nextLine());
										questiondao.updateQuestion(quest, new Question("", "", newDifficulty));
										quest.setDifficulty(newDifficulty);
										System.out.println("Done");
									} else {
										menu4 = 0;
									}

								}
								if (menu4 == 2) {
									System.out.println(
											"Do you want to\n1- Add a topic to Full Question?\n2- Modify Full Question Topics?");
									int chosen1 = Integer.parseInt(input.nextLine());
									while (chosen1 != 1 && chosen1 != 2) {
										System.out.println("Invalid Input");
										System.out.println(
												"Do you want to\n1- Add a topic to Full Question?\n2- Modify Full Question Topics?");
										chosen1 = Integer.parseInt(input.nextLine());
									}
									if (chosen1 == 1) {
										System.out.println("Enter topic");
										String topicTitle = input.nextLine();

										rltndao.relateTopicToQuestion(quest, new Topic(topicTitle));
									} else if (chosen1 == 2) {
										System.out.println("Which topic you want to modify?");
										System.out.println("Enter the topic ID");
										int topicId = Integer.parseInt(input.nextLine());

										Topic[] topicsInFullQuestion = fullQuestion.getTopics();
										ArrayList<Integer> topicIds = new ArrayList<>();
										for (Topic i : topicsInFullQuestion) {
											topicIds.add(i.getId());
										}
										if (topicIds.contains(topicId)) {
											TopicDAO topicdao = new TopicDAO();
											Topic topicChosen = new Topic(topicdao.getTopic(topicId), topicId);
											System.out.println(topicChosen);
											System.out.println(
													"Do you want to \n1- Unrelate topic from question?\n2- Update topic?");
											int chosen = Integer.parseInt(input.nextLine());
											while (chosen != 1 && chosen != 2) {
												System.out.println("Invalid Input");
												System.out.println(
														"Do you want to \n1- Unrelate topic from question?\n2- Update topic?");
												chosen = Integer.parseInt(input.nextLine());
											}
											if (chosen == 1) {
												rltndao.unRelateTopicFromQuestion(topicChosen, quest);
											} else {
												System.out.println("Enter new topic");
												String newTopic = input.nextLine();
												Topic newCreatedTopic = new Topic(newTopic, topicId);
												Topic oldTopic = new Topic(topicdao.getTopic(topicId), topicId);
												if (topicdao.getID(newTopic) != 0) {
													rltndao.unRelateTopicFromQuestion(oldTopic, quest);
													rltndao.relateTopicToQuestion(quest, newCreatedTopic);
												} else {
													topicdao.updateTopic(oldTopic, newCreatedTopic);
												}
											}

										} else {
											System.out.println("Topic ID not in question");

										}
									}

								} else if (menu4 == 3) {
									System.out.println(
											"Do you want to\n1- Add a choice to Full Question?\n2- Modify Full Question Choices?");
									int chosen1 = Integer.parseInt(input.nextLine());
									while (chosen1 != 1 && chosen1 != 2) {
										System.out.println("Invalid Input");
										System.out.println(
												"Do you want to\n1- Add a choice to Full Question?\n2- Modify Full Question Choices?");
										chosen1 = Integer.parseInt(input.nextLine());
									}
									if (chosen1 == 1) {
										System.out.println("Enter choice");
										String choiceTitle = input.nextLine();

										rltndao.relateChoiceToQuestion(quest, new Choice(choiceTitle));
									} else if (chosen1 == 2) {
										System.out.println("Which choice you want to modify?");
										System.out.println("Enter the choice ID");
										int choiceId = Integer.parseInt(input.nextLine());

										Choice[] choicesInFullQuestion = fullQuestion.getChoices();
										ArrayList<Integer> choicesIds = new ArrayList<>();
										for (Choice i : choicesInFullQuestion) {
											choicesIds.add(i.getId());
										}
										if (choicesIds.contains(choiceId)) {
											ChoiceDAO choicedao = new ChoiceDAO();
											Choice oldChoice = new Choice(choicedao.getChoice(choiceId), choiceId);
											System.out.println(oldChoice);
											System.out.println(
													"Do you want to \n1- Unrelate choice from question?\n2- Update choice?");
											int chosen = Integer.parseInt(input.nextLine());
											while (chosen != 1 && chosen != 2) {
												System.out.println("Invalid Input");
												System.out.println(
														"Do you want to \n1- Unrelate choice from question?\n2- Update choice?");
												chosen = Integer.parseInt(input.nextLine());
											}
											if (chosen == 1) {
												rltndao.unRelateChoiceFromQuestion(oldChoice, quest);
											} else {
												System.out.println("Enter new choice");
												String newChoice = input.nextLine();
												Choice newCreatedChoice = new Choice(newChoice, choiceId);
												if (choicedao.getID(newChoice) != 0) {
													rltndao.unRelateChoiceFromQuestion(oldChoice, quest);
													rltndao.relateChoiceToQuestion(quest, newCreatedChoice);
												} else {
													choicedao.updateChoice(oldChoice, newCreatedChoice);
												}
											}
										} else {
											System.out.println("Choice ID not in question");
										}
									}
								} else if (menu4 == 4) {
									menu3 = 0;
								}
								if (!rltndao.answerInChoices(fullQuestion)) {
									menu3 = 2;
									menu4 = 0;
									System.out.println("Answer not in choices please recheck modifications");
								}
							}
						}

					}
					// add full question
					if (menu3 == 3) {
						System.out.println("Insert your question");
						String question = input.nextLine();
						System.out.println("Insert Answer");
						String answer = input.nextLine();
						System.out.println("Insert difficulty");
						int difficulty = Integer.parseInt(input.nextLine());
						Question questionCreated = new Question(question, answer, difficulty);

						System.out.println("How many topics you want to put for this question?");
						int topicSize = Integer.parseInt(input.nextLine());
						Topic[] topics = new Topic[topicSize];
						for (int i = 0; i < topicSize; i++) {
							System.out.println("Topic " + (i + 1));
							String topicString = input.nextLine();
							topics[i] = new Topic(topicString);
						}

						System.out.println("How many choices you want to put for this question?");
						int choiceSize = Integer.parseInt(input.nextLine());

						Choice[] choices = new Choice[choiceSize];
						for (int i = 0; i < choiceSize; i++) {
							System.out.println("Choice " + (i + 1));
							String choiceString = input.nextLine();
							choices[i] = new Choice(choiceString);
						}
						RelationQuestionDAO rltqdao = new RelationQuestionDAO();
						rltqdao.createFullQuestion(new FullQuestion(questionCreated, topics, choices));

					}
					if (menu3 == 4) {
						menu2 = 0;
					}
				}

				// Students
				while (menu2 == 2) {
					int menu3 = 0;
					System.out.println("What would you like to do Students?");
					System.out.println("1- Create a student");
					System.out.println("2- Get all students");
					System.out.println("3- Update student");
					System.out.println("4- Exit");
					menu3 = Integer.parseInt(input.nextLine());

					while (menu3 != 1 && menu3 != 2 && menu3 != 3 && menu3 != 4) {
						System.out.println("Invalid input.");

						System.out.println("What would you like to do Students?");
						System.out.println("1- Create a student");
						System.out.println("2- Get all students");
						System.out.println("3- Update student");
						System.out.println("4- Exit");
						menu3 = Integer.parseInt(input.nextLine());

					}
					if (menu3 == 1) {
						StudentDAO sdao = new StudentDAO();
						System.out.println("Enter Student First Name");
						String fname = input.nextLine();
						System.out.println("Enter Student Last Name");
						String lname = input.nextLine();

						System.out.println("Enter Student gender 1 character ");
						String gender = input.nextLine();
						if (gender.length() > 1) {
							gender = String.valueOf(gender.charAt(0));

						}
						while (!gender.equalsIgnoreCase("m") && !gender.equalsIgnoreCase("f")) {
							System.out.println("There are only two genders.");
							System.out.println("Invalid input please enter M or F");
							gender = input.nextLine();
							if (gender.length() > 1) {
								gender = String.valueOf(gender.charAt(0));

							}
						}

						System.out.println("Enter student birth year");
						int year = Integer.parseInt(input.nextLine());

						System.out.println("Enter student birth month");
						int month = Integer.parseInt(input.nextLine());

						System.out.println("Enter student birth day");
						int day = Integer.parseInt(input.nextLine());

						Student s1 = new Student(fname, lname, LocalDate.of(year, month, day), gender);
						sdao.createStudent(s1);
					}
					if (menu3 == 2) {
						StudentDAO sdao = new StudentDAO();
						int counter = 1;
						for (Student i : sdao.getAllStudents()) {
							System.out.println("Student " + counter + ": " + i.toString());
							counter += 1;
						}
						System.out.println("There are a total of " + (counter - 1) + " students");
					}
					while (menu3 == 3) {
						int menu4 = 0;
						StudentDAO sdao = new StudentDAO();
						System.out.println("Enter student ID you want to modify");
						int studentId = Integer.parseInt(input.nextLine());

						Student studentFound = sdao.getStudentById(studentId);
						if (studentFound.getId() == 0) {
							System.out.println("Student not found");
							menu3 = 0;
						} else {
							while (menu4 != 5) {
								studentFound = sdao.getStudentById(studentId);
								System.out.println(studentFound.toString());
								System.out.println("What do you want to modify?");
								System.out.println("1- Student first name");
								System.out.println("2- Student last name");
								System.out.println("3- Student gender");
								System.out.println("4- Student birthday");
								System.out.println("5- Exit");
								menu4 = Integer.parseInt(input.nextLine());

								Student newStudent = new Student();
								newStudent.setFirstName(studentFound.getFirstName());
								newStudent.setLastName(studentFound.getLastName());
								newStudent.setGender(studentFound.getGender());
								newStudent.setDob(studentFound.getDob());

								while (menu4 != 1 && menu4 != 2 && menu4 != 3 && menu4 != 4 && menu4 != 5) {
									System.out.println("Invalid Input");
									System.out.println("What do you want to modify?");
									System.out.println("1- Student first name");
									System.out.println("2- Student last name");
									System.out.println("3- Student gender");
									System.out.println("4- Student birthday");
									System.out.println("5- Exit");
									menu4 = Integer.parseInt(input.nextLine());

								}
								if (menu4 == 1) {
									System.out.println("Enter first name");
									String fname = input.nextLine();
									newStudent.setFirstName(fname);
									sdao.updateStudent(studentFound, newStudent);
								}
								if (menu4 == 2) {
									System.out.println("Enter last name");
									String lname = input.nextLine();
									newStudent.setLastName(lname);
									sdao.updateStudent(studentFound, newStudent);
								}
								if (menu4 == 3) {
									System.out.println("Enter gender 1 character ");
									String gender = input.nextLine();
									if (gender.length() > 1) {
										gender = String.valueOf(gender.charAt(0));

									}
									while (!gender.equalsIgnoreCase("m") && !gender.equalsIgnoreCase("f")) {
										System.out.println("There are only two genders.");
										System.out.println("Invalid input please enter M or F");
										gender = input.nextLine();
										if (gender.length() > 1) {
											gender = String.valueOf(gender.charAt(0));

										}
									}
									newStudent.setGender(gender);
									sdao.updateStudent(studentFound, newStudent);
								}
								if (menu4 == 4) {
									System.out.println("What do you want to update?");
									System.out.println("1- Birth Day");
									System.out.println("2- Birth Month");
									System.out.println("3- Birth Year");
									int chooser = Integer.parseInt(input.nextLine());

									while (chooser != 1 && chooser != 2 && chooser != 3) {
										System.out.println("Invalid Input");
										System.out.println("What do you want to update?");
										System.out.println("1- Birth Day");
										System.out.println("2- Birth Month");
										System.out.println("3- Birth Year");
									}

									if (chooser == 1) {
										System.out.println("Enter updated day");
										int newDay = Integer.parseInt(input.nextLine());
										LocalDate date = newStudent.getDob();
										LocalDate newDate = LocalDate.of(date.getYear(), date.getMonthValue(), newDay);
										newStudent.setDob(newDate);
										sdao.updateStudent(studentFound, newStudent);
									}
									if (chooser == 2) {
										System.out.println("Enter updated month");
										int newMonth = Integer.parseInt(input.nextLine());
										LocalDate date = newStudent.getDob();
										LocalDate newDate = LocalDate.of(date.getYear(), newMonth,
												date.getDayOfMonth());
										newStudent.setDob(newDate);
										sdao.updateStudent(studentFound, newStudent);
									}
									if (chooser == 3) {
										System.out.println("Enter updated year");
										int newYear = Integer.parseInt(input.nextLine());
										LocalDate date = newStudent.getDob();
										LocalDate newDate = LocalDate.of(newYear, date.getMonthValue(),
												date.getDayOfMonth());
										newStudent.setDob(newDate);
										sdao.updateStudent(studentFound, newStudent);
									}
								}
								if (menu4 == 5) {
									menu3 = 0;
								}
							}
						}
					}
					if (menu3 == 4) {
						menu2 = 0;
					}

				}
				// Quizes
				while (menu2 == 3) {
					int menu3 = 0;
					System.out.println("What would you like to do Quizes?");
					System.out.println("1- Create Full Quiz");
					System.out.println("2- Output Full Quiz on text");
					System.out.println("3- Assign quiz to student");
					System.out.println("4- Get All Quizes");
					System.out.println("5- Get student grade to quiz");
					System.out.println("6- Get all grades to quiz");
					System.out.println("7- Get all students who didn't finish their quizes");
					System.out.println("8- Get average of quiz");
					System.out.println("9- Get all quizes of specific student");
					System.out.println("10- Exit");
					menu3 = Integer.parseInt(input.nextLine());

					while (menu3 != 1 && menu3 != 2 && menu3 != 3 && menu3 != 4 && menu3 != 5 && menu3 != 6
							&& menu3 != 7 && menu3 != 8 && menu3 != 9 && menu3 != 10) {
						System.out.println("Invalid input.");
						System.out.println("What would you like to do Quizes?");
						System.out.println("1- Create Full Quiz");
						System.out.println("2- Output Full Quiz on text");
						System.out.println("3- Assign quiz to student");
						System.out.println("4- Get All Quizes");
						System.out.println("5- Get student grade to quiz");
						System.out.println("6- Get all grades to quiz");
						System.out.println("7- Get all students who didn't finish their quizes");
						System.out.println("8- Get average of quiz");
						System.out.println("9- Get all quizes of specific student");
						System.out.println("10- Exit");
						menu3 = Integer.parseInt(input.nextLine());

					}
					if (menu3 == 1) {
						System.out.println("Do you want to");
						System.out.println("1- Create Full Quiz from Topic");
						System.out.println("2- Create Own Full Quiz");
						int chosen = Integer.parseInt(input.nextLine());

						while (chosen != 1 && chosen != 2) {
							System.out.println("Invalid input");
							System.out.println("Do you want to");
							System.out.println("1- Create Full Quiz from Topic");
							System.out.println("2- Create Own Full Quiz");
							chosen = Integer.parseInt(input.nextLine());
						}

						if (chosen == 1) {
							TopicDAO tdao = new TopicDAO();
							ArrayList<Topic> allTopics = tdao.getAllTopics();
							for (Topic i : allTopics) {
								System.out.println(i);
							}
							System.out.println("What's the Topic you want?");
							System.out.println("Enter topic ID");
							int topicId = Integer.parseInt(input.nextLine());
							Topic chosenTopic = new Topic();
							boolean exists = false;
							for (Topic i : allTopics) {
								if (i.getId() == topicId) {
									chosenTopic = i;
									exists = true;
									break;
								}
							}
							if (!exists) {
								System.out.println("Couldnt find topic");
							} else {
								System.out.println("What is the Quiz Title?");
								String title = input.nextLine();
								System.out.println(
										"How many questions you want inside?\nNote that if not too many questions exists all questions related to topic will show");
								int numberOfQuestions = Integer.parseInt(input.nextLine());
								RelationQuizQuestionDAO rltqqdao = new RelationQuizQuestionDAO();
								rltqqdao.getQuizFromTopic(title, chosenTopic, numberOfQuestions);
							}
						}
						// create manual full Quiz
						else {
							System.out.println("What will the quiz title be?");
							String title = input.nextLine();
							Quiz quiz = new Quiz(title);

							ArrayList<FullQuestion> allQuestion = new ArrayList<>();

							System.out.println("How many Questions you want to create?");
							int questionSize = Integer.parseInt(input.nextLine());
							int i = 0;
							while (i < questionSize) {
								boolean questionExists = false;
								System.out.println("Enter Question " + (i + 1));
								String questionTitle = input.nextLine();

								for (int j = 0; j < allQuestion.size(); j++) {
									if (allQuestion.get(j).getQuestion().getQuestionString().equals(questionTitle)) {
										questionExists = true;
										break;
									}

								}
								while (questionExists) {
									questionExists = false;
									System.out.println("Question already inputted before");
									System.out.println("Enter Question " + (i + 1));
									questionTitle = input.nextLine();

									for (int j = 0; j < allQuestion.size(); j++) {
										if (allQuestion.get(j).getQuestion().getQuestionString()
												.equals(questionTitle)) {
											questionExists = true;
											break;
										}
									}
								} 

								System.out.println("Enter Answer");
								String answer = input.nextLine();

								System.out.println("Enter Question Difficulty");
								int difficulty = Integer.parseInt(input.nextLine());

								Question question = new Question(questionTitle, answer, difficulty);

								System.out.println("How many topics do you want?");
								int topicSize = Integer.parseInt(input.nextLine());
								Topic[] topics = new Topic[topicSize];
								for (int j = 0; j < topicSize; j++) {
									System.out.println("Enter topic:");
									String topicTitle = input.nextLine();
									topics[j] = new Topic(topicTitle);
								}

								System.out.println("How many choices do you want?");
								int choiceSize = Integer.parseInt(input.nextLine());
								Choice[] choices = new Choice[choiceSize];
								for (int j = 0; j < choiceSize; j++) {
									System.out.println("Enter topic:");
									String choiceTitle = input.nextLine();
									choices[j] = new Choice(choiceTitle);
								}

								FullQuestion fq = new FullQuestion(question, topics, choices);
								RelationQuestionDAO rltqdao = new RelationQuestionDAO();
								if (rltqdao.answerInChoices(fq)) {
									i--;
									System.out.println("Answer is not in questions, redo this question");
								} else {
									allQuestion.add(fq);
								}
								i++;
							}

							RelationQuizQuestionDAO rltqqdao = new RelationQuizQuestionDAO();
							FullQuiz fullQuiz = new FullQuiz(quiz, allQuestion);
							rltqqdao.createFullQuiz(fullQuiz);

						}
					} else if (menu3 == 2) {
						QuizDAO qdao = new QuizDAO();
						System.out.println("Enter Quiz ID");
						int quizId = Integer.parseInt(input.nextLine());
						Quiz quizFound = new Quiz(quizId, qdao.getTitleById(quizId));
						if (quizFound.getId() == 0) {
							System.out.println("Quiz not found");
						} else {

							RelationQuizQuestionDAO rltqqdao = new RelationQuizQuestionDAO();
							FullQuiz fq = rltqqdao.getQuiz(quizFound);
							rltqqdao.outOnText(fq);
						}
					} else if (menu3 == 3) {
						StudentDAO sdao = new StudentDAO();
						System.out.println("Input student ID");
						int studentId = Integer.parseInt(input.nextLine());
						Student studentFound = sdao.getStudentById(studentId);
						if (studentFound.getId() == 0) {
							System.out.println("Student not found");
						} else {
							QuizDAO qdao = new QuizDAO();
							System.out.println("Enter Quiz ID");
							int quizId = Integer.parseInt(input.nextLine());
							Quiz quizFound = new Quiz(quizId, qdao.getTitleById(quizId));
							if (quizFound.getId() == 0) {
								System.out.println("Quiz not found");
							} else {
								StudentQuizDAO sqdao = new StudentQuizDAO();
								RelationQuizQuestionDAO rltqqdao = new RelationQuizQuestionDAO();
								FullQuiz fq = rltqqdao.getQuiz(quizFound);
								if (!sqdao.isPresent(studentFound, fq)) {
									sqdao.setGrade(studentFound, fq, -1);
								} else {
									System.out.println("Quiz is already related to Student");
								}

							}
						}
					} else if (menu3 == 4) {
						RelationQuizQuestionDAO rltqqdao = new RelationQuizQuestionDAO();

						for (FullQuiz i : rltqqdao.getAllQuizes()) {
							System.out.println(i);
						}
					}
					// get student grade to quiz
					else if (menu3 == 5) {
						StudentDAO sdao = new StudentDAO();
						System.out.println("Input student ID");
						int studentId = Integer.parseInt(input.nextLine());
						Student studentFound = sdao.getStudentById(studentId);
						if (studentFound.getId() == 0) {
							System.out.println("Student not found");
						} else {
							QuizDAO qdao = new QuizDAO();
							System.out.println("Enter Quiz ID");
							int quizId = Integer.parseInt(input.nextLine());
							Quiz quizFound = new Quiz(quizId, qdao.getTitleById(quizId));
							if (quizFound.getId() == 0) {
								System.out.println("Quiz not found");
							} else {
								StudentQuizDAO sqdao = new StudentQuizDAO();
								RelationQuizQuestionDAO rltqqdao = new RelationQuizQuestionDAO();
								FullQuiz fq = rltqqdao.getQuiz(quizFound);
								int totalQuizGrade = rltqqdao.totalGradeOfQuiz(fq);
								if (sqdao.isPresent(studentFound, fq)) {
									int grade = sqdao.getGrade(studentFound, fq);
									if(grade!=-1) {
									System.out.println(studentFound.getFirstName() + ": Quiz :" + quizFound.getId()
											+ " Grade: " + grade + "/" + totalQuizGrade);
									}
									else {
										System.out.println(studentFound.getFirstName() + " didn't do the quiz yet");
									}
								} else {
									System.out.println("Quiz not related to Student");
								}

							}
						}

					}
					// all student grade to quiz
					else if (menu3 == 6) {
						StudentQuizDAO squizdao = new StudentQuizDAO();
						RelationQuizQuestionDAO rltqqdao = new RelationQuizQuestionDAO();
						QuizDAO quizdao = new QuizDAO();

						System.out.println("Enter quiz ID");
						int quizId = Integer.parseInt(input.nextLine());
						Quiz quizFound = new Quiz(quizId, quizdao.getTitleById(quizId));
						if (quizFound.getTitle().equals("")) {
							System.out.println("Quiz not found");
						} else {
							FullQuiz fq = rltqqdao.getQuiz(quizFound);
							int totalQuizGrade = rltqqdao.totalGradeOfQuiz(fq);
							Hashtable<Student, Integer> allStudentGrades = squizdao.getQuizGrades(fq);
							Enumeration<Student> keys = allStudentGrades.keys();
							int count = 1;
							while (keys.hasMoreElements()) {
								Student key = keys.nextElement();
								if (allStudentGrades.get(key) != -1) {
									System.out.println(key.getFirstName() + ": " + allStudentGrades.get(key) + " / "
											+ totalQuizGrade);
									count++;
								}
							}
							count--;
							if (count == 0) {
								System.out.println("No one did this quiz");
							} else {
								System.out.println("Total Number of students who did this is exam = " + count);
							}

						}

					}
					// average
					else if (menu3 == 8) {
						StudentQuizDAO squizdao = new StudentQuizDAO();
						RelationQuizQuestionDAO rltqqdao = new RelationQuizQuestionDAO();
						QuizDAO quizdao = new QuizDAO();

						System.out.println("Enter quiz ID");
						int quizId = Integer.parseInt(input.nextLine());
						Quiz quizFound = new Quiz(quizId, quizdao.getTitleById(quizId));
						if (quizFound.getTitle().equals("")) {
							System.out.println("Quiz not found");
						} else {
							Hashtable<Student, Integer> allStudentGrades = squizdao
									.getQuizGrades(rltqqdao.getQuiz(quizFound));
							Enumeration<Student> keys = allStudentGrades.keys();
							int total = 0;
							int count = 1;
							while (keys.hasMoreElements()) {
								Student key = keys.nextElement();
								if (allStudentGrades.get(key) != -1) {
									total += allStudentGrades.get(key);
									count++;
								}
							}
							count--;
							if (count == 0) {
								System.out.println("No one did this quiz");
							} else {
								System.out.println("Total = " + total);
								System.out.println("Average = " + total / count);
							}

						}

					}
					// students didnt do
					else if (menu3 == 7) {
						StudentQuizDAO squizdao = new StudentQuizDAO();
						RelationQuizQuestionDAO rltqqdao = new RelationQuizQuestionDAO();
						QuizDAO quizdao = new QuizDAO();

						System.out.println("Enter quiz ID");
						int quizId = Integer.parseInt(input.nextLine());
						Quiz quizFound = new Quiz(quizId, quizdao.getTitleById(quizId));
						if (quizFound.getTitle().equals("")) {
							System.out.println("Quiz not found");
						} else {
							FullQuiz fq = rltqqdao.getQuiz(quizFound);
							Hashtable<Student, Integer> allStudentGrades = squizdao.getQuizGrades(fq);
							Enumeration<Student> keys = allStudentGrades.keys();
							int count = 1;
							while (keys.hasMoreElements()) {
								Student key = keys.nextElement();
								if (allStudentGrades.get(key) == -1) {
									System.out.println(count + ": " + key.getFirstName());
									count++;
								}
							}
							count--;
							if (count == 0) {
								System.out.println("All assigned students did this quiz");
							} else {
								System.out.println("Total Number of students who didn't do this quiz = " + count);
							}

						}
					} else if (menu3 == 9) {
						StudentDAO sdao = new StudentDAO();
						System.out.println("Input student ID");
						int studentId = Integer.parseInt(input.nextLine());
						Student studentFound = sdao.getStudentById(studentId);
						if (studentFound.getId() == 0) {
							System.out.println("Student not found");
						} else {
							StudentQuizDAO squizdao = new StudentQuizDAO();
							Hashtable<FullQuiz, Integer> allQuizesAndGrades = squizdao
									.getAllStudentQuizes(studentFound);
							Enumeration<FullQuiz> keys = allQuizesAndGrades.keys();
							int counterDone = 0;
							int counterUndone = 0;
							while (keys.hasMoreElements()) {
								String out = "";
								FullQuiz key = keys.nextElement();
								String title = key.getQuiz().getTitle();
								if (allQuizesAndGrades.get(key) != -1) {
									out += counterDone + ". " + title + ": Grade: " + allQuizesAndGrades.get(key);
									System.out.println(out);
									counterDone += 1;
								} else {
									System.out.println(counterUndone + ". " + title + " UNDONE");
									counterUndone += 1;
								}
							}
							System.out.println("Total Done Quizes = " + counterDone);
							System.out.println("Total Undone Quizes = " + counterUndone);
						}
					} else if (menu3 == 10) {
						menu2 = 0;
					}

				}
				if (menu2 == 4) {
					menu1 = 0;
					System.out.println("Thank you! Come again.");

				}

			}

			// student
			while (menu1 == 2)

			{
				int menu2 = 0;
				String firstName = "";
				String lastName = "";
				System.out.println("Input your first name");
				firstName = input.nextLine();
				System.out.println("Input your last name");
				lastName = input.nextLine();
				if (firstName.equals("") && lastName.equals("")) {
					System.out.println("Thank you, come again.");
					menu1 = 0;
					menu2 = 0;
					break;
				}
				StudentDAO sdao = new StudentDAO();
				boolean broke = false;
				Student student = sdao
						.getStudentByName(new Student(firstName, lastName, LocalDate.of(1996, 4, 16), "M"));
				while (student.getId() == 0) {
					System.out.println("Couldnt find you in the Database");
					System.out.println("Input your first name");
					firstName = input.nextLine();
					System.out.println("Input your last name");
					lastName = input.nextLine();
					
					if (firstName.equals("") && lastName.equals("")) {
						System.out.println("Thank you, come again.");
						menu1 = 0;
						menu2 = 0;
						broke = true;
						break;
					}
					student = sdao.getStudentByName(new Student(firstName, lastName, LocalDate.of(1996, 4, 16), "M"));
					

				}
				if (!broke) {
					System.out.println("Hello " + student.getFirstName());
					while (menu2 != 5) {
						System.out.println("What would you like to do?");
						System.out.println("1- Answer quiz");
						System.out.println("2- Get grade of quiz");
						System.out.println("3- Get finshed quizes");
						System.out.println("4- Get unfinished quizes");
						System.out.println("5- Exit");
						menu2 = Integer.parseInt(input.nextLine());

						while (menu2 != 1 && menu2 != 2 && menu2 != 3 && menu2 != 4 && menu2 != 5) {
							System.out.println("Invalid input.");
							System.out.println("What would you like to do?");
							System.out.println("1- Answer quiz");
							System.out.println("2- Get grade of quiz");
							System.out.println("3- Get finshed quizes");
							System.out.println("4- Get unfinished quizes");
							System.out.println("5- Exit");
							menu2 = Integer.parseInt(input.nextLine());

						}
						if (menu2 == 1) {

							QuizDAO qdao = new QuizDAO();
							System.out.println("Enter Quiz ID");
							int quizId = Integer.parseInt(input.nextLine());

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
								} else if (!squizdao.isPresent(student, fullQuiz)) {
									System.out.println("This quiz is not assigned to you.");
								} else {
									ArrayList<String> answers = new ArrayList<>();

									for (int i = 0; i < fullQuiz.getFullQuestion().size(); i++) {
										System.out.println(i + 1 + ". "
												+ fullQuiz.getFullQuestion().get(i).getQuestion().getQuestionString());
										int randNumber = 0;
										ArrayList<Integer> numbersChosen = new ArrayList<>();
										for (int j = 0; j < fullQuiz.getFullQuestion().get(i)
												.getChoices().length; j++) {

											System.out.print("\t");
											randNumber = rand
													.nextInt(fullQuiz.getFullQuestion().get(i).getChoices().length);
											while (numbersChosen.contains(randNumber)) {
												randNumber = rand
														.nextInt(fullQuiz.getFullQuestion().get(i).getChoices().length);
											}
											numbersChosen.add(randNumber);
											System.out.println(j + 1 + ". "
													+ fullQuiz.getFullQuestion().get(i).getChoices()[randNumber]
															.getChoiceString());
										}
										System.out.println("Your answer is:");
										int picked = Integer.parseInt(input.nextLine());

										String answer = fullQuiz.getFullQuestion().get(i).getChoices()[numbersChosen
												.get(picked - 1)].getChoiceString();
										answers.add(answer);
									}
									StudentAnswersDAO sanswerdao = new StudentAnswersDAO();
									sanswerdao.createAnswers(student, fullQuiz, answers);
									squizdao.updateGrade(student, fullQuiz);
								}
							}

						} else if (menu2 == 2) {
							QuizDAO qdao = new QuizDAO();
							System.out.println("Enter Quiz ID");
							int quizId = Integer.parseInt(input.nextLine());

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
								} else if(!squizdao.isPresent(student, fullQuiz)) {
									System.out.println("Quiz is not assigned");
								}else {
									int max = rltnqqdao.totalGradeOfQuiz(fullQuiz);
									System.out.println(student.getFirstName() + ": " + quiz.getTitle() + " Grade = "
											+ grade + " / " + max);
								}
							}
						} else if (menu2 == 3) {
							StudentQuizDAO squizdao = new StudentQuizDAO();
							Hashtable<FullQuiz, Integer> quizesDone = squizdao.getAllStudentQuizes(student);
							if (quizesDone.size() == 0) {
								System.out.println("You haven't done any quiz");
							} else {
								Enumeration<FullQuiz> keys = quizesDone.keys();
								int counter = 1;
								while (keys.hasMoreElements()) {
									String out = "";
									FullQuiz key = keys.nextElement();
									String title = key.getQuiz().getTitle();
									if (quizesDone.get(key) != -1) {
										out += counter + ". " + title + ": Grade: " + quizesDone.get(key);
										System.out.println(out);
										counter += 1;
									}
								}
								System.out.println("Total quizes done = " + (counter - 1));
							}
						} else if (menu2 == 4) {
							StudentQuizDAO squizdao = new StudentQuizDAO();
							ArrayList<FullQuiz> quizesUndone = squizdao.getStudentQuizesByGrade(student, -1);
							if (quizesUndone.isEmpty()) {
								System.out.println("You have finished all your quizes");
							} else {
								int counter = 1;
								for (FullQuiz i : quizesUndone) {
									System.out.println(counter + ". " + i.getQuiz().getTitle() + " Quiz ID: "+ i.getQuiz().getId());
									counter += 1;
								}
								System.out.println("Total quizes undone = " + (counter - 1));

							}

						} else if (menu2 == 5) {
							System.out.println("Thank you for using Jad's program!");
							menu1 = 0;
						}
					}

				}
			}
			if (menu1 == 3) {
				System.out.println("Thanks for Jad's program!");
			}
		}

	}

}
