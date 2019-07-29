package PaymentEmail;

public class PaymentModel {

        private String TransferReference;
        private String BeneficiaryName;
        private String SenderName;
        private String ValueDate;
        private String Currency;
        private String Amount;
        private String BeneficiaryAccount;
        private String paymentId;
        private String BeneficiaryBank;
        private String Purpose;
        private String Status;

    public String getTransferReference() {
        return TransferReference;
    }

    public void setTransferReference(String transferReference) {
        TransferReference = transferReference;
    }

    public String getBeneficiaryName() {
        return BeneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        BeneficiaryName = beneficiaryName;
    }

    public String getSenderName() {
        return SenderName;
    }

    public void setSenderName(String senderName) {
        SenderName = senderName;
    }

    public String getValueDate() {
        return ValueDate;
    }

    public void setValueDate(String valueDate) {
        ValueDate = valueDate;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getBeneficiaryAccount() {
        return BeneficiaryAccount;
    }

    public void setBeneficiaryAccount(String beneficiaryAccount) {
        BeneficiaryAccount = beneficiaryAccount;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getBeneficiaryBank() {
        return BeneficiaryBank;
    }

    public void setBeneficiaryBank(String beneficiaryBank) {
        BeneficiaryBank = beneficiaryBank;
    }

    public String getPurpose() {
        return Purpose;
    }

    public void setPurpose(String purpose) {
        Purpose = purpose;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
