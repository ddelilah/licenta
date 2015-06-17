package app.execution;

public class Time {

	private long startTime;
	private long endTime;

	public Time() {

	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public long getExecutionTime() {
		return this.endTime - this.startTime;
	}
}
