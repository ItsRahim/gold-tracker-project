from app.config.logging import log

"""
This is a Python file used to store website information.

It provides a centralised location for all sources this api will use to gather information about gold prices.
Information includes website name, url and html elements to scrape

Author: Rahim Ahmed
Version: 1.0
"""

"""
Data Source: Investing.com

This script scrapes data from the Investing.com website to retrieve
current XAU/GBP price information.
"""
UK_INVESTING_DICT = {
    "name": "UK Investing",
    "request_name": "uk-investing",
    "url": "https://uk.investing.com/currencies/xau-gbp",
    "element": ["div", {'data-test': 'instrument-price-last'}]
}

BLOOMBERG_DICT = {
    "name": "Bloomberg",
    "request_name": "bloomberg",
    "url": "https://www.bloomberg.com/quote/XAUGBP:CUR",
    "element": ["div", {'data-component': 'sized-price', 'class': 'sized-price SizedPrice_extraLarge-05pKbJRbUH8-'}]
}

CNBC_DICT = {
    "name": "CNBC",
    "request_name": "cnbc",
    "url": "https://www.cnbc.com/quotes/XAUGBP=",
    "element": ["span", {'class': 'QuoteStrip-lastPrice'}]
}

FORBES_DICT = {
    "name": "Forbes",
    "request_name": "forbes",
    "url": "https://www.forbes.com/advisor/money-transfer/currency-converter/xau-gbp/",
    "element": ["span", {'class': 'amount'}]
}

# TODO: Fake account agent required
GOLD_UK_DICT = {
    "name": "Gold UK",
    "request_name": "gold-uk",
    "url": "https://www.cnbc.com/quotes/XAUGBP=",
    "element": ["span", {'class': 'QuoteStrip-lastPrice'}]
}


def get_source(requested_source: str) -> dict or None:
    sources = [UK_INVESTING_DICT, BLOOMBERG_DICT, CNBC_DICT, FORBES_DICT, GOLD_UK_DICT]
    try:
        for source in sources:
            if source.get("request_name") == requested_source.lower():
                return source
        return None
    except Exception as e:
        log.error(f"An error occurred while searching for the dictionary: {e}")
        return None
