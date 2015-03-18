package models;

public abstract class RackModel extends CloudModel {
	
	private String topOfTheRackSwitchAddress;

	public RackModel() {

	}

	public RackModel(int id, String name) {
		super(id, name);
	}


	public RackModel(String topOfTheRackSwitchAddress) {
		super();
		this.topOfTheRackSwitchAddress = topOfTheRackSwitchAddress;
	}

	public String getTopOfTheRackSwitchAddress() {
		return topOfTheRackSwitchAddress;
	}

	public void setTopOfTheRackSwitchAddress(String topOfTheRackSwitchAddress) {
		this.topOfTheRackSwitchAddress = topOfTheRackSwitchAddress;
	}

}
