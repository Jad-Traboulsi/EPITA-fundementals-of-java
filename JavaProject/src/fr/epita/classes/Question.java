package fr.epita.classes;


public class Question {
	
	private int id;
	private String question;
	private String answer;
	private int difficulty;
	
	public Question() {
	}

	public Question(String question, String answer, int difficulty) {
		super();
		this.question = question;
		this.answer = answer;
		this.difficulty = difficulty;
	}
	

	public Question(String question, String answer, int difficulty, int id) {
		super();
		this.question = question;
		this.answer = answer;
		this.difficulty = difficulty;
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	@Override
	public String toString() {
		return "Question [id=" + id + ", question=" + question + ", answer=" + answer + ", difficulty=" + difficulty
				+ "]";
	}


	
	
	

}
