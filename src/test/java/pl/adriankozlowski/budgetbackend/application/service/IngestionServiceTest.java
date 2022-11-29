package pl.adriankozlowski.budgetbackend.application.service;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;

class IngestionServiceTest {

    @Test
    public void shouldPrepareListOfObjects() throws Exception {
        IngestionService ingestionService = new IngestionService(null, null,null);
        List<CsvTransaction> csvTransactions = ingestionService.beanBuilderExample(Paths.get(this.getClass().getResource("/Lista_transakcji_nr_0146070292_041122 - Lista_transakcji_nr_0146070292_041122.csv").toURI()));
        System.out.println();

    }

}