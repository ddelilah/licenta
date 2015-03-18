package models;

import intercloudmigration.models.Disk;
import intercloudmigration.models.Graphics;
import intercloudmigration.models.Network;
import intercloudmigration.models.OS;
import intercloudmigration.models.TemplateTagsEnum;

import java.io.Serializable;
import java.util.List;

public class TemplateModelON extends TemplateModel implements Serializable { 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
    private int cpu;
    private int memory;
    private List<Disk> disks;
    private Graphics graphics;
    private Network nic;
    private OS os;
    
	public TemplateModelON(int id, String name) {
		super(id, name);
		// TODO Auto-generated constructor stub
	}
	
	public TemplateModelON(String name, int cpu, int memory,
            List<Disk> disk, Graphics graphics, Network nic, OS os) {
        this.name = name;
        this.cpu = cpu;
        this.memory = memory;
        this.disks = disk;
        this.graphics = graphics;
        this.nic = nic;
        this.os = os;
    }

    public TemplateModelON() {
    }
    
    public String createStringFromDisks(){
        String disksString="";
        for(Disk disk:this.disks){
            disksString = disksString+TemplateTagsEnum.DISK.getType() + "= [" 
                    +TemplateTagsEnum.DISK.getFirstChild() + " = "
                    +disk.getDiskID()+","+
                    TemplateTagsEnum.DISK.getSecondChild()+ " = "
                    +disk.getImage().getImageId()+","+
                    TemplateTagsEnum.DISK.getThirdChild()+ " = "
                    +disk.getImage().getImageType()+
                    "]\n";
                
         }
     
      return disksString;
    }

    @Override
    public String toString() {
        return TemplateTagsEnum.NAME.getType() + " = " + this.getName() + "\n"
                + TemplateTagsEnum.CPU.getType() + " = " + this.getCpu() + "\n"
                + TemplateTagsEnum.MEMORY.getType() + " = " + this.getMemory() + "\n"
                + createStringFromDisks()
                + TemplateTagsEnum.GRAPHICS.getType() + "= [ "
                + TemplateTagsEnum.GRAPHICS.getFirstChild() + " = " + this.getGraphics().getListen() + ", "
                + TemplateTagsEnum.GRAPHICS.getSecondChild() + " = " + this.getGraphics().getType() + "] \n"
                + TemplateTagsEnum.NIC.getType() + " = [ "
                + TemplateTagsEnum.NIC.getFirstChild() + " = " + this.nic.getNetworkId() + "] \n"
                + TemplateTagsEnum.OS.getType() + " = ["
                + TemplateTagsEnum.OS.getFirstChild() + " = " + this.os.getArch()+","
                + TemplateTagsEnum.OS.getSecondChild() + " = " + this.os.getBoot()
                + "]";
    }

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the cpu
	 */
	public int getCpu() {
		return cpu;
	}

	/**
	 * @param cpu the cpu to set
	 */
	public void setCpu(int cpu) {
		this.cpu = cpu;
	}

	/**
	 * @return the memory
	 */
	public int getMemory() {
		return memory;
	}

	/**
	 * @param memory the memory to set
	 */
	public void setMemory(int memory) {
		this.memory = memory;
	}

	/**
	 * @return the disks
	 */
	public List<Disk> getDisks() {
		return disks;
	}

	/**
	 * @param disks the disks to set
	 */
	public void setDisks(List<Disk> disks) {
		this.disks = disks;
	}

	/**
	 * @return the graphics
	 */
	public Graphics getGraphics() {
		return graphics;
	}

	/**
	 * @param graphics the graphics to set
	 */
	public void setGraphics(Graphics graphics) {
		this.graphics = graphics;
	}

	/**
	 * @return the nic
	 */
	public Network getNic() {
		return nic;
	}

	/**
	 * @param nic the nic to set
	 */
	public void setNic(Network nic) {
		this.nic = nic;
	}

	/**
	 * @return the os
	 */
	public OS getOs() {
		return os;
	}

	/**
	 * @param os the os to set
	 */
	public void setOs(OS os) {
		this.os = os;
	}

	
	
	

}
