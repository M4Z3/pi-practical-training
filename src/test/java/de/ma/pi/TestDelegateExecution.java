package de.ma.pi;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineServices;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.runtime.Incident;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.FlowElement;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TestDelegateExecution implements DelegateExecution {

    Map<String, Object> vars = new HashMap<>();
    TestDelegateExecution(Map<String, Object> vars){
        this.vars.putAll(vars);
    }

    @Override
    public String getProcessInstanceId() {
        return null;
    }

    @Override
    public String getProcessBusinessKey() {
        return null;
    }

    @Override
    public void setProcessBusinessKey(String s) {

    }

    @Override
    public String getProcessDefinitionId() {
        return null;
    }

    @Override
    public String getParentId() {
        return null;
    }

    @Override
    public String getCurrentActivityId() {
        return null;
    }

    @Override
    public String getCurrentActivityName() {
        return null;
    }

    @Override
    public String getActivityInstanceId() {
        return null;
    }

    @Override
    public String getParentActivityInstanceId() {
        return null;
    }

    @Override
    public String getCurrentTransitionId() {
        return null;
    }

    @Override
    public DelegateExecution getProcessInstance() {
        return null;
    }

    @Override
    public DelegateExecution getSuperExecution() {
        return null;
    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public String getTenantId() {
        return null;
    }

    @Override
    public void setVariable(String s, Object o, String s1) {

    }

    @Override
    public Incident createIncident(String s, String s1) {
        return null;
    }

    @Override
    public Incident createIncident(String s, String s1, String s2) {
        return null;
    }

    @Override
    public void resolveIncident(String s) {

    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getEventName() {
        return null;
    }

    @Override
    public String getBusinessKey() {
        return null;
    }

    @Override
    public BpmnModelInstance getBpmnModelInstance() {
        return null;
    }

    @Override
    public FlowElement getBpmnModelElementInstance() {
        return null;
    }

    @Override
    public ProcessEngineServices getProcessEngineServices() {
        return null;
    }

    @Override
    public ProcessEngine getProcessEngine() {
        return null;
    }

    @Override
    public String getVariableScopeKey() {
        return null;
    }

    @Override
    public Map<String, Object> getVariables() {
        return null;
    }

    @Override
    public VariableMap getVariablesTyped() {
        return null;
    }

    @Override
    public VariableMap getVariablesTyped(boolean b) {
        return null;
    }

    @Override
    public Map<String, Object> getVariablesLocal() {
        return null;
    }

    @Override
    public VariableMap getVariablesLocalTyped() {
        return null;
    }

    @Override
    public VariableMap getVariablesLocalTyped(boolean b) {
        return null;
    }

    @Override
    public Object getVariable(String s) {
        return this.vars.get(s);
    }

    @Override
    public Object getVariableLocal(String s) {
        return null;
    }

    @Override
    public <T extends TypedValue> T getVariableTyped(String s) {
        return null;
    }

    @Override
    public <T extends TypedValue> T getVariableTyped(String s, boolean b) {
        return null;
    }

    @Override
    public <T extends TypedValue> T getVariableLocalTyped(String s) {
        return null;
    }

    @Override
    public <T extends TypedValue> T getVariableLocalTyped(String s, boolean b) {
        return null;
    }

    @Override
    public Set<String> getVariableNames() {
        return null;
    }

    @Override
    public Set<String> getVariableNamesLocal() {
        return null;
    }

    @Override
    public void setVariable(String s, Object o) {
        this.vars.put(s, o);
    }

    @Override
    public void setVariableLocal(String s, Object o) {

    }

    @Override
    public void setVariables(Map<String, ?> map) {

    }

    @Override
    public void setVariablesLocal(Map<String, ?> map) {

    }

    @Override
    public boolean hasVariables() {
        return false;
    }

    @Override
    public boolean hasVariablesLocal() {
        return false;
    }

    @Override
    public boolean hasVariable(String s) {
        return false;
    }

    @Override
    public boolean hasVariableLocal(String s) {
        return false;
    }

    @Override
    public void removeVariable(String s) {

    }

    @Override
    public void removeVariableLocal(String s) {

    }

    @Override
    public void removeVariables(Collection<String> collection) {

    }

    @Override
    public void removeVariablesLocal(Collection<String> collection) {

    }

    @Override
    public void removeVariables() {

    }

    @Override
    public void removeVariablesLocal() {

    }
}
