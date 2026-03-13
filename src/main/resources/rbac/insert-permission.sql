INSERT INTO public.permission (name, description, status, created_at, updated_at)
VALUES
('VIEW_TOUR', 'Cho phép xem danh sách và thông tin chi tiết các tour du lịch', 'ACTIVE', NOW(), NOW()),
('CREATE_TOUR', 'Cho phép tạo mới các tour du lịch và lộ trình', 'ACTIVE', NOW(), NOW()),
('UPDATE_TOUR', 'Cho phép chỉnh sửa thông tin tour, giá và lịch trình', 'ACTIVE', NOW(), NOW()),
('DELETE_TOUR', 'Cho phép xóa hoặc ẩn các tour khỏi hệ thống', 'ACTIVE', NOW(), NOW()),

('VIEW_BOOKING', 'Cho phép xem danh sách đơn đặt tour của khách hàng', 'ACTIVE', NOW(), NOW()),
('UPDATE_BOOKING', 'Cho phép xác nhận, hủy hoặc cập nhật trạng thái đơn đặt tour', 'ACTIVE', NOW(), NOW()),

('MANAGE_EMPLOYEE', 'Toàn quyền tạo, sửa, xóa tài khoản nhân viên', 'ACTIVE', NOW(), NOW()),
('MANAGE_ROLE', 'Cho phép thiết lập vai trò và gán quyền cho nhân viên', 'ACTIVE', NOW(), NOW()),

('VIEW_FEEDBACK', 'Cho phép xem và quản lý phản hồi từ khách hàng', 'ACTIVE', NOW(), NOW()),
('MANAGE_IMAGE', 'Cho phép tải lên và quản lý thư viện hình ảnh tour', 'ACTIVE', NOW(), NOW());