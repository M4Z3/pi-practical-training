package de.ma.pi.automation;

import java.util.ArrayList;

public class ProcessInstance {
    ArrayList<Object> links = new ArrayList<Object>();
    private String id;
    private String definitionId;
    private String businessKey = null;
    private String caseInstanceId = null;
    private boolean ended;
    private boolean suspended;
    private String tenantId = null;


    // Getter Methods

    public String getId() {
        return id;
    }

    public String getDefinitionId() {
        return definitionId;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public String getCaseInstanceId() {
        return caseInstanceId;
    }

    public boolean getEnded() {
        return ended;
    }

    public boolean getSuspended() {
        return suspended;
    }

    public String getTenantId() {
        return tenantId;
    }

    // Setter Methods

    public void setId( String id ) {
        this.id = id;
    }

    public void setDefinitionId( String definitionId ) {
        this.definitionId = definitionId;
    }

    public void setBusinessKey( String businessKey ) {
        this.businessKey = businessKey;
    }

    public void setCaseInstanceId( String caseInstanceId ) {
        this.caseInstanceId = caseInstanceId;
    }

    public void setEnded( boolean ended ) {
        this.ended = ended;
    }

    public void setSuspended( boolean suspended ) {
        this.suspended = suspended;
    }

    public void setTenantId( String tenantId ) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return "ProcessInstance{" +
                "links=" + links +
                ", id='" + id + '\'' +
                ", definitionId='" + definitionId + '\'' +
                ", businessKey='" + businessKey + '\'' +
                ", caseInstanceId='" + caseInstanceId + '\'' +
                ", ended=" + ended +
                ", suspended=" + suspended +
                ", tenantId='" + tenantId + '\'' +
                '}';
    }
}
