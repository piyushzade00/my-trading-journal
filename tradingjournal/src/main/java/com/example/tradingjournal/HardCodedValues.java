package com.example.tradingjournal;

public class HardCodedValues {

    public static class Strings{

        public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        public static final String PASSWORD_REGEX = "^[a-zA-Z0-9._%+#@!*-]{8,20}$";
        public static final String USERNAME_INVALID = "Please enter a valid UserName";
        public static final String PASSWORD_INVALID = "Please enter a valid Password";
        public static final String EMAIL_INVALID = "Please enter a valid Email ID";
        public static final String ATLEAST_ONE_PRIMARY_ACCOUNT = "Please make at least one account as primary";
        public static final String DUPLICATE_ACCOUNT_NAME = "Duplicate Account Name";
        public static final String TRANSACTION_DATETIME_CANNOT_BE_BEFORE_ACCOUNT_CREATION_DATETIME = "Transaction Date and Time cannot be before Account Creation Date and Time";
        public static final String TRANSACTION_DATETIME_CANNOT_BE_AFTER_CURRENT_DATETIME = "Transaction Date and Time cannot be after Current Date and Time";
        public static final String AMOUNT_SHOULD_BE_GREATER_THAN_ZERO  = "Amount should be greater than zero";
        public static final String WITHDRAWAL_AMOUNT_SHOULD_BE_LESS_THAN_ACCOUNT_BALANCE = "Withdrawal Amount should be less than Account Balance";
        public static final String WITHDRAWAL = "Withdrawal";
        public static final String DEPOSIT = "Deposit";
        public static final String AT_LEAST_ONE_TRANSACTION_SHOULD_BE_PRESENT_WHEN_CREATING_NEW_ACCOUNT = "At least one transaction should be present when creating new account";
        public static final String FIRST_TRANSACTION_CANNOT_BE_A_WITHDRAWAL_TRANSACTION = "First Transaction in the Account cannot be Withdrawal Transaction";

    }

}
