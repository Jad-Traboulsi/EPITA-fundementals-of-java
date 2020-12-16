package fr.epita.classes;

public class Choice {
	private String choice;	
	private int id;
	public Choice() {
	}

	public Choice(String choice) {
		super();
		this.choice = choice;
	}
	public Choice(String choice,int id) {
		super();
		this.choice = choice;
		this.id = id;
	}
	

	public String getChoice() {
		return choice;
	}

	public void setChoice(String choice) {
		this.choice = choice;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Choice [choice=" + choice + ", id=" + id + "]";
	}


}
