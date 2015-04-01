package ru.edu.pgtk.weducation.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import ru.edu.pgtk.weducation.ejb.DelegatesEJB;
import ru.edu.pgtk.weducation.ejb.PersonsEJB;
import ru.edu.pgtk.weducation.ejb.PlacesEJB;
import ru.edu.pgtk.weducation.ejb.SchoolsEJB;
import ru.edu.pgtk.weducation.ejb.SpecialitiesEJB;
import ru.edu.pgtk.weducation.ejb.StudyCardsEJB;
import ru.edu.pgtk.weducation.entity.Delegate;
import ru.edu.pgtk.weducation.entity.Person;
import ru.edu.pgtk.weducation.entity.Place;
import ru.edu.pgtk.weducation.entity.PlaceType;
import ru.edu.pgtk.weducation.entity.School;
import ru.edu.pgtk.weducation.entity.Speciality;
import ru.edu.pgtk.weducation.entity.StudyCard;

@Stateless
public class ImportCardEJB {

  @EJB
  private PlacesEJB places;
  @EJB
  private SchoolsEJB schools;
  @EJB
  private SpecialitiesEJB specialities;
  @EJB
  private PersonsEJB persons;
  @EJB
  private StudyCardsEJB cards;
  @EJB
  private DelegatesEJB delegates;
  private Connection con;

  private static final String NOT_FOUND = "Запрос не вернул ни одной записи.";
  private static final String NULL = "При обработке возникло NullPointerException.";
  private static final String ERROR = "Ошибка при импорте ";
  private static final String PLACE = " населенного пункта. ";
  private static final String SCHOOL = " учебного заведения. ";
  private static final String CARD = " личной карточки. ";
  private static final String PERSON = " персоны. ";
  private static final String SPECIALITY = " специальности. ";

  @PostConstruct
  private void openConnection() {
    try {
      InitialContext initialContext = new InitialContext();
      DataSource dataSource = (DataSource) initialContext.lookup("jdbc/pgtk_mssql");
      if (null != dataSource) {
        con = dataSource.getConnection();
      }
    } catch (SQLException | NamingException e) {
      throw new EJBException("Ошибка! " + e.toString());
    }
  }

  @PreDestroy
  private void closeConnection() {
    try {
      con.close();
    } catch (Exception e) {
      // И что тут делать?
    }
  }

  /**
   * Получаем специальность по коду группы
   *
   * @param groupCode код группы
   * @return
   */
  private Speciality getSpeciality(final String groupCode) {
    try {
      PreparedStatement stmt = con
              .prepareStatement(
                      "SELECT * FROM Specialities, Groups WHERE (sp_pcode = gr_speccode) AND (gr_pcode=?);",
                      ResultSet.TYPE_SCROLL_INSENSITIVE,
                      ResultSet.CONCUR_READ_ONLY);
      stmt.setString(1, groupCode);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.first()) {
          // Что-то нашли, выбираем данные
          Speciality result = new Speciality();
          result.setKey(rs.getString("sp_Fullkey"));
          result.setFullName(rs.getString("sp_name"));
          result.setShortName(rs.getString("sp_shortName"));
          result.setKvalification(rs.getString("sp_kvalification"));
          result.setSpecialization(rs.getString("sp_specialization"));
          // А может такая специальность есть?
          Speciality existing = specialities.findLike(result);
          if (null != existing) {
            return existing;
          }
          return specialities.save(result);
        }
      }
      throw new EJBException(ERROR + SPECIALITY + NOT_FOUND);
    } catch (SQLException e) {
      throw new EJBException(ERROR + SPECIALITY + e.getMessage());
    } catch (NullPointerException e) {
      throw new EJBException(ERROR + SPECIALITY + NULL);
    }
  }

  private Person getPerson(final String personCode) {
    try {
      PreparedStatement stmt = con.prepareStatement(
              "SELECT * FROM students WHERE (st_pcode=?);",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      stmt.setString(1, personCode);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.first()) {
          Person result = new Person();
          result.setPassportSeria(rs.getString("st_passptSeria"));
          result.setPassportNumber(rs.getString("st_PassptNum"));
          result.setFirstName(rs.getString("st_FName"));
          result.setMiddleName(rs.getString("st_MName"));
          result.setLastName(rs.getString("st_LName"));
          result.setBirthDate(rs.getDate("st_birthDate"));
          result.setBirthPlace(rs.getString("st_birthPlace"));
          result.setMale(rs.getBoolean("st_ismale"));
          result.setForeign(false);
          result.setInvalid(false);
          result.setHomePhone(rs.getString("st_HomePhone"));
          result.setWorkPhone(rs.getString("st_WorkPhone"));
          result.setMobilePhone(rs.getString("st_CellPhone"));
          result.setPassportDate(rs.getDate("st_passptDate"));
          result.setPassportDept(rs.getString("st_passptDept"));
          result.setPlace(getPlace(rs.getString("st_plcode")));
          result.setAddress(rs.getString("st_Address"));
          result.setInn(rs.getString("st_INN"));
          result.setSnils(rs.getString("st_socialNum"));
          result.setOrphan(rs.getBoolean("st_isParentLess"));
          // А может такая персона уже есть? Вернем существующую.
          Person existing = persons.findLike(result);
          if (existing != null) {
            return existing;
          }
          return persons.save(result);
        }
      }
      throw new EJBException(ERROR + PERSON + NOT_FOUND);
    } catch (SQLException e) {
      throw new EJBException(ERROR + PERSON + e.getMessage());
    } catch (NullPointerException e) {
      throw new EJBException(ERROR + PERSON + NULL);
    }
  }
  
  private List<Delegate> getDelegates(final String personCode) {
    List<Delegate> result = new ArrayList<>();
    try {
      PreparedStatement stmt = con.prepareStatement(
              "SELECT * FROM parents WHERE (pr_stcode=?);",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      stmt.setString(1, personCode);
      try (ResultSet rs = stmt.executeQuery()) {
        while(rs.next()) {
          Delegate item = new Delegate();
          item.setFullName(rs.getString("pr_Name"));
          item.setJob(rs.getString("pr_JobPlace"));
          item.setPost(rs.getString("pr_Job"));
          item.setWorkPhone(rs.getString("pr_WorkPhone"));
          item.setMobilePhone(rs.getString("pr_CellPhone"));
          item.setHomePhone(rs.getString("pr_HomePhone"));
          item.setDescription(rs.getString("pr_Note"));
          result.add(item);
        }
      }
    } catch (SQLException e) {
      throw new EJBException(ERROR + SCHOOL + e.getMessage());
    } catch (NullPointerException e) {
      throw new EJBException(ERROR + SCHOOL + NULL);
    }
    return result;
  }

  private School getSchool(final String personCode) {
    try {
      PreparedStatement stmt = con.prepareStatement(
              "SELECT * FROM students, schools WHERE (st_sccode = sc_pcode) AND (st_pcode=?);",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      stmt.setString(1, personCode);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.first()) {
          School result = new School();
          result.setFullName(rs.getString("sc_Name"));
          result.setShortName(rs.getString("sc_ShortName"));
          result.setPlace(rs.getString("sc_place"));
          result.setDirector(rs.getString("sc_DName"));
          // Попробуем найти существующую...
          School existing = schools.findLike(result);
          if (null != existing) {
            return existing;
          }
          return schools.save(result);
        }
      }
      throw new EJBException(ERROR + SCHOOL + NOT_FOUND);
    } catch (SQLException e) {
      throw new EJBException(ERROR + SCHOOL + e.getMessage());
    } catch (NullPointerException e) {
      throw new EJBException(ERROR + SCHOOL + NULL);
    }
  }

  private Place getPlace(final String placeCode) {
    try {
      PreparedStatement stmt = con.prepareStatement(
              "SELECT * FROM places WHERE (pl_pcode = ?);",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      stmt.setString(1, placeCode);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.first()) {
          // Читаем поля записи
          Place result = new Place();
          result.setName(rs.getString("pl_Name"));
          result.setType(PlaceType.forValue(rs.getInt("pl_kind") - 1));
          Place existing = places.findLike(result);
          if (existing != null) {
            return existing;
          }
          return places.save(result);
        }
      }
    } catch (SQLException e) {
      throw new EJBException(ERROR + PLACE + e.getMessage());
    } catch (NullPointerException e) {
      throw new EJBException(ERROR + PLACE + NULL);
    }
    throw new EJBException("Не удалось получить населенный пункт");
  }

  /**
   * Импортирует все ыозможные данные.
   *
   */
  public void importAll() {
    try {
      PreparedStatement grpStatement = con.prepareStatement(
              "SELECT * FROM groups WHERE ((SELECT COUNT(*) FROM Students WHERE (st_grcode = gr_pcode)) > 0);",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      ResultSet grpRS = grpStatement.executeQuery();
      while (grpRS.next()) {
        // Бежим по группе, читаем данные и подготавливаем всё для импорта
        // TODO Добавить логику для отбора групп
        importGroup(grpRS.getString("gr_pcode"));
      }
    } catch (SQLException e) {
      throw new EJBException("Исключение при импорте данных " + e.getMessage());
    }
  }

  public void importGroup(final String grpCode) {
    try {
      PreparedStatement stmt = con.prepareStatement(
              "SELECT st_pcode FROM students WHERE (st_grcode=?);",
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      stmt.setString(1, grpCode);
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          importCard(rs.getString("st_pcode"));
        }
      }
    } catch (SQLException e) {
      throw new EJBException("Исключение при импорте группы " + e.getMessage());
    }
  }
  
  public void importCard(final String personCode) {
    try {
      PreparedStatement stmt = con.prepareStatement(
        "SELECT *, " +
        "(SELECT com_PDirector FROM comissions, sessions WHERE (ss_comcode = com_pcode) AND (ss_stcode = st_pcode)) AS com_PDirector, " +
        "(SELECT com_Date FROM comissions, sessions WHERE (ss_comcode = com_pcode) AND (ss_stcode = st_pcode)) AS com_Date " +
        "FROM students LEFT JOIN commands ON (cm_stcode = st_pcode) " +
        "WHERE (st_pcode= ?)",
        ResultSet.TYPE_SCROLL_INSENSITIVE,
        ResultSet.CONCUR_READ_ONLY);
      stmt.setString(1, personCode);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.first()) {
          Person psn = getPerson(personCode);
          // Удаляем имеющихся делегатов у персоны
          for(Delegate d: delegates.fetchAll(psn)) {
            delegates.delete(d);
          }
          // Импортируем делегатов
          for(Delegate d: getDelegates(personCode)) {
            d.setPerson(psn);
            delegates.save(d);
          }
          // А вдруг есть старые карточки? Seek and destroy!
          for(StudyCard c: cards.findByPerson(psn)) {
            cards.delete(c);
          }
          StudyCard card = new StudyCard();
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
          // Сохраняем новую карточку
          cards.save(card);
        }
      }
      throw new EJBException(ERROR + CARD + NOT_FOUND);
    } catch (SQLException | ParseException e) {
      throw new EJBException(ERROR + CARD + e.getMessage());
    } catch (NullPointerException e) {
      throw new EJBException(ERROR + CARD + NULL);
    }
  }
}
