INSERT INTO public.permission (name, description, status, created_at, updated_at)
VALUES
-- Quản lý Quyền (Permission)
('CREATE_PERMISSION', 'Cho phép tạo mới các quyền trong hệ thống', 'ACTIVE', NOW(), NOW()),
('VIEW_PERMISSION', 'Cho phép xem danh sách các quyền hiện có', 'ACTIVE', NOW(), NOW()),
('DELETE_PERMISSION', 'Cho phép xóa quyền khỏi hệ thống', 'ACTIVE', NOW(), NOW()),

-- Quản lý Vai trò (Role)
('CREATE_ROLE', 'Cho phép tạo mới các vai trò nhân viên', 'ACTIVE', NOW(), NOW()),
('VIEW_ROLE', 'Cho phép xem danh sách các vai trò', 'ACTIVE', NOW(), NOW()),
('DELETE_ROLE', 'Cho phép xóa vai trò (nếu không có nhân viên nào đang giữ)', 'ACTIVE', NOW(), NOW()),
('MAP_ROLE_PERMISSION', 'Cho phép gán hoặc gỡ quyền cho các vai trò', 'ACTIVE', NOW(), NOW()),

-- Quản lý Nhân viên (Employee)
('CREATE_EMPLOYEE', 'Cho phép tạo tài khoản nhân viên mới', 'ACTIVE', NOW(), NOW()),
('VIEW_EMPLOYEE', 'Cho phép xem thông tin hồ sơ nhân viên', 'ACTIVE', NOW(), NOW()),
('UPDATE_EMPLOYEE', 'Cho phép cập nhật thông tin cá nhân của nhân viên', 'ACTIVE', NOW(), NOW()),
('UPDATE_EMPLOYEE_STATUS', 'Cho phép thay đổi trạng thái hoạt động (khóa/mở) tài khoản nhân viên', 'ACTIVE', NOW(), NOW()),
('UPDATE_EMPLOYEE_ROLE', 'Cho phép thay đổi vai trò của nhân viên', 'ACTIVE', NOW(), NOW()),

-- Bảo mật tài khoản
('VERIFY_PASSWORD', 'Quyền xác thực mật khẩu hiện tại', 'ACTIVE', NOW(), NOW()),
('UPDATE_PASSWORD', 'Quyền cho phép nhân viên tự đổi mật khẩu', 'ACTIVE', NOW(), NOW()),

-- Quản lý Hình ảnh Tour
('ADD_TOUR_IMAGE', 'Cho phép tải lên hình ảnh cho các tour du lịch', 'ACTIVE', NOW(), NOW()),
('SET_PRIMARY_TOUR_IMAGE', 'Cho phép thiết lập hình ảnh đại diện chính cho tour', 'ACTIVE', NOW(), NOW()),

-- Quản lý Lộ trình (Route)
('CREATE_ROUTE', 'Cho phép tạo mới lộ trình chi tiết cho tour', 'ACTIVE', NOW(), NOW()),
('UPDATE_ROUTE', 'Cho phép chỉnh sửa lộ trình đã có', 'ACTIVE', NOW(), NOW()),

-- Quản lý Khung giờ (Schedule)
('CREATE_SCHEDULE', 'Cho phép tạo lịch khởi hành cho các tour', 'ACTIVE', NOW(), NOW()),
('DELETE_SCHEDULE', 'Cho phép xóa hoặc hủy lịch khởi hành', 'ACTIVE', NOW(), NOW()),

-- Quản lý Món ăn (Dish) trong Tour
('CREATE_DISH', 'Cho phép thêm món ăn mới vào danh mục của tour', 'ACTIVE', NOW(), NOW()),
('UPDATE_DISH', 'Cho phép cập nhật thông tin món ăn', 'ACTIVE', NOW(), NOW()),
('DELETE_DISH', 'Cho phép xóa món ăn khỏi hệ thống', 'ACTIVE', NOW(), NOW()),

-- Quản lý Tour tổng thể
('CREATE_TOUR', 'Cho phép tạo mới một tour du lịch', 'ACTIVE', NOW(), NOW()),
('UPDATE_TOUR', 'Cho phép cập nhật thông tin chung của tour', 'ACTIVE', NOW(), NOW()),
('DELETE_TOUR', 'Cho phép xóa tour du lịch', 'ACTIVE', NOW(), NOW()),

('PROCESS_RELOCATE_BOOKING_REQUEST', 'Cho phép xử lý yêu cầu dời tour', 'ACTIVE', NOW(), NOW());