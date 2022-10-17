package stepanalyzer.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "currency")
@NamedQuery(name = "Currency.findAll", query = "SELECT c FROM Currency c")
public class Currency {
	@Id
	@SequenceGenerator(allocationSize = 1, name = "CURRENCY_TOKENCURRENCY_GENERATOR", sequenceName = "CURRENCY_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CURRENCY_TOKENCURRENCY_GENERATOR")
	@Column(name = "token_currency")
	private Long tokenCurrency;

	@Column(name = "currency_name", nullable = false)
	private String currencyName;

	public Long getTokenCurrency() {
		return tokenCurrency;
	}

	public void setTokenCurrency(Long tokenCurrency) {
		this.tokenCurrency = tokenCurrency;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

}
