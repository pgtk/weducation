package ru.edu.pgtk.weducation.entity;

import javax.ejb.EJBException;

public enum PlaceType {

  CIT, POS, SEL, PGT, DER;

  public String getDescription() {
    switch (this) {
      case CIT:
        return "город";
      case POS:
        return "поселок";
      case SEL:
        return "село";
      case PGT:
        return "поселок городского типа";
      case DER:
        return "деревня";
    }
    throw new EJBException("Попытка вернуть описание для неизвестного типа населенного пункта!");
  }
  
  public String getPrefix() {
    switch (this) {
      case CIT:
        return "г.";
      case POS:
        return "п.";
      case SEL:
        return "с.";
      case PGT:
        return "пгт.";
      case DER:
        return "д.";
    }
    throw new EJBException("Попытка вернуть описание для неизвестного типа населенного пункта!");
  }
}
