DELETE FROM DELIVERY;

-- 3 'ORDER_RECEIVED' delivery orders
-- 2 'PREPARING' delivery orders
-- 1 'PACKAGING' delivery orders
-- 2 'SHIPPED' delivery orders
-- 2 'DELIVERED' delivery orders
-- initiated with TOTAL 10 delivery orders

-- orderNo: 'NO_001', deliveryId: 'ID_001', state: ORDER_RECEIVED
INSERT INTO DELIVERY (ID, JSON_STR, ORDER_NO, STATUS)
VALUES ('ID_001',
        '{"id":{"value":"ID_001"},"order":{"no":{"value":"NO_001"},"openedAt":"2021-12-16T05:29:23Z","orderer":{"id":"PeachMan","name":"Lee Heejong"}},"sender":{"name":"Good Farmer","city":"Blue Mountain","telephone":"010-1111-2222","address1":"Pine Valley 123","address2":null},"receiver":{"name":"Kim HakSung","city":"Seoul","telephone":"010-1234-1234","address1":"Teheran-ro 100","address2":"Royal Palace 123"},"status":{"type":"ORDER_RECEIVED","timestamp":"2022-01-13T13:04:29.021687Z"},"items":{"items":[{"name":"복숭아","quantity":10}]}}',
        'NO_001',
        'ORDER_RECEIVED');

-- orderNo: 'NO_002', deliveryId: 'ID_002', state: ORDER_RECEIVED
INSERT INTO DELIVERY (ID, JSON_STR, ORDER_NO, STATUS)
VALUES ('ID_002',
        '{"id":{"value":"ID_002"},"order":{"no":{"value":"NO_002"},"openedAt":"2021-12-16T05:29:23Z","orderer":{"id":"PeachMan","name":"Lee Heejong"}},"sender":{"name":"Good Farmer","city":"Blue Mountain","telephone":"010-1111-2222","address1":"Pine Valley 123","address2":null},"receiver":{"name":"Kim HakSung","city":"Seoul","telephone":"010-1234-1234","address1":"Teheran-ro 100","address2":"Royal Palace 123"},"status":{"type":"ORDER_RECEIVED","timestamp":"2022-01-13T13:04:29.021687Z"},"items":{"items":[{"name":"복숭아","quantity":10}]}}',
        'NO_002',
        'ORDER_RECEIVED');

-- orderNo: 'NO_003', deliveryId: 'ID_003', state: ORDER_RECEIVED
INSERT INTO DELIVERY (ID, JSON_STR, ORDER_NO, STATUS)
VALUES ('ID_003',
        '{"id":{"value":"ID_003"},"order":{"no":{"value":"NO_003"},"openedAt":"2021-12-16T05:29:23Z","orderer":{"id":"PeachMan","name":"Lee Heejong"}},"sender":{"name":"Good Farmer","city":"Blue Mountain","telephone":"010-1111-2222","address1":"Pine Valley 123","address2":null},"receiver":{"name":"Kim HakSung","city":"Seoul","telephone":"010-1234-1234","address1":"Teheran-ro 100","address2":"Royal Palace 123"},"status":{"type":"ORDER_RECEIVED","timestamp":"2022-01-13T13:04:29.021687Z"},"items":{"items":[{"name":"복숭아","quantity":10}]}}',
        'NO_003',
        'ORDER_RECEIVED');

-- orderNo: 'NO_004', deliveryId: 'ID_004', state: PREPARING
INSERT INTO DELIVERY (ID, JSON_STR, ORDER_NO, STATUS)
VALUES ('ID_004',
        '{"id":{"value":"ID_004"},"order":{"no":{"value":"NO_004"},"openedAt":"2021-12-16T05:29:23Z","orderer":{"id":"PeachMan","name":"Lee Heejong"}},"sender":{"name":"Good Farmer","city":"Blue Mountain","telephone":"010-1111-2222","address1":"Pine Valley 123","address2":null},"receiver":{"name":"Kim HakSung","city":"Seoul","telephone":"010-1234-1234","address1":"Teheran-ro 100","address2":"Royal Palace 123"},"status":{"type":"PREPARING","timestamp":"2022-01-13T13:04:29.021687Z"},"items":{"items":[{"name":"복숭아","quantity":10}]}}',
        'NO_004',
        'PREPARING');

-- orderNo: 'NO_005', deliveryId: 'ID_005', state: PREPARING
INSERT INTO DELIVERY (ID, JSON_STR, ORDER_NO, STATUS)
VALUES ('ID_005',
        '{"id":{"value":"ID_005"},"order":{"no":{"value":"NO_005"},"openedAt":"2021-12-16T05:29:23Z","orderer":{"id":"PeachMan","name":"Lee Heejong"}},"sender":{"name":"Good Farmer","city":"Blue Mountain","telephone":"010-1111-2222","address1":"Pine Valley 123","address2":null},"receiver":{"name":"Kim HakSung","city":"Seoul","telephone":"010-1234-1234","address1":"Teheran-ro 100","address2":"Royal Palace 123"},"status":{"type":"PREPARING","timestamp":"2022-01-13T13:04:29.021687Z"},"items":{"items":[{"name":"복숭아","quantity":10}]}}',
        'NO_005',
        'PREPARING');

-- orderNo: 'NO_006', deliveryId: 'ID_006', state: PACKAGING
INSERT INTO DELIVERY (ID, JSON_STR, ORDER_NO, STATUS)
VALUES ('ID_006',
        '{"id":{"value":"ID_006"},"order":{"no":{"value":"NO_006"},"openedAt":"2021-12-16T05:29:23Z","orderer":{"id":"PeachMan","name":"Lee Heejong"}},"sender":{"name":"Good Farmer","city":"Blue Mountain","telephone":"010-1111-2222","address1":"Pine Valley 123","address2":null},"receiver":{"name":"Kim HakSung","city":"Seoul","telephone":"010-1234-1234","address1":"Teheran-ro 100","address2":"Royal Palace 123"},"status":{"type":"PACKAGING","timestamp":"2022-01-13T13:04:29.021687Z"},"items":{"items":[{"name":"복숭아","quantity":10}]}}',
        'NO_006',
        'PACKAGING');

-- orderNo: 'NO_007', deliveryId: 'ID_007', state: SHIPPED
INSERT INTO DELIVERY (ID, JSON_STR, ORDER_NO, STATUS)
VALUES ('ID_007',
        '{"id":{"value":"ID_007"},"order":{"no":{"value":"NO_007"},"openedAt":"2021-12-16T05:29:23Z","orderer":{"id":"PeachMan","name":"Lee Heejong"}},"sender":{"name":"Good Farmer","city":"Blue Mountain","telephone":"010-1111-2222","address1":"Pine Valley 123","address2":null},"receiver":{"name":"Kim HakSung","city":"Seoul","telephone":"010-1234-1234","address1":"Teheran-ro 100","address2":"Royal Palace 123"},"status":{"type":"SHIPPED","timestamp":"2022-01-13T13:04:29.021687Z"},"items":{"items":[{"name":"복숭아","quantity":10}]}}',
        'NO_007',
        'SHIPPED');

-- orderNo: 'NO_008', deliveryId: 'ID_008', state: SHIPPED
INSERT INTO DELIVERY (ID, JSON_STR, ORDER_NO, STATUS)
VALUES ('ID_008',
        '{"id":{"value":"ID_008"},"order":{"no":{"value":"NO_008"},"openedAt":"2021-12-16T05:29:23Z","orderer":{"id":"PeachMan","name":"Lee Heejong"}},"sender":{"name":"Good Farmer","city":"Blue Mountain","telephone":"010-1111-2222","address1":"Pine Valley 123","address2":null},"receiver":{"name":"Kim HakSung","city":"Seoul","telephone":"010-1234-1234","address1":"Teheran-ro 100","address2":"Royal Palace 123"},"status":{"type":"SHIPPED","timestamp":"2022-01-13T13:04:29.021687Z"},"items":{"items":[{"name":"복숭아","quantity":10}]}}',
        'NO_008',
        'SHIPPED');

-- orderNo: 'NO_009', deliveryId: 'ID_009', state: DELIVERED
INSERT INTO DELIVERY (ID, JSON_STR, ORDER_NO, STATUS)
VALUES ('ID_009',
        '{"id":{"value":"ID_009"},"order":{"no":{"value":"NO_009"},"openedAt":"2021-12-16T05:29:23Z","orderer":{"id":"PeachMan","name":"Lee Heejong"}},"sender":{"name":"Good Farmer","city":"Blue Mountain","telephone":"010-1111-2222","address1":"Pine Valley 123","address2":null},"receiver":{"name":"Kim HakSung","city":"Seoul","telephone":"010-1234-1234","address1":"Teheran-ro 100","address2":"Royal Palace 123"},"status":{"type":"DELIVERED","timestamp":"2022-01-13T13:04:29.021687Z"},"items":{"items":[{"name":"복숭아","quantity":10}]}}',
        'NO_009',
        'DELIVERED');

-- orderNo: 'NO_010', deliveryId: 'ID_010', state: DELIVERED
INSERT INTO DELIVERY (ID, JSON_STR, ORDER_NO, STATUS)
VALUES ('ID_010',
        '{"id":{"value":"ID_010"},"order":{"no":{"value":"NO_010"},"openedAt":"2021-12-16T05:29:23Z","orderer":{"id":"PeachMan","name":"Lee Heejong"}},"sender":{"name":"Good Farmer","city":"Blue Mountain","telephone":"010-1111-2222","address1":"Pine Valley 123","address2":null},"receiver":{"name":"Kim HakSung","city":"Seoul","telephone":"010-1234-1234","address1":"Teheran-ro 100","address2":"Royal Palace 123"},"status":{"type":"DELIVERED","timestamp":"2022-01-13T13:04:29.021687Z"},"items":{"items":[{"name":"복숭아","quantity":10}]}}',
        'NO_010',
        'DELIVERED');
