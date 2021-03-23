package com.sergiuoltean.drools.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.sergiuoltean.drools.ImportProduct;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ResultProcessor {

  public byte[] process(List<ImportProduct> list) throws JsonProcessingException {
    CsvMapper csvMapper = new CsvMapper();
    CsvSchema schema = csvMapper.schemaFor(ImportProduct.class).withHeader().withoutQuoteChar()
            .withColumnSeparator(',');
    return csvMapper.writer(schema).writeValueAsBytes(list);
  }

}
