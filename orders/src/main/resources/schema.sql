DROP TABLE ORDER_ITEMS;
DROP TABLE ORDERS;

CREATE TABLE ORDERS
(
    order_id         uuid          NOT NULL PRIMARY KEY,
    order_status     VARCHAR2(32)  NOT NULL,
    username         VARCHAR2(32)  NOT NULL,
    restaurant_id    uuid          NOT NULL,
    delivery_address VARCHAR2(200) NOT NULL,
    notes            VARCHAR2(1000),
    created_on       TIMESTAMP     NOT NULL
);

CREATE TABLE ORDER_ITEMS
(
    order_item_id uuid          NOT NULL PRIMARY KEY,
    order_id      uuid          NOT NULL,
    item_id       uuid          NOT NULL,
    item_name     VARCHAR2(120) NOT NULL,
    item_price    NUMERIC       NOT NULL,
    item_quantity INTEGER       NOT NULL,
    CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES orders (order_id)
);