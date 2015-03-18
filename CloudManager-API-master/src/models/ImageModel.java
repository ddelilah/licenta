package models;

import util.ImageType;

public abstract class ImageModel extends CloudModel {

	private ImageType type;
	private String description;
	private String path;

	/**
	 * @param id
	 * @param name
	 */
	public ImageModel(int id, String name) {
		super(id, name);
	}

	public ImageModel(int id, String name, String descripton, String path,
			ImageType type) {
		super(id, name);
		this.description = descripton;
		this.path = path;
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public ImageType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(ImageType type) {
		this.type = type;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

}
