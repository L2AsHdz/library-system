CREATE TABLE User (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(150),
    username VARCHAR(50),
    email VARCHAR(100),
    password VARCHAR(500),
    birth_date DATE,
    user_role ENUM('ADMIN', 'STUDENT', 'LIBRARIAN')
);

CREATE TABLE Career (
    career_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(150)
);

CREATE TABLE Student (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    career_id BIGINT,
    academic_number BIGINT,
    is_sanctioned BOOLEAN,
    FOREIGN KEY (career_id) REFERENCES Career(career_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);

CREATE TABLE Author (
    author_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(150)
);

CREATE TABLE Book (
    book_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(150),
    author_id BIGINT,
    stock BIGINT,
    publication_date DATE,
    publisher VARCHAR(150),
    FOREIGN KEY (author_id) REFERENCES Author(author_id)
);

CREATE TABLE Loan (
    loan_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT,
    book_id BIGINT,
    loan_price DOUBLE,
    loan_arrears DOUBLE,
    loan_date DATE,
    return_date DATE,
    status ENUM('RESERVATION', 'ACTIVE', 'RETURNED', 'ARREAR', 'EXPIRED'),
    FOREIGN KEY (student_id) REFERENCES Student(user_id),
    FOREIGN KEY (book_id) REFERENCES Book(book_id)
);

CREATE TABLE Parameter (
    parameter_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    parameter_name VARCHAR(50),
    parameter_value DOUBLE
);

INSERT INTO Parameter (parameter_name, parameter_value) VALUES
    ('loan_days', 3),
    ('loan_price', 5),
    ('daily_cost_arrears', 15),
    ('cost_sanction', 150),
    ('reservation_expiration_days', 1);