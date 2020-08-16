package net.thinkbase.dev.gittools.service.vo;

public class ExportResult {
	private boolean success = false;
	private String message;
	
	private String downloadAddress;

	public ExportResult(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	public ExportResult(String message) {
		this(true, message);
	}

	public boolean isSuccess() {
		return success;
	}

	public String getMessage() {
		return message;
	}

	public String getDownloadAddress() {
		return downloadAddress;
	}

	public void setDownloadAddress(String downloadAddress) {
		this.downloadAddress = downloadAddress;
	}
	
}
