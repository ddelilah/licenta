package app.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Entity
@Table(name = "hdd")
@Proxy(lazy = false)
public class HDD implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "hdd_id")
	private int hddId;

	@Column(name = "capacity")
	private float capacity;

	@Column(name = "name")
	private String name;

	public int getHddId() {
		return hddId;
	}

	public void setHddId(int hddId) {
		this.hddId = hddId;
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

	@Override
	public String toString() {
		return "HDD [hddId=" + hddId + ", capacity=" + capacity + ", name="
				+ name + "]";
	}
	
}