package esea.esea_api.statistics.dto;

public class DailyLlmUserUsageDtoImpl implements DailyLlmUserUsageDto {
    private String userId;
    private String llmModel;
    private Integer count;
    private Integer inputUsage;
    private Integer outputUsage;
    private Integer totalUsage;
    private String userName;
    private String deptNm;

    public DailyLlmUserUsageDtoImpl(String userId, String llmModel, Integer count, Integer inputUsage, Integer outputUsage, String userName, String deptNm) {
        this.userId = userId;
        this.llmModel = llmModel;
        this.count = count;
        this.inputUsage = inputUsage;
        this.outputUsage = outputUsage;
        this.totalUsage = inputUsage + outputUsage;
        this.userName = userName;
        this.deptNm = deptNm;
    }

    public DailyLlmUserUsageDtoImpl(DailyLlmUserUsageDto dto) {
        this.userId = dto.getUserId();
        this.llmModel = dto.getLlmModel();
        this.count = dto.getCount();
        this.inputUsage = dto.getInputUsage();
        this.outputUsage = dto.getOutputUsage();
        this.totalUsage = dto.getInputUsage() + dto.getOutputUsage();
        this.userName = dto.getUserName();
        this.deptNm = dto.getDeptNm();
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public String getLlmModel() {
        return llmModel;
    }

    @Override
    public Integer getCount() {
        return count;
    }

    @Override
    public Integer getInputUsage() {
        return inputUsage;
    }

    @Override
    public Integer getOutputUsage() {
        return outputUsage;
    }

    @Override
    public Integer getTotalUsage() {
        return totalUsage;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getDeptNm() {
        return deptNm;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public void setDeptNm(String deptNm) {
        this.deptNm = deptNm;
    }
}
