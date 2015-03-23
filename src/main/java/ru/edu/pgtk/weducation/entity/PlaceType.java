package ru.edu.pgtk.weducation.entity;

import javax.ejb.EJBException;

public enum PlaceType {

  CIT, DER, POS, SEL, PGT;

  public String getDescription() {
    switch (this) {
      case CIT:
        return "город";
      case DER:
        return "деревня";
      case POS:
        return "поселок";
      case SEL:
        return "село";
      case PGT:
        return "поселок городского типа";
    }
    throw new EJBException("Попытка вернуть описание для неизвестного типа населенного пункта!");
  }
}
