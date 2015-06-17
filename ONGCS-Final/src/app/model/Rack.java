package app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Proxy;

/**
 * The Rack model class
 */

@Entity
@Table(name = "rack")
@Proxy(lazy = false)
public class Rack implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rack_id")
	private int rackId;

	@Column(name = "capacity")
	private float capacity;

	@Column(name = "name")
	private String name;

	@Column(name = "state")
	private String state;

	@Column(name = "utilization")
	private float utilization;

	@Column(name = "power_value")
	private float powerValue;

	@Column(name = "cooling_value")
	private float coolingValue;

	@OneToMany(mappedBy = "rack", cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Server> servers = new ArrayList<Server>();

	public int getRackId() {
		return rackId;
	}

	public void setRackId(int rackId) {
		this.rackId = rackId;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public float getUtilization() {
		return utilization;
	}

	public void setUtilization(float utilization) {
		this.utilization = utilization;
	}

	public float getPowerValue() {
		return powerValue;
	}

	public void setPowerValue(float powerValue) {
		this.powerValue = powerValue;
	}

	public float getCoolingValue() {
		return coolingValue;
	}

	public void setCoolingValue(float coolingValue) {
		this.coolingValue = coolingValue;
	}

	public List<Server> getServers() {
		return servers;
	}

	public void setServers(List<Server> servers) {
		this.servers = servers;
	}

	@Override
	public String toString() {
		return "Rack [rackId=" + rackId + ", capacity=" + capacity + ", name="
				+ name + ", state=" + state + ", utilization=" + utilization
				+ ", powerValue=" + powerValue + ", coolingValue="
				+ coolingValue + ", servers=" + servers + "]";
	}

}