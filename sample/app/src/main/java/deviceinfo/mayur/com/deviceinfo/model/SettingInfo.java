package deviceinfo.mayur.com.deviceinfo.model;

/**
 * Created by mayurkaul on 04/10/17.
 */

public class SettingInfo {
    private String label;
    private String value;

    public SettingInfo(String label, String value)
    {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
