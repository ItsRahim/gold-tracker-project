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
    "element": ["span", {"class": "text-2xl", "data-test": "instrument-price-last"}]
}

# TODO: Change implementation - DO NOT USE
UNUSED_TRADING_VIEW_DICT = {
    "name": "Trading View",
    "request_name": "trading_view",
    "url": "https://www.tradingview.com/symbols/XAUGBP/",
    "element": ["span", {"class": "last-JWoJqCpY js-symbol-last"}]
}

# TODO: Fix the element to retrieve price - DO NOT USE
UNUSED_BLOOMBERG_DICT = {
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

# TODO: Fake user agent required
GOLD_UK_DICT = {
    "name": "Gold UK",
    "request_name": "gold_uk",
    "url": "https://www.cnbc.com/quotes/XAUGBP=",
    "element": ["span", {'class': 'QuoteStrip-lastPrice'}]
}

# TODO: Fake user agent required
BULLION_BY_POST_DICT = {
    "name": "BullionByPost",
    "request_name": "bullion_by_post",
    "url": "https://www.bullionbypost.co.uk/gold-price/",
    "element": ["span", {'name': 'current_price_field', 'data-currency': 'default'}]
}


def get_source(requested_source: str) -> dict or None:
    sources = [UK_INVESTING_DICT, UNUSED_TRADING_VIEW_DICT, UNUSED_BLOOMBERG_DICT, CNBC_DICT, FORBES_DICT,
               GOLD_UK_DICT, BULLION_BY_POST_DICT]
    try:
        for source in sources:
            if source.get("request_name") == requested_source.lower():
                return source
        return None
    except Exception as e:
        log.error(f"An error occurred while searching for the dictionary: {e}")
        return None
