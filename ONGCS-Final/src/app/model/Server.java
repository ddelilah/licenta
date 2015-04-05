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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;


@Entity
@Table(name = "server")
@Proxy(lazy = false)
public class Server implements Comparable<Server> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "server_id")
	private int serverId;

	@Column(name = "state")
	private String state;
	@Column(name = "name")
	private String name;

	@Column(name = "cooling_value")
	private float coolingValue;

	@Column(name = "power_value")
	private float powerValue;
	
	@Column(name = "server_MIPS")
	private int serverMIPS;

	@Column(name = "utilization")
	private float utilization;

	@Column(name = "e_idle")
	private float idleEnergy;

	@ManyToOne
	@JoinColumn(name = "cpu_id")
	private CPU cpu;

	@ManyToOne
	@JoinColumn(name = "ram_id")
	private RAM ram;

	@ManyToOne
	@JoinColumn(name = "hdd_id")
	private HDD hdd;

	@ManyToOne
	@JoinColumn(name = "rack_id")
	private Rack rack;

	@OneToMany(mappedBy = "server", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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

	public Server(int serverId, String state, String name, float coolingValue,
			float powerValue, float utilization, float idleEnergy, CPU cpu,
			RAM ram, HDD hdd, Rack rack) {
		super();
		this.serverId = serverId;
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

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
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

	public int getServerMIPS() {
		return serverMIPS;
	}

	public void setServerMIPS(int serverMIPS) {
		this.serverMIPS = serverMIPS;
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

	@Override
	public String toString() {
		return "Server [serverId=" + serverId + ", state=" + state + ", name="
				+ name + ", coolingValue=" + coolingValue + ", powerValue="
				+ powerValue + ", utilization=" + utilization + ", idleEnergy="
				+ idleEnergy + ", cpu=" + cpu + ", ram=" + ram + ", hdd=" + hdd
				+ ", rack=" + rack + ", correspondingVMs=" + correspondingVMs
				+ "]";
	}

}