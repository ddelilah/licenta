package app.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name = "cpu")
public class CPU {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "cpu_id")
	private int idCpu;
	
	@Column(name = "nr_cores")
	private int nr_cores;
	
	@Column(name = "frequency")
	private float frequency;

	@Column(name = "cpu_utilization")
	private float cpu_utilization;
	
	@Column(name = "name")
	private String name;

	public int getIdCpu() {
		return idCpu;
	}

	public void setIdCpu(int idCpu) {
		this.idCpu = idCpu;
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
	
}
