INSERT INTO public.permission (name, description, status, created_at, updated_at)
SELECT v.name, v.description, v.status, NOW(), NOW()
FROM (VALUES
    ('CREATE_PERMISSION', 'Cho phép tạo mới các quyền trong hệ thống', 'ACTIVE'),
    ('VIEW_PERMISSION', 'Cho phép xem danh sách các quyền hiện có', 'ACTIVE'),
    ('DELETE_PERMISSION', 'Cho phép xóa quyền khỏi hệ thống', 'ACTIVE'),

    ('CREATE_ROLE', 'Cho phép tạo mới các vai trò nhân viên', 'ACTIVE'),
    ('VIEW_ROLE', 'Cho phép xem danh sách các vai trò', 'ACTIVE'),
    ('DELETE_ROLE', 'Cho phép xóa vai trò (nếu không có nhân viên nào đang giữ)', 'ACTIVE'),
    ('MAP_ROLE_PERMISSION', 'Cho phép gán hoặc gỡ quyền cho các vai trò', 'ACTIVE'),

    ('CREATE_EMPLOYEE', 'Cho phép tạo tài khoản nhân viên mới', 'ACTIVE'),
    ('VIEW_EMPLOYEE', 'Cho phép xem thông tin hồ sơ nhân viên', 'ACTIVE'),
    ('UPDATE_EMPLOYEE', 'Cho phép cập nhật thông tin cá nhân của nhân viên', 'ACTIVE'),
    ('UPDATE_EMPLOYEE_STATUS', 'Cho phép thay đổi trạng thái hoạt động (khóa/mở) tài khoản nhân viên', 'ACTIVE'),
    ('UPDATE_EMPLOYEE_ROLE', 'Cho phép thay đổi vai trò của nhân viên', 'ACTIVE'),

    ('VERIFY_PASSWORD', 'Quyền xác thực mật khẩu hiện tại', 'ACTIVE'),
    ('UPDATE_PASSWORD', 'Quyền cho phép nhân viên tự đổi mật khẩu', 'ACTIVE'),

    ('ADD_TOUR_IMAGE', 'Cho phép tải lên hình ảnh cho các tour du lịch', 'ACTIVE'),
    ('SET_PRIMARY_TOUR_IMAGE', 'Cho phép thiết lập hình ảnh đại diện chính cho tour', 'ACTIVE'),

    ('CREATE_ROUTE', 'Cho phép tạo mới lộ trình chi tiết cho tour', 'ACTIVE'),
    ('UPDATE_ROUTE', 'Cho phép chỉnh sửa lộ trình đã có', 'ACTIVE'),

    ('CREATE_SCHEDULE', 'Cho phép tạo lịch khởi hành cho các tour', 'ACTIVE'),
    ('DELETE_SCHEDULE', 'Cho phép xóa hoặc hủy lịch khởi hành', 'ACTIVE'),

    ('CREATE_DISH', 'Cho phép thêm món ăn mới vào danh mục của tour', 'ACTIVE'),
    ('UPDATE_DISH', 'Cho phép cập nhật thông tin món ăn', 'ACTIVE'),
    ('DELETE_DISH', 'Cho phép xóa món ăn khỏi hệ thống', 'ACTIVE'),

    ('CREATE_TOUR', 'Cho phép tạo mới một tour du lịch', 'ACTIVE'),
    ('UPDATE_TOUR', 'Cho phép cập nhật thông tin chung của tour', 'ACTIVE'),
    ('DELETE_TOUR', 'Cho phép xóa tour du lịch', 'ACTIVE'),

    ('PROCESS_RELOCATE_BOOKING_REQUEST', 'Cho phép xử lý yêu cầu dời tour', 'ACTIVE')
) AS v(name, description, status)
WHERE NOT EXISTS (
    SELECT 1 FROM public.permission p WHERE p.name = v.name
);
