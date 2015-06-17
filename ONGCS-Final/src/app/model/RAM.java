package app.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

/**
 * The RAM model class
 */

@Entity
@Table(name = "ram")
@Proxy(lazy = false)
public class RAM implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ram_id")
	private int ramId;

	@Column(name = "capacity")
	private float capacity;

	@Column(name = "name")
	private String name;

	public int getRamId() {
		return ramId;
	}

	public void setRamId(int ramId) {
		this.ramId = ramId;
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
		return "RAM [ramId=" + ramId + ", capacity=" + capacity + ", name="
				+ name + "]";
	}

}
