package ru.edu.pgtk.weducation.reports.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;

/**
 * Класс для представления информации о пропусках в каких-либо отчетах.
 *
 * Данный класс отличается от {@code Missing} отсутствием какой-либо информации
 * о неделе, месяце или годе. Тут есть только информация о персоне, личной
 * карточке и количестве пропусков.
 *
 * @author Воронин Леонид
 */
@Entity
@SqlResultSetMappings({
  @SqlResultSetMapping(
    name = "missingSummary",
    classes = {
      @ConstructorResult(targetClass = ReportMissing.class,
        columns = {
          @ColumnResult(name = "mis_crdcode", type = Integer.class),
          @ColumnResult(name = "mis_legal", type = Integer.class),
          @ColumnResult(name = "mis_illegal", type = Integer.class),}
      )
    }
  )
})
public class ReportMissing implements Serializable {

  @Id
  @Column(name = "mis_crdcode")
  private int cardCode;
  @Column(name = "mis_legal")
  private int legal;
  @Column(name = "mis_illegal")
  private int illegal;

  /**
   * Конструктор класса.
   *
   * @param cardCode код личной карточки персоны
   * @param legal количество уважительных пропусков
   * @param illegal количество неуважительных пропусков
   */
  public ReportMissing(final int cardCode, final int legal, final int illegal) {
    this.cardCode = cardCode;
    this.legal = legal;
    this.illegal = illegal;
  }

  public ReportMissing() {
  }

  public int getAll() {
    return legal + illegal;
  }

  public int getLegal() {
    return legal;
  }

  public void setLegal(int legal) {
    this.legal = legal;
  }

  public int getIllegal() {
    return illegal;
  }

  public void setIllegal(int illegal) {
    this.illegal = illegal;
  }

  public int getCardCode() {
    return cardCode;
  }
}
