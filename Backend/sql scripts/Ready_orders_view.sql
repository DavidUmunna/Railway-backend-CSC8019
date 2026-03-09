CREATE VIEW ready_orders AS
SELECT 
    p.order_id,
    c.customer_name,
    p.pickup_time,
    p.total_amount,

FROM purchaseorder p
JOIN customer c ON p.customer_id = c.customer_id
WHERE p.order_status = 'Completed';
