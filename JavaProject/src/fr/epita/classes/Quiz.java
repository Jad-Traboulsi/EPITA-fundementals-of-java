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
	public String toString() {
		return "Quiz [id=" + id + ", title=" + title + "]";
	}
	
}
