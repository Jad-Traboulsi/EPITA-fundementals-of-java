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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Topic other = (Topic) obj;
		if (id != other.id)
			return false;
		if (topic == null) {
			if (other.topic != null)
				return false;
		} else if (!topic.equals(other.topic))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Topic [topic=" + topic + ", id=" + id + "]";
	}
	
	
}
