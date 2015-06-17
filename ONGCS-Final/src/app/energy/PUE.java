package app.energy;

public class PUE {
	/*
	 * Power Usage Effectiveness - PUE metric is associated with the data center
	 * infrastructure. PUE is not a data center productivity metric, nor is it a
	 * standalone, comprehensive efficiency metric
	 */
	private float totalFacilityPower;
	private float itEquipmentPower;
	private float dcCoolingPower;
	private float dcPowerConsumption;

	public PUE() {
	}

	public PUE(float itEquipmentPower, float dcCoolingPower,
			float dcPowerConsumption) {
		this.itEquipmentPower = itEquipmentPower;
		this.dcCoolingPower = dcCoolingPower;
		this.dcPowerConsumption = dcPowerConsumption;
	}

	public float getPUE(float itEquipmentPower, float dcCoolingPower) {
		return (float) (itEquipmentPower + dcCoolingPower) / itEquipmentPower;
	}

	public float getDcCoolingPower() {
		return dcCoolingPower;
	}

	public void setDcCoolingPower(float dcCoolingPower) {
		this.dcCoolingPower = dcCoolingPower;
	}

	public float getDcPowerConsumption() {
		return dcPowerConsumption;
	}

	public void setDcPowerConsumption(float dcPowerConsumption) {
		this.dcPowerConsumption = dcPowerConsumption;
	}

	public float getTotalFacilityPower() {
		return totalFacilityPower;
	}

	public void setTotalFacilityPower(float totalFacilityPower) {
		this.totalFacilityPower = totalFacilityPower;
	}

	public float getItEquipmentPower() {
		return itEquipmentPower;
	}

	public void setItEquipmentPower(float itEquipmentPower) {
		this.itEquipmentPower = itEquipmentPower;
	}

}
