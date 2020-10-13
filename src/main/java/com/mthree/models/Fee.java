package com.mthree.models;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="fees")
public class Fee {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "fee_id")
	private int id;
	
	@Column(name="type")
	@Enumerated(value=EnumType.STRING)
	private FeeType type;
	
	@Column(name="value")
	private BigDecimal value;
	
	public Fee() {}
	
	public Fee(int id, FeeType type, BigDecimal value) {
		this.id = id;
		this.type = type;
		this.value = value;
	}

	
	/** 
	 * @return int
	 */
	public int getId() {
		return id;
	}

	
	/** 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	
	/** 
	 * @return FeeType
	 */
	public FeeType getType() {
		return type;
	}

	
	/** 
	 * @param type
	 */
	public void setType(FeeType type) {
		this.type = type;
	}

	
	/** 
	 * @return BigDecimal
	 */
	public BigDecimal getValue() {
		return value;
	}

	
	/** 
	 * @param value
	 */
	public void setValue(BigDecimal value) {
		this.value = value;
	}

	
	/** 
	 * @return String
	 */
	@Override
	public String toString() {
		return "Fee [id=" + id + ", type=" + type + ", value=" + value + "]";
	}
}
