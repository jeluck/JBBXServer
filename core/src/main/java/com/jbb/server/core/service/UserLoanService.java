package com.jbb.server.core.service;

import java.util.List;

import com.jbb.server.core.domain.LoanRecord;
import com.jbb.server.core.domain.PaymentRecord;

public interface UserLoanService {

    List<LoanRecord> selectLoanRecords();

    LoanRecord selectLoanRecord(int id);

    void createLoanRecord(LoanRecord record);

    void updateLoanRecord(LoanRecord record);

    void deleteLoanRecord(int recordId);

    List<PaymentRecord> selectPaymentRecords();

    PaymentRecord selectPaymentRecord(int id);

    void createPaymentRecord(PaymentRecord record);

    void updatePaymentRecord(PaymentRecord record);

    void deletePaymentRecord(int id);

}
