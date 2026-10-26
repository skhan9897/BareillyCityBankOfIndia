package com.bank.bareillycitybankofindia.service.impl;

import com.bank.bareillycitybankofindia.model.Account;
import com.bank.bareillycitybankofindia.model.Transfer;
import com.bank.bareillycitybankofindia.service.ReportService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ReportServiceImpl implements ReportService {

    @Override
    public void generateAccountStatementPdf(Account account, List<Transfer> transactions, OutputStream out) throws Exception {
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage();
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 18);
                cs.newLineAtOffset(50, 750);
                cs.showText("BAREILLY CITY BANK OF INDIA");
                cs.endText();

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 12);
                cs.newLineAtOffset(50, 725);
                cs.showText("ACCOUNT STATEMENT");
                cs.endText();

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 10);
                cs.newLineAtOffset(50, 700);
                cs.showText("Account Number: " + (account != null ? account.getAccountNumber() : "N/A"));
                cs.newLineAtOffset(0, -15);
                cs.showText("Customer Name: " + (account != null && account.getCustomerName() != null ? account.getCustomerName() : "Valued Customer"));
                cs.newLineAtOffset(0, -15);
                cs.showText("Available Balance: INR " + (account != null ? account.getAvailableBalance() : "0.00"));
                cs.endText();

                int y = 630;
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 10);
                cs.newLineAtOffset(50, y);
                cs.showText("Ref No                 Type          Amount (INR)     Status");
                cs.endText();

                y -= 20;
                cs.setFont(PDType1Font.HELVETICA, 9);
                for (Transfer t : transactions) {
                    if (y < 80) break;
                    cs.beginText();
                    cs.newLineAtOffset(50, y);
                    String line = String.format("%-22s %-12s %-16s %-10s",
                            t.getTransactionRef(),
                            t.getTransactionType(),
                            t.getAmount().toString(),
                            t.getStatus());
                    cs.showText(line);
                    cs.endText();
                    y -= 15;
                }
            }
            doc.save(out);
        }
    }

    @Override
    public void generateTransactionsCsv(List<Transfer> transactions, OutputStream out) throws Exception {
        try (OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
             CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT.builder().setHeader("Transaction Ref", "Type", "Amount", "Status", "Date").build())) {
            for (Transfer t : transactions) {
                printer.printRecord(
                        t.getTransactionRef(),
                        t.getTransactionType(),
                        t.getAmount(),
                        t.getStatus(),
                        t.getCreatedAt()
                );
            }
            printer.flush();
        }
    }
}
