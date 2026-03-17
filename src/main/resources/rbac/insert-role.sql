INSERT INTO public.role (name, status, created_at, updated_at)
SELECT v.name, v.status, NOW(), NOW()
FROM (VALUES
    ('ADMIN', 'ACTIVE'),
    ('DISPATCH', 'ACTIVE'),
    ('TOURGUIDE', 'ACTIVE'),
    ('EMPLOYEE', 'ACTIVE')
) AS v(name, status)
WHERE NOT EXISTS (
    SELECT 1 FROM public.role r WHERE r.name = v.name
);

INSERT INTO public.employee (role_id, employee_name, email, password, status, created_at, updated_at)
SELECT r.role_id, 'Admin', 'felixiter.travel@gmail.com',
       '$2a$10$IXBBliXi8Xs73uioBUnJIeeDECtPhi2nM6yUE8JWPYqSsEEu3cKbS',
       'ACTIVE', NOW(), NOW()
FROM public.role r
WHERE r.name = 'ADMIN'
  AND NOT EXISTS (
    SELECT 1 FROM public.employee e WHERE e.email = 'felixiter.travel@gmail.com'
  );


INSERT INTO public.employee (role_id, employee_name, email, password, status, created_at, updated_at)
SELECT r.role_id, 'Employee', 'employee@gmail.com',
       '$2a$10$IXBBliXi8Xs73uioBUnJIeeDECtPhi2nM6yUE8JWPYqSsEEu3cKbS',
       'ACTIVE', NOW(), NOW()
FROM public.role r
WHERE r.name = 'EMPLOYEE'
  AND NOT EXISTS (
    SELECT 1 FROM public.employee e WHERE e.email = 'employee@gmail.com'
);
