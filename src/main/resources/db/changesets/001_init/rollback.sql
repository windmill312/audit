do
$$
    declare
tabname record;
begin
for tabname in (select tablename
                        from pg_tables
                        where schemaname = 'audit'
                          and tablename not in ('databasechangelog', 'databasechangeloglock'))
            loop
                execute 'DROP TABLE IF EXISTS ' || quote_ident(tabname.tablename) || ' CASCADE';
end loop;
end
$$;