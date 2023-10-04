CREATE TABLE category (
  id int PRIMARY KEY AUTO_INCREMENT,
  name varchar(45) NOT NULL,
  constraint category_pk
                        primary key (id)
);


CREATE TABLE item (
  id int PRIMARY KEY AUTO_INCREMENT,
  name varchar(45) NOT NULL,
  price double NOT NULL,
  description varchar(45) DEFAULT NULL,
  creation_date datetime DEFAULT NULL,
  constraint item_pk
                    primary key (id)
);

CREATE TABLE item_has_category (
  item_id int NOT NULL,
  category_id int NOT NULL
);

alter table item_has_category add foreign key(category_id) REFERENCES category(id) on delete cascade on update cascade ;
alter table item_has_category add foreign key(item_id) REFERENCES item(id) on delete cascade on update cascade ;