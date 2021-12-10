package com.aws.peach.domain.delivery;

public class Shipment { // root entity
    private String orderId; // monolith 쪽에서 관리하는 아이디
    private String sender;
    private String senderAddress;
    private String receiver;
    private String receiverAddress;
    private String id;
    private ShipmentStatus status;
}
