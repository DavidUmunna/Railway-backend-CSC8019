CREATE TABLE customer (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_name CHAR(100) NOT NULL,
    customer_email VARCHAR(150) NOT NULL,
    customer_phone_number VARCHAR(20)
);

CREATE TABLE purchaseorder (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    order_date DATE NOT NULL,
    pickup_time TIME NOT NULL,
    order_status ENUM('Accepted','In Progress','Collected','Completed') NOT NULL ,
    station_id INT NOT NULL ,
    total_amount DECIMAL(8,2) DEFAULT 0.00 NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
    FOREIGN KEY  (station_id) REFERENCES station(station_id)
);

CREATE TABLE menu_item (
    menu_item_id INT AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(150) NOT NULL,
    item_description VARCHAR(300),
    item_price DECIMAL(8,2),
    isavailable BOOLEAN DEFAULT TRUE
);

CREATE TABLE order_item (
    purchase_order_id INT NOT NULL,
    menu_item_id INT NOT NULL,
    quantity INT,
    unit_price DECIMAL(7,2) DEFAULT 0.00 NOT NULL,
    line_total DECIMAL(8,2) DEFAULT 0.00 NOT NULL,
    PRIMARY KEY (purchase_order_id, menu_item_id),  
    FOREIGN KEY (purchase_order_id) REFERENCES purchaseorder(order_id),
    FOREIGN KEY (menu_item_id) REFERENCES menu_item(menu_item_id)
);

CREATE TABLE staff (
    staff_id INT AUTO_INCREMENT PRIMARY KEY,
    staff_name VARCHAR(100),
    staff_role CHAR(150),
    username CHAR(150),
    password VARCHAR(300)
);

CREATE TABLE order_staff_relation (
    order_id INT NOT NULL,
    staff_id INT NOT NULL,
    PRIMARY KEY (order_id, staff_id),
    FOREIGN KEY (order_id) REFERENCES purchaseorder(order_id) ON DELETE CASCADE,
    FOREIGN KEY (staff_id) REFERENCES staff(staff_id) ON DELETE RESTRICT
);

CREATE TABLE station(
    station_id INT NOT NULL,
    station_name varchar(100)
);
