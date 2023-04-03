package de.ma.pi;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PartAvailabilityChecker  implements JavaDelegate {
    private final Logger logger = LoggerFactory.getILoggerFactory().getLogger(this.getClass().getName());
    public static final String INPUT_DEVICE_TYPE = "Input_device_type";
    public static final String AVAILABLE_PC = "available_pc";
    public static final String OUT_OF_STOCK_PC = "out_of_stock_pc";
    public static final String NOT_FUNCTIONAL_PC = "not_functional_pc";
    public static final String OUTPUT_PARTS_AVAILABLE = "parts_available";
    public static final String ANSWER_NO = "no";
    public static final String ANSWER_YES = "yes";
    Map<String, Integer> deviceStorage = Stream.of(
            new AbstractMap.SimpleImmutableEntry<>(AVAILABLE_PC, 1),
            new AbstractMap.SimpleImmutableEntry<>(OUT_OF_STOCK_PC, 0),
            new AbstractMap.SimpleImmutableEntry<>(NOT_FUNCTIONAL_PC, 10))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        logger.debug("starting service task");

        String inputDeviceType = Objects.requireNonNull( (String) delegateExecution.getVariable(INPUT_DEVICE_TYPE));
        logger.debug("input device type is: " + inputDeviceType);
        Integer integer = deviceStorage.get(inputDeviceType.toLowerCase());
        logger.debug("input device stored: " + integer);

        if (integer != null && integer > 0) {
            delegateExecution.setVariable(OUTPUT_PARTS_AVAILABLE, ANSWER_YES);
        } else {
            delegateExecution.setVariable(OUTPUT_PARTS_AVAILABLE, ANSWER_NO);
        }

    }
}
