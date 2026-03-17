TRUNCATE TABLE public.role_permission CASCADE;

-- ==========================================
-- 1. ADMIN: Gán toàn bộ 26 quyền
-- ==========================================
INSERT INTO public.role_permission (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM public.role r
         CROSS JOIN public.permission p
WHERE r.name = 'ADMIN';

-- ==========================================
-- 2. DISPATCH (Điều phối viên): Quản lý Tour, Lịch trình, Món ăn
-- ==========================================
INSERT INTO role_permission (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM role r, permission p
WHERE r.name = 'DISPATCH'
  AND p.name IN (
                 'CREATE_TOUR', 'UPDATE_TOUR', 'DELETE_TOUR',
                 'ADD_TOUR_IMAGE', 'SET_PRIMARY_TOUR_IMAGE',
                 'CREATE_ROUTE', 'UPDATE_ROUTE',
                 'CREATE_SCHEDULE', 'DELETE_SCHEDULE',
                 'CREATE_DISH', 'UPDATE_DISH', 'DELETE_DISH',
                 'VIEW_EMPLOYEE', 'VERIFY_PASSWORD', 'UPDATE_PASSWORD'
    );

-- ==========================================
-- 3. TOURGUIDE (Hướng dẫn viên): Xem thông tin phục vụ dẫn tour
-- ==========================================
INSERT INTO role_permission (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM role r, permission p
WHERE r.name = 'TOURGUIDE'
  AND p.name IN (
                 'VIEW_TOUR',
                 'VIEW_EMPLOYEE',
                 'VERIFY_PASSWORD', 'UPDATE_PASSWORD'
    );

-- ==========================================
-- 4. EMPLOYEE (Nhân viên chung): Quyền cơ bản
-- ==========================================
INSERT INTO role_permission (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM role r, permission p
WHERE r.name = 'EMPLOYEE'
  AND p.name IN (
                 'VIEW_TOUR',
                 'VIEW_EMPLOYEE',
                 'VERIFY_PASSWORD', 'UPDATE_PASSWORD',
                 'PROCESS_RELOCATE_BOOKING_REQUEST'
    );


