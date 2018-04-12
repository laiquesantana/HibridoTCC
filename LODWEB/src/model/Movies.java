package model;

public class Movies {

	private int id;
	private String title;
	private String description;
	private String imdb_url;
	private String release_date;

	public Movies(int id, String title,String release_date, String imdb_url, String description) {
		super();
		this.id = id;
		this.title = title;
		this.release_date = release_date;
		this.imdb_url = imdb_url;
		this.description = description;
	}

	public String getImdb_url() {
		return imdb_url;
	}

	public void setImdb_url(String imdb_url) {
		this.imdb_url = imdb_url;
	}

	public String getRelease_date() {
		return release_date;
	}

	public void setRelease_date(String release_date) {
		this.release_date = release_date;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


}
