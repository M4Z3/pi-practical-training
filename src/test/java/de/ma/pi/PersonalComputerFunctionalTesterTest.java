package de.ma.pi;

import org.assertj.core.util.Maps;
import org.junit.Test;

import static de.ma.pi.PartAvailabilityChecker.*;
import static de.ma.pi.PersonalComputerFunctionalTester.OUTPUT_RIG_FUNCTIONAL;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PersonalComputerFunctionalTesterTest {

    @Test
    public void test_functionality_of_functional_pc() {
        PersonalComputerFunctionalTester tester = new PersonalComputerFunctionalTester();

        TestDelegateExecution delegateExecution = new TestDelegateExecution(Maps.newHashMap(INPUT_DEVICE_TYPE, AVAILABLE_PC));
        tester.execute(delegateExecution);
        Object resultVar = delegateExecution.getVariable(OUTPUT_RIG_FUNCTIONAL);
        assertTrue(resultVar == ANSWER_YES);

    }
    @Test
    public void test_functionality_of_not_functional_pc() {
        PersonalComputerFunctionalTester tester = new PersonalComputerFunctionalTester();

        TestDelegateExecution delegateExecution = new TestDelegateExecution(Maps.newHashMap(INPUT_DEVICE_TYPE, NOT_FUNCTIONAL_PC));
        tester.execute(delegateExecution);
        Object resultVar = delegateExecution.getVariable(OUTPUT_RIG_FUNCTIONAL);
        assertTrue(resultVar == ANSWER_NO);

    }
}
