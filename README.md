PaymentService
==============
Description of theProject
----------------

Account replenishment system. Users register and receive an account in the system, which they can replenish with the help of Administrator. Administrator transfers a certain amount to a personal account through the admin interface. The system is completely i18n (RU, EN).

**Roles in the system:**

1.	Guest
2.	User
3.	Administrator

**Guest:**

1.	Can be registered and become a User of the system
	*	In the course of registration, he enters his email, password, confirms the password
	*	The system produces validation ofthe email address, checks whetherthe email exists or not, verifies the password and confirmation of thepassword
	*	After successful registration, the User is automatically logged in the system without an additional password checking
2.	Can login the system
	*	To log in, he must specify his email and password

**User:**

1.	After logging in, he can see his current balance.
2.	May logout from the system (back to the login page)

**Administrator:**

1.	Sees the page of management of User’s account
	*	The list is arranged in a table, which displays the User's email, current balance and the date of registration
	*	A feature to choose a quantity of users per page is available
	*	Pagination is available
	*	A feature to search the User by Email address is available
2.	Can replenish Users’ accounts balances
	*	Clicking on a User's email, pop-ups window in which an amount of money to replenish the User's balance can be entered
	*	After the User's balance has been replenished, the page is not refreshed, but the balance is updated (Ajax)
3.	Can browse the Log of operations
	*	The Log is arranged in a table, which displays the user's email, the admin's email, date and amount of replenishment
	*	Pagination is available
	*	A feature to choose a quantity of operations per page is available
	*	A feature to search by the date of replenishment (you can set the start and/or end dates)is available

Technologies
----------

**Backend**

Backend is represented by a multilayered web-based system using IOC. Frameworks are represented by Spring(MVC, Web, Data, Security), Hibernate(ORM, Validation). Testing :Spring test(Mock-MVC), Mockito, Junit. Connection pool: c3p0. Servlet  container is represented by Tomcat 7. Multileveled logging is represented by Log4j.

**Frontend**

HTML/CSS, Javascript(Jquery)

Database
-----------

RDBMS MySQL

```
CREATE database payment;
USE payment;

create table if not exists users (
idint(7) auto_increment,
emailvarchar(255) not null unique,
passwordvarchar(255) not null,
registred TIMESTAMP not null,
balance float(7,2) default 0,
primary key (id)
)ENGINE = InnoDB CHARACTER SET=UTF8;

create table if not exists roles (
idint(7) auto_increment,
rolevarchar(255) not null unique,
primary key (id)
)ENGINE = InnoDB CHARACTER SET=UTF8;

create table if not exists users_roles (
idint(7) auto_increment,
user_idint(7) not null,
role_idint(7) not null,
primary key (id),
FOREIGN KEY (user_id) REFERENCES users(id)
	ON UPDATE CASCADE
    ON DELETE CASCADE,
FOREIGN KEY (role_id) REFERENCES roles(id)
	ON UPDATE CASCADE
	ON DELETE CASCADE
)ENGINE = InnoDB CHARACTER SET=UTF8;

create table if not exists operations (
idint(7) auto_increment,
amount float(7,2) not null,
created TIMESTAMP not null,
primary key (id)
)ENGINE = InnoDB CHARACTER SET=UTF8;

create table if not exists users_operations (
idint(7) auto_increment,
user_idint(7) not null,
operation_idint(7) not null,
primary key (id),
FOREIGN KEY (user_id) REFERENCES users(id)
	ON UPDATE CASCADE
    ON DELETE CASCADE,
FOREIGN KEY (operation_id) REFERENCES operations(id)
	ON UPDATE CASCADE
	ON DELETE CASCADE
)ENGINE = InnoDB CHARACTER SET=UTF8;

create table if not exists admins_operations (
idint(7) auto_increment,
admin_idint(7) not null,
operation_idint(7) not null,
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

