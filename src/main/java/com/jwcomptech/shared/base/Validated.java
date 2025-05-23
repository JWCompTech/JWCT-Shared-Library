package com.jwcomptech.shared.base;

import com.jwcomptech.shared.Condition;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.jwcomptech.shared.utils.CheckIf.checkArgumentNotNull;

/**
 * A base object to easily add validations to any class.
 * @since 0.0.1
 */
@SuppressWarnings("unused")
@Data
public class Validated implements Serializable {
    /**
     * The result of all validations.
     */
    private boolean valid;
    /**
     * Is true if the evaluation has been run.
     */
    private boolean alreadyValidated;
    /**
     * The list of true validations
     * -- GETTER --
     *  Returns the list of true validations.
     *
     * @return the list of true validations.

     */
    @Getter
    private final List<Condition> trueValidations;
    /**
     * The list of false validations
     * -- GETTER --
     *  Returns the list of false validations.
     *
     * @return the list of false validations.

     */
    @Getter
    private final List<Condition> falseValidations;

    /**
     * Creates a new empty instance.
     */
    public Validated() {
        valid = false;
        alreadyValidated = false;
        trueValidations = new ArrayList<>();
        falseValidations = new ArrayList<>();
    }

    /**
     * Required for serialization support.
     *
     * @see Serializable
     */
    @Serial
    private static final long serialVersionUID = 4520181131454855130L;

    /**
     * Evaluates all conditions and saves the result;
     * @return this instance
     */
    public Validated evaluateAll() {
        boolean failure = false;

        for(Condition condition : trueValidations) {
            if(condition.evaluate().isResultFalse()) {
                failure = true;
                break;
            }
        }

        if(!failure) {
            for (Condition condition : falseValidations) {
                if (condition.evaluate().isResultTrue()) {
                    failure = true;
                    break;
                }
            }
        }

        if(!failure) valid = true;

        alreadyValidated = true;

        return this;
    }

    /**
     * Returns the result of all validations and runs {@link #evaluateAll()} if
     * it has yet to be run.
     * @return the result of all validations
     */
    public boolean isValid() {
        if(!alreadyValidated) evaluateAll();
        return valid;
    }

    /**
     * Runs the specified runnable if the evaluation result is valid.
     * @param runnable the runnable to run
     * @return this instance
     * @throws IllegalArgumentException if runnable is null
     */
    public Validated ifValid(final Runnable runnable) {
        checkArgumentNotNull(runnable, "Runnable cannot be null or empty!");
        if(isValid()) runnable.run();
        return this;
    }

    /**
     * Runs the specified runnable if the evaluation result is invalid.
     * @param runnable the runnable to run
     * @return this instance
     * @throws IllegalArgumentException if runnable is null
     */
    public Validated ifInvalid(final Runnable runnable) {
        checkArgumentNotNull(runnable, "Runnable cannot be null or empty!");
        if(!isValid()) runnable.run();
        return this;
    }

    /**
     * Adds the specified condition(s) to the true list.
     * @param conditions one or more conditions to add
     * @return this instance
     */
    public Validated addToTrue(Condition... conditions) {
        this.trueValidations.addAll(List.of(conditions));
        return this;
    }

    /**
     * Adds the specified condition(s) to the false list.
     * @param conditions one or more conditions to add
     * @return this instance
     */
    public Validated addToFalse(Condition... conditions) {
        this.falseValidations.addAll(List.of(conditions));
        return this;
    }
}
