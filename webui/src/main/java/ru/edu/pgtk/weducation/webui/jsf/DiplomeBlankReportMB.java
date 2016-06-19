package ru.edu.pgtk.weducation.webui.jsf;

import ru.edu.pgtk.weducation.core.ejb.DepartmentsDAO;
import ru.edu.pgtk.weducation.core.ejb.SessionDAO;
import ru.edu.pgtk.weducation.core.entity.Account;
import ru.edu.pgtk.weducation.core.entity.AccountRole;
import ru.edu.pgtk.weducation.core.entity.Department;
import ru.edu.pgtk.weducation.core.reports.dao.DiplomeBlanksDAO;
import ru.edu.pgtk.weducation.core.reports.entity.DiplomeBlank;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Компонент-подложка для генерации отчета по бланкам дипломов.
 * Created by leonid on 19.06.16.
 */
@Named("diplomeBlankReport")
@ViewScoped
public class DiplomeBlankReportMB implements Serializable {

    @EJB
    private transient DiplomeBlanksDAO blanksDao;
    @Inject
    private transient SessionDAO sessionDao;
    @EJB
    private transient DepartmentsDAO departmentsDao;
    private int studyFormCode = 0;
    private List<DiplomeBlank> diplomes;
    private boolean error;
    private boolean departmentOnly;

    @PostConstruct
    public void getList() {
        if (sessionDao != null) {
            departmentOnly = false;
            // Получим текущего пользователя
            Account currentUser = sessionDao.getUser();
            if (currentUser != null) {
                if (currentUser.isDepartment()) {
                    try {
                        // Если пользователь принадлежит отделению - выводим только список отделения
                        departmentOnly = true;
                        Department dep = departmentsDao.get(currentUser.getCode());
                        diplomes = blanksDao.fetchForDepartment(dep);
                    } catch (Exception e) {
                        Utils.addMessage(e);
                        error = true;
                    }
                } else {
                    AccountRole role = currentUser.getRole();
                    if (role == AccountRole.ADMIN || role == AccountRole.DEPOT) {
                        // Это админ или учебная часть. выводим всё
                        try {
                            diplomes = studyFormCode == 0 ? blanksDao.fetchAll() : blanksDao.fetchAll(studyFormCode == 2);
                        } catch (Exception e) {
                            Utils.addMessage(e);
                            error = true;
                        }
                    } else {
                        Utils.addMessage("Вам не разрешено просматривать этот отчет");
                        error = true;
                    }
                }
            } else {
                // Похоже, пользователь не авторизован!
                Utils.addMessage("Невозможно получить информацию о пользователе. Возможно, вы не осуществили вход!");
                error = true;
            }
        }
    }

    public int getStudyFormCode() {
        return studyFormCode;
    }

    public void setStudyFormCode(int studyFormCode) {
        this.studyFormCode = studyFormCode;
    }

    public List<DiplomeBlank> getDiplomes() {
        return diplomes;
    }

    public boolean isError() {
        return error;
    }

    public boolean isDepartmentOnly() {
        return departmentOnly;
    }
}
