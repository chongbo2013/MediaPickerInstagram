package me.ningsk.common.whitelist;

public class WhiteItem
{
    private String mDevice;
    private PropertyValueExchanger mPropertyValueExchanger = new PropertyValueExchanger();

    public WhiteItem(String device)
    {
        this.mDevice = device;
    }

    public String getDevice()
    {
        return this.mDevice;
    }

    public void setDevice(String device) {
        this.mDevice = device;
    }

    public PropertyValueExchanger getPropertyValueExchanger() {
        return this.mPropertyValueExchanger;
    }

    public void setPropertyValueExchanger(PropertyValueExchanger propertyValueExchanger) {
        this.mPropertyValueExchanger = propertyValueExchanger;
    }
}
