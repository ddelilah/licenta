package app.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import app.constants.VMState;

@Entity
@Table(name = "vm")
public class VirtualMachine implements Comparable<VirtualMachine> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "vm_id")
	private int idVm;

	@Column(name = "state")
	private VMState state;

	@Column(name = "name")
	private String name;

	@Column(name = "power_value")
	private float powerValue;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="cpu_id")
	private CPU cpu;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "ram_id")
	private RAM ram;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "hdd_id")
	private HDD hdd;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "server_id")
	private Server server;

	public VirtualMachine() {

	}

	public VirtualMachine(int idVm, VMState state, String name,
			float powerValue, CPU cpu, RAM ram, HDD hdd, Server server) {
		this.idVm = idVm;
		this.state = state;
		this.name = name;
		this.powerValue = powerValue;
		this.cpu = cpu;
		this.ram = ram;
		this.hdd = hdd;
		this.server = server;
	}

	public VirtualMachine(int idVm, VMState state, String name, CPU cpu,
			RAM ram, HDD hdd) {
		this.idVm = idVm;
		this.state = state;
		this.name = name;
		this.cpu = cpu;
		this.ram = ram;
		this.hdd = hdd;
	}

	public int getIdVm() {
		return idVm;
	}

	public void setIdVm(int idVm) {
		this.idVm = idVm;
	}

	public VMState getState() {
		return state;
	}

	public void setState(VMState state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPowerValue() {
		return powerValue;
	}

	public void setPowerValue(float powerValue) {
		this.powerValue = powerValue;
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

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	@Override
	public int compareTo(VirtualMachine o) {
		// TODO Auto-generated method stub
		return 0;
	}
}