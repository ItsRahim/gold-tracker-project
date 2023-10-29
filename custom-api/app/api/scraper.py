from bs4 import BeautifulSoup
import requests
from app.config.logging import log
from fake_useragent import UserAgent

ua = UserAgent().random

user_agent = {'User-Agent': ua}


def get_gold_price(source: dict) -> float or None:
    try:
        result = requests.get(source["url"], headers=user_agent)
        result.raise_for_status()
        log.info(f'Successfully fetched data from {source["url"]}')

        gold_info = BeautifulSoup(result.text, "html.parser")

        price_element = gold_info.find(source["element"][0], source["element"][1])

        if price_element:
            price = (price_element.text.strip()
                     .replace(',', ''))
            return round(float(price), 2)
        else:
            log.error(f'Price element not found on the page - {gold_info}')
            return None
    except requests.exceptions.RequestException as e:
        log.error(f"Failed to fetch data from {source['name']}: {str(e)}")
        return None
    except Exception as e:
        log.error(f"An error occurred: {str(e)}")
        return None
