insert into category (description,info) values ('American','from the home of most awesome recipes discover .....');
insert into category (description) values ('Armenian');
insert into category (description) values ('Austrian');
insert into category (description) values ('Egyptian');
insert into category (description,info) values ('Mexican','from the home of taqella , discover .....');
insert into category (description,info) values ('Italian','from the home of pizza discover .....');
insert into category (description) values ('Fast Food');
insert into unit_measure (description) values ('');
insert into unit_measure (description) values ('teaspoon');
insert into unit_measure (description) values ('tablespoon');
insert into unit_measure (description) values ('cup');
insert into unit_measure (description) values ('pinch');
insert into unit_measure (description) values ('ounce');
insert into unit_measure (description) values ('dash');
insert into recipe (title,prep_time,cook_time) values ('Koshary',20,15);
insert into recipe (title,prep_time,cook_time) values ('Margritta',30,10);
insert into ingredient(description,amount,unit_measure_id,recipe_id) values ('dummy ingredient',2.0,3,1);
insert into ingredient(description,amount,unit_measure_id,recipe_id) values ('another dummy ingredient',1.0,5,2);

