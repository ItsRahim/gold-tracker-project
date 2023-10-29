from dataclasses import dataclass
from datetime import datetime
from app.config.logging import log


@dataclass
class Gold:
    source: str
    price: float
    date: datetime

    def __post_init__(self):
        self.format_price()
        self.format_datetime()

    def format_datetime(self):
        try:
            formatted_date = datetime.strptime(str(self.date), "%Y-%m-%d %H:%M:%S")
            return formatted_date
        except ValueError:
            log.error(f"Error formatting date for Gold object: {self}")
            return None

    def format_price(self):
        try:
            self.price = round(float(self.price), 2)
        except ValueError:
            self.price = None
            log.error(f"Error formatting price for Gold object: {self}")

    def to_dict(self):
        return {
            'source': self.source,
            'price': self.price,
            'request_date': self.date
        }
