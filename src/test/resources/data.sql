INSERT INTO todo_users (id, sub) VALUES
  (1, '1'),
  (2, '2');

INSERT INTO todo (id, message, completed, user_id) VALUES
  (100, 'todo1', false, 1),
  (101, 'todo2', false, 1),
  (102, 'todo3', false, 1),
  (103, 'todo4', false, 1),
  (104, 'todo5', true, 1),
  (105, 'todo6', true, 1),
  (106, 'todo1 by user2', false, 2);