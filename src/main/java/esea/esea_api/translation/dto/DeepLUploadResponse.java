package esea.esea_api.translation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.ToString;

@ToString
public class DeepLUploadResponse {
    @JsonProperty("document_id")
    private String documentId;

    @JsonProperty("document_key")
    private String documentKey;

    // Getter, Setter
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDocumentKey() {
        return documentKey;
    }

    public void setDocumentKey(String documentKey) {
        this.documentKey = documentKey;
    }
}

