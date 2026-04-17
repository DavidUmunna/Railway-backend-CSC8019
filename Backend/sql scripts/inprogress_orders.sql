CREATE PROCEDURE getpendingOrders()
BEGIN
SELECT 
        po.order_id,
        c.customer_name,
        po.order_date,
        po.pickup_time,
        po.order_status,
        po.total_amount
    FROM purchaseorder po
    JOIN customer c ON po.customer_id = c.customer_id

    WHERE 
       (po.order_status=`in Progress`)
	ORDER BY po.pickup_time ASC;

    

END;
