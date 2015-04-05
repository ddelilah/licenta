package app.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

import app.constants.VMState;

@Entity
@Table(name = "vm")
@Proxy(lazy = false)
public class VirtualMachine implements Comparable<VirtualMachine>,Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "vm_id")
	private int vmId;

	@Column(name = "state")
	private String state;

	@Column(name = "name")
	private String name;

	@Column(name = "vm_mips")
	private int vmMips;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "cpu_id")
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

	public VirtualMachine(int vmId, String state, String name,
			int vmMips, CPU cpu, RAM ram, HDD hdd, Server server) {
		this.vmId = vmId;
		this.state = state;
		this.name = name;
		this.vmMips = vmMips;
		this.cpu = cpu;
		this.ram = ram;
		this.hdd = hdd;
		this.server = server;
	}

	public VirtualMachine(int vmId, String state, String name, CPU cpu,
			RAM ram, HDD hdd) {
		this.vmId = vmId;
		this.state = state;
		this.name = name;
		this.cpu = cpu;
		this.ram = ram;
		this.hdd = hdd;
	}

	public int getVmId() {
		return vmId;
	}

	public void setVmId(int vmId) {
		this.vmId = vmId;
	}

	public String getState() {
		return state;
	}

	public void setState(String string) {
		this.state = string;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getVmMips() {
		return vmMips;
	}

	public void setVmMips(int vmMips) {
		this.vmMips = vmMips;
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

	@Override
	public String toString() {
		return "VirtualMachine [vmId=" + vmId + ", state=" + state + ", name="
				+ name + ", vmMips=" + vmMips + ", cpu=" + cpu.getCpuId()
				+ ", ram=" + ram.getRamId() + ", hdd=" + hdd.getHddId() + ", server=" + server.getServerId() + "]";
	}
	
}