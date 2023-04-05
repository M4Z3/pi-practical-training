package de.ma.pi;

import org.apache.ibatis.logging.LogFactory;
import org.assertj.core.util.Maps;
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

import static de.ma.pi.PartAvailabilityChecker.*;
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
  public void test_everything_available_and_functional() throws SQLException {
    ProcessInstance processInstance = runtimeService()
        .startProcessInstanceByMessage("Message_start_personal_computer_sale", Maps.newHashMap(PROCESS_VAR_DEVICE_TYPE, AVAILABLE_PC));

    assertThat(processInstance).hasPassed("Activity_create_offer");
    assertThat(processInstance).isWaitingAt("Usertask_call_present_offer");

    complete(task(), withVariables("order_accepted", "answer_yes"));
    assertThat(processInstance).hasPassed("Activity_create_order");

    // execute Servicetask_check_parts_available
    execute(job());
    assertThat(processInstance).hasPassed("Servicetask_check_parts_available");
    assertThat(processInstance).hasPassed("Activity_build_rig");
    assertThat(processInstance).hasNotPassed("Usertask_call_alternative_parts");

    // execute Servicetask_test_rig
    execute(job());
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
            .startProcessInstanceByMessage("Message_start_personal_computer_sale", Maps.newHashMap(PROCESS_VAR_DEVICE_TYPE, AVAILABLE_PC));

    assertThat(processInstance).hasPassed("Activity_create_offer");
    assertThat(processInstance).isWaitingAt("Usertask_call_present_offer");

    complete(task(), withVariables("order_accepted", "answer_no", "offer_requested", "answer_yes"));
    assertThat(processInstance).hasNotPassed("Flow_new_offer_requested_no");
    assertThat(processInstance).isNotEnded();
    assertThat(processInstance).hasPassed("Activity_revise_offer");

    complete(task(), withVariables("order_accepted", "answer_yes"));
    assertThat(processInstance).hasPassed("Activity_create_order");

    // execute Servicetask_check_parts_available
    execute(job());
    assertThat(processInstance).hasPassed("Servicetask_check_parts_available");
    assertThat(processInstance).hasPassed("Activity_build_rig");
    assertThat(processInstance).hasNotPassed("Usertask_call_alternative_parts");

    // execute Servicetask_test_rig
    execute(job());
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
            .startProcessInstanceByMessage("Message_start_personal_computer_sale", Maps.newHashMap(PROCESS_VAR_DEVICE_TYPE, AVAILABLE_PC));


    assertThat(processInstance).hasPassed("Activity_create_offer");
    assertThat(processInstance).isWaitingAt("Usertask_call_present_offer");

    complete(task(), withVariables("order_accepted", "answer_no", "offer_requested", "answer_no"));
    assertThat(processInstance).hasNotPassed("Flow_new_offer_requested_yes");
    assertThat(processInstance).isEnded();

  }

  @Test
  @Deployment(resources = PERSONAL_COMPUTER_SALES_BPMN) // only required for process test coverage
  public void test_customer_dont_pay() throws SQLException {
    ProcessInstance processInstance = runtimeService()
            .startProcessInstanceByMessage("Message_start_personal_computer_sale", Maps.newHashMap(PROCESS_VAR_DEVICE_TYPE, AVAILABLE_PC));


    assertThat(processInstance).hasPassed("Activity_create_offer");
    assertThat(processInstance).isWaitingAt("Usertask_call_present_offer");

    complete(task(), withVariables("order_accepted", "answer_yes"));
    assertThat(processInstance).hasPassed("Activity_create_order");

    // execute Servicetask_check_parts_available
    execute(job());
    assertThat(processInstance).hasPassed("Servicetask_check_parts_available");
    assertThat(processInstance).hasPassed("Activity_build_rig");
    assertThat(processInstance).hasNotPassed("Usertask_call_alternative_parts");

    // execute Servicetask_test_rig
    execute(job());
    assertThat(processInstance).hasPassed("Servicetask_test_rig");
    assertThat(processInstance).hasPassed("Activity_ship_rig");
    assertThat(processInstance).hasPassed("Activity_send_invoice");
    assertThat(processInstance).isWaitingAt("Usertask_check_payment");

    complete(task(), withVariables("customer_paid", "answer_no"));
    assertThat(processInstance).hasPassed("Activity_start_dept_collection");
    assertThat(processInstance).isEnded();

  }

  @Test
  @Deployment(resources = PERSONAL_COMPUTER_SALES_BPMN) // only required for process test coverage
  public void test_order_out_of_stock_pc() throws SQLException {
    ProcessInstance processInstance = runtimeService()
            .startProcessInstanceByMessage("Message_start_personal_computer_sale", Maps.newHashMap(PROCESS_VAR_DEVICE_TYPE, OUT_OF_STOCK_PC));


    assertThat(processInstance).hasPassed("Activity_create_offer");
    assertThat(processInstance).isWaitingAt("Usertask_call_present_offer");

    complete(task(), withVariables("order_accepted", "answer_yes"));
    assertThat(processInstance).hasPassed("Activity_create_order");

    // execute Servicetask_check_parts_available
    execute(job());
    assertThat(processInstance).hasPassed("Servicetask_check_parts_available");
    assertThat(processInstance).isWaitingAt("Usertask_call_alternative_parts");

    complete(task(), withVariables("order_cancelled", "answer_no", "new_device_type", AVAILABLE_PC));
    assertThat(processInstance).hasNotPassed("Flow_order_cancelled_yes");

    // execute Servicetask_check_parts_available
    execute(job());
    assertThat(processInstance).hasPassed("Servicetask_check_parts_available");
    assertThat(processInstance).hasPassed("Activity_build_rig");

    // execute Servicetask_test_rig
    execute(job());
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
  public void test_order_not_functional_pc() throws SQLException {
    ProcessInstance processInstance = runtimeService()
            .startProcessInstanceByMessage("Message_start_personal_computer_sale", Maps.newHashMap(PROCESS_VAR_DEVICE_TYPE, NOT_FUNCTIONAL_PC));

    assertThat(processInstance).hasPassed("Activity_create_offer");
    assertThat(processInstance).isWaitingAt("Usertask_call_present_offer");

    complete(task(), withVariables("order_accepted", "answer_yes"));
    assertThat(processInstance).hasPassed("Activity_create_order");

    // execute Servicetask_check_parts_available
    execute(job());
    assertThat(processInstance).hasPassed("Servicetask_check_parts_available");
    assertThat(processInstance).hasPassed("Activity_build_rig");
    assertThat(processInstance).hasNotPassed("Usertask_call_alternative_parts");

    // execute Servicetask_test_rig
    execute(job());
    assertThat(processInstance).hasPassed("Servicetask_test_rig");
    assertThat(processInstance).hasNotPassed("Activity_ship_rig");

    complete(task(), withVariables("order_cancelled", "answer_no", "new_device_type", AVAILABLE_PC));
    assertThat(processInstance).hasNotPassed("Flow_order_cancelled_yes");

    // execute Servicetask_check_parts_available
    execute(job());
    assertThat(processInstance).hasPassed("Servicetask_check_parts_available");
    assertThat(processInstance).hasPassed("Activity_build_rig");

    // execute Servicetask_test_rig
    execute(job());
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
  public void test_parts_not_available_order_canceled() {
    ProcessInstance processInstance = runtimeService()
            .startProcessInstanceByMessage("Message_start_personal_computer_sale", Maps.newHashMap(PROCESS_VAR_DEVICE_TYPE, OUT_OF_STOCK_PC));

    assertThat(processInstance).hasPassed("Activity_create_offer");

    assertThat(processInstance).isWaitingAt("Usertask_call_present_offer");
    complete(task(), withVariables("order_accepted", "answer_yes"));
    assertThat(processInstance).hasPassed("Activity_create_order");

    // execute Servicetask_check_parts_available
    execute(job());
    assertThat(processInstance).hasPassed("Servicetask_check_parts_available");
    assertThat(processInstance).hasNotPassed("Activity_build_rig");
    complete(task(), withVariables("order_cancelled", "answer_yes", "new_device_type", AVAILABLE_PC));
    assertThat(processInstance).hasPassed("Usertask_call_alternative_parts");


    assertThat(processInstance).isEnded();
  }
}
