INSERT INTO school (id, name, hour_price) VALUES (1, 'SP1', 7.50);
INSERT INTO school (id, name, hour_price) VALUES (2, 'SP2', 10.00);

INSERT INTO parent (id, firstName, lastName) VALUES (1, 'John', 'Smith');
INSERT INTO parent (id, firstName, lastName) VALUES (2, 'Alex', 'Don');

INSERT INTO child (id, firstName, lastName, parent_id, school_id) VALUES (1, 'John Jr', 'Smith', 1, 1);
INSERT INTO child (id, firstName, lastName, parent_id, school_id) VALUES (2, 'Alex Jr', 'Don', 2, 2);

INSERT INTO attendance (child_id, entry_date, exit_date) VALUES (1, '2024-02-20 6:30:00', '2024-02-20 14:30:00');