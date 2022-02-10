# Peach
event driven microservice

## delivery backoffice workflow
A Postman collection is available under the `doc` directory.
1. Request Delivery Order (`POST /delivery`)
2. Request Delivery Preparing (`PUT /delivery/{deliverId}/prepare`)
   - delivery state change message published
3. Request Delivery Packaging (`PUT /delivery/{deliverId}/package`)
   - delivery state change message published
4. Request Delivery Shipping (`PUT /delivery/{deliverId}/ship`)
   - delivery state change message published
5. Request Delivery Complete (temporary fix) (`PUT /delivery/{deliverId}/complete`)
   - delivery state change message published

## delivery search APIs
1. Find Delivery by delivery ID (`GET /delivery/{deliverId}`)
2. Find delivery by order number (`GET /delivery?orderNo={orderNo}`)
3. Search deliveries by state (`GET /delivery/searches?pageNo={0}&pageSize={10}&state={state}`)
   - Find all: `GET /delivery/searches?pageNo={0}&pageSize={10}`
   - Find by state: `state={paid, preparing, packaging, shipped, delivered}`