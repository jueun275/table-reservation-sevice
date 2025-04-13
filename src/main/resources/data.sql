INSERT INTO users (name, email, password, role, phone_number, created_at, updated_at)
VALUES
    ('홍길동', 'hong@example.com', 'pass1', 'PARTNER', '010-1234-5678', NOW(), NOW()),
    ('이길동', 'lee@example.com', 'pass2', 'USER', '010-2345-6789', NOW(), NOW()),
    ('최길동', 'kim@example.com', 'pass3', 'USER', '010-3456-7890', NOW(), NOW());