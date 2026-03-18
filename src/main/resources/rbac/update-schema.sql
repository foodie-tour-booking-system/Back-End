-- Update booking_status check constraint in bookings table
ALTER TABLE bookings DROP CONSTRAINT IF EXISTS bookings_booking_status_check;
ALTER TABLE bookings ADD CONSTRAINT bookings_booking_status_check CHECK (booking_status IN ('PENDING', 'CONFIRMED', 'COMPLETED', 'CANCELLED', 'RESCHEDULED'));

-- Update booking_status check constraint in booking_log table
ALTER TABLE booking_log DROP CONSTRAINT IF EXISTS booking_log_booking_status_check;
ALTER TABLE booking_log ADD CONSTRAINT booking_log_booking_status_check CHECK (booking_status IN ('PENDING', 'CONFIRMED', 'COMPLETED', 'CANCELLED', 'RESCHEDULED'));

