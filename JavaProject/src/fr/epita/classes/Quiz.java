package fr.epita.classes;

public class Quiz {
	private int id;
	private String title;
	
	public Quiz() {
		
	}
	
	public Quiz(String title) {
		super();
		this.id = id;
		this.title = title;
	}
	
	public Quiz(int id, String title) {
		super();
		this.id = id;
		this.title = title;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}




	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Quiz other = (Quiz) obj;
		if (id != other.id)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Quiz [id=" + id + ", title=" + title + "]";
	}
	
}
