CREATE TABLE IF NOT EXISTS ORDERS
(
    order_id         uuid          NOT NULL PRIMARY KEY,
    order_status     VARCHAR(32)  NOT NULL,
    username         VARCHAR(32)  NOT NULL,
    restaurant_id    uuid          NOT NULL,
    delivery_address VARCHAR(200) NOT NULL,
    notes            VARCHAR(1000),
    created_on       TIMESTAMP     NOT NULL
);

CREATE TABLE IF NOT EXISTS ORDER_ITEMS
(
    order_item_id uuid          NOT NULL PRIMARY KEY,
    order_id      uuid          NOT NULL,
    item_id       integer          NOT NULL,
    item_name     VARCHAR(120) NOT NULL,
    item_price    NUMERIC       NOT NULL,
    item_quantity INTEGER       NOT NULL,
    CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES orders (order_id)
);