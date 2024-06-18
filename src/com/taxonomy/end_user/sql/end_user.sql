-- :name create-user*
-- :command :returning-execute
-- :result :one
insert into end_user (username, password, first_name, last_name, email, roles)
values (:username, :password, :first-name, :last-name, :email, :roles)
returning *;

-- :name activate-user*
-- :command :returning-execute
-- :result :one
update end_user
set active = true
where username = :username
returning *;

-- :name change-user-password*
-- :command :returning-execute
-- :result :one
update end_user
set password = :password
where username = :username
returning *;

-- :name update-user-info*
-- :command :returning-execute
-- -- :result :one
update end_user
set first_name = :first-name, last_name = :last-name, email = :email
where username = :username
returning *;

-- :name get-users
-- :result :many
select username, first_name, last_name, email, roles, active, created_at
from end_user
--~ (when (:limit params) "limit :limit")
--~ (when (:offset params) "offset :offset")
order by last_name, first_name;

-- :name get-users-count
-- :result :one
select count(*) from users;

-- :name get-user-by-username
-- :result :one
select *
from end_user
where username = :username;

-- :name get-user-by-username-and-password
-- :result :one
select username, first_name, last_name, email, roles, active, created_at
from end_user
where username = :username and password = :password;

-- :name get-user-by-username-and-email
-- :result :one
select username, first_name, last_name, email, roles, active, created_at
from end_user
where username = :username and email = :email;

-- :name delete-user :! :n
delete from end_user where username = :username;
