package de.ma.pi;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static de.ma.pi.PartAvailabilityChecker.NOT_FUNCTIONAL_PC;

public class PersonalComputerFunctionalTester implements JavaDelegate {
    public static final String ANSWER_NO = "no";
    public static final String ANSWER_YES = "yes";
    private final Logger logger = LoggerFactory.getILoggerFactory().getLogger(this.getClass().getName());
    public static final String INPUT_DEVICE_TYPE = "Input_device_type";
    public static final String OUTPUT_RIG_FUNCTIONAL = "rig_functional";
    @Override
    public void execute(DelegateExecution delegateExecution)  {
        logger.debug("starting service task");

        String inputDeviceType = Objects.requireNonNull( (String) delegateExecution.getVariable(INPUT_DEVICE_TYPE));
        logger.debug("input device type is: " + inputDeviceType);

        if (inputDeviceType.equalsIgnoreCase(NOT_FUNCTIONAL_PC)) {
            delegateExecution.setVariable(OUTPUT_RIG_FUNCTIONAL, ANSWER_NO);
        } else {
            delegateExecution.setVariable(OUTPUT_RIG_FUNCTIONAL, ANSWER_YES);
        }

    }
}
