package repository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "Words")
public class Word {

	private Integer id;
	private String string;

	@Id
	@Column(name = "word_id")
	public Integer getId() {
		return id;
	}

	@Column(name = "word_string")	
	public String getString() {
		return string;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setString(String string) {
		this.string = string;
	}

}
