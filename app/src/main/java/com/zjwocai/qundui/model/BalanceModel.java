package com.zjwocai.qundui.model;

import java.io.Serializable;

/**
 * 余额
 * Created by NieLiQin on 2016/7/31.
 */
public class BalanceModel implements Serializable {

    /**
     * balance : 5000.0
     */

    private String balance;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
