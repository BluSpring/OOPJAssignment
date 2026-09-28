package xyz.bluspring.systems.hms.data.records;

import xyz.bluspring.systems.hms.utils.data.DataSerializer;

public enum TestStatus {
    REQUESTED("Test Requested"),
    WAITING("Waiting for Results"),
    COMPLETED("Test Completed"),
    ;

    public static final DataSerializer<TestStatus> SERIALIZER = DataSerializer.fromEnum(TestStatus.class);
    private final String properName;

    TestStatus(String properName) {
        this.properName = properName;
    }

    public String getProperName() {
        return properName;
    }
}
