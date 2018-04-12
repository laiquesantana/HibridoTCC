package model;

public class Users {

	private int id;
	private String age;
	private String gender;
	private int occupation_id;
	private int zip_code;

	public Users(int id, int age,String gender, int occupation_id, int zip_code) {
		super();
		this.id = id;
		this.gender = gender;
		this.occupation_id = occupation_id;
		this.zip_code = zip_code;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getOccupation_id() {
		return occupation_id;
	}

	public void setOccupation_id(int occupation_id) {
		this.occupation_id = occupation_id;
	}

	public int getZip_code() {
		return zip_code;
	}

	public void setZip_code(int zip_code) {
		this.zip_code = zip_code;
	}


}
