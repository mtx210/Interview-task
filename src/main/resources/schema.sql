CREATE TABLE IF NOT EXISTS school
(
    id         INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    name       VARCHAR(255)                   NOT NULL,
    hour_price NUMERIC                        NOT NULL
);

CREATE TABLE IF NOT EXISTS parent
(
    id        INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    firstName VARCHAR(20)                    NOT NULL,
    lastName  VARCHAR(50)                    NOT NULL
);

CREATE TABLE IF NOT EXISTS child
(
    id        INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    firstName VARCHAR(20)                    NOT NULL,
    lastName  VARCHAR(50)                    NOT NULL,
    parent_id INT                            NOT NULL,
    school_id INT                            NOT NULL,
    FOREIGN KEY (parent_id) REFERENCES parent (id),
    FOREIGN KEY (school_id) REFERENCES school (id)
);

CREATE TABLE IF NOT EXISTS attendance
(
    id         INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    child_id   INT                            NOT NULL,
    entry_date DATETIME                       NOT NULL,
    exit_date  DATETIME                       NOT NULL,
    FOREIGN KEY (child_id) REFERENCES child (id)
);