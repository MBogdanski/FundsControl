package fundsControl.models;

import java.math.BigDecimal;

public class User {

    private BigDecimal accountBalance;

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }
}
