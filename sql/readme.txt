To install database, you need PostgreSQL. Version 9 recommended. Earlier versions not tested!

1. Install postgresql 9 (e.g. run "apt-get postgresql" in debian console as root).
2. Create new role for managing database (e.g. "weducation") and empty database (e.g. weducation).
3. Run script "weducation.sql" (e.g. run command psql -U weducation -h localhost -d weducation -f weducation.sql).
4. Copy file "liquibase.properties" into project root directory.
5. Build project to apply all liquibase changes.
6. Run script "addon.sql" to apply all addons.