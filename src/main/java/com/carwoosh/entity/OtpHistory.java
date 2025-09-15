package com.carwoosh.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "otp_history")
public class OtpHistory
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Integer historyId;
    @Column(name = "receiver_detail")
    private String  receiverDetail;
    @Column(name = "otp")
    private String  otp;
    @Column(name = "txn_id")
    private String  txnId;
    @Column(name = "expiry")
    private Date    expiry;
    @Column(name = "verified")
    private Boolean verified;
}
