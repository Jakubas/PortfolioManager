package my.app.domains.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import my.app.domains.stock.Stock;

@Entity
public class Tracker {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "stock_id")
	private Stock stock;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	public Tracker(Stock stock, User user) {
		this.stock = stock;
		this.user = user;
	}
	
	public Tracker() {}

	public int getId() {
		return id;
	}

	public Stock getStock() {
		return stock;
	}

	public User getUser() {
		return user;
	}
}
