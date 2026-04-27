CREATE VIEW ready_orders AS
SELECT 
    p.order_id,
    c.customer_firstname,
    c.customer_lastname,
    c.customer_phone_number,
    p.pickup_time,
    p.total_amount

FROM purchaseorder p
JOIN customer c ON p.customer_id = c.customer_id
WHERE p.order_status = 'Completed';
