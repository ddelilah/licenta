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
@Table(name = "cpu")
@Proxy(lazy = false)
public class CPU implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cpu_id")
	private int cpuId;

	@Column(name = "nr_cores")
	private int nr_cores;

	@Column(name = "frequency")
	private float frequency;

	@Column(name = "cpu_utilization")
	private float cpu_utilization;

	@Column(name = "name")
	private String name;

	public int getCpuId() {
		return cpuId;
	}

	public void setCpuId(int cpuId) {
		this.cpuId = cpuId;
	}

	public int getNr_cores() {
		return nr_cores;
	}

	public void setNr_cores(int nr_cores) {
		this.nr_cores = nr_cores;
	}

	public float getFrequency() {
		return frequency;
	}

	public void setFrequency(float frequency) {
		this.frequency = frequency;
	}

	public float getCpu_utilization() {
		return cpu_utilization;
	}

	public void setCpu_utilization(float cpu_utilization) {
		this.cpu_utilization = cpu_utilization;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "CPU [cpuId=" + cpuId + ", nr_cores=" + nr_cores
				+ ", frequency=" + frequency + ", cpu_utilization="
				+ cpu_utilization + ", name=" + name + "]";
	}

}
