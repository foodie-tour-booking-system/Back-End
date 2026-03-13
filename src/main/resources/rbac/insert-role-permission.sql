TRUNCATE TABLE public.role_permission CASCADE;

INSERT INTO role_permission (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM public.role r, public.permission p
WHERE r.name = 'ADMIN'
  AND p.name IN (
                 'VIEW_TOUR', 'CREATE_TOUR', 'UPDATE_TOUR', 'DELETE_TOUR',
                 'VIEW_BOOKING', 'UPDATE_BOOKING',
                 'MANAGE_EMPLOYEE', 'MANAGE_ROLE',
                 'VIEW_FEEDBACK', 'MANAGE_IMAGE'
    );


INSERT INTO role_permission (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM public.role r, public.permission p
WHERE r.name = 'EMPLOYEE'
  AND p.name IN (
                 'VIEW_TOUR',
                 'VIEW_BOOKING',
                 'VIEW_FEEDBACK'
    );


-- DÙNG CHO ROLE VỚI FULL QUYỀN
INSERT INTO public.role_permission (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM public.role r
         CROSS JOIN public.permission p
WHERE r.name = 'SYSTEM'; -- ĐỔI TEN SAU