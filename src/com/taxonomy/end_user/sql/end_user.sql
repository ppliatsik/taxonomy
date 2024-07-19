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
set active = true, email_activation = false
where username = :username
returning *;

-- :name activate-user-by-email*
-- :command :returning-execute
-- :result :one
update end_user
set active = true, email_activation = true
where username = :username
returning *;

-- :name deactivate-user*
-- :command :returning-execute
-- :result :one
update end_user
set active = false
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

-- :name get-user-by-username*
-- :result :one
select *
from end_user
where username = :username;

-- :name get-active-user-by-username
-- :result :one
select *
from end_user
where username = :username and active = true;

-- :name get-user-by-username-and-password
-- :result :one
select username, first_name, last_name, email, roles, active, created_at
from end_user
where username = :username and password = :password and active = true;

-- :name get-user-by-username-and-email
-- :result :one
select username, first_name, last_name, email, roles, active, created_at
from end_user
where username = :username and email = :email and active = true;

-- :name delete-user :! :n
delete from end_user where username = :username;

-- :name create-confirmation-token*
-- :command :returning-execute
-- :result :one
insert into confirmation_token (username, token, valid_to)
values (:username, :token, :valid_to)
on conflict (username) do nothing
returning *;

-- :name get-valid-confirmation-token-by-token
-- :result :one
select *
from confirmation_token
where token = :token and valid_to > now();

-- :name get-confirmation-token-by-username
-- :result :one
select *
from confirmation_token
where username = :username;

-- :name delete-invalid-user-confirmation-token :! :n
delete from confirmation_token where valid_to < now();

-- :name delete-user-confirmation-token :! :n
delete from confirmation_token where username = :username;
