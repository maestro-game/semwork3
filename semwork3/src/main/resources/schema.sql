-- create table if not exists persistent_logins
-- (
--     username  varchar(64) not null,
--     series    varchar(64) primary key,
--     token     varchar(64) not null,
--     last_used timestamp   not null
-- );

CREATE OR REPLACE FUNCTION generate_uid(size INT) RETURNS TEXT AS
'DECLARE
    characters TEXT := ''ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_'';
    l          INT  := length(characters);
    i          INT;
    done       bool := false;
    output     TEXT;
BEGIN
    WHILE NOT done
        LOOP
            output := '''';
            FOR i in 1..size
                LOOP
                    output := output || substr(characters, floor(random() * l)::int + 1, 1);
                END LOOP;
            done := NOT exists(SELECT 1
                               FROM content_source
                               WHERE id = output);
        END LOOP;
    RETURN output;

END;' LANGUAGE plpgsql VOLATILE;

-- CREATE OR REPLACE FUNCTION generate_uid_for_token(size INT) RETURNS TEXT AS
-- 'DECLARE
--     characters TEXT := ''ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_'';
--     l          INT  := length(characters);
--     i          INT;
--     done       bool := false;
--     output     TEXT;
-- BEGIN
--     WHILE NOT done
--         LOOP
--             output := '''';
--             FOR i in 1..size
--                 LOOP
--                     output := output || substr(characters, floor(random() * l)::int + 1, 1);
--                 END LOOP;
--             done := NOT exists(SELECT 1
--                                FROM content_source
--                                WHERE string_id = output);
--         END LOOP;
--     RETURN output || l;
--
-- END;' LANGUAGE plpgsql VOLATILE;
