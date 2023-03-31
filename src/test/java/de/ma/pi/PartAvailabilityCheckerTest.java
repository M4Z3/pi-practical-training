package de.ma.pi;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Maps;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineServices;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.runtime.Incident;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.FlowElement;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static de.ma.pi.PartAvailabilityChecker.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PartAvailabilityCheckerTest {

    @Test
    public void test_order_available() throws Exception {
        PartAvailabilityChecker checker = new PartAvailabilityChecker();

        TestDelegateExecution delegateExecution = new TestDelegateExecution(Maps.newHashMap(INPUT_DEVICE_TYPE, AVAILABLE_PC));
        checker.execute(delegateExecution);
        Object resultVar = delegateExecution.getVariable("parts_available");
        assertTrue(resultVar == "yes");

    }
    @Test
    public void test_order_unavailable() throws Exception {
        PartAvailabilityChecker checker = new PartAvailabilityChecker();

        TestDelegateExecution delegateExecution = new TestDelegateExecution(Maps.newHashMap(INPUT_DEVICE_TYPE, OUT_OF_STOCK_PC));
        checker.execute(delegateExecution);
        Object resultVar = delegateExecution.getVariable("parts_available");
        assertTrue(resultVar == "no");

    }
}
