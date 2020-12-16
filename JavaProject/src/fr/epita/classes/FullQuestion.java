package fr.epita.classes;

import java.util.Arrays;

public class FullQuestion {
	private Question question;
	private Topic[] topics;
	private Choice[] choices;
	
	
	public FullQuestion(Question question, Topic[] topics, Choice[] choices) {
		super();
		this.question = question;
		this.topics = topics;
		this.choices = choices;
	}

	public FullQuestion() {
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public Topic[] getTopics() {
		return topics;
	}

	public void setTopics(Topic[] topics) {
		this.topics = topics;
	}

	public Choice[] getChoices() {
		return choices;
	}

	public void setChoices(Choice[] choices) {
		this.choices = choices;
	}

	@Override
	public String toString() {
		return "FullQuestion [question=" + question + ", topics=" + Arrays.toString(topics) + ", choices="
				+ Arrays.toString(choices) + "]";
	}
	
	

}
