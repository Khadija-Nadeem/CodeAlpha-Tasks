CREATE DATABASE Stock_Trading_Platform;
USE Stock_Trading_Platform;

CREATE TABLE market_data (
stock_id INT AUTO_INCREMENT PRIMARY KEY,
symbol VARCHAR(10) NOT NULL,
company_name VARCHAR(100),
price DECIMAL(10,2),
volume INT,
last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- used to track when records are created or modified.
);

INSERT INTO market_data (symbol, company_name, price, volume)
VALUES 
    ('AAPL', 'Apple Inc.', 175.10, 50000),
    ('GOOGL', 'Alphabet Inc.', 2735.30, 10000),
    ('MSFT', 'Microsoft Corp.', 332.10, 75000),
    ('AMZN', 'Amazon.com Inc.', 3341.25, 15000),
    ('TSLA', 'Tesla Inc.', 730.50, 20000),
    ('NFLX', 'Netflix Inc.', 485.60, 12000),
    ('NVDA', 'NVIDIA Corp.', 455.20, 22000),
    ('META', 'Meta Platforms Inc.', 316.85, 18000),
    ('INTC', 'Intel Corp.', 39.60, 30000),
    ('ORCL', 'Oracle Corp.', 108.95, 17000),
    ('IBM', 'International Business Machines Corp.', 142.35, 12000),
    ('DIS', 'The Walt Disney Company', 92.45, 35000),
    ('BA', 'Boeing Co.', 205.65, 25000),
    ('CSCO', 'Cisco Systems Inc.', 58.20, 28000),
    ('MCD', 'McDonald\'s Corp.', 274.15, 15000),
    ('HD', 'Home Depot Inc.', 330.25, 16000),
    ('KO', 'The Coca-Cola Company', 59.70, 31000),
    ('PEP', 'PepsiCo Inc.', 177.55, 20000),
    ('WMT', 'Walmart Inc.', 155.10, 22000),
    ('PFE', 'Pfizer Inc.', 40.80, 29000);
SELECT * FROM market_data;


CREATE TABLE company_details (
    company_id INT AUTO_INCREMENT PRIMARY KEY,
    company_name VARCHAR(255) UNIQUE NOT NULL,
    sector VARCHAR(100),
    market_cap VARCHAR(100),
    ceo VARCHAR(100),
    headquarters VARCHAR(255),
    recent_news TEXT
);

INSERT INTO company_details (company_name, sector, market_cap, ceo, headquarters, recent_news)
VALUES 
    ('Apple Inc.', 'Technology', '2.7 Trillion USD', 'Tim Cook', 'Cupertino, CA', 'Apple announces new iPhone.'),
    ('Alphabet Inc.', 'Technology', '1.8 Trillion USD', 'Sundar Pichai', 'Mountain View, CA', 'Google unveils new AI features.'),
    ('Microsoft Corp.', 'Technology', '2.5 Trillion USD', 'Satya Nadella', 'Redmond, WA', 'Microsoft launches new Surface Laptop.'),
    ('Amazon.com Inc.', 'E-commerce', '1.8 Trillion USD', 'Andy Jassy', 'Seattle, WA', 'Amazon introduces Prime Day discounts.'),
    ('Tesla Inc.', 'Automotive', '1 Trillion USD', 'Elon Musk', 'Palo Alto, CA', 'Tesla unveils new Model S.'),
    ('Netflix Inc.', 'Entertainment', '300 Billion USD', 'Ted Sarandos', 'Los Gatos, CA', 'Netflix announces new series.'),
    ('NVIDIA Corp.', 'Technology', '800 Billion USD', 'Jensen Huang', 'Santa Clara, CA', 'NVIDIA releases new graphics card.'),
    ('Meta Platforms Inc.', 'Technology', '900 Billion USD', 'Mark Zuckerberg', 'Menlo Park, CA', 'Meta introduces new VR headset.'),
    ('Intel Corp.', 'Technology', '500 Billion USD', 'Pat Gelsinger', 'Santa Clara, CA', 'Intel reveals new processor.'),
    ('Oracle Corp.', 'Technology', '400 Billion USD', 'Safra Catz', 'Austin, TX', 'Oracle announces cloud service expansion.'),
    ('IBM', 'Technology', '120 Billion USD', 'Arvind Krishna', 'Armonk, NY', 'IBM unveils new quantum computing advancements.'),
    ('The Walt Disney Company', 'Entertainment', '350 Billion USD', 'Bob Chapek', 'Burbank, CA', 'Disney announces new theme park attractions.'),
    ('Boeing Co.', 'Aerospace', '150 Billion USD', 'David Calhoun', 'Chicago, IL', 'Boeing delivers new aircraft model.'),
    ('Cisco Systems Inc.', 'Technology', '200 Billion USD', 'Chuck Robbins', 'San Jose, CA', 'Cisco launches new networking solutions.'),
    ('McDonald\'s Corp.', 'Fast Food', '220 Billion USD', 'Chris Kempczinski', 'Chicago, IL', 'McDonald\'s introduces new menu items.'),
    ('Home Depot Inc.', 'Retail', '300 Billion USD', 'Ted Decker', 'Atlanta, GA', 'Home Depot expands online services.'),
    ('The Coca-Cola Company', 'Beverages', '250 Billion USD', 'James Quincey', 'Atlanta, GA', 'Coca-Cola launches new product line.'),
    ('PepsiCo Inc.', 'Beverages', '250 Billion USD', 'Ramon Laguarta', 'Purchase, NY', 'PepsiCo announces new snack options.'),
    ('Walmart Inc.', 'Retail', '400 Billion USD', 'Doug McMillon', 'Bentonville, AR', 'Walmart enhances online shopping experience.'),
    ('Pfizer Inc.', 'Pharmaceuticals', '300 Billion USD', 'Albert Bourla', 'New York, NY', 'Pfizer develops new vaccine.');
SELECT * FROM company_details;

CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    transaction_type ENUM('Buy', 'Sell') NOT NULL,
    stock_symbol VARCHAR(10) NOT NULL,
    company_name VARCHAR(255) NOT NULL,
    number_of_shares INT NOT NULL,
    price_per_share DECIMAL(10, 2) NOT NULL,
    total_amount DECIMAL(15, 2) NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
SELECT * FROM transactions;

CREATE TABLE users( 
    user_id INT AUTO_INCREMENT PRIMARY KEY ,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,  
	email VARCHAR(255),                  
    phone VARCHAR(20)                     
);
SELECT * FROM  users;

CREATE INDEX idx_symbol ON market_data(symbol); -- foreign key constraint (symbol in this case) must be either a PRIMARY KEY or have an index.

CREATE TABLE portfolio (
    userID INT,
    stock_symbol VARCHAR(10),
    quantity INT,
    total_amount DECIMAL(20, 2),
    purchase_price DECIMAL(20, 2),  -- Price per share at the time of purchase
    transaction_type ENUM('Buy', 'Sell') NOT NULL,
    FOREIGN KEY (userID) REFERENCES users(user_id),
    FOREIGN KEY (stock_symbol) REFERENCES market_data(symbol)
);
SELECT * FROM portfolio;

-- to do
SET SQL_SAFE_UPDATES = 0; -- off safe mood

UPDATE market_data SET price = 320.00 WHERE symbol = 'AAPL';
UPDATE market_data SET price = 2780.00 WHERE symbol = 'GOOGL';
UPDATE market_data SET price = 340.00 WHERE symbol = 'MSFT';
UPDATE market_data SET price = 3380.00 WHERE symbol = 'AMZN';
UPDATE market_data SET price = 740.00 WHERE symbol = 'TSLA';
UPDATE market_data SET price = 490.00 WHERE symbol = 'NFLX';
UPDATE market_data SET price = 460.00 WHERE symbol = 'NVDA';
UPDATE market_data SET price = 299.00 WHERE symbol = 'META';
UPDATE market_data SET price = 41.00 WHERE symbol = 'INTC';
UPDATE market_data SET price = 110.00 WHERE symbol = 'ORCL';
UPDATE market_data SET price = 145.00 WHERE symbol = 'IBM';
UPDATE market_data SET price = 80.00 WHERE symbol = 'DIS';
UPDATE market_data SET price = 210.00 WHERE symbol = 'BA';
UPDATE market_data SET price = 60.00 WHERE symbol = 'CSCO';
UPDATE market_data SET price = 280.00 WHERE symbol = 'MCD';
UPDATE market_data SET price = 335.00 WHERE symbol = 'HD';
UPDATE market_data SET price = 61.00 WHERE symbol = 'KO';
UPDATE market_data SET price = 180.00 WHERE symbol = 'PEP';
UPDATE market_data SET price = 160.00 WHERE symbol = 'WMT';
UPDATE market_data SET price = 42.00 WHERE symbol = 'PFE';

SET SQL_SAFE_UPDATES = 1; -- on safe mood