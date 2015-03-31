package app.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import app.constants.RackState;

@Entity
@Table(name = "rack")
public class Rack {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rack_id")
	private int rackId;

	@Column(name = "capacity")
	private float capacity;

	@Column(name = "name")
	private String name;

	@Column(name = "state")
	private RackState state;

	@Column(name = "utilization")
	private float utilization;

	@Column(name = "power_value")
	private float powerValue;

	@Column(name = "cooling_value")
	private float coolingValue;

	@OneToMany(mappedBy = "rack", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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

	public RackState getState() {
		return state;
	}

	public void setState(RackState state) {
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
}