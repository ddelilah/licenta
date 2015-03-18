package models;

public class ServerModelON extends ServerModel {

	private float cpuFrequency;
	private int totalCpu;// coreNO*100
	private float usedCpu;
	private int totalMem;
	private float usedMem;
	private int totalDisk;
	private float usedDisk;
	private int state;
	private String macAddress;

	public ServerModelON() {
	}

	public ServerModelON(int id, String ipAddress) {
		super(id, ipAddress);
	}

	/**
	 * @return the cpuFrequency
	 */
	public float getCpuFrequency() {
		return cpuFrequency;
	}

	/**
	 * @param cpuFrequency
	 *            the cpuFrequency to set
	 */
	public void setCpuFrequency(float cpuFrequency) {
		this.cpuFrequency = cpuFrequency;
	}

	/**
	 * @return the totalCpu
	 */
	public int getTotalCpu() {
		return totalCpu;
	}

	/**
	 * @param totalCpu
	 *            the totalCpu to set
	 */
	public void setTotalCpu(int totalCpu) {
		this.totalCpu = totalCpu;
	}

	/**
	 * @return the usedCpu
	 */
	public float getUsedCpu() {
		return usedCpu;
	}

	/**
	 * @param usedCpu
	 *            the usedCpu to set
	 */
	public void setUsedCpu(float usedCpu) {
		this.usedCpu = usedCpu;
	}

	/**
	 * @return the totalMem
	 */
	public int getTotalMem() {
		return totalMem;
	}

	/**
	 * @param totalMem
	 *            the totalMem to set
	 */
	public void setTotalMem(int totalMem) {
		this.totalMem = totalMem;
	}

	/**
	 * @return the usedMem
	 */
	public float getUsedMem() {
		return usedMem;
	}

	/**
	 * @param usedMem
	 *            the usedMem to set
	 */
	public void setUsedMem(float usedMem) {
		this.usedMem = usedMem;
	}

	/**
	 * @return the totalDisk
	 */
	public int getTotalDisk() {
		return totalDisk;
	}

	/**
	 * @param totalDisk
	 *            the totalDisk to set
	 */
	public void setTotalDisk(int totalDisk) {
		this.totalDisk = totalDisk;
	}

	/**
	 * @return the usedDisk
	 */
	public float getUsedDisk() {
		return usedDisk;
	}

	/**
	 * @param usedDisk
	 *            the usedDisk to set
	 */
	public void setUsedDisk(float usedDisk) {
		this.usedDisk = usedDisk;
	}

	/**
	 * @return the state
	 */
	public int getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(int state) {
		this.state = state;
	}

	/**
	 * @return the macAddress
	 */
	public String getMacAddress() {
		return macAddress;
	}

	/**
	 * @param macAddress
	 *            the macAddress to set
	 */
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
}
