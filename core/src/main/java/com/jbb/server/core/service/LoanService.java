package com.jbb.server.core.service;

import java.util.List;

import com.jbb.server.core.domain.LoanRecord;
import com.jbb.server.core.domain.LoanSummary;

public interface LoanService {
    LoanSummary getTotal(int userId);

    void createLoanRecord(int userId, LoanRecord loanRecord);

    void updateLoanRecord(int userId, LoanRecord loanRecord);

    void deleteLoanRecord(int userId, int loanRecordId);

    List<LoanRecord> getLoanRecords(int userId, boolean detail);

    LoanRecord getLoanRecordById(int userId, int loanRecordId);
}
