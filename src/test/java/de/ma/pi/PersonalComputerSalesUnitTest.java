package de.ma.pi;

import org.apache.ibatis.logging.LogFactory;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.camunda.bpm.extension.process_test_coverage.junit.rules.TestCoverageProcessEngineRuleBuilder;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import java.sql.SQLException;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.*;


/**
 * Test case starting an in-memory database-backed Process Engine.
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class PersonalComputerSalesUnitTest {

  public static final String PERSONAL_COMPUTER_SALES_BPMN = "personal_computer_sales.bpmn";
  public static final String PROCESS_VAR_DEVICE_TYPE = "device_type";

  static {
    LogFactory.useSlf4jLogging(); // MyBatis
  }

  @ClassRule
  @Rule
  public static ProcessEngineRule rule = TestCoverageProcessEngineRuleBuilder.create().build();

  @Before
  public void setup() {
    init(rule.getProcessEngine());
    Mocks.register("PartAvailabilityChecker", new PartAvailabilityChecker());
  }

  @Test
  @Deployment(resources = PERSONAL_COMPUTER_SALES_BPMN) // only required for process test coverage
  public void testHappyPath() throws SQLException {
    ProcessInstance processInstance = runtimeService()
        .createProcessInstanceByKey(ProcessConstants.PERSONAL_COMPUTER_SALES_PROCESS_DEFINITION_KEY)
//        .setVariable(PROCESS_VAR_DEVICE_TYPE, AVAILABLE_PC)
        .execute();

    assertThat(processInstance).hasPassed("Activity_create_offer");

    assertThat(processInstance).isWaitingAt("Usertask_call_present_offer");
    complete(task(), withVariables("order_accepted", "answer_yes"));

    assertThat(processInstance).hasPassed("Activity_create_order");
    assertThat(processInstance).hasPassed("Servicetask_check_parts_available");
    assertThat(processInstance).hasNotPassed("Gateway_parts_not_available");
    assertThat(processInstance).hasPassed("Servicetask_test_rig");
    assertThat(processInstance).hasPassed("Activity_ship_rig");
    assertThat(processInstance).hasPassed("Activity_send_invoice");

    assertThat(processInstance).isWaitingAt("Usertask_check_payment");
    complete(task(), withVariables("customer_paid", "answer_yes"));

    assertThat(processInstance).hasNotPassed("Activity_start_dept_collection");
    assertThat(processInstance).isEnded();

  }

  @Test
  @Deployment(resources = PERSONAL_COMPUTER_SALES_BPMN) // only required for process test coverage
  public void test_customer_accepts_revised_offer() throws SQLException {
    ProcessInstance processInstance = runtimeService()
        .createProcessInstanceByKey(ProcessConstants.PERSONAL_COMPUTER_SALES_PROCESS_DEFINITION_KEY)
        .execute();

    assertThat(processInstance).hasPassed("Activity_create_offer");

    assertThat(processInstance).isWaitingAt("Usertask_call_present_offer");
    complete(task(), withVariables("order_accepted", "answer_no", "offer_requested", "answer_yes"));
    assertThat(processInstance).hasNotPassed("Flow_new_offer_requested_no");
    assertThat(processInstance).hasPassed("Activity_revise_offer");
    complete(task(), withVariables("order_accepted", "answer_yes"));

    assertThat(processInstance).hasPassed("Activity_create_order");
    assertThat(processInstance).hasPassed("Servicetask_check_parts_available");
    assertThat(processInstance).hasNotPassed("Gateway_parts_not_available");
    assertThat(processInstance).hasPassed("Servicetask_test_rig");
    assertThat(processInstance).hasPassed("Activity_ship_rig");
    assertThat(processInstance).hasPassed("Activity_send_invoice");

    assertThat(processInstance).isWaitingAt("Usertask_check_payment");
    complete(task(), withVariables("customer_paid", "answer_yes"));

    assertThat(processInstance).hasNotPassed("Activity_start_dept_collection");
    assertThat(processInstance).isEnded();

  }

  @Test
  @Deployment(resources = PERSONAL_COMPUTER_SALES_BPMN) // only required for process test coverage
  public void test_customer_dont_accepts_revised_offer() throws SQLException {
    ProcessInstance processInstance = runtimeService()
            .createProcessInstanceByKey(ProcessConstants.PERSONAL_COMPUTER_SALES_PROCESS_DEFINITION_KEY)
            .execute();

    assertThat(processInstance).hasPassed("Activity_create_offer");

    assertThat(processInstance).isWaitingAt("Usertask_call_present_offer");
    complete(task(), withVariables("order_accepted", "answer_no", "offer_requested", "answer_no"));
    assertThat(processInstance).isEnded();

  }

  @Test
  @Deployment(resources = PERSONAL_COMPUTER_SALES_BPMN) // only required for process test coverage
  public void test_customer_dont_pay() throws SQLException {
    ProcessInstance processInstance = runtimeService()
            .createProcessInstanceByKey(ProcessConstants.PERSONAL_COMPUTER_SALES_PROCESS_DEFINITION_KEY)
            .execute();

    assertThat(processInstance).hasPassed("Activity_create_offer");

    assertThat(processInstance).isWaitingAt("Usertask_call_present_offer");
    complete(task(), withVariables("order_accepted", "answer_yes"));

    assertThat(processInstance).hasPassed("Activity_create_order");
    assertThat(processInstance).hasPassed("Servicetask_check_parts_available");
    assertThat(processInstance).hasNotPassed("Gateway_parts_not_available");
    assertThat(processInstance).hasPassed("Servicetask_test_rig");
    assertThat(processInstance).hasPassed("Activity_ship_rig");
    assertThat(processInstance).hasPassed("Activity_send_invoice");

    assertThat(processInstance).isWaitingAt("Usertask_check_payment");
    complete(task(), withVariables("customer_paid", "answer_no"));

    assertThat(processInstance).hasNotPassed("Flow_customer_paid_yes");
    assertThat(processInstance).hasPassed("Activity_start_dept_collection");
    assertThat(processInstance).isEnded();

  }


  @Test
  @Deployment(resources = PERSONAL_COMPUTER_SALES_BPMN) // only required for process test coverage
  public void test_parts_not_available_order_canceled() {
    ProcessInstance processInstance = runtimeService()
            .createProcessInstanceByKey(ProcessConstants.PERSONAL_COMPUTER_SALES_PROCESS_DEFINITION_KEY)
            .execute();

    assertThat(processInstance).hasPassed("Activity_create_offer");

    assertThat(processInstance).isWaitingAt("Usertask_call_present_offer");
    complete(task(), withVariables("order_accepted", "answer_yes"));
    assertThat(processInstance).hasPassed("Activity_create_order");
    assertThat(processInstance).hasPassed("Servicetask_check_parts_available");
    complete(task(), withVariables("Servicetask_check_parts_available", "answer_no"));
    assertThat(processInstance).hasPassed("Usertask_offer_alternative_part_selection");
    complete(task(), withVariables("Usertask_offer_alternative_part_selection", "answer_customer_cancels"));

    assertThat(processInstance).isEnded();
  }
}
