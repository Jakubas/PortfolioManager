package my.app.domains.stock;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Index {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@NotNull
	private String name;
	
	@NotNull
	private String ticker;
	
	public Index() {}
	
	public Index(String name, String ticker) {
		this.name = name;
		this.ticker = ticker;
	}

	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getTicker() {
		return ticker;
	}
}
