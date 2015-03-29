package app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name = "hdd")
public class HDD {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "hdd_id")
	private int idHdd;
	
	@Column(name = "capacity")
	private float capacity;
	
	@Column(name = "name")
	private String name;

	public int getIdHdd() {
		return idHdd;
	}

	public void setIdHdd(int idHdd) {
		this.idHdd = idHdd;
	}

	public float getCapacity() {
		return capacity;
	}

	public void setCapacity(float capacity) {
		this.capacity = capacity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}

