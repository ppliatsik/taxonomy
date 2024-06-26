-- :name create-product*
-- :command :returning-execute
-- :result :one
insert into product (created_by)
values (:created-by)
returning *;

-- :name publish-product
-- :command :returning-execute
-- :result :one
update product
set is_published = true
where id = :id
returning *;

-- :name unpublish-product
-- :command :returning-execute
-- :result :one
update product
set is_published = false
where id = :id
returning *;

-- :name get-products
-- :result :many
select *
from product
where is_published = true
--~ (when (:limit params) "limit :limit")
--~ (when (:offset params) "offset :offset")
;

-- :name get-products-count
-- :result :one
select count(*)
from product
where is_published = true;

-- :name get-product-by-id
-- :result :one
select *
from product
where id = :id;

-- :name delete-product :! :n
delete from product where id = :id;
