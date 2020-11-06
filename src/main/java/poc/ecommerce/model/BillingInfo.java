package poc.ecommerce.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "billinginfo")
public class BillingInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String cardName;
	private String cardNumber;
	private String dates;

	/**
	 * @param cardName
	 * @param dates
	 * @param cardNumber
	 */
	public BillingInfo(String cardName, String cardNumber, String dates) {
		super();
		this.cardName = cardName;
		this.cardNumber = cardNumber;
		this.dates = dates;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public String getDates() {
		return dates;
	}

	public void setDates(String dates) {
		this.dates = dates;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	@Override
	public String toString() {
		return "BillingInfo [id=" + id + ", cardName=" + cardName + ", dates=" + dates + ", cardNumber=" + cardNumber
				+ "]";
	}

}