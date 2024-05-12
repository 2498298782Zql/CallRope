package zql.CallRope.point.model;

import javax.lang.model.element.NestingKind;

public enum SpanEnvironment {
    TEST_ENVIRONMENT("TEST_ENVIRONMENT"),
    PRODUCTION_ENVIRONMENT("PRODUCTION_ENVIRONMENT");

    SpanEnvironment(String typeName) {
        this.typeName = typeName;
    }

    String typeName;
}
