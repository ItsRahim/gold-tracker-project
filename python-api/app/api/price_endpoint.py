import ast
from datetime import datetime

from fastapi import APIRouter
from fastapi.encoders import jsonable_encoder

from app.config.logging import log
from app.data.gold_sources import get_source
from app.kafka.producer import KafkaHandler
from app.models.gold import Gold
from app.service.scraper import get_gold_price

price_router = APIRouter()
kafka_handler = KafkaHandler()


@price_router.get("/{requested_source}")
async def root(requested_source: str) -> Gold | dict[str, str]:
    log.info(f"Received request for gold price from source: {requested_source}")

    source = get_source(requested_source)
    if source is not None:
        source_name, source_url, source_element = source

        log.info(f"Found information for: {source_name}")

        gold_price = get_gold_price(source_name, source_url, ast.literal_eval(source_element))

        request_time = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        log.info(f"Retrieved gold price from {source_name}")

        gold = Gold(source_name, gold_price, request_time)

        kafka_handler.send_price(gold)
        log.debug("Gold object data sent to Kafka producer")

        return jsonable_encoder(gold)
    else:
        log.warning(f"No data found with the requested source: {requested_source}")
        return {"error": f"No information found for requested source: {requested_source}"}
