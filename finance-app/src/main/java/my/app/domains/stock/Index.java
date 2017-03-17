package my.app.domains.stock;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Index_Fund") 
public class Index {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@NotNull
	private String name;
	
	@NotNull
	private String ticker;
	
	@OneToMany(mappedBy = "index")
	private List<IndexDailyInformation> indexDailyInformations;
	
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

	public List<IndexDailyInformation> getIndexDailyInformations() {
		return indexDailyInformations;
	}
}
