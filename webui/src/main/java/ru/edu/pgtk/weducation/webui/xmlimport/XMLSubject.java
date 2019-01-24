package ru.edu.pgtk.weducation.webui.xmlimport;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для реализации дисциплин, либо МДК.
 *
 * @author Воронин Леонид
 */
public class XMLSubject {

    private String index = "";    // индекс (ЕН, ОП)
    private String name = "";     // Наименование предмета
    private XMLModule module = null; // родительский элемент
    private List<XMLSubjectLoad> load = new ArrayList<>(); // список нагрузки по семестрам

    public XMLSubject() {
        super();
    }

    @Override
    public String toString() {
        return "Subject [index=" + index + ", name=" + name + ", load=" + load + "]";
    }

    public XMLSubject(XMLModule module, String index, String name) {
        this.index = index;
        this.name = name;
        this.module = module;
    }

    public void addLoad(XMLSubjectLoad subjLoad) {
        load.add(subjLoad);
    }

    public List<XMLSubjectLoad> getLoad() {
        return load;
    }

    public String getFullName() {
        return index + " " + name;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public XMLModule getModule() {
        return module;
    }

    public void setModule(XMLModule module) {
        this.module = module;
    }
}
