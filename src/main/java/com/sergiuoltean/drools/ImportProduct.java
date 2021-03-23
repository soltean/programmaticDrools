package com.sergiuoltean.drools;

import java.io.Serializable;
import lombok.Data;

@Data
public class ImportProduct implements Serializable {
  static final long serialVersionUID = 1L;
  private String id;
  private String category;
  private String status;
  private Double price;

  public ImportProduct() {
  }

  public ImportProduct(String id, String category, String status, Double price) {
    this.id = id;
    this.category = category;
    this.status = status;
    this.price = price;
  }
}