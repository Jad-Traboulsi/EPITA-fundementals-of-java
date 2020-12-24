package fr.epita.classes;

import java.util.ArrayList;

public class FullQuiz {
	private Quiz quiz;
	private ArrayList<FullQuestion> fullQuestion;
	
	public FullQuiz() {
		
	}
	
	public FullQuiz(Quiz quiz, ArrayList<FullQuestion> fullQuestion) {
		super();
		this.quiz = quiz;
		this.fullQuestion = fullQuestion;
	}
	public Quiz getQuiz() {
		return quiz;
	}
	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}
	public ArrayList<FullQuestion> getFullQuestion() {
		return fullQuestion;
	}

	public void setFullQuestion(ArrayList<FullQuestion> fullQuestion) {
		this.fullQuestion = fullQuestion;
	}


	@Override
	public String toString() {
		return "FullQuiz [quiz=" + quiz + ", fullQuestion=" + fullQuestion + "]";
	}
	
	
}
