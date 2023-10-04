INSERT INTO category
VALUES (1, 'Category1');
INSERT INTO category
VALUES (2, 'Category2');

INSERT INTO item
VALUES (1, 'Item1', 12.0, 'description', '2020-09-20 13:00:00');
INSERT INTO item
VALUES (2, 'Item2', 3.0, 'description', '2020-09-25 18:00:00');

INSERT INTO item_has_category
VALUES(1, 1);
INSERT INTO item_has_category
VALUES(2, 2);
