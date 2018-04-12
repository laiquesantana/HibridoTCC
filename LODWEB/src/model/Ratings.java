package model;

public class Ratings {

	private int id;
	private int user_id;
	private int movie_id;
	private int rating;

	public Ratings(int id, int user_id, int movie_id, int rating) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.movie_id = movie_id;
		this.rating = rating;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getMovie_id() {
		return movie_id;
	}

	public void setMovie_id(int movie_id) {
		this.movie_id = movie_id;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	
}