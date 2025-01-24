package esea.esea_api.translation.dto;

import lombok.ToString;

@ToString
public class DeepLResponse {
    private String originalFilename;
    private String translatedFilename;
    private String originalFileUrl;
    private String translatedFileUrl;
    private String upFileUrl;
    private String downFileUrl;

    public DeepLResponse(String originalFilename, String translatedFilename, String originalFileUrl, String translatedFileUrl, String upFileUrl, String downFileUrl) {
        this.originalFilename = originalFilename;
        this.translatedFilename = translatedFilename;
        this.originalFileUrl = originalFileUrl;
        this.translatedFileUrl = translatedFileUrl;
        this.upFileUrl = upFileUrl;
        this.downFileUrl = downFileUrl;
    }

	public String getOriginalFilename() {
		return originalFilename;
	}

	public void setOriginalFilename(String originalFilename) {
		this.originalFilename = originalFilename;
	}

	public String getTranslatedFilename() {
		return translatedFilename;
	}

	public void setTranslatedFilename(String translatedFilename) {
		this.translatedFilename = translatedFilename;
	}

	public String getOriginalFileUrl() {
		return originalFileUrl;
	}

	public void setOriginalFileUrl(String originalFileUrl) {
		this.originalFileUrl = originalFileUrl;
	}

	public String getTranslatedFileUrl() {
		return translatedFileUrl;
	}

	public void setTranslatedFileUrl(String translatedFileUrl) {
		this.translatedFileUrl = translatedFileUrl;
	}

	public String getUpFileUrl() {
		return upFileUrl;
	}

	public void setUpFileUrl(String upFileUrl) {
		this.upFileUrl = upFileUrl;
	}

	public String getDownFileUrl() {
		return downFileUrl;
	}

	public void setDownFileUrl(String downFileUrl) {
		this.downFileUrl = downFileUrl;
	}
  
}

