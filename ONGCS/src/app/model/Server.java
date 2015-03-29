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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import app.constants.ServerState;

@Entity
@Table(name = "server")
public class Server implements Comparable<Server> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "server_id")
	private int idServer;

	@Column(name = "state")
	private ServerState state;

	@Column(name = "name")
	private String name;

	@Column(name = "cooling_value")
	private float coolingValue;

	@Column(name = "power_value")
	private float powerValue;

	@Column(name = "utilization")
	private float utilization;

	@Column(name = "e_idle")
	private float idleEnergy;
	
	@ManyToOne
	//@JoinTable(name = "cpu", joinColumns = { @JoinColumn(name = "cpu_id", referencedColumnName = "cpu_id") }, inverseJoinColumns = { @JoinColumn(name = "cpu_id", referencedColumnName = "cpu_id") })
	private CPU cpu;
	
	@ManyToOne
//	@JoinTable(name = "ram", joinColumns = { @JoinColumn(name = "ram_id", referencedColumnName = "ram_id") }, inverseJoinColumns = { @JoinColumn(name = "ram_id", referencedColumnName = "ram_id") })
	private RAM ram;

	@ManyToOne
//	@JoinTable(name = "hdd", joinColumns = { @JoinColumn(name = "hdd_id", referencedColumnName = "hdd_id") }, inverseJoinColumns = { @JoinColumn(name = "hdd_id", referencedColumnName = "hdd_id") })
	private HDD hdd;

	@ManyToOne
//	@JoinTable(name = "rack", joinColumns = { @JoinColumn(name = "rack_id", referencedColumnName = "rack_id") }, inverseJoinColumns = { @JoinColumn(name = "rack_id", referencedColumnName = "rack_id") })
	private Rack rack;
	
	@OneToMany(mappedBy = "server",cascade  = CascadeType.ALL,fetch=FetchType.EAGER )
	private List<VirtualMachine> correspondingVMs = new ArrayList<VirtualMachine>();
	
	public Server() {

	}

	public Server(float idleEnergy, CPU cpu, RAM ram, HDD hdd) {
		super();
		this.idleEnergy = idleEnergy;
		this.cpu = cpu;
		this.ram = ram;
		this.hdd = hdd;
	}

	public Server(int idServer, ServerState state, String name,
			float coolingValue, float powerValue, float utilization,
			float idleEnergy, CPU cpu, RAM ram, HDD hdd, Rack rack) {
		super();
		this.idServer = idServer;
		this.state = state;
		this.name = name;
		this.coolingValue = coolingValue;
		this.powerValue = powerValue;
		this.utilization = utilization;
		this.idleEnergy = idleEnergy;
		this.cpu = cpu;
		this.ram = ram;
		this.hdd = hdd;
		this.rack = rack;
	}

	public int getIdServer() {
		return idServer;
	}

	public void setIdServer(int idServer) {
		this.idServer = idServer;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getCoolingValue() {
		return coolingValue;
	}

	public void setCoolingValue(float coolingValue) {
		this.coolingValue = coolingValue;
	}

	public float getPowerValue() {
		return powerValue;
	}

	public void setPowerValue(float powerValue) {
		this.powerValue = powerValue;
	}

	public float getUtilization() {
		return utilization;
	}

	public void setUtilization(float utilization) {
		this.utilization = utilization;
	}

	public float getIdleEnergy() {
		return idleEnergy;
	}

	public void setIdleEnergy(float idleEnergy) {
		this.idleEnergy = idleEnergy;
	}
	
	

	public ServerState getState() {
		return state;
	}

	public void setState(ServerState state) {
		this.state = state;
	}

	public CPU getCpu() {
		return cpu;
	}

	public void setCpu(CPU cpu) {
		this.cpu = cpu;
	}

	public RAM getRam() {
		return ram;
	}

	public void setRam(RAM ram) {
		this.ram = ram;
	}

	public HDD getHdd() {
		return hdd;
	}

	public void setHdd(HDD hdd) {
		this.hdd = hdd;
	}

	public Rack getRack() {
		return rack;
	}

	public void setRack(Rack rack) {
		this.rack = rack;
	}

	public List<VirtualMachine> getCorrespondingVMs() {
		return correspondingVMs;
	}

	public void setCorrespondingVMs(List<VirtualMachine> correspondingVMs) {
		this.correspondingVMs = correspondingVMs;
	}

	@Override
	public int compareTo(Server arg0) {
		// TODO Auto-generated method stub
		return 0;
	}




}
