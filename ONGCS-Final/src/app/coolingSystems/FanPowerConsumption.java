package app.coolingSystems;

public class FanPowerConsumption {
	
	private HACS hacs = new HACS();
	private CACS cacs = new CACS();
	private ParallelPlacementStrategy parallel = new ParallelPlacementStrategy();
	
	
	public float computePercentageDecreaseInFanSpeed(float idealFanSpeed, float fanSpeed){
		return (float) ((fanSpeed - idealFanSpeed) / fanSpeed)*100;
	} 
	
	public float computePercentageDecreaseInAirVelocity(float idealAirVelocity, float airVelocity){
		return (float) ((airVelocity - idealAirVelocity) / airVelocity)*100;
	} 
	
	public float computePercentageDecreaseInFanPowerConsumption(float idealAirVelocity, float airVelocity){
		return (float) ((Math.pow(airVelocity, 3) - Math.pow(idealAirVelocity, 3)) / Math.pow(airVelocity, 3))*100;
	}

	public void getPercentagesHACS(float tIn, float airLoss){
		
		float idealAirMassFlowRate = hacs.computeMinMassFlowRate(tIn);
		float idealVolumetricAirFlow = hacs.computeVolumetricAirFlow(idealAirMassFlowRate);
		float idealAirVelocity = hacs.computeAirVelocity(idealVolumetricAirFlow);
		
		float airMassFlowRate =parallel.computeHeatRecirculation(airLoss,tIn);
		float volumetricAirFlow = parallel.computeVolumetricAirFlow(airMassFlowRate);
		float airVelocity = parallel.computeAirVelocity(volumetricAirFlow);
		
		System.out.println("\n\n\n[Computing % decrease in volumetric air flow]\n\n -------------------- [HACS]-------------------------");
		System.out.println("[Tin = "+ tIn+ " ]");
		System.out.println("[Ideal air mass flow rate] m = "+idealAirMassFlowRate);
		System.out.println("[Ideal volumetric air flow rate] f = "+idealVolumetricAirFlow);
		System.out.println("[Ideal air velocity] v = "+idealAirVelocity);

		System.out.println("---------------------------------------");
		System.out.println("\n[Loss = "+ airLoss+ " ]");
		System.out.println("[Air mass flow rate] m = "+airMassFlowRate);
		System.out.println("[Volumetric air flow rate] f = "+volumetricAirFlow);
		System.out.println("[Air velocity] v = "+airVelocity);
		//----------- COMPUTE DECREASE IN FAN POWER CONSUMPTION ----------------------------
		
		System.out.println("\nDecrease in Fan Speed is "+computePercentageDecreaseInFanSpeed(idealVolumetricAirFlow, volumetricAirFlow)+"%");
		System.out.println("Decrease in Air Velocity is "+computePercentageDecreaseInAirVelocity(idealAirVelocity,airVelocity)+"%");
		System.out.println("Decrease in Fan Power Consumption is "+computePercentageDecreaseInFanPowerConsumption(idealAirVelocity,airVelocity)+"%");
	}
	/*
public void getPercentagesCACS(float tIn, float airLoss){
		
		float idealAirMassFlowRate = cacs.computeMinMassFlowRate(tIn);
		float idealVolumetricAirFlow = cacs.computeVolumetricAirFlow(idealAirMassFlowRate);
		float idealAirVelocity = cacs.computeAirVelocity(idealVolumetricAirFlow);
		
		float airMassFlowRate =parallel.computeHeatRecirculation(airLoss,tIn);
		float volumetricAirFlow = parallel.computeVolumetricAirFlow(airMassFlowRate);
		float airVelocity = parallel.computeAirVelocity(volumetricAirFlow);
		
		System.out.println("[Computing % decrease in volumetric air flow]\n\n -------------------- [CACS]-------------------------");
		System.out.println("[Tin = "+ tIn+ " ]");
		System.out.println("[Ideal air mass flow rate] m = "+idealAirMassFlowRate);
		System.out.println("[Ideal volumetric air flow rate] f = "+idealVolumetricAirFlow);
		System.out.println("[Ideal air velocity] v = "+idealAirVelocity);

		System.out.println("---------------------------------------");
		System.out.println("\n[Loss = "+ airLoss+ " ]");
		System.out.println("[Air mass flow rate] m = "+airMassFlowRate);
		System.out.println("[Volumetric air flow rate] f = "+volumetricAirFlow);
		System.out.println("[Air velocity] v = "+airVelocity);
		//----------- COMPUTE DECREASE IN FAN POWER CONSUMPTION ----------------------------
		
		System.out.println("\nDecrease in Fan Speed is "+computePercentageDecreaseInFanSpeed(idealVolumetricAirFlow, volumetricAirFlow)+"%");
		System.out.println("Decrease in Air Velocity is "+computePercentageDecreaseInAirVelocity(idealAirVelocity,airVelocity)+"%");
		System.out.println("Decrease in Fan Power Consumption is "+computePercentageDecreaseInFanPowerConsumption(idealAirVelocity,airVelocity)+"%");
	}*/
	public static void main(String[]args){
		
		FanPowerConsumption fp = new FanPowerConsumption();
		
		fp.getPercentagesHACS(20, 0.1f);
		fp.getPercentagesHACS(20, 0.2f);
		fp.getPercentagesHACS(20, 0.3f);
		fp.getPercentagesHACS(20, 0.4f);
		fp.getPercentagesHACS(20, 0.5f);

	}
}
