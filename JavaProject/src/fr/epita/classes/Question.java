package fr.epita.classes;


public class Question {
	
	private int id;
	private String questionString;
	private String answer;
	private int difficulty;
	
	public Question() {
	}

	public Question(String question, String answer, int difficulty) {
		super();
		this.questionString = question;
		this.answer = answer;
		this.difficulty = difficulty;
	}
	

	public Question(String question, String answer, int difficulty, int id) {
		super();
		this.questionString = question;
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

	public String getQuestionString() {
		return questionString;
	}

	public void setQuestionString(String question) {
		this.questionString = question;
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Question other = (Question) obj;
		if (answer == null) {
			if (other.answer != null)
				return false;
		} else if (!answer.equals(other.answer))
			return false;
		if (difficulty != other.difficulty)
			return false;
		if (id != other.id)
			return false;
		if (questionString == null) {
			if (other.questionString != null)
				return false;
		} else if (!questionString.equals(other.questionString))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Question [id=" + id + ", questionString=" + questionString + ", answer=" + answer + ", difficulty=" + difficulty
				+ "]";
	}


	
	
	

}
