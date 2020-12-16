package fr.epita.launcher;

import java.sql.SQLException;
import java.util.ArrayList;

import fr.epita.classes.Question;
import fr.epita.classes.Topic;
import fr.epita.classes.Choice;
import fr.epita.classes.FullQuestion;
import fr.epita.doa.ChoiceDAO;
import fr.epita.doa.QuestionsDOA;
import fr.epita.doa.RelationQuestionDAO;
import fr.epita.doa.TopicDAO;

public class Launcher {
	public static void main(String[] args) throws Exception {
		


		QuestionsDOA qdoa = new QuestionsDOA();
		TopicDAO tdoa = new TopicDAO();
		ChoiceDAO cdoa = new ChoiceDAO();
		

		Topic[]topics = new Topic[4];
		topics[0] = new Topic("Programming Langauge");
		topics[1] = new Topic("Programming");
		topics[2] = new Topic("Java");
		topics[3] = new Topic("Python");
		
		tdoa.createTopic(topics[3]);
		ArrayList<Topic> all = tdoa.getAllTopics();
		for(Topic i: all)
		{
			System.out.println(i);
		}
		//tdoa.deleteTopic(topics[1]);
		

		Choice[]choices = new Choice[3];
		choices[0] = new Choice("Programming Langauge");
		choices[1] = new Choice("All of the above");
		choices[2] = new Choice("Java");
		ArrayList<Choice> allChoices = cdoa.getAllChoices();
		for(Choice i: allChoices)
		{
			System.out.println(i);
		}
		cdoa.createChoice(new Choice("ProgrammingLangauge"));
		/*for(Choice i: choices)
		{
			cdoa.createChoice(i);
		}
		cdoa.deleteChoice(choices[0]);

		cdoa.updateChoice(choices[2], choices[1]);
		cdoa.updateChoice(choices[2], choices[1]);
		cdoa.updateChoice(choices[2], choices[2]);
		cdoa.updateChoice(new Choice(""), choices[2]);
		cdoa.updateChoice(choices[2], new Choice(""));
		cdoa.updateChoice(choices[2], new Choice("None"));*/
		
		Question q = new Question("How Are You?!??","ProgrammingLangauge",1);
		Question q2 = new Question("","Meh",-1);
		/*for(Question i: qdoa.getAllQuestions())
			System.out.println(i);
		qdoa.updateQuestion(q.getQuestion(), q2);
		*/
		
		FullQuestion full = new FullQuestion(q, topics, choices);
		
		RelationQuestionDAO reldao = new RelationQuestionDAO();
		
		reldao.createFullQuestion(full);
		//reldao.unRelateQuestionFromAll(full.getQuestion());
		System.out.println(reldao.getAllRelatedToTopic(topics[3]));
		reldao.unRelateChoiceFromAll(choices[0]);
		System.out.println(reldao.getAllRelatedToTopic(topics[3]));
		

		
	}
}
