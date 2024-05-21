CREATE TABLE User (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(150) NOT NULL,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(500) NOT NULL,
    birth_date DATE,
    status SMALLINT DEFAULT 1,
    user_role ENUM('ADMIN', 'STUDENT', 'LIBRARIAN') NOT NULL
);

CREATE TABLE Career (
    career_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL
);

CREATE TABLE Student (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    career_id BIGINT NOT NULL,
    academic_number BIGINT NOT NULL,
    is_sanctioned BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (career_id) REFERENCES Career(career_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);

CREATE TABLE Book (
    book_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(150) NOT NULL,
    author VARCHAR(150) NOT NULL,
    stock BIGINT DEFAULT 0,
    publication_date DATE NOT NULL,
    publisher VARCHAR(150) NOT NULL
);

CREATE TABLE Loan (
    loan_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    loan_price DOUBLE,
    loan_arrears DOUBLE,
    loan_date DATE NOT NULL,
    return_date DATE,
    status ENUM('RESERVATION', 'ACTIVE', 'RETURNED', 'ARREAR', 'EXPIRED') NOT NULL,
    FOREIGN KEY (student_id) REFERENCES Student(user_id),
    FOREIGN KEY (book_id) REFERENCES Book(book_id)
);

CREATE TABLE Parameter (
    parameter_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    parameter_name VARCHAR(50) NOT NULL,
    parameter_value DOUBLE NOT NULL
);

INSERT INTO Parameter (parameter_name, parameter_value) VALUES
    ('loan_days', 3),
    ('loan_price', 5),
    ('daily_cost_arrears', 15),
    ('cost_sanction', 150),
    ('reservation_expiration_days', 1);