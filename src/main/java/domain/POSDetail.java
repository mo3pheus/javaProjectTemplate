package domain;

public class POSDetail {
    String headerRowKey;
    String txType;
    String cashier;
    double valueNum;
    double quantity;
    String serverTime;
    String registerTime;

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getHeaderRowKey() {
        return headerRowKey;
    }

    public void setHeaderRowKey(String headerRowKey) {
        this.headerRowKey = headerRowKey;
    }

    public String getTxType() {
        return txType;
    }

    public void setTxType(String txType) {
        this.txType = txType;
    }

    public String getCashier() {
        return cashier;
    }

    public void setCashier(String cashier) {
        this.cashier = cashier;
    }

    public double getValueNum() {
        return valueNum;
    }

    public void setValueNum(double valueNum) {
        this.valueNum = valueNum;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }
}
