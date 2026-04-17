CREATE FUNCTION isStaffAssigned( staffId INT) RETURNS BOOLEAN
READS SQL DATA DETERMINISTIC
BEGIN
    RETURN EXISTS (
        SELECT 1
        FROM order_staff_relation
        WHERE  staff_id = staffId
    );
END;