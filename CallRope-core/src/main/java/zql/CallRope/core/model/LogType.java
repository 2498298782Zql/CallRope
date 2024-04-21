package zql.CallRope.core.model;

public enum LogType {
    Err("Err"),
    Info("Info"),
    Debug("Debug");
    private final String type;

    LogType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
