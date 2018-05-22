select
    id
    , item_id
    , quantity
from
    item_inventory
where
    item_id = /* itemId */'foo'