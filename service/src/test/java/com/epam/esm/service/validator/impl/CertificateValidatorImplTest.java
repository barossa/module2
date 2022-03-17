package com.epam.esm.service.validator.impl;

import com.epam.esm.validator.CertificateValidator;
import com.epam.esm.validator.ErrorMessageProvider;
import com.epam.esm.validator.TagValidator;
import com.epam.esm.validator.impl.CertificateValidatorImpl;
import com.epam.esm.validator.impl.TagValidatorImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

class CertificateValidatorImplTest {

    private CertificateValidator certificateValidator;

    @BeforeEach
    public void setUp() {
        ErrorMessageProvider messageProvider = code -> "Error message";
        TagValidator tagValidator = new TagValidatorImpl(messageProvider);
        certificateValidator = new CertificateValidatorImpl(tagValidator, messageProvider);
    }

    @ParameterizedTest
    @MethodSource("incorrectNames")
    void invalidNameTest(String name) {
        List<String> errors = certificateValidator.validateName(name);
        Assertions.assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("correctNames")
    void validNameTest(String name) {
        List<String> errors = certificateValidator.validateName(name);
        Assertions.assertTrue(errors.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("incorrectDescriptions")
    void invalidDescriptionTest(String description) {
        List<String> errors = certificateValidator.validateDescription(description);
        Assertions.assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("correctDescriptions")
    void validDescriptionTest(String description) {
        List<String> errors = certificateValidator.validateDescription(description);
        Assertions.assertTrue(errors.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("incorrectPrices")
    void invalidPriceTest(BigDecimal price) {
        List<String> errors = certificateValidator.validatePrice(price);
        Assertions.assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("correctPrices")
    void validPriceTest(BigDecimal price) {
        List<String> errors = certificateValidator.validatePrice(price);
        Assertions.assertTrue(errors.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("incorrectDurations")
    void invalidDurationTest(long duration) {
        List<String> errors = certificateValidator.validateDuration(duration);
        Assertions.assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("correctDurations")
    void validDurationTest(long duration) {
        List<String> errors = certificateValidator.validateDuration(duration);
        Assertions.assertTrue(errors.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("incorrectTags")
    void invalidTagTest(String tagName) {
        List<String> errors = certificateValidator.validateTagName(tagName);
        Assertions.assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("correctTags")
    void validTagTest(String tagName) {
        List<String> errors = certificateValidator.validateTagName(tagName);
        Assertions.assertTrue(errors.isEmpty());
    }


    private static Stream<String> incorrectNames() {
        return Stream.of("Name!!!",
                "Certif#cate",
                "Cer+tif+icate",
                "Oops");
    }

    private static Stream<String> correctNames() {
        return Stream.of("Certificate",
                "Certificate of the year",
                "Winner 2021",
                "The - best");
    }

    private static Stream<String> incorrectDescriptions() {
        return Stream.of("descr",
                "qwertyuiopasdghjkl;zxcvbnm,./qwertyuioplkjhgfdsazxcvbnm,.lkjhgfdsaqwertyuioplkjhgfdsazxcvjjjdhdhdhd!");
    }

    private static Stream<String> correctDescriptions() {
        return Stream.of("Description",
                "Certificate #1",
                "THE BEST !@#",
                "M0$t p*pular");
    }

    private static Stream<BigDecimal> incorrectPrices() {
        return Stream.of(new BigDecimal(-10),
                new BigDecimal(1_000_000_000_001L));
    }

    private static Stream<BigDecimal> correctPrices() {
        return Stream.of(new BigDecimal(1000),
                new BigDecimal(1_000_000_000));
    }

    private static Stream<Long> incorrectDurations() {
        return Stream.of(-7L,
                -256L,
                368L,
                0L);
    }

    private static Stream<Long> correctDurations() {
        return Stream.of(356L,
                20L,
                123L);
    }

    private static Stream<String> incorrectTags() {
        return Stream.of("Name",
                "Tag$",
                "N@me",
                "NameOfMyTagTooooooLooong");
    }

    private static Stream<String> correctTags() {
        return Stream.of("TagName",
                "Number-one",
                "tag25",
                "off-30");
    }

}
