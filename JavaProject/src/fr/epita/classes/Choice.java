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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Choice other = (Choice) obj;
		if (choice == null) {
			if (other.choice != null)
				return false;
		} else if (!choice.equals(other.choice))
			return false;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Choice [choice=" + choice + ", id=" + id + "]";
	}


}
