CREATE TABLE customer (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_firstname VARCHAR(50) NOT NULL,
    customer_lastname VARCHAR(50) NOT NULL,
    customer_phone_number VARCHAR(14)
);

CREATE TABLE station(
    station_id INT PRIMARY KEY,
    station_name VARCHAR(100),
    weekday_opening_hours VARCHAR(55),
    saturday_opening_hours VARCHAR(55),
    closed_on_sunday BOOLEAN
);

CREATE TABLE staff (
    staff_id INT AUTO_INCREMENT PRIMARY KEY,
    staff_name VARCHAR(100),
    staff_role CHAR(150),
    username CHAR(150),
    password VARCHAR(300)
);

CREATE TABLE purchase_order (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    staff_id INT,
    order_date DATE NOT NULL,
    pickup_time TIME NOT NULL,
    order_status ENUM('ACCEPTED','IN_PROGRESS','COMPLETED', 'COLLECTED', 'CANCELLED') NOT NULL,
    station_id INT,
    total_amount DECIMAL(8,2) DEFAULT 0.00 NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
    FOREIGN KEY (staff_id) REFERENCES staff(staff_id),
    FOREIGN KEY (station_id) REFERENCES station(station_id)
);

CREATE TABLE menu_item (
    menu_item_id INT AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(150) NOT NULL,
    item_description VARCHAR(300),
    is_available BOOLEAN DEFAULT TRUE
);

CREATE TABLE menu_item_type(
    menu_item_type_id INT AUTO_INCREMENT PRIMARY KEY,
    menu_item_id INT,
    size_name ENUM('Regular','Large'),
    price DECIMAL(8,2) DEFAULT 0.00 NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (menu_item_id) REFERENCES menu_item(menu_item_id)
);

CREATE TABLE order_item (
    purchase_order_id INT,
    menu_item_type_id INT,
    quantity INT,
    unit_price DECIMAL(8,2) DEFAULT 0.00 NOT NULL,
    line_total DECIMAL(8,2) DEFAULT 0.00 NOT NULL,
    PRIMARY KEY (purchase_order_id, menu_item_type_id),
    FOREIGN KEY (purchase_order_id) REFERENCES purchase_order(order_id),
    FOREIGN KEY (menu_item_type_id) REFERENCES menu_item_type(menu_item_type_id)
);

CREATE TABLE order_staff_relation (
    order_id INT,
    staff_id INT,
    PRIMARY KEY (order_id, staff_id),
    FOREIGN KEY (order_id) REFERENCES purchase_order(order_id) ON DELETE CASCADE,
    FOREIGN KEY (staff_id) REFERENCES staff(staff_id) ON DELETE RESTRICT
);
