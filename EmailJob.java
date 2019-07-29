package PaymentEmail;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailJob implements Job {
    loadProps loader = new loadProps();
    private Connection conn = loader.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(EmailJob.class);


    public EmailJob() {
    }

    private boolean isValidString(String str) {
        boolean isValid = false;
        if (str != null && str.trim().length() > 0) {
            isValid = true;
        }

        return isValid;
    }

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        try{
            // Get connection from LoadProps page
            // Load property files to get Details
            Properties prop = ProperLoader.getProps();

            String BeneEmailCopy = prop.getProperty("mail.beneCopy");
            //String BeneEmailCopy1 = prop.getProperty("mail.beneCopy1");
            String query = prop.getProperty("query");
            String query1 = prop.getProperty("query1");
            String query3 = prop.getProperty("query3");
            String Query2 = prop.getProperty("query2");
            String Query4 = prop.getProperty("query4");
            String Query5 = prop.getProperty("query5");
            String Query6 = prop.getProperty("query6");
            String Query7 = prop.getProperty("query7");
            String Query8 = prop.getProperty("query8");

            // Execute the Methods to start Email sending

            this.sendEmailNotificationForAPM(query, BeneEmailCopy, Query2, query1, conn);
            this.sendBulkEmailNotificationForAPM(Query7, Query5, BeneEmailCopy, conn, Query8);
            this.sendEmailNotification(query3, Query2, query1, conn);
            this.sendBulkEmailNotification(Query6, Query4, query1, conn);

        }catch (Exception ex){
            ex.printStackTrace();
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    // Method to get email address when the column is empty on the payment table
    private String getAlternateBeneEmail(String Code, String companyId,  String Sql) {
        System.out.println("About to get alternate beneficiary Email.....");
        String NewBeneEmail = "";
        try {
            PreparedStatement ps = conn.prepareStatement(Sql);
            ps.setString(1, companyId);
            ps.setString(2, Code);
            ResultSet rs =  ps.executeQuery();
            while(rs.next()){
                NewBeneEmail = rs.getString("vendor_email");
                System.out.println("Alternate Email..... " + NewBeneEmail);
            }
        } catch (Exception var7) {
            var7.printStackTrace();
            logger.error("Error message : " + var7);

            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (Exception var37) {
                var37.printStackTrace();
            }
        }

        return NewBeneEmail;
    }
    // Method to update payment table
    private void updateTableColumn(Long paymentId, String Query2) {
            if (paymentId != null) {
                try {
                PreparedStatement prepare = conn.prepareStatement(Query2);
                prepare.setString(1, "Y");
                prepare.setDouble(2, (double)paymentId);
                prepare.execute();
                logger.info("Table column for payment " + paymentId + " has been successful updated");

                 } catch (Exception var4) {
                    var4.printStackTrace();
                      logger.info("Update failed for payment : " + paymentId);
                     try {
                        if (!conn.isClosed()) {
                           conn.close();
                        }
                     } catch (Exception var37) {
                          var37.printStackTrace();
                     }
                }

            }
    }
    // Method to update bulk payment table
    private void updateBulkTableColumn(Long paymentId, String Query4) {
        if (paymentId != null) {
            try {
                PreparedStatement prepare = this.conn.prepareStatement(Query4);
                prepare.setString(1, "Y");
                prepare.setDouble(2, (double) paymentId);
                prepare.execute();
                logger.info("Bulk table column for payment " + paymentId + " has been successful updated");
            } catch (Exception var4) {
                var4.printStackTrace();
                logger.info("Update failed for payment : " + paymentId);
                try {
                    if (!conn.isClosed()) {
                        conn.close();
                    }
                } catch (Exception var37) {
                    var37.printStackTrace();
                }
            }
        }
    }


    // Method to update bulk payment table
    private void updateBulkTableBatchColumn(Long batchId, String Query40) {
        if (batchId != null) {
            try {
                PreparedStatement prepare = this.conn.prepareStatement(Query40);
                prepare.setString(1, "B");
                prepare.setDouble(2, (double) batchId);
                prepare.execute();
                logger.info("Bulk table column for payment " + batchId + " has been successful updated");
            } catch (Exception var4) {
                var4.printStackTrace();
                logger.info("Update failed for batch : " + batchId);
                try {
                    if (!conn.isClosed()) {
                        conn.close();
                    }
                } catch (Exception var37) {
                    var37.printStackTrace();
                }
            }
        }
    }

    // Method to send Email notification to uploader and beneficiary for APM payments
    private void sendEmailNotificationForAPM(String querystring, String uploader, String Query2, String query1, Connection con) {
        System.out.println("PROCESSING SINGLE NOTIFICATION FOR APM...................................");
            PreparedStatement ps = null;
            ResultSet reader = null;
            String sendToBene = "";
          try {
              if (con.isClosed() || con == null) {
                  con = loader.getInstance();
              }
              ps = con.prepareStatement(querystring);
              reader = ps.executeQuery();
              long payment_Id = 0;
              //for (ResultSet reader = ps.executeQuery(); reader.next(); this.updateTableColumn(payment_Id, Query2)) {
              while(reader.next()){
                  LinkedHashMap<String, String> rowsData = new LinkedHashMap();
                  String ref = reader.getString("Trans_ref");
                  rowsData.put("Your Ref", ref);
                  rowsData.put("Beneficiary Name", reader.getString("vendor_name"));
                  String senderName = reader.getString("account_name");
                  rowsData.put("Sender Name", senderName);
                  String date = reader.getDate("payment_due_date").toString();
                  rowsData.put("Value Date", date);
                  rowsData.put("Currency", reader.getString("payment_currency"));
                  String amt = GenerateReceipts.formatAmount(Double.toString(reader.getDouble("amount")));
                  rowsData.put("Amount", amt);
                  String num = reader.getString("vendor_acct_no");
                  if (this.isValidString(num)) {
                      if (num.length() >= 10) {
                          num = num.substring(0, 10);
                          num = num.substring(0, 2) + "******" + num.substring(8);
                          rowsData.put("Beneficiary Account", num);
                      }else{
                          rowsData.put("Beneficiary Account", "**********");
                      }
                  } else {
                      rowsData.put("Beneficiary Account", "**********");
                  }
                  payment_Id = (long) reader.getDouble("payment_id");
                  rowsData.put("Payment ID", String.valueOf(payment_Id));
                  rowsData.put("Beneficiary Bank", reader.getString("vendor_bank"));
                  if(reader.getString("payment_type").equalsIgnoreCase("INVOICE/DISCOUNT")){
                      rowsData.put("Tenor", String.valueOf(reader.getInt("tenor")));
                      rowsData.put("Discounted Days", String.valueOf(reader.getInt("discounted_days")));
                      rowsData.put("Discounted Amount", GenerateReceipts.formatAmount(Double.toString(reader.getDouble("discounted_amount"))));
                      rowsData.put("Invoice Date", reader.getString("invoice_date").toString());
                      rowsData.put("Invoice Duedate", reader.getString("invoice_duedate").toString());
                  }
                  if(isValidString(reader.getString("invoice_number"))){
                      rowsData.put("Invoice Number", reader.getString("invoice_number"));
                  }
                  String purpose = reader.getString("vendor_category");
                  rowsData.put("Purpose", purpose);
                  String status = reader.getString("ptystatus");
                  rowsData.put("Status", status);
                  String CompanyCode = reader.getString("company_code");
                  String VendorCode = reader.getString("vendor_code");
                  String send_Ben_Email = reader.getString("SEND_BEN_EMAIL");
                  String bene_Email2 = "";
                  String bene_Email = reader.getString("bene_email");
                  String bene_Name = reader.getString("vendor_name");
                  String AccountNumber = reader.getString("debit_acct_no");
                  /*if (this.isValidString(AccountNumber)) {
                      if (AccountNumber.length() >= 10) {
                          AccountNumber = AccountNumber.substring(0, 10);
                          AccountNumber = AccountNumber.substring(0, 2) + "******" + AccountNumber.substring(8);
                          rowsData.put("Account Number", AccountNumber);
                      }else{
                          rowsData.put("Account Number", "**********");
                      }
                  }else {
                      rowsData.put("Account Number", "**********");
                  }*/
                  String AccountName = reader.getString("FULLNAME");
                  List validEmailsToBene;
                  boolean emailSendToBene;
                  if (send_Ben_Email.equalsIgnoreCase("Y")) {
                      if (!this.isValidString(bene_Email)) {
                          if (this.isValidString(VendorCode)) {
                              bene_Email2 = this.getAlternateBeneEmail(VendorCode, CompanyCode, query1);
                          }
                          if (!this.isValidString(bene_Email2) || ("").equalsIgnoreCase(bene_Email2)) {
                              bene_Email2 = "";
                          }

                          sendToBene = bene_Email2;
                      } else {
                          sendToBene = bene_Email;
                      }
                      //sendToBene = "taiwo.ahmed@ucitech.com.ng"; // comment out later
                      logger.info("beneficiary : " + sendToBene);
                      validEmailsToBene = GenerateReceipts.validateEmails(sendToBene);
                      if (!sendToBene.equalsIgnoreCase("") && null != sendToBene && validEmailsToBene.size() > 0) {
                          if (status.equalsIgnoreCase("PROCESSED")) {
                              // Call method that processes the notification & send mail to beneficiary
                              emailSendToBene = GenerateReceipts.sendEmail(payment_Id, num, bene_Name.trim(), validEmailsToBene, rowsData, ref, amt, status, date, purpose, senderName);
                              if (emailSendToBene) {
                                  logger.info("Email successfully sent to beneficiary.");
                              } else {
                                  logger.info("Email sending failed to beneficiary.");
                              }
                          } else {
                              logger.info("Payment " + payment_Id + " to " + bene_Name + " is a failed transaction.");
                          }
                      } else {
                          logger.info("No Email address available for this beneficiary or Invalid Email");
                      }
                  } else {
                      logger.info("No Email subscription for this beneficiary");
                  }

                  validEmailsToBene = GenerateReceipts.validateEmails(uploader);
                  logger.info("Uploader : " + uploader);
                  if (!uploader.equalsIgnoreCase("") && null != uploader && validEmailsToBene.size() > 0) {
                      // Call method that processes the notification & send mail to uploader
                      emailSendToBene = GenerateReceipts.sendEmail(payment_Id, AccountNumber, AccountName.trim(), validEmailsToBene, rowsData, ref, amt, status, date, purpose, senderName);
                     // GenerateReceipts.sendEmail(payment_Id, AccountNumber, AccountName.trim(), validEmailsToBene, rowsData, ref, amt, status, date, purpose, senderName);

                      if (emailSendToBene) {
                          logger.info("Email sending successful to uploader.");
                      } else {
                          logger.info("Email sending failed to uploader.");
                      }
                  } else {
                      logger.info("No Email address available for this uploader or Invalid Email");
                  }
                  this.updateTableColumn(payment_Id, Query2);
              }
          } catch (Exception ex) {

              ex.printStackTrace();

              try {
                  if (!ps.isClosed()) {
                      ps.close();
                  }
              } catch (Exception e) {
                  e.printStackTrace();
              }
              try {
                  if (!con.isClosed()) {
                      con.close();
                  }
              } catch (Exception e) {
                  e.printStackTrace();
              }
          }
        System.out.println("ENDING SINGLE NOTIFICATION FOR APM...................................");
    }

    // Method to send Email notification to beneficiary for payments
    private void sendEmailNotification(String query_string, String Query2, String query1, Connection con) {
        System.out.println("PROCESSING SINGLE NOTIFICATION...................................");
        ResultSet reader = null;
        PreparedStatement ps = null;
        String sendToBene = "";
        try {
            if (con.isClosed() || con == null) {
                con = loader.getInstance();
            }
            ps = con.prepareStatement(query_string);
            reader = ps.executeQuery();
            long payment_Id = 0;
            while (reader.next()){
                LinkedHashMap<String, String> rowsData = new LinkedHashMap();
                String ref = reader.getString("Trans_ref");
                rowsData.put("Your Ref", ref);
                rowsData.put("Beneficiary Name", reader.getString("vendor_name"));
                String senderName = reader.getString("account_name");
                rowsData.put("Sender Name", senderName);
                String date = reader.getDate("payment_due_date").toString();
                rowsData.put("Value Date", date);
                rowsData.put("Currency", reader.getString("payment_currency"));
                String amt = GenerateReceipts.formatAmount(Double.toString(reader.getDouble("amount")));
                rowsData.put("Amount", amt);
                String num = reader.getString("vendor_acct_no");
                if (this.isValidString(num)) {
                    if (num.length() >= 10) {
                        num = num.substring(0, 10);
                        num = num.substring(0, 2) + "******" + num.substring(8);
                        rowsData.put("Beneficiary Account", num);
                    }else{
                        rowsData.put("Beneficiary Account", "**********");
                    }
                } else {
                    rowsData.put("Beneficiary Account", "**********");
                }
                payment_Id = (long) reader.getDouble("payment_id");
                rowsData.put("Payment ID", String.valueOf(payment_Id));
                rowsData.put("Beneficiary Bank", reader.getString("vendor_bank"));
                if(reader.getString("payment_type").equalsIgnoreCase("INVOICE/DISCOUNT")){
                    rowsData.put("Tenor", String.valueOf(reader.getInt("tenor")));
                    rowsData.put("Discounted Days", String.valueOf(reader.getInt("discounted_days")));
                    rowsData.put("Discounted Amount", GenerateReceipts.formatAmount(Double.toString(reader.getDouble("discounted_amount"))));
                    rowsData.put("Invoice Date", reader.getString("invoice_date").toString());
                    rowsData.put("Invoice Duedate", reader.getString("invoice_duedate").toString());
                }
                if(isValidString(reader.getString("invoice_number"))){
                    rowsData.put("Invoice Number", reader.getString("invoice_number"));
                }
                String purpose = reader.getString("vendor_category");
                rowsData.put("Purpose", purpose);
                String status = reader.getString("ptystatus");
                rowsData.put("Status", status);
                String CompanyCode = reader.getString("company_code");
                String VendorCode = reader.getString("vendor_code");
                String send_Ben_Email = reader.getString("SEND_BEN_EMAIL");
                String bene_Email2 = "";
                String bene_Email = reader.getString("bene_email");
                String bene_Name = reader.getString("vendor_name");
             /*   String AccountNumber = reader.getString("debit_acct_no");
                if (this.isValidString(AccountNumber)) {
                    if (AccountNumber.length() >= 10) {
                        AccountNumber = AccountNumber.substring(0, 10);
                        AccountNumber = AccountNumber.substring(0, 2) + "******" + AccountNumber.substring(8);
                        rowsData.put("Account Number", AccountNumber);
                    }else{
                        rowsData.put("Account Number", "**********");
                    }
                }else{
                    rowsData.put("Account Number", "*********");
                }*/
               // String AccountName = reader.getString("FULLNAME");
                List validEmailsToBene;
                boolean emailSendToBene;
                if (send_Ben_Email.equalsIgnoreCase("Y")) {
                    if (!this.isValidString(bene_Email)) {
                        logger.info("vendor code : " + VendorCode);
                        if (isValidString(VendorCode)) {
                            bene_Email2 = getAlternateBeneEmail(VendorCode, CompanyCode, query1);
                        }
                        if (!this.isValidString(bene_Email2) || ("").equalsIgnoreCase(bene_Email2)) {
                            bene_Email2 = "";
                        }
                        sendToBene = bene_Email2;
                    } else {
                        sendToBene = bene_Email;
                    }
                     // sendToBene = "taiwo.ahmed@ucitech.com.ng"; // comment out later
                    logger.info("beneficiary : " + sendToBene);
                    validEmailsToBene = GenerateReceipts.validateEmails(sendToBene);
                    if (!sendToBene.equalsIgnoreCase("") && null != sendToBene && validEmailsToBene.size() > 0) {
                        if (status.equalsIgnoreCase("PROCESSED")) {
                            // Call method that processes the notification & send mail to beneficiary
                            emailSendToBene = GenerateReceipts.sendEmail(payment_Id, num, bene_Name.trim(), validEmailsToBene, rowsData, ref, amt, status, date, purpose, senderName);
                            if (emailSendToBene) {
                                logger.info("Email successfully sent to beneficiary.");
                            } else {
                                logger.info("Email sending failed to beneficiary.");
                            }
                        } else {
                            logger.info("Payment " + payment_Id + " to " + bene_Name + " is a failed transaction.");
                        }
                    } else {
                        logger.info("No Email address available for this beneficiary or Invalid Email");
                    }
                } else {
                    logger.info("No Email subscription for this beneficiary");
                }
                this.updateTableColumn(payment_Id, Query2);
            }
        } catch (Exception ex) {

            ex.printStackTrace();

            try {
                if (!ps.isClosed()) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (!con.isClosed()) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("ENDING SINGLE NOTIFICATION...................................");
    }

    // Method to send Email notification to uploader and beneficiary for APM bulk  payments
    private void sendBulkEmailNotificationForAPM(String SQL, String querystring, String uploader, Connection con, String batchUpdateQuery) {
        System.out.println("PROCESSING BULK NOTIFICATION FOR APM...................................");
        PreparedStatement ps = null;
        ResultSet reader = null;
        try {
            if (con.isClosed() || con == null) {
                con = loader.getInstance();
            }
            List<Long> Batch_List = this.getBulkBatchIdForAPM(SQL, con);
            List<PaymentModel> rowsData = null;
            PaymentModel pay = null;
            if(Batch_List.size() > 0 && Batch_List != null){
                for(int i = 0; i < Batch_List.size(); i++){
                    rowsData = new ArrayList<>();
                    ps = con.prepareStatement(querystring);
                    ps.setDouble(1, (double) Batch_List.get(i));
                    reader = ps.executeQuery();
                   System.out.println("Batch " + i +" : " + Batch_List.get(i));
                    while (reader.next()) {
                        pay = new PaymentModel();
                        pay.setTransferReference(reader.getString("Trans_ref"));
                        pay.setBeneficiaryName(reader.getString("vendor_name"));
                        pay.setBeneficiaryBank(reader.getString("vendor_bank"));
                        pay.setSenderName(reader.getString("account_name"));
                        pay.setPaymentId(String.valueOf((long)reader.getDouble("payment_id")));
                        pay.setValueDate(reader.getDate("payment_due_date").toString());
                        pay.setAmount(GenerateReceipts.formatAmount(Double.toString(reader.getDouble("amount"))));
                        pay.setCurrency(reader.getString("payment_currency"));
                        String num = reader.getString("vendor_acct_no");
                        if (this.isValidString(num)) {
                            if (num.length() >= 10) {
                                num = num.substring(0, 10);
                                num = num.substring(0, 2) + "******" + num.substring(8);
                                pay.setBeneficiaryAccount(num);
                            }else{
                                pay.setBeneficiaryAccount("**********");
                            }

                        } else {
                            pay.setBeneficiaryAccount("**********");
                        }
                        pay.setPurpose(reader.getString("vendor_category") == null ? "" : reader.getString("vendor_category"));
                        pay.setStatus(reader.getString("ptystatus"));
                        rowsData.add(pay);
                    }

                    List<String> validEmailsToBene = GenerateReceipts.validateEmails(uploader);
                    boolean emailSendToAPM = GenerateReceipts.sendBulkEmail(Batch_List.get(i), validEmailsToBene, rowsData);
                    if (emailSendToAPM) {
                        logger.info("Email sending successful to uploader.");
                    } else {
                        logger.info("Email sending failed to uploader.");
                    }
                    updateBulkTableBatchColumn(Batch_List.get(i), batchUpdateQuery);
                }
            }
        } catch (Exception ex) {

            ex.printStackTrace();

            try {
                if (!ps.isClosed()) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (!con.isClosed()) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("ENDING BULK NOTIFICATION FOR APM...................................");
    }

    // Method to send Email notification to beneficiary for bulk payments
    private void sendBulkEmailNotification(String query_string, String Query2, String query1, Connection con) {
        System.out.println("PROCESSING BULK NOTIFICATION...................................");
        PreparedStatement ps = null;
        ResultSet reader = null;
        String sendToBene = "";
        try {
            if (con.isClosed() || con == null) {
                con = loader.getInstance();
            }
            ps = con.prepareStatement(query_string);
            reader = ps.executeQuery();
            long payment_Id = 0;
            //for (ResultSet reader = ps.executeQuery(); reader.next(); this.updateBulkTableColumn(payment_Id, Query2)) {
            while(reader.next()){
                LinkedHashMap<String, String> rowsData = new LinkedHashMap();
                String ref = reader.getString("Trans_ref");
                rowsData.put("Your Ref", ref);
                rowsData.put("Beneficiary Name", reader.getString("vendor_name"));
                String senderName = reader.getString("account_name");
                rowsData.put("Sender Name", senderName);
                String date = reader.getDate("payment_due_date").toString();
                rowsData.put("Value Date", date);
                rowsData.put("Currency", reader.getString("payment_currency"));
                String amt = GenerateReceipts.formatAmount(Double.toString(reader.getDouble("amount")));
                rowsData.put("Amount", amt);
                String num = reader.getString("vendor_acct_no");
                if (this.isValidString(num)) {
                    if (num.length() >= 10) {
                        num = num.substring(0, 10);
                        num = num.substring(0, 2) + "******" + num.substring(8);
                        rowsData.put("Beneficiary Account", num);
                    }else{
                        rowsData.put("Beneficiary Account", "**********");
                    }
                } else {
                    rowsData.put("Beneficiary Account", "**********");
                }
                payment_Id = (long) reader.getDouble("payment_id");
                rowsData.put("Payment ID", String.valueOf(payment_Id));
                rowsData.put("Beneficiary Bank", reader.getString("vendor_bank"));
                if(reader.getString("payment_type").equalsIgnoreCase("INVOICE/DISCOUNT")){
                    rowsData.put("Tenor", String.valueOf(reader.getInt("tenor")));
                    rowsData.put("Discounted Days", String.valueOf(reader.getInt("discounted_days")));
                    rowsData.put("Discounted Amount", GenerateReceipts.formatAmount(Double.toString(reader.getDouble("discounted_amount"))));
                    rowsData.put("Invoice Date", reader.getString("invoice_date").toString());
                    rowsData.put("Invoice Duedate", reader.getString("invoice_duedate").toString());
                }
                if(isValidString(reader.getString("invoice_number"))){
                    rowsData.put("Invoice Number", reader.getString("invoice_number"));
                }
                String purpose = reader.getString("vendor_category");
                rowsData.put("Purpose", purpose);
                String status = reader.getString("ptystatus");
                rowsData.put("Status", status);
                String CompanyCode = reader.getString("company_code");
                String VendorCode = reader.getString("vendor_code");
                String send_Ben_Email = reader.getString("SEND_BEN_EMAIL");
                String bene_Email2 = "";
                String bene_Email = reader.getString("bene_email");
                String bene_Name = reader.getString("vendor_name");
               /* String AccountNumber = reader.getString("debit_acct_no");
                if (this.isValidString(AccountNumber)) {
                    if (AccountNumber.length() >= 10) {
                        AccountNumber = AccountNumber.substring(0, 10);
                        AccountNumber = AccountNumber.substring(0, 2) + "******" + AccountNumber.substring(8);
                        rowsData.put("Account Number", AccountNumber);
                    }else{
                        rowsData.put("Account Number", "**********");
                    }
                }else{
                    rowsData.put("Account Number", "**********");
                }*/
                // String AccountName = reader.getString("FULLNAME");
                List validEmailsToBene;
                boolean emailSendToBene;
                if (send_Ben_Email.equalsIgnoreCase("Y")) {
                    if (!this.isValidString(bene_Email)) {
                        if (this.isValidString(VendorCode)) {
                            bene_Email2 = this.getAlternateBeneEmail(VendorCode, CompanyCode, query1);
                        }
                        if (!this.isValidString(bene_Email2) || ("").equalsIgnoreCase(bene_Email2)) {
                            bene_Email2 = "";
                        }

                        sendToBene = bene_Email2;
                    } else {
                        sendToBene = bene_Email;
                    }
                    //  sendToBene = "taiwo.ahmed@ucitech.com.ng"; // comment out later
                    logger.info("beneficiary : " + sendToBene);
                    validEmailsToBene = GenerateReceipts.validateEmails(sendToBene);
                    if (!sendToBene.equalsIgnoreCase("") && null != sendToBene && validEmailsToBene.size() > 0) {
                        if (status.equalsIgnoreCase("PROCESSED")) {
                            // Call method that processes the notification & send mail to beneficiary
                            emailSendToBene = GenerateReceipts.sendEmail(payment_Id, num, bene_Name.trim(), validEmailsToBene, rowsData, ref, amt, status, date, purpose, senderName);
                            if (emailSendToBene) {
                                logger.info("Email successfully sent to beneficiary.");
                            } else {
                                logger.info("Email sending failed to beneficiary.");
                            }
                        } else {
                            logger.info("Payment " + payment_Id + " to " + bene_Name + " is a failed transaction.");
                        }
                    } else {
                        logger.info("No Email address available for this beneficiary or Invalid Email");
                    }
                } else {
                    logger.info("No Email subscription for this beneficiary");
                }
                this.updateBulkTableColumn(payment_Id, Query2);
            }
        } catch (Exception ex) {

            ex.printStackTrace();
            try {
                if (!ps.isClosed()) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (!con.isClosed()) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("ENDING BULK NOTIFICATION...................................");
    }


    private List<Long> getBulkBatchIdForAPM(String SQL, Connection con){
        List<Long> list = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(SQL);
            ResultSet reader = ps.executeQuery();
            while(reader.next()){
                list.add((long) reader.getDouble("batchid"));
            }
        }catch (Exception e){
            e.printStackTrace();

            try {
                if (!con.isClosed()) {
                    con.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return list;
    }
}