    ОПИСАНИЕ
Данный проект является информационной системой для автоматизации
учебного процесса среднеспециального учебного заведения.

Проект создается для Прокопьевского горнотехнического техникума, поэтому
некоторые детали реализации (например, учет пропусков, начисление стипендии)
могут могут незначительно отличаться для других учебных заведений.

Если вас заинтересовал данный проект, вы можете свободно использовать эти
наработки для реализации своих потребностей. Любые предложения по функционалу
и его реализации вы можете присылать на адрес электронной почты gooamoko@rambler.ru


    1. УСТАНОВКА
Для установки программного продукта вам потребуется:
1. Java Development Kit версии 7 или 8.
2. Сервер приложений Glassfish 4.
3. Утилита сборки Apache Maven 3.
4. СУБД PostgreSQL версии 9 (желательно 9.1 или выше).


    1.1. СОЗДАНИЕ БАЗЫ ДАННЫХ
Данный проект не создает базу данных автоматически. Это означает, что вам нужно
будет создать пользователя и базу данных в СУБД PostgreSQL вручную.
В папке sql данного проекта можно найти необходимые для создания схемы данных
файлы, а также текстовый документ readme.txt с пояснениями о последовательности
действий по созданию базы данных.


    1.2. СОЗДАНИЕ РЕСУРСОВ ДЛЯ БАЗЫ ДАННЫХ НА СЕРВЕРЕ ПРИЛОЖЕНИЙ
Как только база данных будет создана, вы можете добавить JDBC connection pool
и JDBC Resource на сервере приложений Glassfish. Файл persistence.xml настроен на
имя ресурса weducation. Если имя ресурса будет другим, скорректируйте его
в persistence.xml


    1.3. ТЕСТИРОВАНИЕ
При сборке приложения выполняется прогон ряда тестов. Тестируются в основном
EJB компоненты, отвечающие за работу с базой данных. Для тестов используется
embedded-glassfish, который запускается отдельно для каждого теста.
Для настройки embedded-glassfish вам понадобятся конфигурационные файлы домена и
описания ресурсов JDBC в папке glassfish проекта.

