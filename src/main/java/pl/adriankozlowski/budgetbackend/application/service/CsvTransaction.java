package pl.adriankozlowski.budgetbackend.application.service;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;

@Data
public class CsvTransaction {

    @CsvBindByName(column = "Data transakcji")
    private String transactionDate;
    @CsvBindByName(column = "Tytuł")
    private String title;
    @CsvBindByName(column = "Nr transakcji")
    private String transactionNumber;
    @CsvBindByName(column = "Kwota transakcji (waluta rachunku)")
    private String value;
    @CsvBindByName(column = "Kwota blokady/zwolnienie blokady")
    private String block;
    @CsvBindByName(column = "Nr rachunku")
    private String account;
    @CsvBindByName(column = "Konto")
    private String accountName;
    @CsvBindByName(column = "Saldo po transakcji")
    private String saldo;
}
