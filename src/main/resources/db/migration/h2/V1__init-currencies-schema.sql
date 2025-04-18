    DROP TABLE if EXISTS currencies CASCADE;

    DROP TABLE if EXISTS exchange_currencies CASCADE;

    DROP TABLE if EXISTS rates CASCADE;

    CREATE TABLE currencies (
          id uuid NOT NULL,
          version integer default 0,
          created timestamp(6) with time zone,
          updated timestamp(6) with time zone,
          base_currency varchar(3) NOT NULL UNIQUE,
          converted_sum float(53),
          primary key (id)
    );

    create table exchange_currencies (
        currency_id uuid not null,
        type varchar(3),
        foreign key (currency_id) references currencies
    );

    create table rates (
        currency_type varchar(3) not null,
        rate float(53),
        currency_id uuid not null,
        primary key (currency_id, currency_type),
        foreign key (currency_id) references currencies
    );