package com.sergiuoltean.drools.processor;

import com.sergiuoltean.drools.RulesAndFacts;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExcelProcessor implements Processor {

  protected static final int DATA_START_INDEX = 1;

  @Override
  public void process(Exchange exchange) throws Exception {
    RulesAndFacts rulesAndFacts = exchange.getIn().getBody(RulesAndFacts.class);
    final InputStream dynamicRulesStream = rulesAndFacts.getDynamicRulesStream();
    if (Objects.nonNull(dynamicRulesStream)) {
      Workbook workbook = new XSSFWorkbook(dynamicRulesStream);
      rulesAndFacts.setDeleteRuleData(buildDeleteData(workbook));
    }
  }

  private List<Double> buildDeleteData(Workbook workbook) {
    List<Double> deleteRuleData = new ArrayList<>();
    Sheet deleteRules = workbook.getSheetAt(0);

    deleteRules.iterator().forEachRemaining(row -> {
      if (row.getRowNum() > DATA_START_INDEX && rowHasCells(row)) {
        double productId = row.getCell(0).getNumericCellValue();
        deleteRuleData.add(productId);
      }
    });
    return deleteRuleData;
  }

  private boolean rowHasCells(Row row) {
    return row.getPhysicalNumberOfCells() > 0 && row.getCell(row.getFirstCellNum()).getCellType() != CellType.BLANK;
  }

}