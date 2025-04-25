    DROP TABLE if EXISTS exchange_currencies CASCADE;

    DROP TABLE if EXISTS rates CASCADE;

    DROP TABLE if EXISTS currencies CASCADE;

    CREATE TABLE currencies (
          id binary(16) NOT NULL,
          version integer default 0,
          created TIMESTAMP,
          updated TIMESTAMP,
          base_currency varchar(3) NOT NULL UNIQUE,
          converted_sum float(53),
          PRIMARY KEY (id)
    );

    CREATE TABLE exchange_currencies (
        currency_id binary(16) NOT NULL,
        type varchar(3),
        FOREIGN KEY (currency_id) REFERENCES currencies(id)
    );

    CREATE TABLE rates (
        currency_id binary(16) NOT NULL,
        currency_type varchar(3) NOT NULL,
        rate float(53),
        PRIMARY KEY (currency_id, currency_type),
        FOREIGN KEY (currency_id) REFERENCES currencies(id)
    );