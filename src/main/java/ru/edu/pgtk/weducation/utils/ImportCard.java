package ru.edu.pgtk.weducation.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.ejb.EJBException;
import ru.edu.pgtk.weducation.entity.Person;
import ru.edu.pgtk.weducation.entity.School;
import ru.edu.pgtk.weducation.entity.Speciality;
import ru.edu.pgtk.weducation.entity.StudyCard;

public class ImportCard {

  public String pcode = ""; // Идентификатор группы
  private int cardCount = 0;
  private Connection con;
  private static final String NOT_FOUND = "Запрос не вернул ни одной записи.";
  private static final String NULL = "При обработке возникло NullPointerException.";
  private static final String ERROR = "Ошибка при импорте ";
  private static final String SCHOOL = " учебного заведения. ";
  private static final String CARD = " личной карточки. ";
  private static final String FMARK = " итоговой оценки. ";
  private static final String CMARK = " оценки за курсовой проект. ";
  private static final String PMARK = " оценки за практику. ";
  private static final String GMARK = " оценки за гос. экзамен. ";
  private static final String PERSON = " персоны. ";
  private static final String SPECIALITY = " специальности. ";
  private static final String SUBJECT = " дисциплины. ";
  private static final String PRACTIC = " практики. ";

  private void logAndThrow(String message) {
    throw new EJBException(message);
  }

  public int getCardCount() {
    return cardCount;
  }

  /**
   * Получаем специальность по коду группы
   *
   * @param groupCode код группы
   * @return
   */
  private Speciality getSpeciality(String groupCode) {
    Speciality result = null;
    try {
      PreparedStatement stmt = con
              .prepareStatement(
                      "SELECT * FROM Specialities, Groups WHERE (sp_pcode = gr_speccode) AND (gr_pcode=?);",
                      ResultSet.TYPE_SCROLL_INSENSITIVE,
                      ResultSet.CONCUR_READ_ONLY);
      stmt.setString(1, groupCode);
      ResultSet rs = stmt.executeQuery();
      if (rs.first()) {
        // Что-то нашли, выбираем данные
        result = new Speciality();
        result.setKey(rs.getString("sp_Fullkey"));
        result.setFullName(rs.getString("sp_name"));
        result.setShortName(rs.getString("sp_shortName"));
        result.setKvalification(rs.getString("sp_kvalification"));
        result.setSpecialization(rs.getString("sp_specialization"));
        // Если такая специальность уже есть, то будем использовать
        // существующую
      } else {
        logAndThrow(ERROR + SPECIALITY + NOT_FOUND);
      }
      rs.close();
      con.close();
    } catch (SQLException e) {
      logAndThrow(ERROR + SPECIALITY + e.getMessage());
    } catch (NullPointerException e) {
      logAndThrow(ERROR + SPECIALITY + NULL);
    }
    return result;
  }

  private Person getPerson(String personCode) {
    Person result = null;
    try {
      PreparedStatement stmt = con.prepareStatement(
              "SELECT * FROM students WHERE (st_pcode=?);",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      stmt.setString(1, personCode);
      ResultSet rs = stmt.executeQuery();
      if (rs.first()) {
        result = new Person();
        result.setBirthDate(rs.getDate("st_birthDate"));
        result.setBirthPlace(rs.getString("st_birthPlace"));
        result.setFirstName(rs.getString("st_FName"));
        result.setMiddleName(rs.getString("st_MName"));
        result.setLastName(rs.getString("st_LName"));
        result.setMale(rs.getBoolean("st_ismale"));
        result.setForeign(false);

        // А может такая персона уже есть?
        //Person ex = Person.findLike(result);
//				if (ex != null) {
//					return ex;
//				} else {
        // Сохранить новую персону
//				}
      } else {
        logAndThrow(ERROR + PERSON + NOT_FOUND);
      }
      rs.close();
      con.close();
    } catch (SQLException e) {
      logAndThrow(ERROR + PERSON + e.getMessage());
    } catch (NullPointerException e) {
      logAndThrow(ERROR + PERSON + NULL);
    }
    return result;
  }

  private School getSchool(String personCode) {
    School result = null;
    try {
      PreparedStatement stmt = con
              .prepareStatement(
                      "SELECT * FROM students, schools WHERE (st_sccode = sc_pcode) AND (st_pcode=?);",
                      ResultSet.TYPE_SCROLL_INSENSITIVE,
                      ResultSet.CONCUR_READ_ONLY);
      stmt.setString(1, personCode);
      ResultSet rs = stmt.executeQuery();
      if (rs.first()) {
        // Попробуем найти существующую по короткому наименованию
        // TODO найти школу

        // Создадим новую запись, если найти не удалось
        result = new School();
        result.setFullName(rs.getString("sc_Name"));
        result.setShortName(rs.getString("sc_ShortName"));
        result.setPlace(rs.getString("sc_place"));
        result.setDirector(rs.getString("sc_DName"));
      } else {
        logAndThrow(ERROR + SCHOOL + NOT_FOUND);
      }
      rs.close();
      con.close();
    } catch (SQLException e) {
      logAndThrow(ERROR + SCHOOL + e.getMessage());
    } catch (NullPointerException e) {
      logAndThrow(ERROR + SCHOOL + NULL);
    }
    return result;
  }

  private boolean prepareCard(String personCode) {
    boolean result = false;
    try {
      PreparedStatement stmt = con.prepareStatement(
              "SELECT * FROM students, comissions, sessions WHERE (ss_stcode = st_pcode) "
              + "AND (ss_comcode = com_pcode) AND (st_pcode=?);",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      stmt.setString(1, personCode);
      ResultSet rs = stmt.executeQuery();
      if (rs.first()) {
        StudyCard card = new StudyCard();
        Person psn = getPerson(personCode);
        card.setPerson(psn);
        School scl = getSchool(personCode);
        card.setSchool(scl);
        Speciality spc = getSpeciality(rs.getString("st_grcode"));
        card.setSpeciality(spc);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        card.setBeginDate(sdf.parse("01.09." + rs.getString("st_inYear")));
        card.setEndDate(sdf.parse("30.06." + rs.getString("st_outYear")));
        int attributes = rs.getInt("st_Attributes");
        card.setRemanded((attributes & 127) > 0);
        card.setRed(rs.getBoolean("st_isRed"));
        card.setComissionDirector(rs.getString("com_PDirector"));
        card.setComissionDate(rs.getDate("com_Date"));
        card.setDocumentDate(sdf.parse("25.06." + rs.getString("st_documentsYear").trim()));
        card.setDocumentName(rs.getString("st_documents"));
        card.setDocumentOrganization(""); // Ну нет сведений в старой базе!
        card.setDiplomeNumber(rs.getString("st_DiplNum"));
        card.setRegistrationNumber(rs.getString("st_DiplRegNum"));
        card.setDiplomeDate(rs.getDate("st_diplGetDate"));
        String theme = rs.getString("st_DProject");
        card.setGosExam(true);
        card.setDiplomeMark(rs.getInt("st_GOSMark"));
        if ((null != theme) && (!theme.isEmpty())) {
          card.setDiplomeTheme(theme);
          card.setGosExam(false);
          card.setDiplomeMark(rs.getInt("st_DMark"));
        }
        card.setExtramural(rs.getBoolean("st_isOutZaoch"));
				// А вдруг есть старые карточки? Seek and destroy!

        // Сохраняем новую карточку
        // Сохраняем информацию об оценках
        result = true;
      }
      rs.close();
      con.close();
    } catch (SQLException e) {
      logAndThrow(ERROR + CARD + e.getMessage());
    } catch (ParseException e) {
      logAndThrow(ERROR + CARD + e.getMessage());
    } catch (NullPointerException e) {
      logAndThrow(ERROR + CARD + NULL);
    }
    return result;
  }

  /**
   * Импортирует карточки из одной базы данных в другую.
   *
   * @return количество успешно импортированных карточек
   */
  public int importCards() {
    int count = 0;
    try {
      PreparedStatement stmt = con
              .prepareStatement(
                      "SELECT st_pcode FROM students WHERE (st_Attributes = 0) AND (st_grcode=?) ORDER BY st_FullName;",
                      ResultSet.TYPE_SCROLL_INSENSITIVE,
                      ResultSet.CONCUR_READ_ONLY);
      stmt.setString(1, pcode);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        cardCount += 1;
        if (prepareCard(rs.getString("st_pcode"))) {
          count += 1;
        }
      }
      rs.close();
      con.close();
    } catch (SQLException e) {
      throw new EJBException("Исключение при подготовке списка для импорта. "
              + e.getMessage());
    }
    return count;
  }

  /**
   * Возвращает список групп для выбора
   *
   * @return
   */
  public Map<String, String> importGroups() {
    LinkedHashMap<String, String> result = new LinkedHashMap<>();
    try {
      PreparedStatement stmt = con
              .prepareStatement("SELECT gr_pcode, gr_Name, COUNT(st_pcode) AS gr_students FROM Groups, Students "
                      + "WHERE (st_Attributes = 0) AND (st_grcode = gr_pcode) AND (gr_Attributes = 0) "
                      + "GROUP BY gr_pcode, gr_Name  ORDER BY gr_Name;");
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        result.put(rs.getString("gr_pcode"), rs.getString("gr_Name")
                + " (студентов: " + rs.getString("gr_students") + ")");
      }
      con.close();
    } catch (SQLException e) {
      result.put("Exception", e.getMessage());
    }
    return result;
  }
}
