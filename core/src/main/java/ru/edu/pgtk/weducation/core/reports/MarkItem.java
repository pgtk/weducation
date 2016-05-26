package ru.edu.pgtk.weducation.core.reports;

import ru.edu.pgtk.weducation.core.entity.FinalMark;
import ru.edu.pgtk.weducation.core.entity.FinalPracticMark;
import ru.edu.pgtk.weducation.core.entity.GOSMark;
import ru.edu.pgtk.weducation.core.utils.Utils;

/**
 * Класс для хранения элемента списка итоговых оценок в выписке. В элементы
 * именно этого класса будут преобразованы итоговые оценки, практики и т.п.
 *
 * @author Воронин Леонид
 *
 */
class MarkItem {

  public String subject = "";
  public String load = "";
  public String mark = "x";

  MarkItem(final String subject, final String load, final String mark) {
    this.subject = subject;
    this.load = load;
    this.mark = mark;
  }

  MarkItem(final String subject, final float load, final int mark) {
    this.subject = subject;
    this.load = Utils.getLenString(load);
    this.mark = Utils.getMarkString(mark);
  }

  MarkItem(final String subject, final int load, final int mark) {
    this.subject = subject;
    this.load = load + "";
    this.mark = Utils.getMarkString(mark);
  }

  MarkItem(final FinalMark mark) {
    if (mark.isModuleMark()) {
      this.subject = mark.getModule().getName();
    } else {
      this.subject = mark.getSubject().getFullName();
    }
    if (mark.getMaximumLoad() > 0) {
      this.load = mark.getMaximumLoad() + "";
    }
    this.mark = Utils.getMarkString(mark.getMark());
  }

  MarkItem(final FinalPracticMark mark) {
    this.subject = mark.getPractic().getName();
    this.load = Utils.getLenString(mark.getPractic().getLength());
    this.mark = Utils.getMarkString(mark.getMark());
  }

  MarkItem(final GOSMark mark) {
    this.subject = "Государственный экзамен (" + mark.getSubject().getFullName() + ")";
    this.load = "x";
    this.mark = Utils.getMarkString(mark.getMark());
  }

}
