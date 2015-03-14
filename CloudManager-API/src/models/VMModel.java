package models;

import util.VMState;

/**
 * Contains data regarding a Virtual Machine
 */
public abstract class VMModel extends CloudModel {

	private int templateId;
	private VMState state;
	private int serverId;
	private Float cpu;
	private int cores;
	private int memory;

	public VMModel(int id, String name) {
		super(id, name);
	}

	/**
	 * @param id
	 * @param name
	 * @param templateId
	 * @param state
	 * @param serverId
	 */
	public VMModel(int id, String name, int templateId, VMState state,
			int serverId) {
		super(id, name);
		this.templateId = templateId;
		this.state = state;
		this.serverId = serverId;
	}

	/**
	 * @return the cpu
	 */
	public Float getCpu() {
		return cpu;
	}

	/**
	 * @param cpu
	 *            the cpu to set
	 */
	public void setCpu(float cpu) {
		this.cpu = cpu;
	}

	/**
	 * @return the cores
	 */
	public int getCores() {
		return cores;
	}

	/**
	 * @param cores
	 *            the cores to set
	 */
	public void setCores(int cores) {
		this.cores = cores;
	}

	/**
	 * @return the memory
	 */
	public int getMemory() {
		return memory;
	}

	/**
	 * @param memory
	 *            the memory to set
	 */
	public void setMemory(int memory) {
		this.memory = memory;
	}

	/**
	 * @return the templateId
	 */
	public int getTemplateId() {
		return templateId;
	}

	/**
	 * @param templateId
	 *            the templateId to set
	 */
	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	/**
	 * @return the state
	 */
	public VMState getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(VMState state) {
		this.state = state;
	}

	/**
	 * @return the serverId
	 */
	public int getServerId() {
		return serverId;
	}

	/**
	 * @param serverId
	 *            the serverId to set
	 */
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

}
