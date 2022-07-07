package com.infosupport.demo6startwithconcurrencycontrol;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;
import java.math.BigDecimal;

@Entity
public class Account {
    @Id
    @GeneratedValue
    private int id;
    @Version
    private long version;
    private String name;
    private BigDecimal balance;

    public Account()
    {

    }
    public Account(String name, BigDecimal balance)
    {
        this.name = name;
        this.balance = balance;
    }

    public void Add(BigDecimal payment)
    {
        this.balance = this.balance.add(payment);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
