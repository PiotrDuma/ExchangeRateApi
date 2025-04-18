INSERT INTO currencies (id, created, updated, base_currency, converted_sum)
VALUES ('9ce00e0f-0454-45df-9786-3cf24c28ea24', NOW(), NOW(), 'USD', 0);

INSERT INTO exchange_currencies (currency_id, type)
VALUES ('9ce00e0f-0454-45df-9786-3cf24c28ea24', 'EUR');

INSERT INTO exchange_currencies (currency_id, type)
VALUES ('9ce00e0f-0454-45df-9786-3cf24c28ea24', 'JPY');

INSERT INTO rates (currency_type, rate, currency_id)
VALUES ('JPY' , 123 ,'9ce00e0f-0454-45df-9786-3cf24c28ea24');

INSERT INTO rates (currency_type, rate, currency_id)
VALUES ('EUR' , 1.1 ,'9ce00e0f-0454-45df-9786-3cf24c28ea24');



INSERT INTO currencies (id, created, updated, base_currency, converted_sum)
VALUES ('4c8cc6dd-2cbe-4e14-ba54-cef0f1d8be54', NOW(), NOW(), 'PLN', 0);

INSERT INTO exchange_currencies (currency_id, type)
VALUES ('4c8cc6dd-2cbe-4e14-ba54-cef0f1d8be54', 'EUR');

INSERT INTO rates (currency_type, rate, currency_id)
VALUES ('EUR' , 4.2 ,'4c8cc6dd-2cbe-4e14-ba54-cef0f1d8be54');