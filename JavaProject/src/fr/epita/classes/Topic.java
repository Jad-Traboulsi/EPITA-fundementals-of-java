package fr.epita.classes;

public class Topic {
	private String topic;	
	private int id;
	
	public Topic() {
	}
	
	
	public Topic(String topic) {
		super();
		this.topic = topic;
	}
	public Topic(String topic,int id) {
		super();
		this.topic = topic;
		this.id = id;
	}

	

	public String getTopic() {
		return topic;
	}


	public void setTopic(String topic) {
		this.topic = topic;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	@Override
	public String toString() {
		return "Topic [topic=" + topic + ", id=" + id + "]";
	}
	
	
}
