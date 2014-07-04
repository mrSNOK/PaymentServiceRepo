PaymentService
==============
Описание проекта
----------------

Пользователи имеют счет в системе, который они могут пополнять при помощи Администратора. Администратор вносит определенную сумму на личный счет через администраторский интерфейс. Система полностью i18n (RU, EN).

**Роли в системе:**

1.	Гость
2.	Пользователь
3.	Администратор

**Гость:**

1.	Может пройти регистрацию и стать пользователем в системе
	*	При регистрации он вводит свой email, пароль, подтверждение пароля
	*	Система производит валидацию email адреса, проверяет свободен ли email, сверяет пароль и его подтверждение
	*	После успешной регистрации Пользователь автоматический входит в систему без дополнительного ввода пароля
2.	Может войти в систему 
	*	Для входа в систему он должен указать свой email и пароль 

**Пользователь:**

1.	После входа в систему видит свой текущий баланс.
2.	Может выйти из системы (возвращается на страницу Входа)

**Администратор:**

1.	Видит страницу Управления балансами пользователей 
	*	Список организован в виде таблицы, в которой отображается email пользователя, текущий баланс и дата регистрации
	*	Есть возможность выбора кол-ва пользователей на странице
	*	Есть пагинация
	*	Есть возможность поиска по полю Email
2.	Может пополнить счет пользователям
	*	При щелчке на email адрес пользователя Администратору всплывает модальное окно, где он вводит сумму пополнения
	*	После ввода суммы окно браузера со списком балансов пользователей не перегружаеться, но текущий баланс 		 	пользователя обновляется, с учетом суммы пополнения
3.	Может просмотреть журнал всех пополнений
	*	Журнал организован в виде таблицы, в которой отображается Администратор, Пользователь, Дата пополнения и Сумма пополнения.
	*	Есть пагинация
	*	Есть возможность выбора кол-ва операций на странице
	*	Есть возможность поиска по дате пополнения (можно задать начальную и/или конечную дату)

Технологии
----------

**Серверная часть**

Многослойная web-based система с использованием IOC. Фреймворки: Spring(MVC, Web, Data, Security), Hibernate(ORM, Validation). Тестирование :Spring test(Mock-MVC), Mockito, Junit. Пул соединений c3p0. Контейнер сервлетов :Tomcat 7. Многоуровневая система логирования : Log4j.

**Клиентская часть**

HTML/CSS, Javascript(Jquery)

База данных
-----------

СУБД MySQL

```
CREATE database payment;
USE payment;

create table if not exists users (
id int(7) auto_increment,
email varchar(255) not null unique,
password varchar(255) not null,
registred TIMESTAMP not null,
balance float(7,2) default 0,
primary key (id)
)ENGINE = InnoDB CHARACTER SET=UTF8;

create table if not exists roles (
id int(7) auto_increment,
role varchar(255) not null unique,
primary key (id)
)ENGINE = InnoDB CHARACTER SET=UTF8;

create table if not exists users_roles (
id int(7) auto_increment,
user_id int(7) not null,
role_id int(7) not null,
primary key (id),
FOREIGN KEY (user_id) REFERENCES users(id)
	ON UPDATE CASCADE
    ON DELETE CASCADE,
FOREIGN KEY (role_id) REFERENCES roles(id)
	ON UPDATE CASCADE
	ON DELETE CASCADE
)ENGINE = InnoDB CHARACTER SET=UTF8;

create table if not exists operations (
id int(7) auto_increment,
amount float(7,2) not null,
created TIMESTAMP not null,
primary key (id)
)ENGINE = InnoDB CHARACTER SET=UTF8;

create table if not exists users_operations (
id int(7) auto_increment,
user_id int(7) not null,
operation_id int(7) not null,
primary key (id),
FOREIGN KEY (user_id) REFERENCES users(id)
	ON UPDATE CASCADE
    ON DELETE CASCADE,
FOREIGN KEY (operation_id) REFERENCES operations(id)
	ON UPDATE CASCADE
	ON DELETE CASCADE
)ENGINE = InnoDB CHARACTER SET=UTF8;

create table if not exists admins_operations (
id int(7) auto_increment,
admin_id int(7) not null,
operation_id int(7) not null,
primary key (id),
FOREIGN KEY (admin_id) REFERENCES users(id)
	ON UPDATE CASCADE
    ON DELETE CASCADE,
FOREIGN KEY (operation_id) REFERENCES operations(id)
	ON UPDATE CASCADE
	ON DELETE CASCADE
)ENGINE = InnoDB CHARACTER SET=UTF8;

INSERT INTO roles (role) VALUES ('ROLE_USER'), ('ROLE_ADMIN');  
INSERT INTO users (email, password,registred) VALUES ('admin@gmail.com', 'admin',NOW()), ('user1@mail.ru', '1111',NOW()), ('user2@mail.ru', '2222',NOW());  
INSERT INTO users_roles (user_id, role_id) VALUES (1, 2), (2, 1), (3,1);  
```
