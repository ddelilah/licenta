package app.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cooling")
public class Cooling {

	@Id
	@Column(name = "cooling_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int coolingId;

	@Column(name = "fan_speed")
	private float fanSpeed;

	@Column(name = "power_value")
	private float powerValue;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="rack_id")
	private Rack rack;

	public Cooling() {

	}

	public int getCoolingId() {
		return coolingId;
	}

	public void setCoolingId(int coolingId) {
		this.coolingId = coolingId;
	}

	public Rack getRack() {
		return rack;
	}

	public void setRack(Rack rack) {
		this.rack = rack;
	}

	public float getFanSpeed() {
		return fanSpeed;
	}

	public void setFanSpeed(float fanSpeed) {
		this.fanSpeed = fanSpeed;
	}

	public float getPowerValue() {
		return powerValue;
	}

	public void setPowerValue(float powerValue) {
		this.powerValue = powerValue;
	}

	@Override
	public String toString() {
		return "Cooling [coolingId=" + coolingId + ", fanSpeed=" + fanSpeed
				+ ", powerValue=" + powerValue + ", rack=" + rack + "]";
	}

}
