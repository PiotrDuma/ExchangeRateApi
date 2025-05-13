INSERT INTO currencies (id, created, updated, base_currency, converted_sum)
VALUES (UUID_TO_BIN('9ce00e0f-0454-45df-9786-3cf24c28ea24', true), NOW(), NOW(), 'USD', 0);

INSERT INTO exchange_currencies (currency_id, type)
VALUES (UUID_TO_BIN('9ce00e0f-0454-45df-9786-3cf24c28ea24', true), 'EUR');

INSERT INTO exchange_currencies (currency_id, type)
VALUES (UUID_TO_BIN('9ce00e0f-0454-45df-9786-3cf24c28ea24', true), 'JPY');

INSERT INTO rates (currency_type, rate, currency_id)
VALUES ('JPY' , 123, UUID_TO_BIN('9ce00e0f-0454-45df-9786-3cf24c28ea24', true));

INSERT INTO rates (currency_type, rate, currency_id)
VALUES ('EUR' , 1.1, UUID_TO_BIN('9ce00e0f-0454-45df-9786-3cf24c28ea24', true));


INSERT INTO currencies (id, created, updated, base_currency, converted_sum)
VALUES (UUID_TO_BIN('4c8cc6dd-2cbe-4e14-ba54-cef0f1d8be54', true), NOW(), NOW(), 'PLN', 0);

INSERT INTO exchange_currencies (currency_id, type)
VALUES (UUID_TO_BIN('4c8cc6dd-2cbe-4e14-ba54-cef0f1d8be54', true), 'EUR');

INSERT INTO rates (currency_type, rate, currency_id)
VALUES ('EUR' , 0.20, UUID_TO_BIN('4c8cc6dd-2cbe-4e14-ba54-cef0f1d8be54', true));


INSERT INTO currencies (id, created, updated, base_currency, converted_sum)
VALUES (UUID_TO_BIN('e729ab81-c406-4a17-bb1f-fbc307d6990d', true), NOW(), NOW(), 'EUR', 0);

INSERT INTO exchange_currencies (currency_id, type)
VALUES (UUID_TO_BIN('e729ab81-c406-4a17-bb1f-fbc307d6990d', true), 'PLN');

INSERT INTO rates (currency_type, rate, currency_id)
VALUES ('PLN' , 4.2, UUID_TO_BIN('e729ab81-c406-4a17-bb1f-fbc307d6990d', true));

INSERT INTO exchange_currencies (currency_id, type)
VALUES (UUID_TO_BIN('e729ab81-c406-4a17-bb1f-fbc307d6990d', true), 'GBP');

INSERT INTO rates (currency_type, rate, currency_id)
VALUES ('GBP' , 0.84, UUID_TO_BIN('e729ab81-c406-4a17-bb1f-fbc307d6990d', true));