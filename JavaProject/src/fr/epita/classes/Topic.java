package fr.epita.classes;

public class Topic {
	private String topicString;	
	private int id;
	
	public Topic() {
	}
	
	
	public Topic(String topic) {
		super();
		this.topicString = topic;
	}
	public Topic(String topic,int id) {
		super();
		this.topicString = topic;
		this.id = id;
	}

	

	public String getTopicString() {
		return topicString;
	}


	public void setTopicString(String topic) {
		this.topicString = topic;
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
		if (topicString == null) {
			if (other.topicString != null)
				return false;
		} else if (!topicString.equals(other.topicString))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Topic [topicString=" + topicString + ", id=" + id + "]";
	}
	
	
}
