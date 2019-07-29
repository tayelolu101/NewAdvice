package PaymentEmail;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.awt.*;
import java.io.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GenerateReceipts {


    private static void GeneratePdfFile(OutputStream outputStream,  LinkedHashMap<String, String> rowsData) {

        Document document = new Document(PageSize.A4, 0, 0, 0, 0);
        document.addCreationDate();
        document.addTitle("PaymentsAdvice");
        SimpleDateFormat sd = new SimpleDateFormat("EEE, MMM d, yyyy");

        try {

            PdfWriter.getInstance(document, outputStream);
            document.open();
            Image image = Image.getInstance("Zee_Logo.gif");
            image.setAlignment(Image.MIDDLE);
            Paragraph iParagraph = new Paragraph();
            iParagraph.setSpacingAfter(40f);
            document.add(iParagraph);
            document.add(image);

            Font fontBold = new Font();
            fontBold.setColor(Color.DARK_GRAY);
            fontBold.setSize(14f);
            fontBold.setStyle(Font.BOLD);

            Font bold = new Font();
            bold.setStyle(Font.BOLD);

            Font italic = new Font();
            italic.setStyle(Font.ITALIC);
            italic.setSize(9f);

            Paragraph headerText = new Paragraph("Payment Advice", fontBold);
            headerText.setAlignment(Paragraph.ALIGN_CENTER);

            document.add(headerText);

            float [] tWidth = new float[2];
            tWidth[0] = 10f;
            tWidth[1] = 10f;

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(70f);
            table.setWidths(tWidth);
            table.setSpacingBefore(20f);

            PdfPCell cell = new PdfPCell(new Paragraph(sd.format(new Date()), italic));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            cell.setPaddingBottom(5f);
            cell.setBorder(0);
            cell.setBackgroundColor(Color.WHITE);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setBackgroundColor(Color.WHITE);
            table.addCell(cell);

            Set set = rowsData.entrySet();
            Iterator iterator = set.iterator();

            while (iterator.hasNext()) {

                Map.Entry item = (Map.Entry) iterator.next();
                rowsData.put(item.getKey().toString(), item.getValue() != null ?  item.getValue().toString() : "");
            }

            PDFUtil pdfUtil = new PDFUtil();
            pdfUtil.addRows(rowsData, table);

            document.add(table);
            document.close();

        } catch(Exception ex) {

            Logger.getLogger(GenerateReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    private static void GenerateBulkPdfFile(OutputStream outputStream,  List<PaymentModel> rows) {

        PDFUtil util = new PDFUtil();
        Document document=new Document(PageSize.A4.rotate(),0,0,0,0);
        document.addCreationDate();
        document.addTitle("PaymentsAdvice");
        SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yyyy");

        try {

            PdfWriter.getInstance(document, outputStream);
            document.open();
            Image image = Image.getInstance("Zee_Logo.gif");
            image.setAlignment(Image.MIDDLE);
            Paragraph iParagraph = new Paragraph();
            iParagraph.setSpacingAfter(10f);
            document.add(iParagraph);
            document.add(image);

            Font fontBold = new Font();
            fontBold.setColor(Color.DARK_GRAY);
            fontBold.setSize(10f);
            fontBold.setStyle(Font.BOLD);

            Font bold = new Font();
            bold.setStyle(Font.BOLD);

            Font italic = new Font();
            italic.setStyle(Font.ITALIC);
            italic.setSize(9f);

            Paragraph headerText = new Paragraph("Payment Advice", fontBold);
            headerText.setAlignment(Paragraph.ALIGN_CENTER);

            document.add(headerText);

            float [] tWidth = new float[11];
            tWidth[0] = 20f; tWidth[1] = 5f; tWidth[2] = 15f; tWidth[3] = 20f; tWidth[4] = 10f; tWidth[5] = 10f;
            tWidth[6] = 5f; tWidth[7] = 10f; tWidth[8] = 15f; tWidth[9] = 10f; tWidth[10] = 10f;

            PdfPTable table = new PdfPTable(11);
            table.setWidthPercentage(100f);
            //table.setWidths(tWidth);
            table.setSpacingBefore(1f);

            PdfPCell cell = new PdfPCell(new Paragraph("DATE : ", italic));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            cell.setBorder(0);
            cell.setBackgroundColor(Color.WHITE);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph( sd.format(new Date()), italic));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            cell.setPaddingBottom(10f);
            cell.setBorder(0);
            cell.setBackgroundColor(Color.WHITE);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setBackgroundColor(Color.WHITE);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setBackgroundColor(Color.WHITE);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setBackgroundColor(Color.WHITE);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setBackgroundColor(Color.WHITE);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setBackgroundColor(Color.WHITE);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setBackgroundColor(Color.WHITE);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setBackgroundColor(Color.WHITE);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setBackgroundColor(Color.WHITE);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setBackgroundColor(Color.WHITE);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("REF"));
            //cell.setBorder(0);
            // cell.setPadding(1f);
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("ID"));
            // cell.setBorder(0);
            // cell.setPadding(1f);
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("SENDER"));
            //cell.setBorder(0);
            //cell.setPadding(1f);
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("NAME"));
            //cell.setBorder(0);
            //cell.setPadding(1f);
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("ACCT"));
            //cell.setBorder(0);
            //cell.setPadding(1f);
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("BANK"));
            // cell.setBorder(0);
            //cell.setPadding(1f);
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("CURR."));
            //cell.setBorder(0);
            //cell.setPadding(1f);
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("AMT"));
            //cell.setBorder(0);
            // cell.setPadding(1f);
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("DATE"));
            //cell.setBorder(0);
            //cell.setPadding(1f);
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("STATUS"));
            //cell.setBorder(0);
            // cell.setPadding(1f);
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("REASON"));
            // cell.setBorder(0);
            // cell.setPadding(1f);
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            table.addCell(cell);

            for (int row = 0; row < rows.size(); row ++) {
                Color bgc = util.getNextColor();
                cell = new PdfPCell(new Paragraph(rows.get(row).getTransferReference()));
                //cell.setBorder(0);
                //cell.setPadding(1f);
                cell.setBackgroundColor(bgc);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(rows.get(row).getPaymentId()));
                // cell.setBorder(0);
                //cell.setPadding(1f);
                cell.setBackgroundColor(bgc);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(rows.get(row).getSenderName()));
                // cell.setBorder(0);
                //cell.setPadding(1f);
                cell.setBackgroundColor(bgc);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(rows.get(row).getBeneficiaryName()));
                //cell.setBorder(0);
                //cell.setPadding(1f);
                cell.setBackgroundColor(bgc);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(rows.get(row).getBeneficiaryAccount()));
                // cell.setBorder(0);
                // cell.setPadding(1f);
                cell.setBackgroundColor(bgc);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(rows.get(row).getBeneficiaryBank()));
                //cell.setBorder(0);
                //cell.setPadding(1f);
                cell.setBackgroundColor(bgc);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(rows.get(row).getCurrency()));
                //cell.setBorder(0);
                // cell.setPadding(1f);
                cell.setBackgroundColor(bgc);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(rows.get(row).getAmount()));
                //cell.setBorder(0);
                // cell.setPadding(1f);
                cell.setBackgroundColor(bgc);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(rows.get(row).getValueDate()));
                //cell.setBorder(0);
                //cell.setPadding(1f);
                cell.setBackgroundColor(bgc);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(rows.get(row).getStatus()));
                //cell.setBorder(0);
                // cell.setPadding(1f);
                cell.setBackgroundColor(bgc);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(rows.get(row).getPurpose()));
                //cell.setBorder(0);
                //cell.setPadding(1f);
                cell.setBackgroundColor(bgc);
                table.addCell(cell);
            }

            document.add(table);
            document.close();

        } catch(Exception ex) {

            Logger.getLogger(GenerateReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public static boolean sendEmail(long payment_id, String AccountNumber, String AccountHoldersName, List<String> emailTo, LinkedHashMap<String, String> rowsData,
                                    String ref, String amt, String status, String date, String purpose, String senderName) {

        ByteArrayOutputStream outputStream = null;
        boolean condition;
        if(purpose == null || purpose.equalsIgnoreCase("")){
            purpose = "Not stated.";
        }

        try {

            Properties prop = ProperLoader.getProps();

            String Host = prop.getProperty("smtp.host");
            //String copyEmails  = prop.getProperty("mail.copy");
            int Port = Integer.parseInt(prop.getProperty("smtp.port"));
            String sender = prop.getProperty("mail.sender");
            String BCC = prop.getProperty("mail.beneCopy1");
            Properties properties = new Properties();
            properties.put("mail.smtp.host", Host);
            properties.put("mail.smtp.port", Port);
            Session session = Session.getDefaultInstance(properties, null);

            String content = "";
            String subject = "Payment Advice";

            InputStream input = GenerateReceipts.class.getResourceAsStream("/PaymentEmail/CorpIBankEmailTemp.html");
            InputStreamReader inputStreamReader = new InputStreamReader(input);
            BufferedReader br = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder("");
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            content += sb.toString();
            content =  content.replace("$name", AccountHoldersName).replace("$AccountNo", AccountNumber);
            content = content.replace("$reference", ref).replace("$Amount", amt).replace("$Sender", senderName);
            content = content.replace("$Status", status).replace("$date",date).replace("$Purpose", purpose);

            br.close();
            //construct the text body part
            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setContent(content, "text/html");

            //now write the PDF content to the output stream
            outputStream = new ByteArrayOutputStream();
            GeneratePdfFile(outputStream, rowsData);
            byte [] bytes = outputStream.toByteArray();

            //construct the pdf body part
            DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
            MimeBodyPart pdfBodyPart = new MimeBodyPart();
            pdfBodyPart.setDataHandler(new DataHandler(dataSource));
            AccountHoldersName = AccountHoldersName
                    .replace("\\","_")
                    .replace("/", "_")
                    .replace(":","_")
                    .replace("*","_")
                    .replace("<", "_")
                    .replace(">","_")
                    .replace("?","_")
                    .replace("|", "_")
                    .replace("\"","_");
            // Random random = new Random();
            String filename = "Transaction_details_" + AccountHoldersName + "_" + payment_id + ".pdf";
            pdfBodyPart.setFileName(filename);
            pdfBodyPart.saveFile(filename);

            //construct the mime multi part
            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart(textBodyPart);
            mimeMultipart.addBodyPart(pdfBodyPart);

            //create the sender/recipient addresses
            InternetAddress iaSender = new InternetAddress(sender);
            InternetAddress[]  iaRecipients = InternetAddress.parse(String.join(",", emailTo));

            //construct the mime message
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setSender(iaSender);
            mimeMessage.setSubject(subject);
            mimeMessage.setRecipients(Message.RecipientType.TO, iaRecipients);
            mimeMessage.setRecipient(Message.RecipientType.BCC, new InternetAddress(BCC));
            mimeMessage.setContent(mimeMultipart);
            //send the email
             Transport.send(mimeMessage);
            Logger.getLogger(GenerateReceipts.class.getName()).log(Level.INFO, "Email sent successful for payment : " + payment_id);
            condition = true;

            try{
                File file  =  new File(filename);
                file.delete();
            } catch (Exception e){
                Logger.getLogger("Error trying to delete file at " + filename);
                e.printStackTrace();
            }

        } catch(Exception ex) {
            condition = false;
            Logger.getLogger(GenerateReceipts.class.getName()).log(Level.SEVERE, null, ex);
            Logger.getLogger("Message sending failed for Payment " + AccountHoldersName+ " _" + payment_id + " to " + emailTo.toString() );
        } finally {
            //clean off
            if(null != outputStream) {
                try {
                    outputStream.close();
                }
                catch(Exception ex) {ex.printStackTrace();}
            }
        }
        return condition;
    }

    public static boolean sendBulkEmail(long BatchId, List<String> emailTo, List<PaymentModel> rowsData) {

        ByteArrayOutputStream outputStream = null;
        boolean condition;

        try {

            Properties prop = ProperLoader.getProps();

            String Host = prop.getProperty("smtp.host");
            //String copyEmails  = prop.getProperty("mail.copy");
            int Port = Integer.parseInt(prop.getProperty("smtp.port"));
            String sender = prop.getProperty("mail.sender");
            String BCC1 = prop.getProperty("mail.beneCopy1");
            Properties properties = new Properties();
            properties.put("mail.smtp.host", Host);
            properties.put("mail.smtp.port", Port);
            Session session = Session.getDefaultInstance(properties, null);

            String content = "Hello Folashade, <br/>" +
                              "  Kindly find attached a copy of your processed bulk payment upload advice for batch " + BatchId + ". <br/>"+
                              "  Thank you.";
            String subject = "Payment Advice";

           /* InputStream input = GenerateReceipts.class.getResourceAsStream("/PaymentEmail/CorpIBankEmailTemp.html");
            InputStreamReader inputStreamReader = new InputStreamReader(input);
            BufferedReader br = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder("");
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            content += sb.toString();
            content =  content.replace("$name", AccountHoldersName).replace("$AccountNo", AccountNumber);
            content = content.replace("$reference", ref).replace("$Amount", amt).replace("$Sender", senderName);
            content = content.replace("$Status", status).replace("$date",date).replace("$Purpose", purpose);

            br.close(); */
            //construct the text body part
            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setContent(content, "text/html");
            //textBodyPart.setText(content, "utf-8" );

            //now write the PDF content to the output stream
            outputStream = new ByteArrayOutputStream();
            GenerateBulkPdfFile(outputStream, rowsData);
            byte [] bytes = outputStream.toByteArray();

            //construct the pdf body part
            DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
            MimeBodyPart pdfBodyPart = new MimeBodyPart();
            pdfBodyPart.setDataHandler(new DataHandler(dataSource));

            // Random random = new Random();
            String filename = "Transaction_details_for_batch_" + BatchId +".pdf";
            pdfBodyPart.setFileName(filename);
            pdfBodyPart.saveFile(filename);

            //construct the mime multi part
            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart(textBodyPart);
            mimeMultipart.addBodyPart(pdfBodyPart);

            //create the sender/recipient addresses
            InternetAddress iaSender = new InternetAddress(sender);
            InternetAddress[]  iaRecipients = InternetAddress.parse(String.join(",", emailTo));

            //construct the mime message
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setSender(iaSender);
            mimeMessage.setSubject(subject);
            mimeMessage.setRecipients(Message.RecipientType.TO, iaRecipients);
            mimeMessage.setRecipient(Message.RecipientType.BCC, new InternetAddress(BCC1));
            mimeMessage.setContent(mimeMultipart);
            //send the email
            Transport.send(mimeMessage);
            Logger.getLogger(GenerateReceipts.class.getName()).log(Level.INFO, "Email sent successful for batch " + BatchId);
            condition = true;

            try{
                File file  =  new File(filename);
                file.delete();
            } catch (Exception e){
                Logger.getLogger("Error trying to delete file at " + filename);
                e.printStackTrace();
            }

        } catch(Exception ex) {
            condition = false;
            Logger.getLogger(GenerateReceipts.class.getName()).log(Level.SEVERE, null, ex);
            Logger.getLogger("Message sending failed for Payment "  + BatchId);
        } finally {
            //clean off
            if(null != outputStream) {
                try {
                    outputStream.close();
                }
                catch(Exception ex) {ex.printStackTrace();}
            }
        }
        return condition;
    }

    public static String formatAmount(String amount) throws Exception{

        double myAmount = Double.parseDouble(amount);
        NumberFormat formatter = NumberFormat.getIntegerInstance(Locale.ENGLISH);
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return  formatter.format(myAmount);

    }

    public static  List<String> validateEmails(String emailStr) {

        String[] emails  = emailStr.split(",");
        List<String> validEmails  = new ArrayList<String>();
        for (String e : emails){
            Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(e);
            if(matcher.find()){
                validEmails.add(e.trim());
            }
        }
        return validEmails;
    }

}
