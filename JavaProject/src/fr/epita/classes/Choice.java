package fr.epita.classes;

public class Choice {
	private String choiceString;	
	private int id;
	public Choice() {
	}

	public Choice(String choice) {
		super();
		this.choiceString = choice;
	}
	public Choice(String choice,int id) {
		super();
		this.choiceString = choice;
		this.id = id;
	}
	

	public String getChoiceString() {
		return choiceString;
	}

	public void setChoiceString(String choice) {
		this.choiceString = choice;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Choice other = (Choice) obj;
		if (choiceString == null) {
			if (other.choiceString != null)
				return false;
		} else if (!choiceString.equals(other.choiceString))
			return false;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Choice [choiceString=" + choiceString + ", id=" + id + "]";
	}


}
