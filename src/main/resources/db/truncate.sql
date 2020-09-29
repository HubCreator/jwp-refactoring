SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE orders;
TRUNCATE TABLE order_line_item;
TRUNCATE TABLE menu;
TRUNCATE TABLE menu_group;
TRUNCATE TABLE menu_product;
TRUNCATE TABLE order_table;
TRUNCATE TABLE table_group;
TRUNCATE TABLE product;

ALTER TABLE orders
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE order_line_item
    ALTER COLUMN seq RESTART WITH 1;
ALTER TABLE menu
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE menu_group
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE menu_product
    ALTER COLUMN seq RESTART WITH 1;
ALTER TABLE order_table
    ALTER COLUMN id RESTART WITH 1;
 ALTER TABLE table_group
    ALTER COLUMN id RESTART WITH 1;
 ALTER TABLE product
    ALTER COLUMN id RESTART WITH 1;

SET REFERENTIAL_INTEGRITY TRUE;