package com.sellnews.demo;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "Employe_DetailsWithTimeStamp")
public class Employe {
	@Id
	@GeneratedValue
	private long id;
	@Column(name = "Emp_firstName")
	@NotEmpty
	@Max(50) 
	private String firstname;
	@Column(name = "Emp_lastName")
	@NotEmpty
	@Max(50)
	private String lastname;
	@NotEmpty
	@Max(50)
	@Column(name = "Emp_position")
	private String position;
	@NotEmpty
	@Max(20)
	@Column(name = "Emp_phone")
	private String phone;
	@NotEmpty
	@Max(50)
	@Column(name = "Emp_email")
	private String e_mail;
	@Column(name = "Emp_Status")
	private String emp_Status = "true";
	@Column(name = "Emp_RegisterDate")
	private Timestamp emp_registerDate;
	@Column(name = "LastModified")
	private Timestamp lastModified;
	@Column(name="token")
	private String token;

	@JsonProperty("lastModified")
	public Timestamp getLastModified() {
		return lastModified;
	}

	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}

	@JsonCreator
	public Employe() {

	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@JsonProperty("id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@JsonProperty("Firstname")
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	@JsonProperty("Lastname")
	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	@JsonProperty("Position")
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@JsonProperty("Phone")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@JsonProperty("E_mail")
	public String getE_mail() {
		return e_mail;
	}

	public void setE_mail(String e_mail) {
		this.e_mail = e_mail;
	}

	@JsonProperty("Emp_Status")
	public String getEmp_Status() {
		return emp_Status;
	}

	public void setEmp_Status(String emp_Status) {
		this.emp_Status = emp_Status;
	}

	@JsonProperty("Emp_registerDate")
	public Timestamp getEmp_registerDate() {
		return emp_registerDate;
	}

	public void setEmp_registerDate(Timestamp emp_registerDate) {
		this.emp_registerDate = emp_registerDate;
	}

	@JsonCreator
	@Override
	public String toString() {
		return new StringBuffer().append("[").append(" fn :").append(this.firstname).append(" ln :")
				.append(this.lastname).append(" mn :").append(this.phone).append(" em :").append(this.e_mail)
				.append(" pos :").append(this.position).append(" id :").append(this.id).append("]").toString();
	}

}
