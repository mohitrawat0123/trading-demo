import aiohttp
import asyncio
import json
import logging

logging.basicConfig(
    format='%(asctime)s - %(message)s',
    level=logging.INFO
)
logger = logging.getLogger()

URL = "http://localhost:8080/orders"

ORDERS = [
    {"userId": 1, "stockSymbol": "REL", "price": 104.5, "orderType": "BUY", "quantity": 10, "orderExpiry": "DAY"},
    {"userId": 2, "stockSymbol": "REL", "price": 100, "orderType": "BUY", "quantity": 10, "orderExpiry": "DAY"},
    {"userId": 3, "stockSymbol": "REL", "price": 102.5, "orderType": "BUY", "quantity": 10, "orderExpiry": "DAY"},
    {"userId": 4, "stockSymbol": "REL", "price": 98, "orderType": "SELL", "quantity": 5, "orderExpiry": "DAY"},
    {"userId": 2, "stockSymbol": "JFS", "price": 250, "orderType": "BUY", "quantity": 10, "orderExpiry": "DAY"},
    {"userId": 4, "stockSymbol": "REL", "price": 95, "orderType": "SELL", "quantity": 10, "orderExpiry": "DAY"},
    {"userId": 3, "stockSymbol": "JFS", "price": 255, "orderType": "SELL", "quantity": 5, "orderExpiry": "DAY"},
    {"userId": 4, "stockSymbol": "JFS", "price": 249.5, "orderType": "SELL", "quantity": 10, "orderExpiry": "DAY"},
    {"userId": 1, "stockSymbol": "JFS", "price": 255, "orderType": "BUY", "quantity": 10, "orderExpiry": "DAY"},
    {"userId": 4, "stockSymbol": "REL", "price": 105, "orderType": "BUY", "quantity": 10, "orderExpiry": "DAY"},
    {"userId": 5, "stockSymbol": "REL", "price": 104, "orderType": "SELL", "quantity": 10, "orderExpiry": "DAY"},
    {"userId": 6, "stockSymbol": "JFS", "price": 256.7, "orderType": "BUY", "quantity": 10, "orderExpiry": "DAY"},
    {"userId": 2, "stockSymbol": "JFS", "price": 252, "orderType": "SELL", "quantity": 10, "orderExpiry": "DAY"},
]

HEADERS = {
    "accept": "*/*",
    "Content-Type": "application/json"
}


async def send_order(session, order):
    try:
        async with session.post(URL, headers=HEADERS, data=json.dumps(order)) as response:
            response_text = await response.text()
            logger.info(f"Sent order: {order}")
            logger.info(f"Response: {response.status} - {response_text}")
    except Exception as e:
        logger.error(f"Error sending order {order}: {e}")


async def main():
    async with aiohttp.ClientSession() as session:
        tasks = [send_order(session, order) for order in ORDERS]
        await asyncio.gather(*tasks)  # Run all tasks concurrently


if __name__ == "__main__":
    asyncio.run(main())
