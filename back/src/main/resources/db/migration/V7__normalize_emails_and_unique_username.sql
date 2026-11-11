-- V7 добавлена после того, как V1-V6 уже выполнены. Flyway применит только ее, старые миграции повторно не запускаются.
-- Почты читателей приводятся к единому виду: без пробелов по краям и в нижнем регистре.
UPDATE app_user
SET email = lower(trim(email))
WHERE email <> lower(trim(email));

-- Логин, пароль и роль обязательны.
ALTER TABLE app_user ALTER COLUMN username SET NOT NULL;
ALTER TABLE app_user ALTER COLUMN password SET NOT NULL;
ALTER TABLE app_user ALTER COLUMN role SET NOT NULL;

-- Логин уникален на уровне БД, раньше это проверялось только в коде при регистрации.
ALTER TABLE app_user ADD CONSTRAINT uq_app_user_username UNIQUE (username);
