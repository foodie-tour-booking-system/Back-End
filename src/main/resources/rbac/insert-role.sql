INSERT INTO public.role (name, status, created_at, updated_at)
VALUES
    ('ADMIN', 'ACTIVE', NOW(), NOW()),
    ('DISPATCH', 'ACTIVE', NOW(), NOW()),
    ('TOURGUIDE', 'ACTIVE', NOW(), NOW()),
    ('EMPLOYEE', 'ACTIVE', NOW(), NOW())
ON CONFLICT (name) DO NOTHING;